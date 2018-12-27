package org.jetax.testcontainers.consul;

public class ConsulCommand {

    private boolean dev;
    private boolean ui;
    private String join;
    private boolean retryJoin;

    public ConsulCommand() {
        this.dev = true;
    }

    public Boolean isDev() {
        return dev;
    }

    public void setDev(Boolean dev) {
        this.dev = dev;
    }

    public Boolean getUi() {
        return ui;
    }

    public void setUi(Boolean ui) {
        this.ui = ui;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public Boolean getRetryJoin() {
        return retryJoin;
    }

    public void setRetryJoin(Boolean retryJoin) {
        this.retryJoin = retryJoin;
    }

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
