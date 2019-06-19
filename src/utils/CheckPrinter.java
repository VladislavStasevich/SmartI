package utils;

import dao.Item;

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

    public static void print(String path, Item check) {
        new HtmlDocument(path)
                .setTitle(String.format("Талон - %s: %s", check.getType(), check.getName()))
                .line(String.format("%s %s", "Наименование: ", check.getName()))
                .line(String.format("%s %s", "         Тип: ", check.getType()))
                .line(String.format("%s %s", "  Количество: ", check.getCount()))
                .line(String.format("%s %s", "        Цена: ", check.getPrice()))
                .line(String.format("%s %s", "       Сумма: ", check.getPrice() * check.getCount()))
                .save();
    }
}
