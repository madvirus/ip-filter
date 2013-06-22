package org.chimi.ipfilter.web.impl;

import java.io.IOException;
import java.io.Reader;

public class IOUtil {
    public static String read(Reader reader) throws IOException {
        try {
            char[] buff = new char[512];
            int len;
            StringBuilder builder = new StringBuilder();
            while ((len = reader.read(buff)) != -1)
                builder.append(buff, 0, len);
            return builder.toString();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                }
        }

    }
}
