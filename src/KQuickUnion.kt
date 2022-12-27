
// Enable JVM asserts with execute options
fun main(args: Array<String>) {
    test1NotConnected()
    test2Connected()
    test3FindMax()
}

private const val nSize = 10

fun test1NotConnected() {
    val quickUnion = KQuickUnion(nSize)

    // Connecting 1,2,3,4
    quickUnion.union(1, 2)
    quickUnion.union(2,3)
    quickUnion.union(3, 4)

    // Connecting 5,6,7
    quickUnion.union(5,6)
    quickUnion.union(6, 7)
    assert(!quickUnion.isConnected(4, 7)) {
        "Values 4 & 7 should NOT BE CONNECTED"
    }
    println("Test 1 Success")
}

fun test2Connected() {
    val quickUnion = KQuickUnion(nSize)

    // Connecting 1,2,3,4
    quickUnion.union(1, 2)
    quickUnion.union(2,3)
    quickUnion.union(3, 4)

    // Connecting 5,6,7
    quickUnion.union(5,6)
    quickUnion.union(6, 7)

    // Connecting both sets
    quickUnion.union(2, 7)
    assert(quickUnion.isConnected(4, 7)) {
        "Values 4 & 7 should BE CONNECTED"
    }
    println("Test 2 Success")
}


fun test3FindMax() {
    val quickUnion = KQuickUnion(nSize)

    // Connecting 1,2,3,4,8,9
    quickUnion.union(1, 2)
    quickUnion.union(2,3)
    quickUnion.union(3, 4)
    quickUnion.union(8, 9)
    quickUnion.union(2, 9)

    // Connecting 5, 6
    quickUnion.union(5, 6)

    assert(quickUnion.findMaxConnected(3) == 9) {
        "MAX1 should be 9"
    }
    assert(quickUnion.findMaxConnected(9) == 9) {
        "MAX2 should be 9"
    }
    assert(quickUnion.findMaxConnected(5) == 6) {
        "MAX3 should be 6"
    }
    println("Test 3 Success")
}

class KQuickUnion(n: Int) {

    private var roots = IntArray(n) { it }
    private var treeSize = IntArray(n) { 1 }
    private var max = IntArray(n) { it }

    fun union(idx1: Int, idx2: Int) {
        val root1 = findRoot(idx1)
        val root2 = findRoot(idx2)

        if (root1 == root2)
            return

        if (treeSize[root1] < treeSize[root2]) {
            roots[root1] = root2
            treeSize[root2] += treeSize[root1]
        } else {
            roots[root2] = root1
            treeSize[root1] += treeSize[root2]
        }

        // Calculating Max on every step to have MAX value for the connected set
        if (max[root1] > max[root2]) {
            max[root2] = max[root1]
        } else {
            max[root1] = max[root2]
        }
    }

    fun isConnected(idx1: Int, idx2: Int): Boolean =
        findRoot(idx1) == findRoot(idx2)

    fun findMaxConnected(idx: Int): Int =
        max[findRoot(idx)]

    private fun findRoot(idx: Int): Int {
        var root = idx
        while(root != roots[root]) {
            root = roots[root]
        }
        return root
    }
}