import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;


public class ButtonPanel extends JPanel
{
	CommandData m_CommandData;
	PhysicsMemory m_PhysicsMemory;
	
	JButton m_RestartButton;
	JButton m_OneStepButton;
	JButton m_FinishButton;
	
	private void checkEnd()
	{
//		System.out.println(m_CommandData.getRestCount());
		if (m_CommandData.getRestCount() == 0 )
		{
			m_FinishButton.setEnabled(false);
			m_OneStepButton.setEnabled(false);
		}
	}
	private void restart()
	{
		m_FinishButton.setEnabled(true);
		m_OneStepButton.setEnabled(true);
	}
	
	public ButtonPanel(CommandData commandData,PhysicsMemory physicsMemory) 
	{
		super();
		
		m_CommandData = commandData;
		m_PhysicsMemory = physicsMemory;
		
		setLayout(new GridLayout(1, 4));
		m_RestartButton = new JButton("重新开始");
		m_RestartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				m_CommandData.restart();
				restart();
			}
		});
		
		m_OneStepButton = new JButton("执行一条指令");
		m_OneStepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				m_CommandData.executeCommand();
				checkEnd();
			}
		});
		
		m_FinishButton = new JButton("执行至结束");
		m_FinishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_CommandData.executeCommandToEnd();
				checkEnd();
			}
		});
		
		ButtonGroup exeModeButtonGroup = new ButtonGroup();
		JPanel exeModeButtonPanel = new JPanel();
		exeModeButtonPanel.setBorder(new TitledBorder("调页算法:"));
		exeModeButtonPanel.setLayout(new GridLayout(2,1));
		JRadioButton fifoModeButton = new JRadioButton("FIFO");
		fifoModeButton.setSelected(true);
		fifoModeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				m_PhysicsMemory.setPageReplacementMode(PageReplacementMode.FIFO_MODE);
			}
		});
		JRadioButton lruModeButton = new JRadioButton("LRU");
		lruModeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_PhysicsMemory.setPageReplacementMode(PageReplacementMode.LRU_MODE);	
			}
		});
		exeModeButtonGroup.add(fifoModeButton);
		exeModeButtonGroup.add(lruModeButton);
		exeModeButtonPanel.add(fifoModeButton);
		exeModeButtonPanel.add(lruModeButton);
		
		add(m_RestartButton);
		add(m_OneStepButton);
		add(m_FinishButton);	
		add(exeModeButtonPanel);
		restart();
	}
}