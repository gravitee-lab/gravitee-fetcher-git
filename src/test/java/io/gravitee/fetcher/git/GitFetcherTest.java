/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.fetcher.git;

import io.gravitee.fetcher.api.FetcherException;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Nicolas GERAUD (nicolas <AT> graviteesource.com)
 * @author GraviteeSource Team
 */
public class GitFetcherTest {

    @Test
    public void shouldGetExistingFileWithBranch() throws Exception {
        GitFetcherConfiguration gitFetcherConfiguration = new GitFetcherConfiguration();
        gitFetcherConfiguration.setRepository("https://github.com/gravitee-io/issues.git");
        gitFetcherConfiguration.setBranchOrTag("master");
        gitFetcherConfiguration.setPath("README.md");

        GitFetcher gitFetcher = new GitFetcher(gitFetcherConfiguration);
        InputStream is = gitFetcher.fetch();
        assertThat(is).isNotNull();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String content = "";
        while ((line = br.readLine()) != null) {
            content += line;
            assertThat(line).isNotNull();
        }
        br.close();
        assertThat(content).contains("Gravitee.io");
    }

    @Test
    public void shouldGetExistingFileWithTag() throws Exception {
        GitFetcherConfiguration gitFetcherConfiguration = new GitFetcherConfiguration();
        gitFetcherConfiguration.setRepository("https://github.com/gravitee-io/gravitee-gateway.git");
        gitFetcherConfiguration.setBranchOrTag("0.1.0");
        gitFetcherConfiguration.setPath("gravitee-gateway-api/pom.xml");

        GitFetcher gitFetcher = new GitFetcher(gitFetcherConfiguration);
        InputStream is = gitFetcher.fetch();
        assertThat(is).isNotNull();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String content = "";
        while ((line = br.readLine()) != null) {
            content += line;
            assertThat(line).isNotNull();
        }
        br.close();
        assertThat(content).contains("gravitee-gateway-api");
    }

    @Test
    public void shouldGetInexistingPath() throws Exception {
        GitFetcherConfiguration gitFetcherConfiguration = new GitFetcherConfiguration();
        gitFetcherConfiguration.setRepository("https://github.com/gravitee-io/issues.git");
        gitFetcherConfiguration.setBranchOrTag("master");
        gitFetcherConfiguration.setPath("unknown");

        GitFetcher gitFetcher = new GitFetcher(gitFetcherConfiguration);
        InputStream is = null;
        try {
            is = gitFetcher.fetch();
            fail("should not happen");
        } catch (FetcherException fetcherException) {
            assertThat(fetcherException.getMessage()).contains("Unable to fetch git content");
            assertThat(is).isNull();
        }
    }

    @Test
    public void shouldGetInexistingRepo() throws Exception {
        GitFetcherConfiguration gitFetcherConfiguration = new GitFetcherConfiguration();
        gitFetcherConfiguration.setRepository("https://unknown/gravitee-io/issues.git");
        gitFetcherConfiguration.setBranchOrTag("master");
        gitFetcherConfiguration.setPath("README.md");

        GitFetcher gitFetcher = new GitFetcher(gitFetcherConfiguration);
        InputStream is = null;
        try {
            is = gitFetcher.fetch();
            fail("should not happen");
        } catch (FetcherException fetcherException) {
            assertThat(fetcherException.getMessage()).contains("Unable to fetch git content");
            assertThat(is).isNull();
        }
    }

    @Test
    public void shouldGetInexistingBranch() throws Exception {
        GitFetcherConfiguration gitFetcherConfiguration = new GitFetcherConfiguration();
        gitFetcherConfiguration.setRepository("https://github.com/gravitee-io/issues.git");
        gitFetcherConfiguration.setBranchOrTag("munster");
        gitFetcherConfiguration.setPath("README.md");

        GitFetcher gitFetcher = new GitFetcher(gitFetcherConfiguration);
        InputStream is = null;
        try {
            is = gitFetcher.fetch();
            fail("should not happen");
        } catch (FetcherException fetcherException) {
            assertThat(fetcherException.getMessage()).contains("Unable to fetch git content");
            assertThat(is).isNull();
        }
    }
}
