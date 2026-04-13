package advanced.report;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private final Configuration configuration;

    public ReportService() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public void generateAndOpenReport(List<MovieReportRow> movies) {
        File outputFile = new File("movie_report.html");

        try {
            Map<String, Object> root = new HashMap<>();
            root.put("movies", movies);

            Template template = configuration.getTemplate("report.ftl");
            try (Writer out = new FileWriter(outputFile)) {
                template.process(root, out);
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(outputFile.toURI());
            }

        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }
}