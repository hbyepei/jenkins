package jenkins.api;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.JenkinsTriggerHelper;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Crumb;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by yepei.ye on 2017/4/1.
 * Description:
 */
public class JenKinsUtil {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Jenkins jenkins = Jenkins.Builder.getBuilder()
                .username("yepei.ye")
                .password("yepei.ye")
                .gitBranch("new_user_connection")
                .mainClass(MainTest.class)
                .build();
        build(jenkins);
    }

    public static void build(Jenkins jenkins) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(jenkins.getJenkinsUrl());
        JenkinsServer server = new JenkinsServer(uri, jenkins.getUsername(), jenkins.getPassword());
        JenkinsTriggerHelper trigger = new JenkinsTriggerHelper(server);
        Map<String, String> confs = jenkins.getParams();
        BuildWithDetails detials = trigger.triggerJobAndWaitUntilFinished(jenkins.getJobName(), confs, true);
        System.out.println(detials);
    }

    public static String genCrumb(String username, String password) throws URISyntaxException, IOException {
        String host = Jenkins.defaultJenkinsServer;
        String jobName = Jenkins.defaultJobName;
        URI uri = new URI(host);
        JenkinsServer server = new JenkinsServer(uri, username, password);
        JobWithDetails job = server.getJob(jobName);
        Crumb crumb = job.getClient().getQuietly("/crumbIssuer", Crumb.class);
        return crumb.getCrumb();
    }
}
