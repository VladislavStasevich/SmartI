package utils;

import models.TableCheck;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

    public static void print(String path, List<TableCheck> checks) {
        HtmlDocument doc = new HtmlDocument(path).setTitle(String.format("Чек - %s", checks.get(0).getLastFirstMiddleName()));

        doc.line("-----------------------------------------------------")
           .line(String.format("%s %s", "               ФИО: ", checks.get(0).getLastFirstMiddleName()))
           .line(String.format("%s %s", "             Адрес: ", checks.get(0).getAddress()))
           .line(String.format("%s %s", "Пасспортные данные: ", checks.get(0).getPassport()));

        double totalPrice = 0.0;
        for (final TableCheck check: checks) {
            try {
                totalPrice += Double.valueOf(check.getPrice());
            } catch (NumberFormatException ignored) {
            }
            doc.line("-----------------------------------------------------")
               .line(String.format("%s %s", "Производитель: ", check.getManufacturer()))
               .line(String.format("%s %s", "       Модель: ", check.getModel()))
               .line(String.format("%s %s", "    Двигатель: ", check.getEngine()))
               .line(String.format("%s %s", "  Трансмиссия: ", check.getTransmission()))
               .line(String.format("%s %s", "  Год выпуска: ", check.getYear()))
               .line(String.format("%s %s", "         Цена: ", check.getPrice()));
        }

        if (checks.size() > 1) {
            doc.line("=====================================================");
            doc.line(String.format("%s %d", "Итоговая цена: ", (int) totalPrice));
        }

        doc.save();
    }
}
