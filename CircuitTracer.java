import java.awt.List;

import java.awt.Point;

import java.io.FileNotFoundException;

import java.util.ArrayList;

/**
 * 
 * Search for shortest paths between start and end points on a circuit board as
 * 
 * read from an input file using either a stack or queue as the underlying
 * 
 * search state storage structure and displaying output to the console or to a
 * 
 * GUI according to options specified via command-line arguments.
 * 
 * 
 * @author Robbie Gill
 */

public class CircuitTracer {

	private CircuitBoard board;

	private Storage<TraceState> stateStore;

	/**
	 * 
	 * launch the program
	 * 
	 * 
	 * 
	 * @param args
	 * 
	 *            three required arguments: first arg: -s for stack or -q for queue
	 * 
	 *            second arg: -c for console output or -g for GUI output third arg:
	 *            input file name
	 * 
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		if (args.length != 3) {

			printUsage();

			System.exit(1);

		}

		try {

			new CircuitTracer(args);// constructs circuitTracer with given arguments.

		} catch (Exception e) {// in the case an exception isnt caught correctly the stack trace will allow you
								// to trace the point of failure

			e.printStackTrace();

			System.exit(1);

		}

	}

	/** Print instructions for running CircuitTracer from the command line. */

	private static void printUsage() {

		// any command line args error

		System.err.print(" Usage:Java CircuitTracer -s|-q -c|-g Filename");

	}

	/**
	 * 
	 * Set up the CircuitBoard and all other components based on command line
	 * 
	 * arguments.
	 * 
	 * 
	 * 
	 * @param args - command line arguments passed through from main()
	 * 
	 * @throws FileNotFoundException
	 * 
	 */

	private CircuitTracer(String[] args) throws FileNotFoundException {

		// parsing command line args

		String adtVar = args[0]; // argument for stack or -queue

		String mode = args[1]; // argument for -console or -GUI

		String filePath = args[2]; // argument for filename

		// check if parameter is incorrect
		if (adtVar.equals("-s") == false && adtVar.equals("-q") == false) {

			printUsage();

		}
		// condition met if GUI parameter -g is passed.
		if (mode.contentEquals("-g")) {

			System.out.println(" GUI not Implemented");

		}
		// condition met if console parameter -c is passed.
		if (!mode.equals("-c")) {

			printUsage();

		}
		// constructs CircuitTracer using queue implementation.
		if (adtVar.equals("-q")) {

			stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
			// constructs CircuitTracer using stack implementation.
		} else if (adtVar.equals("-s")) {

			stateStore = new Storage<TraceState>(Storage.DataStructure.stack);

		}

		else {

			throw new FileNotFoundException("impropper input please use: -s| -q -c| -g filename");

		}

		// reading in circuit board from the given file

		try {
			board = new CircuitBoard(filePath);

		} catch (FileNotFoundException E) {
			System.exit(1);

		}

		// running the search for most effective paths available

		ArrayList<TraceState> bestPaths = new ArrayList<TraceState>();

		Point startPoint = board.getStartingPoint();

		Point endPoint = board.getEndingPoint();

		TraceState movePath;

		// trace state starting to left adjacent position of the start Point
		if (board.isOpen(startPoint.x - 1, startPoint.y)) {

			movePath = new TraceState(board, startPoint.x - 1, startPoint.y);

			stateStore.store(movePath);

		}
		// trace state starting to right adjacent position of the start Point
		if (board.isOpen(startPoint.x + 1, startPoint.y)) {

			movePath = new TraceState(board, startPoint.x + 1, startPoint.y);

			stateStore.store(movePath);

		}
		// trace state starting in the above adjacent position of the start Point
		if (board.isOpen(startPoint.x, startPoint.y + 1)) {

			movePath = new TraceState(board, startPoint.x, startPoint.y + 1);

			stateStore.store(movePath);

		}
		// trace state starting below adjacent position of the start Point
		if (board.isOpen(startPoint.x, startPoint.y - 1)) {

			movePath = new TraceState(board, startPoint.x, startPoint.y - 1);

			stateStore.store(movePath);

		}

		while (stateStore.isEmpty() == false) {

			TraceState path = stateStore.retrieve();

			if (path.isComplete() == true) {

				if (bestPaths.isEmpty()) {

					bestPaths.add(path);

				} else if (path.pathLength() == bestPaths.get(0).pathLength()) {

					bestPaths.add(path);

				} else if (path.pathLength() < bestPaths.get(0).pathLength()) {

					bestPaths.clear();

					bestPaths.add(path);

				}

			} else {

				// checks position beneath current Trace position
				if (path.isOpen(path.getRow() - 1, path.getCol())) {

					stateStore.store(new TraceState(path, path.getRow() - 1, path.getCol()));

				}
				// checks position to the left of the current Trace position
				if (path.isOpen(path.getRow() + 1, path.getCol())) {

					stateStore.store(new TraceState(path, path.getRow() + 1, path.getCol()));

				}

				if (path.isOpen(path.getRow(), path.getCol() - 1)) {

					stateStore.store(new TraceState(path, path.getRow(), path.getCol() - 1));

				}

				if (path.isOpen(path.getRow(), path.getCol() + 1)) {

					stateStore.store(new TraceState(path, path.getRow(), path.getCol() + 1));

				}

			}

		}
		if (mode.equals("-c")) {

			if (adtVar.equals("-s")) {

				for (int i = 0; i < bestPaths.size(); i++) {

					System.out.println(bestPaths.get(i).toString());

				}

			} else if (adtVar.equals("-q"))

			{

				for (int i = bestPaths.size() - 1; i >= 0; i--) {

					System.out.println(bestPaths.get(i).toString());

				}

			}

		}

	}
}