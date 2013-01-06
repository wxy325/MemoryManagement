import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

public class MemoryViewPanel extends JPanel implements Observer 
{
	private PhysicsMemory m_PhysicsMemory;
	
	//用带有颜色的textField表示frame状态
	private JTextField conditionBeforeExe[] = new JTextField[PhysicsMemory.MEMORY_SIZE];
	private JTextField conditionAfterExe[] = new JTextField[PhysicsMemory.MEMORY_SIZE];
	
	//颜色
	Color blankColor = new Color(255,255,255);	//空frame
	Color fillColor = new Color(0,255,255);	//有页frame
	Color nowColor = new Color(255,255,0);	//当前执行frame
	
	public MemoryViewPanel(PhysicsMemory physicsMemory) 
	{
		super();
		//register observer
		m_PhysicsMemory = physicsMemory;
		m_PhysicsMemory.addObserver(this);
		
//		JLabel test = new JLabel("aaa");
//		JTextField test2 = new JTextField(" a ");
//
//		test2.setEnabled(false);
//		test2.setBackground(new Color(0, 255, 255));
//		test2.setForeground(new Color(255,255,255));		
//		add(test2);
		for (int i = 0; i < PhysicsMemory.MEMORY_SIZE; i++) 
		{
			conditionBeforeExe[i] = new JTextField();
			conditionBeforeExe[i].setBackground(blankColor);
			conditionBeforeExe[i].setEnabled(false);
			conditionAfterExe[i] = new JTextField();
			conditionAfterExe[i].setBackground(blankColor);
			conditionAfterExe[i].setEnabled(false);
		}
		setLayout(new GridLayout(3,PhysicsMemory.MEMORY_SIZE + 1));
		setBorder(new TitledBorder("Memory"));
		add(new JLabel("Frame Number:"));
		for (int i = 0; i < PhysicsMemory.MEMORY_SIZE; i++) 
		{
			add(new JLabel(""+i));
		}
		add(new JLabel("指令执行前"));
		for (int i = 0; i < PhysicsMemory.MEMORY_SIZE; i++) 
		{
			add(conditionBeforeExe[i]);
		}
		add(new JLabel("指令执行后"));
		for (int i = 0; i < PhysicsMemory.MEMORY_SIZE; i++) 
		{
			add(conditionAfterExe[i]);
		}
	}
	
	private void restart()
	{
		for (int i = 0; i < PhysicsMemory.MEMORY_SIZE; i++) 
		{
			conditionBeforeExe[i].setBackground(blankColor);
			conditionBeforeExe[i].setText("");
			conditionAfterExe[i].setBackground(blankColor);
			conditionAfterExe[i].setText("");
		}
	}
	
	private void refresh()
	{
		for (int i = 0; i < PhysicsMemory.MEMORY_SIZE; i++) 
		{
			conditionBeforeExe[i].setBackground(conditionAfterExe[i].getBackground());
			conditionBeforeExe[i].setText(conditionAfterExe[i].getText());
		}
		for (int i = 0; i < PhysicsMemory.MEMORY_SIZE; i++) 
		{
			int page = -1;
			if ( (page = m_PhysicsMemory.getPageInFrame(i)) == -1) 
			{
				conditionAfterExe[i].setText("");
				conditionAfterExe[i].setBackground(blankColor);
			}
			else
			{
				conditionAfterExe[i].setText("" + page);
				conditionAfterExe[i].setBackground(fillColor);
			}
			if (i == m_PhysicsMemory.getNowFrame())
			{
				conditionAfterExe[i].setBackground(nowColor);
			}
		}
	}
	
	@Override
	public void update(Observable o, Object arg) 
	{
		if (m_PhysicsMemory.getNowFrame() == -1)
		{
			restart();
		}
		else 
		{
			refresh();
		}
	}

}
