package xaero.pac.common.server.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileIOHelper {

	public Path quickFileBackupMove(Path file) throws IOException {
		Path backupPath = null;
		int backupNumber = 0;
		while(Files.exists(backupPath = file.resolveSibling(file.getFileName().toString() + ".backup" + backupNumber))) {
			backupNumber++;
		}
		Files.move(file, backupPath);
		return backupPath;
	}

	public void safeMoveAndReplace(Path from, Path to, boolean backupFrom) throws IOException {
		//just using REPLACE_EXISTING seems to bug out for some people and clear "to" and not moving "from"
		Path backupPath = null;
		Path fromBackupPath = null;
		if(backupFrom) {
			while(true) {//keep trying until we succeed
				try {
					fromBackupPath = quickFileBackupMove(from);
					break;
				} catch(IOException ioe2) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {}
				}
			}
		} else
			fromBackupPath = from;
		if(Files.exists(to))
			backupPath = quickFileBackupMove(to);
		Files.move(fromBackupPath, to);
		if(backupPath != null)
			Files.delete(backupPath);
	}

}
