import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.GridLayout;
import java.util.*;
//已执行条数，剩余条数，缺页数，缺页率，当前命令条数，当前命令所在页，当前命令所在frame，下一条命令编号，下一条命令所在页，下一条生成方式
public class CommandConditionPanel extends JPanel implements Observer
{
	private CommandData m_CommandData;
	private PhysicsMemory m_PhysicsMemory;
	
	private JLabel totalCommandNumber;
	private JLabel restCommandNumber;
	private JLabel pageMissingNumber;
	private JLabel pageMissingRate;
	private JLabel nowCommandNumber;
	private JLabel nowCommandPage;
	private JLabel nowCommandFrame;
	private JLabel nextCommandNumber;
	private JLabel nextCommandPage;
	private JLabel nextCommandMode;
	
	public CommandConditionPanel(CommandData commandData, PhysicsMemory physicsMemory) 
	{
		super();
		//register observer
		m_CommandData = commandData;
		m_CommandData.addObserver(this);
		m_PhysicsMemory = physicsMemory;
		m_PhysicsMemory.addObserver(this);
		
		setLayout(new GridLayout(10,2));
		setBorder(new TitledBorder("Infomation"));
		
		totalCommandNumber = new JLabel();
		restCommandNumber = new JLabel();
		pageMissingNumber = new JLabel();
		pageMissingRate = new JLabel();
		nowCommandNumber = new JLabel();
		nowCommandPage = new JLabel();
		nowCommandFrame = new JLabel();;
		nextCommandNumber = new JLabel();
		nextCommandPage = new JLabel();
		nextCommandMode = new JLabel();
		
		commandRestart();
		memoryRestart();
		
		add(new JLabel("已执行条数:"));
		add(totalCommandNumber);
		add(new JLabel("剩余条数:"));
		add(restCommandNumber);
		add(new JLabel("缺页数:"));
		add(pageMissingNumber);
		add(new JLabel("缺页率:"));
		add(pageMissingRate);
		add(new JLabel("当前命令编号:"));
		add(nowCommandNumber);
		add(new JLabel("当前命令所在page:"));
		add(nowCommandPage);
		add(new JLabel("当前命令所在frame:"));
		add(nowCommandFrame);
		add(new JLabel("下一条命令编号:"));
		add(nextCommandNumber);
		add(new JLabel("下一条命令所在page:"));
		add(nextCommandPage);
		add(new JLabel("下一条命令生成方式:"));
		add(nextCommandMode);
	}
	
	private void commandRestart()
	{
		totalCommandNumber.setText("");
		restCommandNumber.setText("");
		nowCommandNumber.setText("");
		nowCommandPage.setText("");
		nextCommandNumber.setText("" + m_CommandData.getNextCommandNumber());
		nextCommandPage.setText("" + (m_CommandData.getNextCommandNumber()/10));
		nextCommandMode.setText(m_CommandData.getNextCommandExeMode().toString());
	}
	private void commandRefresh()
	{

		totalCommandNumber.setText("" + (CommandData.TOTAL_COMMAND_NUMBER - m_CommandData.getRestCount()));
		restCommandNumber.setText("" + m_CommandData.getRestCount());
		nowCommandNumber.setText("" + m_CommandData.getNowCommandNumber());
		nowCommandPage.setText("" + (m_CommandData.getNowCommandNumber() / 10));
		if (m_CommandData.getNextCommandNumber() != -1)
		{
			nextCommandNumber.setText("" + m_CommandData.getNextCommandNumber());
			nextCommandPage.setText("" + (m_CommandData.getNextCommandNumber()/10));
		}
		else
		{
			nextCommandNumber.setText("");
			nextCommandPage.setText("");
		}
		nextCommandMode.setText(m_CommandData.getNextCommandExeMode().toString());
	}
	private void memoryRestart()
	{
		pageMissingNumber.setText("0");
		pageMissingRate.setText("0");
		nowCommandFrame.setText("");
	}
	private void memoryRefresh()
	{
		pageMissingNumber.setText("" + m_PhysicsMemory.getPageMissingNumber());
		pageMissingRate.setText("" + m_PhysicsMemory.getPageMissingRate());
		nowCommandFrame.setText("" + m_PhysicsMemory.getNowFrame());	
	}
	@Override
	public void update(Observable o, Object arg) 
	{
		if (o instanceof CommandData)
		{
			if (m_CommandData.getRestCount() == CommandData.TOTAL_COMMAND_NUMBER)
			{
				commandRestart();
			}
			else
			{
				commandRefresh();
			}
		}
		else if (o instanceof PhysicsMemory) 
		{
			if (m_PhysicsMemory.getNowFrame() == -1)
			{
				memoryRestart();
			}
			else
			{
				memoryRefresh();
			}
		}
		
	}

}
