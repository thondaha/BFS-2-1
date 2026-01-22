import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;

/*
LeetCode 690. Employee Importance
Common step:
- Build a HashMap<id, Employee> for O(1) lookup by id.
*/
public class EmployeeImportance {

    // Employee definition
    static class Employee {
        public int id;
        public int importance;
        public List<Integer> subordinates;

        public Employee(int id, int importance, List<Integer> subs) {
            this.id = id;
            this.importance = importance;
            this.subordinates = subs;
        }
    }

    private HashMap<Integer, Employee> map;

    /*
     APPROACH 1: BFS (Level/Graph traversal using a queue)
     Idea:
     - Put the given id into a queue.
     - Pop an employee id, add their importance to the sum,
       then push all subordinate ids into the queue.
     - Continue until queue is empty.

     Time Complexity: O(n)
       - building the map: O(n)
       - traversal visits each employee in this hierarchy at most once
       - in worst case, hierarchy includes all employees

     Space Complexity: O(n)
       - map uses O(n)
       - queue can hold up to O(n) ids in worst case
     */
    public int getImportance(List<Employee> employees, int id) {
        // Build id -> employee map
        map = new HashMap<>();
        for (Employee employee : employees) {
            map.put(employee.id, employee);
        }

        Queue<Integer> q = new LinkedList<>();
        q.add(id);

        int total = 0;

        while (!q.isEmpty()) {
            int curId = q.poll();
            Employee emp = map.get(curId);

            total += emp.importance;

            for (int subId : emp.subordinates) {
                q.add(subId);
            }
        }

        return total;
    }

    /*
    APPROACH 2: DFS (recursion)
     Idea:
     - Total importance of an employee =
       employee.importance + sum(importance of each subordinate subtree)
     Time Complexity: O(n)
       - building map O(n)
       - DFS visits each reachable employee at most once

     Space Complexity: O(n)
       - map O(n)
       - recursion stack O(h), where h = depth of hierarchy (worst O(n))
     */
    public int getImportanceUsingDFS(List<Employee> employees, int id) {
        map = new HashMap<>();
        for (Employee employee : employees) {
            map.put(employee.id, employee);
        }
        return dfs(id);
    }

    private int dfs(int id) {
        Employee emp = map.get(id);

        int total = emp.importance;
        for (int subId : emp.subordinates) {
            total += dfs(subId);
        }

        return total;
    }

    // Simple main method to test
    public static void main(String[] args) {

        EmployeeImportance solver = new EmployeeImportance();

        Employee e1 = new Employee(1, 5, List.of(2, 3));
        Employee e2 = new Employee(2, 3, new ArrayList<>());
        Employee e3 = new Employee(3, 3, new ArrayList<>());

        List<Employee> employees = List.of(e1, e2, e3);

        int id = 1;

        System.out.println("Input id = " + id);
        System.out.println("BFS Total Importance: " + solver.getImportance(employees, id));
        System.out.println("DFS Total Importance: " + solver.getImportanceUsingDFS(employees, id));

        Employee a1 = new Employee(1, 10, List.of(2));
        Employee a2 = new Employee(2, 5, List.of(3));
        Employee a3 = new Employee(3, 2, new ArrayList<>());

        List<Employee> employees2 = List.of(a1, a2, a3);

        System.out.println();
        System.out.println("Input id = 1 (chain example)");
        System.out.println("BFS Total Importance: " + solver.getImportance(employees2, 1));
        System.out.println("DFS Total Importance: " + solver.getImportanceUsingDFS(employees2, 1));
    }
}
