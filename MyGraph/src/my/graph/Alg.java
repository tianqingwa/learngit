package my.graph;

import java.util.ArrayList;
import java.util.Random;

public class Alg {
	public static int mccp(Graph g) {
		int[] bestS = generateInitialSolution(g);// 生成初始解
		int maxNeighborhood = g.cNum - bestS.length;
		int stopCondition = 0;

		// 改善bests
		int[] s = newSolution(g, bestS);
		// 循环开始
		while (true) {
			if (stopCondition > 5) {
				break;
			}
			s = newSolution(g, bestS);
			while (s.length > bestS.length) {
				bestS = s;
				maxNeighborhood = g.cNum - bestS.length;
				s = newSolution(g, bestS);
			}

			// vns变邻域搜索
			int k = 1;
			// 这里改成循环遍历将s付给s1
			int[] s1 = new int[s.length];
			for (int i = 0; i < s.length; ++i) {
				s1[i] = s[i];
			}
			while (k < maxNeighborhood) {
				s1 = new int[s.length];
				for (int i = 0; i < s.length; ++i) {
					s1[i] = s[i];
				}
				s1 = shake(s1, k, g);
				if (numberOfComponent(s1, g) == 1) {
					s1 = fix(s1, g);
				}
//				System.out.println("s1的长度:"+s1.length);
				s1 = localSearch(s1, g);
				if (s1.length > s.length) {
					s = new int[s1.length];
					for (int i = 0; i < s1.length; ++i) {
						s[i] = s1[i];
					}
					k = 1;
//					k++;
				} else {
					++k;
				}
			}
			if (s.length > bestS.length) {
				bestS = new int[s.length];
				for (int i = 0; i < bestS.length; ++i) {
					bestS[i] = s[i];
				}

				maxNeighborhood = g.cNum - bestS.length;
			}
			stopCondition++;
		}
		return g.cNum - bestS.length;

	}

	public static int numberOfComponent(int[] s1, Graph g) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < s1.length; ++i) {
			list.add(s1[i]);
		}
		int[][] edges = g.edges;
		Graph spanGraph = new Graph();
		spanGraph.init(g.vex, null);
		for (int i = 0; i < list.size(); ++i) {
			for (int j = 0; j < edges.length; ++j) {

				if (edges[j][2] == list.get(i))// 将含有s2[i]颜色的边添加进图中
					spanGraph.addEdges(edges[j][0], edges[j][1], edges[j][2]);
			}
		}
		return dfs(spanGraph);// 返回生成图的联通分支数
	}

	public static int[] localSearch(int[] s1, Graph g) {
		// TODO Auto-generated method stub
		// 如果s'构成的子图连通分支个数大于1
		// 像newsolution一样从s'的补集中添加颜色（用贪心的算法）
		// 比newsolution简单
		ArrayList<Integer> curColor = new ArrayList<>();// 当前颜色的集合
		ArrayList<Integer> remainColor = new ArrayList<>();// 剩余颜色的集合
		for (int i = 0; i < s1.length; ++i) {
			curColor.add(s1[i]);
		}
		for (int i = 0; i < g.color.length; ++i) {
			if (!curColor.contains(g.color[i])) {
				remainColor.add(g.color[i]);
			}
		}
		while (numberOfComponent(curColor, g) > 1) {
			int[] maxComponent = maxNumOfComponent(g, remainColor, curColor);
			if (maxComponent[1] > 1) {
				curColor.add(maxComponent[0]);
				remainColor.remove(Integer.valueOf(maxComponent[0]));
			} else {
				break;
			}
		}
		s1 = new int[curColor.size()];
		for (int i = 0; i < curColor.size(); ++i) {
			s1[i] = curColor.get(i);
		}
		return s1;
	}

	public static int[] maxNumOfComponent(Graph g, ArrayList<Integer> remainColor) {
		// TODO Auto-generated method stub
		int[] maxComponent = { 0, -1 };// 存放最大连通分支个数以及加进来的颜色
		int[][] edges = g.edges;// 存放边
		Graph spanGraph = new Graph();
		spanGraph.init(g.vex, edges);
		// 移除掉包含剩余颜色的边
		for (int i = 0; i < g.color.length; ++i) {
			if (remainColor.contains(g.color[i])) {
				for (int j = 0; j < edges.length; ++j) {
					if (edges[j][2] == g.color[i]) {
						spanGraph.removeEdge(edges[j][0], edges[j][1]);
					}
				}
			}
		}
		for (int k = 0; k < remainColor.size(); ++k) {
			// 把当前剩下的颜色加进子图后的连通分支个数存起来，选一个联通分支个数最大的那个
			int tempcolor = remainColor.get(k);
			for (int i = 0; i < edges.length; ++i) {
				if (edges[i][2] == tempcolor)
					spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
			}
			int temp = dfs(spanGraph);
			// 存储连通分支个数最大的那个颜色以及连通分支个数
			if (maxComponent[1] < temp) {
				maxComponent[0] = tempcolor;
				maxComponent[1] = temp;

			}
			for (int i = 0; i < edges.length; ++i) {
				if (edges[i][2] == tempcolor)
					spanGraph.removeEdge(edges[i][0], edges[i][1]);
			}
		}
		return maxComponent;
	}

	public static int[] fix(int[] s2, Graph g) {
		// TODO Auto-generated method stub
		// 从s2中随机移除颜色，直到s2的连通分支个数大于1
		ArrayList<Integer> list = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < s2.length; ++i) {
			list.add(s2[i]);
		}
		while (numberOfComponent(list, g) <= 1) {
			int nextInt = random.nextInt(list.size());
			list.remove(nextInt);
		}
		s2 = new int[list.size()];
		for (int i = 0; i < s2.length; ++i) {
			s2[i] = list.get(i);
		}
		return s2;
	}

	public static int numberOfComponent(ArrayList<Integer> s2, Graph g) {
		int[][] edges = g.edges;
		Graph spanGraph = new Graph();
		spanGraph.init(g.vex, null);
		for (int i = 0; i < s2.size(); ++i) {
			for (int j = 0; j < edges.length; ++j) {

				if (edges[j][2] == s2.get(i))// 将含有s2[i]颜色的边添加进图中
					spanGraph.addEdges(edges[j][0], edges[j][1], edges[j][2]);
			}
		}
		return dfs(spanGraph);// 返回生成图的联通分支数
	}

	public static int[] shake(int[] s2, int k, Graph g) {
		// 通过向当前解随机添加或者移除k个颜色来生成一个新的解s’
		// 为了多样化解的范围、避免陷入局部最优
		// 在从新的解s’中移除颜色时要检查当前解是否为空
		// 这个方法结束时，S与S'的差异是有k个颜色，就是s与s'的并集减去他俩的交集所得的集合中颜色的个数有k个
		ArrayList<Integer> remainColor = new ArrayList<>();// 剩余的颜色集合
		ArrayList<Integer> newAddColor = new ArrayList<>();// 新添加进的颜色
		ArrayList<Integer> enableColor = new ArrayList<>();// 可以向集合中添加的颜色
		for (int i = 0; i < s2.length; ++i) {
			remainColor.add(s2[i]);
			// tempS.add(s2[i]);
		}

		for (int i = 0; i < g.color.length; i++) {// 将不在s2中的颜色添加进enable color中
			if (!remainColor.contains(g.color[i])) {
				enableColor.add(g.color[i]);
			}
		}

		for (int i = 0; i < k; ++i) {
			Random random = new Random();
			float x = random.nextFloat();// Returns the next pseudorandom,
											// uniformly
			// distributed float value between 0.0 and 1.0
			// from this random number generator's sequence.
			if (x < 0.5 && remainColor.size() > 0) {
				// 随机的从S'中移除一个颜色,这个颜色不移他新添加进去的颜色
				// ，即移除的这个颜色必在s与s'的交集中（可以产生一个0到length-1的随机数。这个数是几就移除第几个//要不把s'先存到一个arraylist中，在处理。）
				int index = random.nextInt(remainColor.size());// 随机生成删除的下标
				remainColor.remove(index);// 移除该颜色
			} else {
				// 随机的添加一个颜色
				if (enableColor.size() > 0) {
					int index = random.nextInt(enableColor.size());
					newAddColor.add(enableColor.get(index));
					enableColor.remove(index);// 移除该颜色
				}

			}
		}
		s2 = new int[remainColor.size() + newAddColor.size()];
		int i = 0;
		// 将shake后的颜色封装到s2数组中
		for (; i < remainColor.size(); ++i) {
			s2[i] = remainColor.get(i);
		}
		for (; i < remainColor.size() + newAddColor.size(); ++i) {
			s2[i] = newAddColor.get(i - remainColor.size());
		}
		return s2;
	}

	public static int[] newSolution(Graph g, int[] bestS) {

		// S是个初始颜色集合为空
		// 从bests的补集的s的补集中选择(best*\s)颜色进来(每次要加联通分支最多的那个进来啊！这事件复杂度还有处理起来可困难多了)，直到再选颜色变为连通图为止
		// 从bests\s中选择颜色，直到不能选为止
		// 互相掺和了bests和best*
		ArrayList<Integer> sList = new ArrayList<>();
		int[] maxComponent = { -1, 0 };
		int[] color = g.color;

		// 把颜色用list存起来，因为接下来要删除颜色出去
		// 贪心
		// 现在能往里加的颜色（best*\s）（因为s为空，所以best*\s就是best*）
		ArrayList<Integer> colorlist = new ArrayList<>();
		ArrayList<Integer> bestsList = new ArrayList<>();
		for (int i = 0; i < bestS.length; ++i) {
			bestsList.add(bestS[i]);
		}
		for (int i = 0; i < color.length; ++i) {
			if (!bestsList.contains(color[i])) {
				colorlist.add(color[i]);
			}
		}

		// 统计添加颜色进去后连通分支的个数
		while (!colorlist.isEmpty()) {
			maxComponent = maxNumOfComponent(g, colorlist, sList);
			// 如果最大连通分支个数大于1了，就添加进去
			if (maxComponent[1] > 1) {
				// 将该颜色从颜色列表中删除
				colorlist.remove(Integer.valueOf(maxComponent[0]));
				// 添加进当前的解s中去
				sList.add(maxComponent[0]);
			} else {
				// 否则的话，就结束循环，执行下一次的循环
				break;
			}
		}

		colorlist = bestsList;
		while (!colorlist.isEmpty()) {
			maxComponent = maxNumOfComponent(g, colorlist, sList);
			// 如果最大连通分支个数大于1了，就添加进去
			if (maxComponent[1] > 1) {
				// 将该颜色从颜色列表中删除
				colorlist.remove(Integer.valueOf(maxComponent[0]));
				// 添加进当前的解s中去
				sList.add(maxComponent[0]);
			} else {
				// 否则的话，就结束循环，执行下一次的循环
				break;
			}
		}

		int[] s = new int[sList.size()];
		for (int i = 0; i < s.length; ++i) {
			s[i] = sList.get(i);
		}

		return s;

	}

	private static int[] maxNumOfComponent(Graph g, ArrayList<Integer> remainColor, ArrayList<Integer> sList) {
		int[] maxComponent = { 0, -1 };// 存放最大连通分支个数以及加进来的颜色
		int[][] edges = g.edges;// 存放边
		Graph spanGraph = new Graph();
		spanGraph.init(g.vex, null);
		//添加已包含的颜色的边
		for (int i = 0; i < sList.size(); ++i) {
			for (int j = 0; j < edges.length; ++j) {
				if (edges[j][2] == sList.get(i)) {
					spanGraph.addEdges(edges[j][0], edges[j][1], edges[j][2]);
				}
			}
		}
		for (int k = 0; k < remainColor.size(); ++k) {
			// 把当前剩下的颜色加进子图后的连通分支个数存起来，选一个联通分支个数最大的那个
			int tempcolor = remainColor.get(k);
			for (int i = 0; i < edges.length; ++i) {
				if (edges[i][2] == tempcolor)
					spanGraph.addEdges(edges[i][0], edges[i][1], edges[i][2]);
			}
			int temp = dfs(spanGraph);
			// 存储连通分支个数最大的那个颜色以及连通分支个数
			if (maxComponent[1] < temp) {
				maxComponent[0] = tempcolor;
				maxComponent[1] = temp;

			}
			for (int i = 0; i < edges.length; ++i) {
				if (edges[i][2] == tempcolor)
					spanGraph.removeEdge(edges[i][0], edges[i][1]);
			}
		}
		return maxComponent;
	}

	public static int[] generateInitialSolution(Graph g) {
		// TODO test this function whether enable
		ArrayList<Integer> bestsList = new ArrayList<>();//
		ArrayList<Integer> colorlist = new ArrayList<>();
		int[] color = g.color;
		int[] maxComponent = { 0, 1 };// {{颜色，连通分支个数}}
		for (int i = 0; i < color.length; ++i) {
			colorlist.add(color[i]);
		}
		while (!colorlist.isEmpty()) {
			maxComponent = maxNumOfComponent(g, colorlist, bestsList);
			// 如果最大连通分支个数大于1了，就添加进去
			// System.out.println("*****联通分支个数：" + maxComponent[1] + " 颜色:" +
			// maxComponent[0]);
			if (maxComponent[1] > 1) {
				// 将该颜色从颜色列表中删除
				colorlist.remove(Integer.valueOf(maxComponent[0]));
				// 添加进当前的解s中去
				bestsList.add(maxComponent[0]);
			} else {
				// 否则的话，就结束循环，执行下一次的循环
				break;
			}
		}
		int[] bests = new int[bestsList.size()];
		for (int i = 0; i < bestsList.size(); ++i) {
			bests[i] = bestsList.get(i);
		}
		return bests;
	}

	public static int dfs(Graph g) {
		int count = 0;
		int[] visited = new int[g.vNum];// 定义一个标志数组
		ArrayList<Integer> stack = new ArrayList<>();
		for (int i = 0; i < g.vNum; ++i) {
			visited[i] = 0;
		}

		for (int i = 0; i < g.vNum; ++i) {
			if (visited[i] == 0) {
				count++;
				visited[i] = 1;
				stack.add(i);
				// System.out.println(g.vList[i].data);

				while (!stack.isEmpty()) {
					int vex = stack.get(stack.size() - 1);
					Edge e = g.vList[vex].firstEdge;
					while (e != null) {// 一次深搜
						if (visited[e.vex] == 1)
							e = e.next;
						else {
							visited[e.vex] = 1;
							stack.add(e.vex);
							// System.out.println(g.vList[e.vex].data);
							e = g.vList[e.vex].firstEdge;
						}
					}
					stack.remove(stack.size() - 1);
				}
			}
		}

		return count;

	}

	// 递归版的深度优先搜索
	public static void dfs(int[] visited, Graph g, int i) {

		if (visited[i] == 0) {
			visited[i] = 1;
			System.out.println(g.vList[i].data);
			Edge e = g.vList[i].firstEdge;
			while (e != null) {
				dfs(visited, g, e.vex);
				e = e.next;
			}
		}

	}

	// public static void bfs(int[] visited, Graph g, int i) {
	// if (visited[i] == 0) {
	// visited[i] = 1;
	// System.out.println(g.vList[i].data);
	// Edge e = g.vList[i].firstEdge;
	// while (e != null) {
	//
	// }
	// }
	// }

}
