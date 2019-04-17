package com.viadee.sonarquest.skillTree.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.viadee.sonarquest.services.ArtefactService;

@Service
public class GitService {

	private Git git;
	private Repository repo;

	private static final Logger LOGGER = LoggerFactory.getLogger(ArtefactService.class);

	public void openRepo() {

		try {
			repo = new FileRepository("D:/clone/");
			 try {
				git = Git.cloneRepository()
						  .setURI( "https://github.com/viadee/sonarQuest.git" )
						  .setDirectory( new File("D:/clone/") )
						  .call();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void readCommits() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new File("D:/Git/sonarQuest/sonarQuest-backend/src/main/resources/export/commits.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		RevWalk walk = new RevWalk(repo);
		List<Ref> branches = null;

		try {
			branches = git.branchList().call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		for (Ref branch : branches) {
			String branchName = branch.getName();
			out.println(branchName);

			Iterable<RevCommit> commits = null;
			try {
				commits = git.log().all().call();
			} catch (GitAPIException | IOException e) {
				e.printStackTrace();
			}

			for (RevCommit commit : commits) {
				out.println(commit.getAuthorIdent().getEmailAddress());
				out.println(commit.getShortMessage());
				out.println(commit.getCommitTime()+" | "+commit.getId());
				//out.println(commit.get);
			}
		}
		
			out.close();
		
	}

}
