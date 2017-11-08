package my.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;;

public class Test {

	public static void main(String[] args) {

		// Graph graph = new Graph();
		// char[] vexs = { 'A', 'B', 'C', 'D', 'E' };
		// char[][] edges = new char[][] { { 'A', 'B', 1 }, { 'A', 'E', 1 }, {
		// 'A', 'D', 0 }, { 'D', 'E', 1 },
		// {'C','D',2}} ;
		// graph.init(vexs, edges);
		// //bfs(graph);
		// int []visited=new int[graph.vNum];
		// Alg alg=new Alg();
		// int count=alg.dfs(graph);
		// System.out.println("=============");
		// for(int i=0;i<graph.vNum;++i){
		// visited[i]=0;
		// }
		// Alg.dfs(visited, graph, 0);
		// System.out.println(count);
		int[][] edges1 = { { 1, 2, 1 }, { 1, 4, 2 }, { 1, 6, 3 }, { 2, 4, 3 }, { 2, 3, 2 }, { 3, 8, 3 }, { 4, 3, 1 },
				{ 4, 5, 2 }, { 6, 5, 2 }, { 5, 7, 3 }, { 5, 8, 1 }, { 6, 8, 1 }, { 7, 8, 2 } };
		Graph graph1 = new Graph();
		int[] vexs1 = { 1, 2, 3, 4, 5, 6, 7, 8 };
		graph1.init(vexs1, edges1);
		// int count=Alg.dfs(graph1);
		// //System.out.println(count);
		// ArrayList<Integer>list=new ArrayList<>();
		// for(int i=0;i<graph1.color.length;++i){
		// list.add(graph1.color[i]);
		// }
		int[] bests = Alg.generateInitialSolution(graph1);
		System.out.println("初始解：");
		for (int i = 0; i < bests.length; ++i) {
			System.out.println(bests[i]);
		}
		
		 System.out.println("改善后的解:");
		 int []s=Alg.newSolution(graph1, bests);
		 for(int i=0;i<s.length;++i){
		 System.out.println(s[i]);
		 }
		 System.out.println("测试抖动:");
		 int[]s2=Alg.shake(s, graph1.color.length-s.length,graph1);
		 for(int i=0;i<s2.length;++i){
		 System.out.println(s2[i]);
		 }
		 System.out.println("测试fix:");
		 int []s3=Alg.fix(s2, graph1);
		 for(int i=0;i<s3.length;++i){
		 System.out.println(s3[i]);
		 }
		GenerateInstance gIn=new GenerateInstance();
		Graph graph=gIn.generateGraph(5,9, 6);
		System.out.println("测试localsearch:");
		//bests = new int[0];
		int[] s4 = Alg.localSearch(bests, graph1);
		for (int i = 0; i < s4.length; ++i) {
			System.out.println(s4[i]);
		}
		System.out.println("测试mccp");
		System.out.println(graph.cNum);
		for(int i=0;i<graph.eNum;++i){
			System.out.println(graph.edges[i][0]+","+graph.edges[i][1]+",颜色:"+graph.edges[i][2]);
		}
		System.out.println(Alg.mccp(graph));

	}

	private static void bfs(Graph graph) {
		HashMap<Integer, Boolean> flag = new HashMap<>();
		for (int i = 0; i < graph.vNum; ++i) {
			flag.put(graph.vList[i].data, false);
		}
		ArrayList<VNode> queue = new ArrayList<>();
		queue.add(graph.vList[0]);
		System.out.println("============");
		while (!queue.isEmpty()) {
			VNode temp = queue.get(0);
			// 如果没有被访问，访问该节点，然后出队，并置标志map为true；否则，直接出队
			if (!flag.get(temp.data)) {
				System.out.println(temp.data);

				flag.put(temp.data, true);// 置访问标志
				queue.remove(0);
				if (temp.firstEdge != null) {
					Edge e = temp.firstEdge;
					while (e != null) {
						queue.add(graph.vList[e.vex]);
						e = e.next;
					}
				}
			} else {
				queue.remove(0);
			}

		}

	}

}
