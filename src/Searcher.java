import java.util.*;

public class Searcher {
    private int[] startState = new int[16];
    private int count = 1;
    private List<Integer> avg = new ArrayList<>(); //stores dfs results

    public static void main(String[] args) {
        Searcher s = new Searcher();
        s.init();
        Node root = new Node(s.startState);
        long startTime = System.nanoTime();
        List<Node> solutions = s.aStar(root);
        s.printPath(solutions);
        s.dfsAverage(root);
        long endTime = System.nanoTime();
        System.out.println((double)(endTime - startTime)/1000000000);
    }

    private void dfsAverage(Node root){
        List<Node> solutions = this.aStar(root);
        this.printPath(solutions);
        for(Node n : solutions){
            //run dfs 100 times to get an average
            for(int i = 0; i < 100; i++){
                Node newNode = new Node(n.getConfiguration());
                this.dfs(newNode);
            }
        }
    }

    // this method shows how I count expanded nodes in IDS
    private void idsCount(Node root){
        List<Node> solutions = this.aStar(root);
        this.printPath(solutions);
        for(Node n : solutions){
             Node newNode = new Node(n.getConfiguration());
             this.count = 0;
             this.ids(newNode);
        }
    }

    private void init(){
        for(int i = 0; i < this.startState.length; i++){
            if(i == 12){
                this.startState[12] = 1;
            }else if(i == 13){
                this.startState[13] = 2;
            }else if(i == 14){
                this.startState[14] = 3;
            }else if(i == 15){
                this.startState[15] = 4;
            }else{
                this.startState[i] = 0;
            }
        }
    }

    private void testInit(){
        for(int i = 0; i < startState.length; i++){
            if(i == 5){
                startState[i] = 1;
            }else if(i == 8){
                startState[i] = 2;
            }else if(i == 13){
                startState[i] = 3;
            }else if(i == 11){
                startState[i] = 4;
            }else{
                startState[i] = 0;
            }
        }
    }

    private List<Node> ids(Node root){
        for(int depth = 0; depth < Integer.MAX_VALUE; depth++){
            Node found = dls(root,depth);
            if(found != null){
                return found.tracePath();
            }
        }
        return null;
    }

    private Node dls(Node root, int depth){
        root.resetChildren();
        //visited list is only used for graph search
       // List<Node> visited = new ArrayList<>();
        Stack<Node> toVisit = new Stack<>();
        toVisit.push(root);

        while(!toVisit.isEmpty()){
            Node current = toVisit.pop();
            this.count++;
           // visited.add(current);

            if(current.isGoal()){
                System.out.println("Goal found");
                System.out.println(this.count);
                return current;
            }

            // check if max depth is reached
            if(current.tracePath().size() - 1 != depth) {
                current.expandAll();

                for (Node child : current.getChildren()) {
                    // this check is only done for graph searches
//                    if(!hasNode(visited,child) && !hasNode(toVisit,child)){
//                        toVisit.push(child);
//                    }
                    toVisit.push(child);
                }
            }
        }

        return null;
    }

    private  List<Node> dfs(Node root){
        //visited list is only used for graph search
       // List<Node> visited = new ArrayList<>();
        Stack<Node> toVisit = new Stack<>();
        List<Node> path = new ArrayList<>();
        boolean goalFound = false;

        toVisit.push(root);
        int count = 0;

        while (!goalFound){
            Node current = toVisit.pop();
          // visited.add(current);
            count++; // counts the number of expanded nodes

            if(current.isGoal()){
                System.out.println("Goal found");
                goalFound = true;
                path = current.tracePath();
                avg.add(count);
            }else{
                current.expandAll();
                Collections.shuffle(current.getChildren());

                for(Node child : current.getChildren()){
                    // this check is only done for graph searches
//                    if(!hasNode(visited,child) && !hasNode(toVisit,child)){
//                        toVisit.push(child);
//                    }
                    count++;
                    toVisit.push(child);
                }
            }
        }

        // get the average of 100 results
        if(avg.size() == 100){
            System.out.println(Math.round(avg.stream().mapToInt(val -> val).average().orElse(0.0)));
            avg.clear();
        }

        return path;
    }

    private List<Node> aStar(Node root){
        //visited list is only used for graph search
       // List<Node> visited = new ArrayList<>();
        PriorityQueue<Node> toVisit = new PriorityQueue<>();
        List<Node> path = new ArrayList<>();
        boolean goalFound = false;

        toVisit.add(root);
        int count = 0;

        while(!goalFound){
            Node currentNode = toVisit.poll();
            count++;
           // visited.add(currentNode);

            currentNode.expandAll();

            if(currentNode.isGoal()) {
                System.out.println("Goal found");
                goalFound = true;
                path = currentNode.tracePath();
                System.out.println(count);
            }else{
                for(Node child : currentNode.getChildren()){
                    // this check is only done for graph searches
//                    if(!hasNode(visited,child) && !toVisit.contains(child)){
//                        toVisit.add(child);
//                    }

                    toVisit.add(child);
                }
            }
        }

        return path;
    }

    private List<Node> bfs(Node root){
        Queue<Node> toVisit = new LinkedList<>();
        //visited list is only used for graph search
       // List<Node> visited = new ArrayList<>();
        List<Node> path = new ArrayList<>();
        boolean goalFound = false;

        toVisit.add(root);
        int count = 0;

        while(!goalFound){
            Node currentNode = toVisit.poll();
            count++;
           // visited.add(currentNode);


            if(currentNode.isGoal()) {
                System.out.println("Goal found");
                goalFound = true;
                path = currentNode.tracePath();
                System.out.println(count);
            }
            currentNode.expandAll();

            for(Node child : currentNode.getChildren()){
                // this check is only done for graph searches
//                if(!hasNode(visited,child) && !toVisit.contains(child)){
//                    toVisit.add(child);
//                }

                toVisit.add(child);
            }
        }

        return path;
    }

    private boolean hasNode(List<Node> list, Node n){
        boolean hasNode = false;

        for(int i = 0; i < list.size(); i++){

            if(list.get(i).isSameConfiguration(n.getConfiguration())){
                hasNode = true;
            }
        }

        return hasNode;
    }

    private void printPath(List<Node> path){
        for(Node n : path){
            n.printConfiguration();
            System.out.println();
        }
    }
}
