import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    private int[] configuration;
    private List<Node> children = new ArrayList<>();
    private Node parent;
    private int agentPosition;

    public Node(int[] configuration) {
        this.configuration = new int[configuration.length];
        copy(configuration,this.configuration);
    }

    public void expandAll(){

        // find agent position
        for(int i = 0; i < configuration.length; i++){
            if(configuration[i] == 4){
                agentPosition = i;
            }
        }

        moveRight(configuration,agentPosition);
        moveLeft(configuration,agentPosition);
        moveUp(configuration,agentPosition);
        moveDown(configuration,agentPosition);
    }

    public void moveLeft(int[] configuration, int pos){
        if(pos % 4 != 0){
            int[] childConfiguration = new int[configuration.length];
            copy(configuration,childConfiguration);

            int temp = childConfiguration[pos];
            childConfiguration[pos] = childConfiguration[pos - 1];
            childConfiguration[pos - 1] = temp;

            Node child = new Node(childConfiguration);
            this.children.add(child);
            child.setParent(this);
        }
    }

    public void moveRight(int[] configuration, int pos){
        if(pos % 4 != 3){
            int[] childConfiguration = new int[configuration.length];
            copy(configuration,childConfiguration);

            int temp = childConfiguration[pos];
            childConfiguration[pos] = childConfiguration[pos + 1];
            childConfiguration[pos + 1] = temp;

            Node child = new Node(childConfiguration);
            this.children.add(child);
            child.setParent(this);
        }
    }

    public void moveUp(int[] configuration, int pos){
        if(pos > 3){
            int[] childConfiguration = new int[configuration.length];
            copy(configuration,childConfiguration);

            int temp = childConfiguration[pos];
            childConfiguration[pos] = childConfiguration[pos - 4];
            childConfiguration[pos - 4] = temp;

            Node child = new Node(childConfiguration);
            this.children.add(child);
            child.setParent(this);
        }
    }

    public void moveDown(int[] configuration, int pos){
        if(pos < 12){
            int[] childConfiguration = new int[configuration.length];
            copy(configuration,childConfiguration);

            int temp = childConfiguration[pos];
            childConfiguration[pos] = childConfiguration[pos + 4];
            childConfiguration[pos + 4] = temp;

            Node child = new Node(childConfiguration);
            this.children.add(child);
            child.setParent(this);
        }
    }

    private void copy(int[] original, int[] copy){
        System.arraycopy(original, 0, copy, 0, original.length);
    }

    // calculates how many moves are needed for a block to reach its final position
    public int getDistanceToFinal(int num, int finalPosition){
        int index = findElement(this.getConfiguration(),num);

        if(index % 4 == finalPosition % 4){
            return Math.abs((index - finalPosition)/4);
        }else if(index % 4 == finalPosition % 3){
            return 1 + Math.abs((index + 1 - finalPosition)/4);
        }else if(index % 4 == finalPosition % 5){
            return 1 + Math.abs((index - 1 - finalPosition)/4);
        }else {
            return 2 + Math.abs((index - 2 - finalPosition)/4);
        }
    }

    // calculate the sum of the moves needed for A,b and C to their final positions
    public int getSumOfDistances(){
        int sum = 0;
        int[] elements = new int[3];
        elements[0] = 1;
        elements[1] = 2;
        elements[2] = 3;

        int[] finalPositions = new int[3];
        finalPositions[0] = 5;
        finalPositions[1] = 9;
        finalPositions[2] = 13;

        for(int i = 0; i < elements.length; i++){
            sum += getDistanceToFinal(elements[i], finalPositions[i]);
        }
        return sum;
    }

    // compare by sum of distances
    @Override
    public int compareTo(Node o) {
        if(this.getSumOfDistances() + this.tracePath().size()-1 > o.getSumOfDistances() + o.tracePath().size()-1){
            return 1;
        }else if(this.getSumOfDistances() + this.tracePath().size()-1 < o.getSumOfDistances() + o.tracePath().size()-1){
            return -1;
        }
        return 0;
    }

    // trace how we reached a given node
    public List<Node> tracePath(){
        List<Node> path = new ArrayList<>();
        Node currentNode = this;
        path.add(currentNode);

        while(currentNode.getParent() != null){
            path.add(currentNode.getParent());
            currentNode = currentNode.getParent();
        }
        return path;
    }

    public int findElement(int[] configuration, int element){
        for(int i = 0; i < configuration.length; i++){
            if(configuration[i] == element){
                return i;
            }
        }
        return -1;
    }

    public boolean isGoal(){
        return this.configuration[5] == 1 && this.configuration[9] == 2 && this.configuration[13] == 3;
    }

    public boolean isSameConfiguration(int[] configuration){
        boolean isSame = true;

        for(int i = 0; i < configuration.length; i++){
            if(this.configuration[i] != configuration[i]){
                isSame = false;
            }
        }
        return isSame;
    }

    public void printConfiguration(){
        for(int i = 0; i < this.configuration.length; i++){

            if((i + 1) % 4 == 0){
                System.out.print(this.configuration[i]);
                System.out.println();
            }else{
                System.out.print(this.configuration[i] + " ");
            }
        }
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void resetChildren(){
        this.children.clear();
    }

    public int[] getConfiguration() {
        return configuration;
    }

    public Node getParent() {
        return parent;
    }
}
