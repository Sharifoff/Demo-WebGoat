package org.owasp.webgoat.application;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Application {

    private static final Application INSTANCE = new Application();

    private Application() {

    }

    public static final Application getInstance() {
        return INSTANCE;
    }

    private String version = "SNAPSHOT";
    private String build = "local";
    private String name = "WebGoat";

    
    public String getVersion() {
        return version;
    }

    


  
    public void setVersion(String version) {
        if (StringUtils.isNotBlank(version)) {
            this.version = version;
        }
    }

    /**
     * @return the build
     */
    public String getBuild() {
        return build;
    }

    /**
     * @param build the build to set
     */
    public void setBuild(String build) {
        if (StringUtils.isNotBlank(build)) {
            this.build = build;
        }
    }

   
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("name", name).
                append("version", version).
                append("build", build).
                toString();
    }
}
