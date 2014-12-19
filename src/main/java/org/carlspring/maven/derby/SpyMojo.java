package org.carlspring.maven.derby;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Mojo(name = "spy")
public class SpyMojo extends AbstractMojo {

	@Parameter(readonly=true, property="project", required=true)
	private MavenProject project;
	
    @Parameter(readonly=true, defaultValue="${session}", required=true)
    private MavenSession session;

    @Parameter(readonly=true, defaultValue="${session.UserProperties}", required=true)
    private Properties properties;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("All plugins:");

		List<Plugin> buildPlugins = project.getBuildPlugins();
		for (Plugin plugin : buildPlugins) {
			getLog().info("Plugin = " + plugin);
		}
		getLog().info("");
		getLog().info("MavenSession = " + session);
		getLog().info("   skipTests = " + session.getUserProperties().getProperty("skipTests"));
		getLog().info("   skipTests = " + properties.getProperty("skipTests"));
		getLog().info("");

		Plugin surefire = lookupPlugin("org.apache.maven.plugins:maven-surefire-plugin");
		getLog().info("Surefire = " + surefire);
		Xpp3Dom  configuration = (Xpp3Dom) surefire.getConfiguration();
		getLog().info("  Configuration = " + configuration);
		if (configuration != null && configuration.getChild("skipTests") != null) {
			getLog().info("   skipTests = " + configuration.getChild("skipTests").getValue());
		}
	}

	private Plugin lookupPlugin(String key) {
		List<Plugin> plugins = project.getBuildPlugins();

		for (Iterator<Plugin> iterator = plugins.iterator(); iterator.hasNext();) {
			Plugin plugin = iterator.next();
			if (key.equalsIgnoreCase(plugin.getKey()))
				return plugin;
		}
		return null;
	}

}
