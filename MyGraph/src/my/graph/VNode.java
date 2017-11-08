package my.graph;

public class VNode {
	int data;
	Edge firstEdge;

	// 添加一条边
	public void add(int v, int color) {
		if (firstEdge == null) {
			firstEdge = new Edge();
			firstEdge.color = color;
			firstEdge.vex = v;
			firstEdge.next = null;
		} else {
			Edge temp = firstEdge;
			while (temp.next != null) {
				temp = temp.next;
			}
			temp.next = new Edge();
			temp.next.color = color;
			temp.next.vex = v;
			temp.next.next = null;
		}

	}

	// 删除一条边
	public boolean remove(int vex1) {
		Edge temp = firstEdge;
		if (temp == null) {
			return false;
		}
		if (temp.vex == vex1) {
			firstEdge = temp.next;
			return true;
		}
		Edge temp2 = temp.next;
		while (temp2 != null) {
			if (temp2.vex == vex1) {
//				System.out.println("fdggfd"+vex1);
				temp.next = temp2.next;// 删除节点
				return true;
			}
			temp = temp2;
			temp2 = temp2.next;
		}
		return false;

	}
}
