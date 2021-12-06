package com.example.shopmebackend.user.export;

import com.example.shopmebackend.common.AbstractExport;
import com.example.shopmecommon.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExport {

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setAtributeHeader(response, ".csv", "text/csv", "user_");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"User id", "Email", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fileMapping = {"id","email", "firstName", "lastName", "roles", "enabled"};

        csvWriter.writeHeader(csvHeader);
        for(User user : listUsers) {
            csvWriter.write(user, fileMapping);
        }

        csvWriter.close();

    }
}
