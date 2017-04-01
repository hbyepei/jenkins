package jenkins.api;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yepei.ye on 2017/4/1.
 * Description:
 */
public class Jenkins {
    public static final String defaultJenkinsServer = "http://jenkins.corp.11bee.com";
    public static final String defaultJobName = "graphx";

    enum YarnQueue {
        online, offline
    }

    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(JenkinsConfig.git_path.getName(), gitPath);
        map.put(JenkinsConfig.jar_name.getName(), jarName);
        map.put(JenkinsConfig.git_branch.getName(), gitBranch);
        map.put(JenkinsConfig.main_class_path.getName(), mainClass);
        map.put(JenkinsConfig.yarn_submit_name.getName(), taskName);
        map.put(JenkinsConfig.driver_memory.getName(), driverMemory);
        map.put(JenkinsConfig.executor_memory.getName(), executorMemory);
        map.put(JenkinsConfig.executor_overhead.getName(), executorOverhead);
        map.put(JenkinsConfig.queue.getName(), queue);
        map.put(JenkinsConfig.user_params.getName(), userParams);
        return map;
    }

    private String gitPath;
    private String jenkinsUrl;
    private String jarName;
    private String jobName;
    private String gitBranch;
    private String mainClass;
    private String taskName;
    private String username;
    private String password;
    private String driverMemory;
    private String executorMemory;
    private String executorOverhead;
    private String queue;
    private String userParams;

    public String getGitPath() {
        return gitPath;
    }

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public String getJarName() {
        return jarName;
    }

    public String getJobName() {
        return jobName;
    }

    public String getGitBranch() {
        return gitBranch;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverMemory() {
        return driverMemory;
    }

    public String getExecutorMemory() {
        return executorMemory;
    }

    public String getExecutorOverhead() {
        return executorOverhead;
    }

    public String getQueue() {
        return queue;
    }

    public String getUserParams() {
        return userParams;
    }

    private Jenkins(Builder builder) {
        this.gitPath = builder.gitPath;
        this.jenkinsUrl = builder.jenkinsUrl;
        this.jarName = builder.jarName;
        this.jobName = builder.jobName;
        this.gitBranch = builder.gitBranch;
        this.mainClass = builder.mainClass.getCanonicalName();
        this.taskName = builder.taskName;
        this.username = builder.username;
        this.password = builder.password;
        this.driverMemory = builder.driverMemory + "g";
        this.executorMemory = builder.executorMemory + "g";
        this.executorOverhead = builder.executorOverhead + "g";
        this.queue = builder.queue.name();
        this.userParams = builder.userParams;
    }

    public static final class Builder {
        private String jenkinsUrl = defaultJenkinsServer;
        private String gitPath = "http://gitlab.corp.11bee.com/nbdata/graphx.git";
        private String jarName = "sparkGraphx-1.0.0-SNAPSHOT-jar-with-dependencies";
        private String jobName = defaultJobName;
        private String gitBranch = "master";
        private Class mainClass;
        private String taskName;
        private String username;
        private String password;
        private int driverMemory = 10;
        private int executorMemory = 30;
        private int executorOverhead = 5;
        private YarnQueue queue = YarnQueue.offline;
        private String userParams = "";

        public static Builder getBuilder() {
            return new Builder();
        }

        private Builder() {
        }

        public Builder gitBranch(String gitBranch) {
            this.gitBranch = gitBranch;
            return this;
        }

        public Builder mainClass(Class mainClass) {
            this.mainClass = mainClass;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder driverMemory(int driverMemory) {
            this.driverMemory = driverMemory;
            return this;
        }

        public Builder executorMemory(int executorMemory) {
            this.executorMemory = executorMemory;
            return this;
        }

        public Builder executorOverhead(int executorOverhead) {
            this.executorOverhead = executorOverhead;
            return this;
        }

        public Builder queue(YarnQueue queue) {
            this.queue = queue;
            return this;
        }

        public Builder userParams(String userParams) {
            this.userParams = userParams;
            return this;
        }

        public Builder taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public Jenkins build() {
            if (StringUtils.isBlank(username)) {
                throw new InvalidParameterException("jenkins用户名不能为空");
            }
            if (StringUtils.isBlank(password)) {
                throw new InvalidParameterException("jenkins密码不能为空");
            }
            if (StringUtils.isBlank(gitBranch)) {
                throw new InvalidParameterException("git分支不能为空");
            }
            if (mainClass == null) {
                throw new InvalidParameterException("入口类不能为null");
            }
            this.taskName = mainClass.getSimpleName().toLowerCase();
            return new Jenkins(this);
        }
    }

    private enum JenkinsConfig {
        git_path("git_path"), git_branch("git_branch"), jar_name("jar_name"), main_class_path("main_class_path"), driver_memory("driver_memory"), executor_memory("executor_memory"), executor_overhead("executor_overhead"), queue("queue"), yarn_submit_name("yarn_submit_name"), user_params("user_params"), none("none");
        private String name;

        public static JenkinsConfig fromName(String name) {
            if (StringUtils.isBlank(name)) return none;
            for (JenkinsConfig c : JenkinsConfig.values()) {
                if (StringUtils.equalsIgnoreCase(name, c.name)) {
                    return c;
                }
            }
            return none;
        }

        JenkinsConfig(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
