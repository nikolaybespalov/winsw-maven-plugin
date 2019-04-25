package com.github.nikolaybespalov.winswmavenplugin;

import com.github.nikolaybespalov.winswmavenplugin.WinswMojo;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.execution.*;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class WinswMojoTest {
    @Rule
    public MojoRule mojoRule = new MojoRule() {
        @Override
        protected void before() throws Throwable {
        }

        @Override
        protected void after() {
        }




//        @Override
//        public MavenSession newMavenSession(MavenProject project) {
//            MavenExecutionRequest request = new DefaultMavenExecutionRequest();
//            MavenExecutionResult result = new DefaultMavenExecutionResult();
//            DefaultRepositorySystemSession repositorySystemSession = MavenRepositorySystemUtils.newSession();
//            LocalRepository localRepo = new LocalRepository("target/local-repo");
//
//            DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
//            RepositorySystem repositorySystem = locator.getService(RepositorySystem.class);
//
//            //DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
//            LocalRepositoryManager lrm = repositorySystem.newLocalRepositoryManager(repositorySystemSession, localRepo);
//            repositorySystemSession.setLocalRepositoryManager(lrm);
//
//            MavenSession session = new MavenSession(getContainer(), repositorySystemSession, request, result);
//            session.setCurrentProject(project);
//            session.setProjects(Arrays.asList(project));
//            return session;
//        }
    };

    @Test
    public void testSomething()
            throws Exception {
//        File pom = new File("target/test-classes/project-to-test/");
//        assertNotNull(pom);
//        assertTrue(pom.exists());
//
//        WinswMojo winswMojo = (WinswMojo) mojoRule.lookupConfiguredMojo(pom, "touch2");
//        assertNotNull(winswMojo);
//        winswMojo.execute();

        // setup with pom set BRANCHNAME  set in pom
//        File pomDir = pom;
//        MavenProject project = mojoRule.readMavenProject(pomDir);
//
//        // Generate session
//        MavenSession session = mojoRule.newMavenSession(project);
//
//        // add localRepo - framework doesn't do this on its own
//        ArtifactRepository localRepo = createLocalArtifactRepository();
//        session.getRequest().setLocalRepository(localRepo);
//
//        // Generate Execution and Mojo for testing
//        MojoExecution execution = mojoRule.newMojoExecution("touch2");
//        WinswMojo winswMojo = (WinswMojo) mojoRule.lookupConfiguredMojo(session, execution);
//        this.mojoRule.setVariableValueToObject( winswMojo, "repoSession", newRepositorySession());
//        winswMojo.execute();
    }

    /**
     * Generate a local repository
     *
     * @return local repository object
     */
    private ArtifactRepository createLocalArtifactRepository() {
        return new MavenArtifactRepository("local",
                "c:\\Users\\Nikolay Bespalov\\.m2\\repository\\",
                new DefaultRepositoryLayout(),
                new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_IGNORE),
                new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_IGNORE)

        );
    }

    private RepositorySystemSession newRepositorySession() throws IOException {

        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        RepositorySystem repositorySystem = locator.getService(RepositorySystem.class);

        LocalRepository localRepo = new LocalRepository("asdasdasd");

        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepositoryManager lrm = repositorySystem.newLocalRepositoryManager(session, localRepo);
        session.setLocalRepositoryManager(lrm);

        return session;
    }
}
