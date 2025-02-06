package xaero.pac.common.server.io;

import java.nio.file.Path;

public class FilePathConfig {

	private final Path path;
	private final boolean loadRecursively;

	public FilePathConfig(Path path, boolean loadRecursively) {
		this.path = path;
		this.loadRecursively = loadRecursively;
	}

	public Path getPath() {
		return path;
	}

	public boolean isLoadRecursively() {
		return loadRecursively;
	}

}
