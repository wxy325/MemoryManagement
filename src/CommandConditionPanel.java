import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.GridLayout;
import java.util.*;
//��ִ��������ʣ��������ȱҳ����ȱҳ�ʣ���ǰ������������ǰ��������ҳ����ǰ��������frame����һ�������ţ���һ����������ҳ����һ�����ɷ�ʽ
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
		
		add(new JLabel("��ִ������:"));
		add(totalCommandNumber);
		add(new JLabel("ʣ������:"));
		add(restCommandNumber);
		add(new JLabel("ȱҳ��:"));
		add(pageMissingNumber);
		add(new JLabel("ȱҳ��:"));
		add(pageMissingRate);
		add(new JLabel("��ǰ������:"));
		add(nowCommandNumber);
		add(new JLabel("��ǰ��������page:"));
		add(nowCommandPage);
		add(new JLabel("��ǰ��������frame:"));
		add(nowCommandFrame);
		add(new JLabel("��һ��������:"));
		add(nextCommandNumber);
		add(new JLabel("��һ����������page:"));
		add(nextCommandPage);
		add(new JLabel("��һ���������ɷ�ʽ:"));
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
