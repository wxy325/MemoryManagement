import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class CommandConditionTable extends JPanel implements Observer
{
	//常量定义
	static final int MAX_COLUMN_WIDTH = 100;
	
	//data member
	JPanel m_SelectButtonPanel;
	
	//剩余命令表
//	private JPanel m_RestCommandTablePanel;
	private JScrollPane m_RestCommandTableScrollPanel;
	private JTable m_RestCommandTable;
	private DefaultTableModel m_RestCommandTableModel;
	
	//命令历史表
//	private JPanel m_HistoryTablePanel;
	private JScrollPane m_HistoryTableScrollPane;
	private JTable m_HistoryTable;
	private DefaultTableModel m_HistoryTableModel;
	private int m_iHistoryCount;
	
	//页表
//	private JPanel m_PageTablePanel;
	private JScrollPane m_PageTableScrollPane;
	private JTable m_PageTable;
	private DefaultTableModel m_PageTableModel;
	
	private CommandData m_CommandData;
	private PhysicsMemory m_PhysicsMemory;
	
	
	//private method
	private void restartCommandTable()
	{
		//RestTable重置
		while(m_RestCommandTableModel.getRowCount() != 0)
		{
			m_RestCommandTableModel.removeRow(0);
		}
		for (int i = 0; i < m_CommandData.TOTAL_COMMAND_NUMBER; i++)
		{
			m_RestCommandTableModel.addRow(new Object[]{"" + i,"" + (i / m_CommandData.COMMAND_NUMBER_PER_PAGE)});
		}
		
		//HistoryTable重置
		m_iHistoryCount = 0;
		while(m_HistoryTableModel.getRowCount() != 0)
		{
			m_HistoryTableModel.removeRow(0);
		}
	}
	private void refreshCommandTable()			//执行指令后刷新
	{
		m_RestCommandTableModel.removeRow(m_CommandData.getNowCommandIndex());
		
		m_HistoryTableModel.addRow(new Object[]{""+m_iHistoryCount,""+m_CommandData.getNowCommandNumber(),""+(m_CommandData.getNowCommandNumber()/m_CommandData.COMMAND_NUMBER_PER_PAGE)});
		m_iHistoryCount++;
   
//		m_HistoryTableScrollPane.scrollRectToVisible(rect);
	}
	private void restartPageTable()
	{
		//PageTable重置
		while(m_PageTableModel.getRowCount() != 0)
		{
			m_PageTableModel.removeRow(0);
		}
		for(int i = 0; i < m_CommandData.TOTAL_COMMAND_NUMBER / m_CommandData.COMMAND_NUMBER_PER_PAGE; i++)
		{
			m_PageTableModel.addRow(new Object[]{"" + i,"","Invalid"});
		}
	}
	private void refreshPageTable()
	{
		while(m_PageTableModel.getRowCount() != 0)
		{
			m_PageTableModel.removeRow(0);
		}
//		System.out.println(m_PhysicsMemory.getPageInFrame(0));
		for(int i = 0; i < m_CommandData.TOTAL_COMMAND_NUMBER / m_CommandData.COMMAND_NUMBER_PER_PAGE; i++)
		{
			int j = 0;
			for (j = 0; j < m_PhysicsMemory.MEMORY_SIZE; j++) 
			{
				if (m_PhysicsMemory.getPageInFrame(j) == i)
				{
					break;
				}
			}
			if (j == m_PhysicsMemory.MEMORY_SIZE) 
			{
				m_PageTableModel.addRow(new Object[]{"" + i,"","Invalid"});	
			}
			else
			{
				m_PageTableModel.addRow(new Object[]{"" + i,"" + j,"Valid"});
			}
		}
	}
	
	
	//member function
	public CommandConditionTable(CommandData data, PhysicsMemory physicMemory) 
	{
		super();
		
		//register Observer
		m_CommandData = data;
		m_CommandData.addObserver(this);
		m_PhysicsMemory = physicMemory;
		m_PhysicsMemory.addObserver(this);
		
		Object[][] cellData = null;
		
		//RestTable
		String RestTableHead[] = {"命令编号","所在page"};
		m_RestCommandTableModel = new DefaultTableModel(cellData, RestTableHead) {

			  public boolean isCellEditable(int row, int column) {
			    return false;
			  }
			};
		m_RestCommandTable = new JTable(m_RestCommandTableModel);
		for (int i = 0; i < RestTableHead.length; i++) 
		{
			m_RestCommandTable.getColumnModel().getColumn(i).setMaxWidth(MAX_COLUMN_WIDTH);	
		}
		m_RestCommandTableScrollPanel = new JScrollPane(m_RestCommandTable);
		m_RestCommandTableScrollPanel.setBorder(new TitledBorder("剩余命令:"));
//		m_RestCommandTablePanel = new JPanel();
//		m_RestCommandTablePanel.setBorder(new TitledBorder("剩余命令："));
//		m_RestCommandTablePanel.add(m_RestCommandTableScrollPanel);
		
		//HistoryTable
		String HistoryTableHead[] = {"执行序号","命令编号","所在page"};

		m_HistoryTableModel = new DefaultTableModel(cellData, HistoryTableHead) {

				public boolean isCellEditable(int row, int column) {
			    return false;
			  }
			};
		m_HistoryTable = new JTable(m_HistoryTableModel);
		for (int i = 0; i < HistoryTableHead.length; i++) 
		{
			m_HistoryTable.getColumnModel().getColumn(i).setMaxWidth(MAX_COLUMN_WIDTH);	
		}
		m_HistoryTableScrollPane = new JScrollPane(m_HistoryTable);
		m_HistoryTableScrollPane.setBorder(new TitledBorder("历史:"));
//		m_HistoryTablePanel = new JPanel();
//		m_HistoryTablePanel.setBorder(new TitledBorder("历史:"));
//		m_HistoryTablePanel.add(m_HistoryTableScrollPane);
		
		//PageTable
		String PageTableHead[] = {"page数","frame数","Valid/Invalid"};
		m_PageTableModel = new DefaultTableModel(cellData, PageTableHead) {

				public boolean isCellEditable(int row, int column) {
				return false;
			  }
			};
		m_PageTable = new JTable(m_PageTableModel);
		for (int i = 0; i < PageTableHead.length; i++) 
		{
			m_PageTable.getColumnModel().getColumn(i).setMaxWidth(MAX_COLUMN_WIDTH);	
		}
		m_PageTableScrollPane = new JScrollPane(m_PageTable);
		m_PageTableScrollPane.setBorder(new TitledBorder("页表:"));
//		m_PageTablePanel = new JPanel();
//		m_PageTablePanel.setLayout(new FlowLayout());
//		m_PageTablePanel.setBorder(new TitledBorder("页表："));
//		m_PageTablePanel.add(m_PageTableScrollPane );
		
		restartCommandTable();
		restartPageTable();
		
		
		//二选一按钮
//		m_SelectButtonPanel = new JPanel();
//		m_SelectButtonPanel.setLayout(new BorderLayout());
//		m_SelectButtonPanel.add(BorderLayout.CENTER,m_RestCommandTableScrollPanel);
//		m_SelectButtonPanel.add(BorderLayout.CENTER,m_RestCommandTablePanel);
//		
//		JRadioButton restTableButton  = new JRadioButton("剩余命令");
//		restTableButton.setSelected(true);
//		
//		restTableButton.addActionListener(new ActionListener() {
//			@Override
//			ActionListener(JPanel panel){m_SelectButtonPanel = panel;};
//			private JPanel m_SelectButtonPanel;
//			public void actionPerformed(ActionEvent arg0) {
				
//				System.out.println( m_CommandData.getExeCount());
//				m_SelectButtonPanel.remove(m_HistoryTableScrollPane);
//				m_SelectButtonPanel.add(BorderLayout.CENTER,m_RestCommandTableScrollPanel);			
//			}
//		});
		
//		JRadioButton historyTableButton = new JRadioButton("历史");
//		historyTableButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				m_RestCommandTableScrollPanel.setBackground(new Color(1, 0, 0));
//				m_SelectButtonPanel.remove(m_RestCommandTablePanel);
//				m_SelectButtonPanel.add(BorderLayout.CENTER,m_HistoryTableScrollPane);
//				m_SelectButtonPanel.setVisible(false);
				
//			}
//		});
		
//		ButtonGroup selectTableButtonsGroup = new ButtonGroup();
//		JPanel selectTableButtonsPanel = new JPanel();
//		selectTableButtonsPanel.add(restTableButton);
//		selectTableButtonsPanel.add(historyTableButton);
//		selectTableButtonsGroup.add(restTableButton);
//		selectTableButtonsGroup.add(historyTableButton);
		
//		m_SelectButtonPanel.add(BorderLayout.SOUTH,selectTableButtonsPanel);
		
		//添加选择按钮
		//
		
		setLayout(new GridLayout(1, 3));
		add(m_PageTableScrollPane);
		add(m_RestCommandTableScrollPanel);
		add(m_HistoryTableScrollPane);

		
	}
	
	
	
	@Override
	public void update(Observable o, Object arg) 
	{
		if (o instanceof CommandData)
		{
			if (m_CommandData.getRestCount() == m_CommandData.TOTAL_COMMAND_NUMBER)
			{
				restartCommandTable();
			}
			else
			{
				refreshCommandTable();
			}
		}
		else if (o instanceof PhysicsMemory)
		{
			if (m_PhysicsMemory.getNowFrame() == -1)
			{
				restartPageTable();
			}
			else
			{
				refreshPageTable();
			}
		}
	}
}
