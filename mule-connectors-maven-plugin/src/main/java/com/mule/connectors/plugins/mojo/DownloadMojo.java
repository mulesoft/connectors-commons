package com.mule.connectors.plugins.mojo;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.mule.connectors.plugins.model.Config;
import com.mule.connectors.plugins.model.Connector;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.io.FileUtils.deleteDirectory;

@Mojo(name = "download", requiresProject = false)
public class DownloadMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException {
        try {
            List<Connector> connectors = new GsonBuilder().create().<Config>fromJson(new JsonReader(new InputStreamReader(DownloadMojo.class.getResourceAsStream("/connectors.json"))), Config.class).getConnectors();
            connectors.stream()
                    .filter(connector -> !connector.getName().equals(""))
                    .forEach(this::download);
            getLog().info("Deleting Configuration directory.");
            deleteDirectory(new File(connectors.get(0).getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void download(Connector connector) {
        try {
            getLog().info(format("Fetching connector: %s", connector.getDescription()));
            Process pwd = Runtime.getRuntime().exec(format("git clone %s %s", connector.getLocation(), connector.getName()));
            pwd.waitFor();
            Stream.of(Optional.ofNullable(new File(format("connectors-config/active/%s", connector.getName())).listFiles())
                    .orElse(new File[]{}))
                    .filter(file -> !file.getName().endsWith(".json"))
                    .forEach(file -> {
                        try {
                            Path from = file.toPath();
                            File targetDirectory = new File(format("%s/src/test/resources/", connector.getName()));
                            Optional.of(targetDirectory).filter(directory -> !directory.exists()).ifPresent(File::mkdirs);
                            Path to = Paths.get(format("%s/%s", targetDirectory.getAbsolutePath(), file.getName()));
                            getLog().info(format("Copying file '%s' from %s to %s", file.getName(), from, to));
                            copy(from, to, REPLACE_EXISTING);
                        } catch (IOException e) {
                            getLog().error(e);
                        }
                    });

        } catch (Exception e) {
            getLog().error(e);
        }
    }
}
