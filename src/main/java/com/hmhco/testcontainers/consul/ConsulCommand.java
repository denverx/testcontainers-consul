package com.hmhco.testcontainers.consul;

/**
 * Allows one to build a consul agent command with support for dev and ui and join options
 * see `https://www.consul.io/docs/agent/options` for more details.
 * 
 * @author iyerk
 *
 */
public class ConsulCommand {

    private boolean dev;
    private boolean ui;
    private String join;
    private boolean retryJoin;

    /**
     * Default constructor. set dev to true.
     */
    public ConsulCommand() {
        this.dev = true;
    }

    /**
     * 
     * @return Boolean - development server mode
     */
    public Boolean isDev() {
        return dev;
    }

    /**
     * set development server mode. Defaults to true. 
     * @param dev - development server mode
     */
    public void setDev(Boolean dev) {
        this.dev = dev;
    }

    /**
     * 
     * @return  Boolean - built-in web UI server
     */
    public Boolean getUi() {
        return ui;
    }

    /**
     * 
     * @param ui - Allows us to configure the built-in web UI server. Defaults to false. 
     */
    public void setUi(Boolean ui) {
        this.ui = ui;
    }

    /**
     * 
     * @return join address
     */
    public String getJoin() {
        return join;
    }

    /**
     * set join address of another agent to join upon starting up.
     * @param join - the join address
     */
    public void setJoin(String join) {
        this.join = join;
    }

    /**
     * 
     * @return retryJoin
     */
    public Boolean getRetryJoin() {
        return retryJoin;
    }

    /**
     * enable retry-join. Similar to -join but allows retrying a join until it is successful
     * @param retryJoin - Boolean. Defaults to false.
     */
    public void setRetryJoin(Boolean retryJoin) {
        this.retryJoin = retryJoin;
    }

    /**
     * command builder that supports dev, ui, join and retry-join.
     * 
     * @return - agent command with options.
     */
    public String toCommand() {
        StringBuilder commandBuilder = new StringBuilder("agent -client 0.0.0.0");
        if (dev) {
            commandBuilder.append(" -dev");
        } else if (ui) {
            commandBuilder.append(" -ui");
        }

        if (join != null && join.length() > 0) {
            commandBuilder.append(" -join ").append(join);
            if (retryJoin) {
                commandBuilder.append(" -retry-join ").append(join);
            }
        }

        return commandBuilder.toString();
    }
}
