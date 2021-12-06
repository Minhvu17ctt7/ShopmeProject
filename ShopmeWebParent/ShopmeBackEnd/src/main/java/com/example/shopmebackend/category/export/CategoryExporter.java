package com.example.shopmebackend.category.export;

import com.example.shopmebackend.common.AbstractExport;
import com.example.shopmecommon.entity.Category;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryExporter extends AbstractExport {

    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
        super.setAtributeHeader(response, ".csv", "text/csv", "categories_");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Category id", "Name"};
        String[] fileMapping = {"id","name"};

        csvWriter.writeHeader(csvHeader);
        for(Category category : listCategories) {
            category.setName(category.getName().replace("--", "  "));
            csvWriter.write(category, fileMapping);
        }

        csvWriter.close();

    }
}
