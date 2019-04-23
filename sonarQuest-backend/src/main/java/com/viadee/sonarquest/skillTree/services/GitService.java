package com.viadee.sonarquest.skillTree.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.Merger;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.io.DisabledOutputStream;
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
				File parentDir = repo.getDirectory();
				if (parentDir.isDirectory() && parentDir.list().length == 0) {
					git = Git.cloneRepository().setURI("https://github.com/viadee/sonarQuest.git").setBare(true)
							.setCloneAllBranches(true).setDirectory(new File("D:/clone/")).call();
				} else {
					git = Git.open(new File("D:/clone/"));
				}

			} catch (GitAPIException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static boolean isDirEmpty(final Path directory) throws IOException {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
			return !dirStream.iterator().hasNext();
		}
	}

	public void readCommits() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(
					new File("D:/Git/sonarQuest/sonarQuest-backend/src/main/resources/export/commits.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		RevWalk walk = new RevWalk(repo);
		List<Ref> branches = null;

		try {
			branches = new Git(repo).branchList().call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		try {

			// for (Ref branch : branches) {
			String branchName = branches.get(0).getName();
			out.println("***************************************************");
			out.println(branchName);

			Iterable<RevCommit> commits = null;
			try {
				commits = git.log().add(branches.get(0).getObjectId()).call();
			} catch (GitAPIException | IOException e) {
				e.printStackTrace();
			}

			for (RevCommit commit : commits) {

				out.println(commit.getAuthorIdent().getEmailAddress());
				out.println(commit.getShortMessage());
				out.println(commit.getCommitTime() + " | " + commit.getId());
				if (commit.getParentCount() > 1) {
					out.println("Is Merge");
				} else {
					out.println("Is NOT Merge");
				}

				try {
					ObjectId head = repo.resolve(Constants.HEAD);
					if (commit.getParentCount() != 0) {
						out.println(diffCommit(commit.getId()));
//								RevCommit parent = walk.parseCommit(commit.getParent(0).getId());
//								DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
//								df.setRepository(repo);
//								df.setDiffComparator(RawTextComparator.DEFAULT);
//								df.setDetectRenames(true);
//								List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
//								for (DiffEntry diff : diffs) {
//								    out.println("Files:"+MessageFormat.format("({0} {1} {2}", diff.getChangeType().name(), diff.getNewMode().getBits(), diff.getNewPath())+"\n");
//								}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			// }
		} catch (RevisionSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			out.close();
			LOGGER.info("Read Commits done");
		}

	}

	public void checkMerge() {
		/*
		 * try (RevWalk revWalk = new RevWalk(repo)) { RevCommit masterHead = null; try
		 * { AnyObjectId masterId =
		 * git.getRepository().exactRef("refs/heads/master").getObjectId(); masterHead =
		 * revWalk.parseCommit(masterId); } catch (RevisionSyntaxException | IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * Iterable<RevCommit> commits = null; try { commits = git.log().all().call(); }
		 * catch (GitAPIException | IOException e) { e.printStackTrace(); }
		 * 
		 * for (RevCommit commit : commits) { try { commit =
		 * revWalk.parseCommit(commit.getId()); if
		 * (revWalk.isMergedInto(masterHead,commit)) { LOGGER.info("Commit " + commit +
		 * " is merged into master"); } else { LOGGER.info("Commit " + commit +
		 * " is NOT merged into master"); } } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 * 
		 * revWalk.dispose(); }
		 */

	}

	private String diffCommit(AnyObjectId hashID) throws IOException {
		// Initialize repositories.

		// Get the commit you are looking for.
		RevCommit newCommit;
		try (RevWalk walk = new RevWalk(repo)) {
			newCommit = walk.parseCommit(hashID);
		}

//	    System.out.println("LogCommit: " + newCommit);
//	    String logMessage = newCommit.getFullMessage();
//	    System.out.println("LogMessage: " + logMessage);
		// Print diff of the commit with the previous one.
		// System.out.println(getDiffOfCommit(newCommit));
		return getDiffOfCommit(newCommit);

	}

	// Helper gets the diff as a string.
	private String getDiffOfCommit(RevCommit newCommit) throws IOException {
		String returnDiff = null;
		// Get commit that is previous to the current one.
		RevCommit oldCommit = getPrevHash(newCommit);
		if (oldCommit == null) {
			return "Start of repo";
		}
		// Use treeIterator to diff.
		AbstractTreeIterator oldTreeIterator = getCanonicalTreeParser(oldCommit);
		AbstractTreeIterator newTreeIterator = getCanonicalTreeParser(newCommit);
//	    OutputStream outputStream = new ByteArrayOutputStream();
//	    try (DiffFormatter formatter = new DiffFormatter(outputStream)) {
//	        formatter.setRepository(git.getRepository());
//	        formatter.format(oldTreeIterator, newTreeIterator);
//	    }
//	    String diff = outputStream.toString();
//	    return diff;
		
				OutputStream outputStream = DisabledOutputStream.INSTANCE;
				 DiffFormatter formatter = new DiffFormatter( outputStream );
				  formatter.setRepository( git.getRepository() );
				  List<DiffEntry> entries = formatter.scan( oldTreeIterator, newTreeIterator );
				  if(entries != null && entries.size()>0) {
					  FileHeader fileHeader = formatter.toFileHeader( entries.get( 0 ) );
						List<Edit> edits = fileHeader.toEditList();
						
						for(Edit edit: edits) {
							returnDiff=returnDiff+edit+"\n";
						}
				  }
				
				
		return returnDiff;
	}

	// Helper function to get the previous commit.
	public RevCommit getPrevHash(RevCommit commit) throws IOException {

		try (RevWalk walk = new RevWalk(repo)) {
			// Starting point
			walk.markStart(commit);
			int count = 0;
			for (RevCommit rev : walk) {
				// got the previous commit.
				if (count == 1) {
					return rev;
				}
				count++;
			}
			walk.dispose();
		}
		// Reached end and no previous commits.
		return null;
	}

	private AbstractTreeIterator getCanonicalTreeParser(ObjectId commitId) throws IOException {
		try (RevWalk walk = new RevWalk(git.getRepository())) {
			RevCommit commit = walk.parseCommit(commitId);
			ObjectId treeId = commit.getTree().getId();
			try (ObjectReader reader = git.getRepository().newObjectReader()) {
				return new CanonicalTreeParser(null, reader, treeId);
			}
		}
	}

}
