import java.awt.BorderLayout;
import java.awt.Button;

import javax.swing.*;
import javax.swing.border.Border;


public class MainWindows extends JFrame
{
	public MainWindows()
	{
		super();
		setLayout(new BorderLayout());
		
		//生成实例
		CommandData commandData = new CommandData();
		PhysicsMemory memory = new PhysicsMemory(commandData);
		ButtonPanel buttonPanel = new ButtonPanel(commandData,memory);
		CommandConditionTable commandConditionTable = new CommandConditionTable(commandData, memory);
		
		//设置窗口其它参数
		setTitle("MemoryManagement  by 1152822_wuxiangyu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 768);
		setLayout(new BorderLayout());
		
		
		add(BorderLayout.SOUTH,buttonPanel);
		add(BorderLayout.CENTER,commandConditionTable);
		add(BorderLayout.NORTH,new MemoryViewPanel(memory));
		add(BorderLayout.EAST,new CommandConditionPanel(commandData, memory));
		setVisible(true);
//		pack();
	}
}
