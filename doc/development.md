# Development

## File Encoding

All files in repository should be in UTF-8 encoding with Unix end of lines (`LF`).

#### Unix End of Lines for Files on Windows

The proper way to get `LF` endings in Windows is:
1. Set `core.autocrlf` to `false`:
```
git config --global core.autocrlf false
```
2. Set `core.eol` to `lf`:
```
git config --global core.eol lf
```
3. Clone repository from git to local folder.

## Profiles

#### Integration-Tests

The profile runs integration tests.
Integration tests execution takes time thus there's no sense to run it on every commit.
Though we strongly recommend to check any branch before pull request with integration tests.
[Travis-CI](https://travis-ci.com) checks all pull requests created on [GitHub](https://github.com).

#### Release

The profile performs next additional actions:
- Adds source code to distribution;
- Adds java docs to distribution;
- Adds GPG sign to distribution;
- Publish database maven plugin to [Nexus Repository Manager](https://oss.sonatype.org/).

## Deploy to Nexus Repository

Database maven plugin publishing to Nexus Repository goes automatically with next command:
```bash
mvn clean deploy -P integration-tests,release
```
Snapshot and release versions available for publishing.

## Deploy to Maven Central Repository

At the moment database maven plugin publishing to [Maven Repository](https://mvnrepository.com) goes in manual mode on Nexus Repository Manager web site.
Only release versions available for publishing.
