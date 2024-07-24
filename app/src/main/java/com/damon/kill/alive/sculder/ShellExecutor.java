package com.damon.kill.alive.sculder;

import com.damon.kill.alive.utils.Logger;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

public class ShellExecutor {
    private static final String COLON_SEPARATOR = ":";

    public static void execute(File dir, Map map, String[] cmds) {
        if (cmds.length == 0) {
            return;
        }

        try {
            ProcessBuilder builder = new ProcessBuilder(new String[0]);
            String envPath = System.getenv("PATH");
            Logger.v(Logger.TAG, "ENV PATH: " + envPath);
            if (envPath != null) {
                String[] split = envPath.split(COLON_SEPARATOR);
                int length = split.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    File f = new File(split[i], "sh");
                    if (f.exists()) {
                        builder.command(new String[]{f.getPath()}).redirectErrorStream(true);
                        break;
                    }
                    i++;
                }
            }
            builder.directory(dir);
            Map<String, String> environment = builder.environment();
            environment.putAll(System.getenv());
            if (map != null) {
                environment.putAll(map);
            }
            StringBuilder sb = new StringBuilder();
            for (String append : cmds) {
                sb.append(append);
                sb.append("\n");
            }

            Process proc = builder.start();
            OutputStream os = proc.getOutputStream();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream(),
                        "utf-8"));
                for (String cmd : cmds) {
                    if (cmd.endsWith("\n")) {
                        os.write(cmd.getBytes());
                    } else {
                        os.write((cmd + "\n").getBytes());
                    }
                }
                os.write("exit 156\n".getBytes());
                os.flush();
                proc.waitFor();
                read(br);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static String read(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String readLine;
        while ((readLine = br.readLine()) != null) {
            sb.append(readLine);
            sb.append("\n");
        }
        Logger.v(Logger.TAG, "read: " + sb);
        return sb.toString();
    }
}
