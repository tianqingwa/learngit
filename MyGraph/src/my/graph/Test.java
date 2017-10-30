 package my.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;;

public class Test {

	public static void main(String[] args) {

		Graph graph = new Graph();
		char[] vexs = { 'A', 'B', 'C', 'D', 'E' };
		char[][] edges = new char[][] { { 'A', 'B', 1 }, { 'A', 'E', 1 }, { 'A', 'D', 0 }, { 'D', 'E', 1 },
				 {'C','D',2}}  ;
		graph.init(vexs, edges);
		//bfs(graph);
		int []visited=new int[graph.vNum];
		Alg alg=new Alg();
		int count=alg.dfs(graph);
		System.out.println("=============");
		for(int i=0;i<graph.vNum;++i){
			visited[i]=0;
		}
		Alg.dfs(visited, graph, 0);
		System.out.println(count);

	}

	private static void bfs(Graph graph) {
		HashMap<Character, Boolean> flag = new HashMap<>();
		for (int i = 0; i < graph.vNum; ++i) {
			flag.put(graph.vList[i].data, false);
		}
		ArrayList<VNode> queue = new ArrayList<>();
		queue.add(graph.vList[0]);
		System.out.println("============");
		while (!queue.isEmpty()) {
			VNode temp = queue.get(0);
			//���û�б����ʣ����ʸýڵ㣬Ȼ����ӣ����ñ�־mapΪtrue������ֱ�ӳ���
			if (!flag.get(temp.data)) {
				System.out.println(temp.data);

				flag.put(temp.data, true);//�÷��ʱ�־
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
