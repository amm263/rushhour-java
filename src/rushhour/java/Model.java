/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rushhour.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author andrea
 */
public class Model {
    private final int rows;
    private final int cols;
    private ArrayList<ArrayList<BoardTreeNode>> boardtree = new ArrayList<>();
    private ArrayList<String> solution = new ArrayList<>();
    private ArrayList<String[]> solution_frames = new ArrayList<>();
            
    public Model(int rows, int cols, String[] board){
        this.rows = rows;
        this.cols = cols;
        ArrayList<BoardTreeNode> origin = new ArrayList<>();
        origin.add(new BoardTreeNode(board, null, ' ', ""));
        this.boardtree.add(origin);
    }
    
    public ArrayList<BoardTreeNode> get_node(int index) {
        if (index<boardtree.size())
            return this.boardtree.get(index);
        return null;
    }
    
    public int rows() {
        return this.rows;
    }
    
    public int cols() {
        return this.cols;
    }
    
    public String getSolution(int index) {
        return this.solution.get(index);
    }
    
    public String[] getSolutionFrames(int index) {
        return this.solution_frames.get(index);
    }
    
    public int getSolutionSize() {
        return this.solution.size();
    }
    
    public void append_frame(int depth, String[] frame, BoardTreeNode parent, char carId, String direction) {
        if (boardtree.size()<=depth)
            boardtree.add(new ArrayList<BoardTreeNode>());
        this.boardtree.get(depth).add(new BoardTreeNode(frame, parent, carId, direction));
    }
    
    public Boolean find_frame(String[] frame) {
        for (ArrayList<BoardTreeNode> node : this.boardtree) {
            for (BoardTreeNode board : node) {
                if (Arrays.equals(frame, board.frame))
                    return true;
            }
        }
        return false;
    }
    
    public void save_solution(BoardTreeNode board, int depth) {
        for (int x=depth; x>0; x--) {
            this.solution.add("Move car"+board.carId+" "+board.direction); 
            this.solution_frames.add(board.frame);
            board = board.parent;
        }
        Collections.reverse(this.solution);
        Collections.reverse(this.solution_frames);
    }
    
    public class BoardTreeNode {
        
        private final String[] frame;
        private final BoardTreeNode parent;
        private final char carId;
        private final String direction;
        
	private BoardTreeNode(String[] frame, BoardTreeNode parent, char carId, String direction) {
		this.frame = frame;
		this.parent = parent;
		this.carId = carId;
		this.direction = direction;
        }
        
        public String[] get_frame() {
            return this.frame;
        }
    }   
}
