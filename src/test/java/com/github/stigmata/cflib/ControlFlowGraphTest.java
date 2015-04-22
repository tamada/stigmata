package com.github.stigmata.cflib;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.github.stigmata.cflib.ControlFlowGraph;
import com.github.stigmata.cflib.ControlFlowGraphExtractVisitor;

public class ControlFlowGraphTest {
    private ControlFlowGraphExtractVisitor cfVisitor;
    private ControlFlowGraphExtractVisitor cfVisitor2;

    @Before
    public void setUp() throws Exception{
        ClassReader reader1 = new ClassReader(getClass().getResource("/resources/MyServer.class").openStream());
        cfVisitor = new ControlFlowGraphExtractVisitor(new ClassWriter(0));
        reader1.accept(cfVisitor, 0);

        ClassReader reader2 = new ClassReader(getClass().getResource("/resources/MyServer2.class").openStream());
        cfVisitor2 = new ControlFlowGraphExtractVisitor(new ClassWriter(0));
        reader2.accept(cfVisitor2, 0);
    }

    @Test
    public void testBasic() throws Exception{
        Iterator<String> iterator = cfVisitor.getMethodNames();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("<init>(I)V", iterator.next());
        Assert.assertFalse(iterator.hasNext());

        ControlFlowGraph graph = cfVisitor.getGraph("<init>(I)V");
        Assert.assertEquals(6, graph.getBasicBlockSize());
        graph.setIncludingExceptionFlow(true);

        Assert.assertEquals(6, graph.getBasicBlockSize());
    }

    @Test
    public void testGraph() throws Exception{
        int[][] graph = cfVisitor.getGraph("<init>(I)V").getGraphMatrix();

        Assert.assertEquals(6, graph.length);

        Assert.assertEquals(0, graph[0][0]);
        Assert.assertEquals(1, graph[0][1]);
        Assert.assertEquals(1, graph[0][2]);
        Assert.assertEquals(0, graph[0][3]);
        Assert.assertEquals(0, graph[0][4]);
        Assert.assertEquals(0, graph[0][5]);

        Assert.assertEquals(0, graph[1][0]);
        Assert.assertEquals(0, graph[1][1]);
        Assert.assertEquals(0, graph[1][2]);
        Assert.assertEquals(1, graph[1][3]);
        Assert.assertEquals(0, graph[1][4]);
        Assert.assertEquals(0, graph[1][5]);

        Assert.assertEquals(0, graph[2][0]);
        Assert.assertEquals(0, graph[2][1]);
        Assert.assertEquals(0, graph[2][2]);
        Assert.assertEquals(1, graph[2][3]);
        Assert.assertEquals(0, graph[2][4]);
        Assert.assertEquals(0, graph[2][5]);

        Assert.assertEquals(0, graph[3][0]);
        Assert.assertEquals(0, graph[3][1]);
        Assert.assertEquals(0, graph[3][2]);
        Assert.assertEquals(0, graph[3][3]);
        Assert.assertEquals(0, graph[3][4]);
        Assert.assertEquals(1, graph[3][5]);

        Assert.assertEquals(0, graph[4][0]);
        Assert.assertEquals(0, graph[4][1]);
        Assert.assertEquals(0, graph[4][2]);
        Assert.assertEquals(0, graph[4][3]);
        Assert.assertEquals(0, graph[4][4]);
        Assert.assertEquals(1, graph[4][5]);

        Assert.assertEquals(0, graph[5][0]);
        Assert.assertEquals(0, graph[5][1]);
        Assert.assertEquals(0, graph[5][2]);
        Assert.assertEquals(0, graph[5][3]);
        Assert.assertEquals(0, graph[5][4]);
        Assert.assertEquals(0, graph[5][5]);
    }

    @Test
    public void testExceptionGraph() throws Exception{
        ControlFlowGraph cfgraph = cfVisitor.getGraph("<init>(I)V");
        cfgraph.setIncludingExceptionFlow(true);
        int[][] graph = cfgraph.getGraphMatrix();

        Assert.assertEquals(6, graph.length);

        Assert.assertEquals(0, graph[0][0]);
        Assert.assertEquals(1, graph[0][1]);
        Assert.assertEquals(1, graph[0][2]);
        Assert.assertEquals(0, graph[0][3]);
        Assert.assertEquals(1, graph[0][4]);
        Assert.assertEquals(0, graph[0][5]);

        Assert.assertEquals(0, graph[1][0]);
        Assert.assertEquals(0, graph[1][1]);
        Assert.assertEquals(0, graph[1][2]);
        Assert.assertEquals(1, graph[1][3]);
        Assert.assertEquals(1, graph[1][4]);
        Assert.assertEquals(0, graph[1][5]);

        Assert.assertEquals(0, graph[2][0]);
        Assert.assertEquals(0, graph[2][1]);
        Assert.assertEquals(0, graph[2][2]);
        Assert.assertEquals(1, graph[2][3]);
        Assert.assertEquals(1, graph[2][4]);
        Assert.assertEquals(0, graph[2][5]);

        Assert.assertEquals(0, graph[3][0]);
        Assert.assertEquals(0, graph[3][1]);
        Assert.assertEquals(0, graph[3][2]);
        Assert.assertEquals(0, graph[3][3]);
        Assert.assertEquals(1, graph[3][4]);
        Assert.assertEquals(1, graph[3][5]);

        Assert.assertEquals(0, graph[4][0]);
        Assert.assertEquals(0, graph[4][1]);
        Assert.assertEquals(0, graph[4][2]);
        Assert.assertEquals(0, graph[4][3]);
        Assert.assertEquals(0, graph[4][4]);
        Assert.assertEquals(1, graph[4][5]);

        Assert.assertEquals(0, graph[5][0]);
        Assert.assertEquals(0, graph[5][1]);
        Assert.assertEquals(0, graph[5][2]);
        Assert.assertEquals(0, graph[5][3]);
        Assert.assertEquals(0, graph[5][4]);
        Assert.assertEquals(0, graph[5][5]);
    }

    @Test
    public void testBasic2() throws Exception{
        Iterator<String> iterator = cfVisitor2.getMethodNames();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("<init>(I)V", iterator.next());
        Assert.assertFalse(iterator.hasNext());

        ControlFlowGraph graph = cfVisitor2.getGraph("<init>(I)V");

        Assert.assertEquals(6, graph.getBasicBlockSize());

        // graph.setIncludingExceptionFlow(true);
        // Assert.assertEquals(6, graph.getBasicBlockSize());
    }

    @Test
    public void testGraph2() throws Exception{
        ControlFlowGraph graph = cfVisitor2.getGraph("<init>(I)V");
        int[][] graphMatrix = graph.getGraphMatrix();

        Assert.assertEquals(6, graphMatrix.length);

        Assert.assertEquals(0, graphMatrix[0][0]);
        Assert.assertEquals(1, graphMatrix[0][1]);
        Assert.assertEquals(1, graphMatrix[0][2]);
        Assert.assertEquals(0, graphMatrix[0][3]);
        Assert.assertEquals(0, graphMatrix[0][4]);
        Assert.assertEquals(0, graphMatrix[0][5]);

        Assert.assertEquals(0, graphMatrix[1][0]);
        Assert.assertEquals(0, graphMatrix[1][1]);
        Assert.assertEquals(0, graphMatrix[1][2]);
        Assert.assertEquals(1, graphMatrix[1][3]);
        Assert.assertEquals(0, graphMatrix[1][4]);
        Assert.assertEquals(0, graphMatrix[1][5]);

        Assert.assertEquals(0, graphMatrix[2][0]);
        Assert.assertEquals(0, graphMatrix[2][1]);
        Assert.assertEquals(0, graphMatrix[2][2]);
        Assert.assertEquals(1, graphMatrix[2][3]);
        Assert.assertEquals(0, graphMatrix[2][4]);
        Assert.assertEquals(0, graphMatrix[2][5]);

        Assert.assertEquals(0, graphMatrix[3][0]);
        Assert.assertEquals(0, graphMatrix[3][1]);
        Assert.assertEquals(0, graphMatrix[3][2]);
        Assert.assertEquals(0, graphMatrix[3][3]);
        Assert.assertEquals(0, graphMatrix[3][4]);
        Assert.assertEquals(1, graphMatrix[3][5]);

        Assert.assertEquals(0, graphMatrix[4][0]);
        Assert.assertEquals(0, graphMatrix[4][1]);
        Assert.assertEquals(0, graphMatrix[4][2]);
        Assert.assertEquals(0, graphMatrix[4][3]);
        Assert.assertEquals(0, graphMatrix[4][4]);
        Assert.assertEquals(1, graphMatrix[4][5]);

        Assert.assertEquals(0, graphMatrix[5][0]);
        Assert.assertEquals(0, graphMatrix[5][1]);
        Assert.assertEquals(0, graphMatrix[5][2]);
        Assert.assertEquals(0, graphMatrix[5][3]);
        Assert.assertEquals(0, graphMatrix[5][4]);
        Assert.assertEquals(0, graphMatrix[5][5]);
    }
}
