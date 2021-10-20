package com.example.shopmebackend.Utils;

import com.example.shopmecommon.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExport {
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setAtributeHeader(response, ".xslx", "application/octet-stream");

    }
}
