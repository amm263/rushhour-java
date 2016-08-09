package rushhour.java;

import java.util.ArrayList;
import java.util.Arrays;
import rushhour.java.Model.BoardTreeNode;

public class Controller {
    
    Boolean verbose = false;
    Model model;
    
    public Controller(Model model, Boolean verbose) {
	this.model = model;
	this.verbose = verbose;
    }
    
    public Controller(Model model) {
	this.model = model;
    }
    
    
    public boolean play() {
        int depth = 0;
        Boolean moved;
        ArrayList<BoardTreeNode> curboardlist; 
        ArrayList<Car> carlist;
        String[] curframe;
        String[] newframe;
        char[] buffer;
        String newrow;
        while (true) {
            curboardlist = (ArrayList<BoardTreeNode>) model.get_node(depth).clone();
            depth++;
            moved = false;
            System.out.format("Depth:"+depth+"\n");
            for (BoardTreeNode board : curboardlist) {
                curframe = board.get_frame();
                carlist = this.get_car_list(curframe);
                for (Car car : carlist) {
                    if (car.carId == 'r') {
                        if (car.tail_y+car.size == model.cols()) {
                            model.save_solution(board, depth);
                            return true;
                        }
                    }
                    if (car.alignment == '-') {
                        //Move Left
                        if ((car.tail_y > 0) && (curframe[car.tail_x].charAt(car.tail_y-1) == '.')) {
                            newframe = curframe.clone();
                            buffer = newframe[car.tail_x].toCharArray();
                            buffer[car.tail_y-1] = car.carId;
                            buffer[car.tail_y+car.size-1] = '.';
                            newrow = new String(buffer);
                            newframe[car.tail_x] = newrow;
                            if (!this.model.find_frame(newframe)) {
                                this.model.append_frame(depth, newframe, board, car.carId, "Left");
                                moved = true;
                            }
                        }
                        //Move Right
                        if ((car.tail_y+car.size < model.cols()) && (curframe[car.tail_x].charAt(car.tail_y+car.size) == '.')) {
                            newframe = curframe.clone();
                            buffer = newframe[car.tail_x].toCharArray();
                            buffer[car.tail_y+car.size] = car.carId;
                            buffer[car.tail_y] = '.';
                            newrow = new String(buffer);
                            newframe[car.tail_x] = newrow;
                            if (!this.model.find_frame(newframe)) {
                                this.model.append_frame(depth, newframe, board, car.carId, "Right");
                                moved = true;
                            }
                        }
                    } else {
                        //Move Up
                        if ((car.tail_x > 0) && (curframe[car.tail_x-1].charAt(car.tail_y) == '.')) {
                            newframe = curframe.clone();
                            buffer = newframe[car.tail_x-1].toCharArray();
                            buffer[car.tail_y] = car.carId;
                            newrow = new String(buffer);
                            newframe[car.tail_x-1] = newrow;
                            buffer = newframe[car.tail_x+car.size-1].toCharArray();
                            buffer[car.tail_y] = '.';
                            newrow = new String(buffer);
                            newframe[car.tail_x+car.size-1] = newrow;
                            if (!this.model.find_frame(newframe)) {
                                this.model.append_frame(depth, newframe, board, car.carId, "Up");
                                moved = true;
                            }
                        }
                        //Move Down
                        if ((car.tail_x+car.size > model.rows()) && (curframe[car.tail_x+car.size].charAt(car.tail_y) == '.')) {
                            newframe = curframe.clone();
                            buffer = newframe[car.tail_x].toCharArray();
                            buffer[car.tail_y] = '.';
                            newrow = new String(buffer);
                            newframe[car.tail_x] = newrow;
                            buffer = newframe[car.tail_x+car.size].toCharArray();
                            buffer[car.tail_y] = car.carId;
                            newrow = new String(buffer);
                            newframe[car.tail_x+car.size] = newrow;
                            if (!this.model.find_frame(newframe)) {
                                this.model.append_frame(depth, newframe, board, car.carId, "Down");
                                moved = true;
                            }
                        }
                        
                    }
                }
            }
            if (!moved)     //Can't move anymore, no solutions found.
                return false;
        }
    }
				
        
    private ArrayList<Car> get_car_list(String[] frame) {
        ArrayList<Car> car_list = new ArrayList<>();   
        char carId;
        char alignment;
        int tail_x;
        int tail_y;
        int size;
        boolean already_found;
        for (int x=0; x < model.rows(); x++) {
            for (int y=0; y < model.cols(); y++) {
                carId = frame[x].charAt(y);
                if ((carId >= 'a' && carId <= 'z') || (carId >= 'A' && carId <= 'Z')) {
                    already_found = false;
                    // Check if car is already in list
                    for (Car car : car_list) {
                        if (carId == car.carId) {
                            already_found = true;
                            break;
                        }
                    }
                    if (!already_found) {
                        carId = frame[x].charAt(y);
                        tail_x = x;
                        tail_y = y;
                        size = 1;
                        alignment = '\0';
                        int i;
                        //Check DOWN
                        if ( x < model.rows() ) {
                            for (i = x+1; i < model.rows(); i++) {
                                if (frame[i].charAt(y) == carId ) {
                                    size += 1;
                                    alignment = '|';
                                } else {
                                    break;
                                }
                            }
                        }
                        // Check RIGHT
                        if ( y < model.cols() ) {
                            for (i = y+1; i < model.cols(); i++) {
                                if (frame[x].charAt(i) == carId) {
                                    size +=1;
                                    alignment = '-';
                                } else {
                                    break;
                                }
                            }
                        }
                        if (alignment != '\0') {
                                car_list.add(new Car(carId, alignment, size, tail_x, tail_y));
                        } else {
                            System.out.format("Error: car "+carId+" size is "+size+" and it's not aligned!");
                            System.exit(-1);
                        }
                    
                    }
                }
            }
        }
        return car_list;
    }       
    
            
    private class Car {
            
        private final char carId;
        private final char alignment;
        private final int size;
        private final int tail_x;
        private final int tail_y;
            
        private Car(char carId, char alignment, int size, int tail_x, int tail_y) {
            this.carId = carId;
            this.alignment = alignment;
            this.size = size;
            this.tail_x = tail_x;
            this.tail_y = tail_y;   
        }
    }
}
