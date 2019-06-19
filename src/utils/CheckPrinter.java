package utils;

import dao.Patient;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckPrinter {
    private static class HtmlDocument {
        String path;
        String title;
        StringBuilder body = new StringBuilder();

        private HtmlDocument(String path) {
            this.path = path;
        }

        HtmlDocument setTitle(String title) {
            this.title = title;
            return this;
        }

        HtmlDocument line(String text) {
            body.append("<pre>");
            body.append(text);
            body.append("</pre>");
            return this;
        }

        void save() {
            try {
                String template =
                        new String(Files.readAllBytes(Paths.get(CheckPrinter.class.getResource("/template.html").toURI())),
                                "utf-8");
                template = template.replace("{{title}}", title);
                template = template.replace("{{body}}", body.toString());
                Files.write(Paths.get(path), template.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void print(String path, Patient check) {
        new HtmlDocument(path)
                .setTitle(String.format("Талон - %s", check.getLastFirstMiddleName()))
                .line(String.format("%s %s", "    Номер карточки: ", check.getCardNumber()))
                .line(String.format("%s %s", "               ФИО: ", check.getLastFirstMiddleName()))
                .line(String.format("%s %s", "             Адрес: ", check.getAddress()))
                .line(String.format("%s %s", "      Дата и время: ", check.getRecord()))
                .save();
    }
}
