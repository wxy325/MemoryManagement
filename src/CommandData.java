import java.util.*;

public class CommandData extends Observable
{
	//��������
	public static final int TOTAL_COMMAND_NUMBER = 320;
	public static final int COMMAND_NUMBER_PER_PAGE = 10;
	
	//private data member 
	private Vector<Integer> m_RestCommands = new Vector<Integer>();		//��¼δִ��ָ��
	private final Vector<Integer> m_AllCommands = new Vector<Integer>();	//��¼����ָ��
	
	private int m_iNowCommandNumber;
	public int getNowCommandNumber() { return m_iNowCommandNumber; }
	
	private int m_iNowCommandIndex;
	public int getNowCommandIndex() { return m_iNowCommandIndex; }
	
	private int m_iNextCommandNumber;
	public int getNextCommandNumber() { return m_iNextCommandNumber; }
	
	private int m_iNextCommandIndex;
	public int getNextCommandIndex() {return m_iNextCommandIndex; }
	
	private CommandExeMode m_Mode;
	public CommandExeMode getNextCommandExeMode(){return m_Mode;}
	private Random randomMaker = new Random();
	
	//private method
	
	private void dataChangeedEnd()				//notify observer
	{
		setChanged();
		notifyObservers();
	}
	
	public void restart()
	{
		m_RestCommands.removeAllElements();
		m_RestCommands.addAll(m_AllCommands);
		
		m_iNowCommandIndex = -1;
		m_iNowCommandNumber = -1;
		m_iNextCommandIndex = -1;
		m_iNextCommandNumber = -1;
		
		setNextMode();
		setNextCommand();
		
		
		dataChangeedEnd();
	}
	private void setNextMode()		//˳��Ϊ ��ǰ�����˳��ִ�У���������˳��ִ��
	{
		int iCommandModeNum = (TOTAL_COMMAND_NUMBER - m_RestCommands.size()) % 4;
		if (m_RestCommands.size() == 0)
		{
			m_Mode = CommandExeMode.END;
		}
		else if (m_iNextCommandIndex == -1)
		{
			m_Mode = CommandExeMode.RANDOM;
		}
		else if (iCommandModeNum == 0)
		{
			m_Mode = CommandExeMode.PRE_RANDOM;
		}
		else if (iCommandModeNum == 2)
		{
			m_Mode = CommandExeMode.POST_RANDOM;
		}
		else
		{
			m_Mode = CommandExeMode.SEQUENCE;
		}
		
	}
	
	//public
	public int getExeCount()
	{
		return TOTAL_COMMAND_NUMBER - m_RestCommands.size();
	}
	
	
	public CommandData()
	{		
		for (int i = 0; i < TOTAL_COMMAND_NUMBER ; i++) 
		{
			m_AllCommands.add(i);
		}
		
		restart();
	}
	
	
	public int getRestCount()
	{
		return m_RestCommands.size();
	}
	
	private void setNextCommand()
	{
		if (m_RestCommands.size() > 0)
		{
			int iNextInt = randomMaker.nextInt();
			int nextIndex = Math.abs(iNextInt);
			
			switch (m_Mode) 
			{
			case RANDOM :
				if (m_RestCommands.size() == 0)
				{
					m_iNextCommandIndex = 0;
				}
				else
				{
					m_iNextCommandIndex = nextIndex % m_RestCommands.size();
				}
				break;
			case PRE_RANDOM:
				if (m_iNowCommandIndex == 0) 
				{
					m_iNextCommandIndex = 0;
				}
				else
				{
					m_iNextCommandIndex = nextIndex % m_iNowCommandIndex;
				}
				break;
			case POST_RANDOM:
				if (m_iNowCommandIndex == m_RestCommands.size())
				{	//��ֹ�������
					m_iNextCommandIndex = m_RestCommands.size() - 1;
				}
				else
				{
					m_iNextCommandIndex = nextIndex % (m_RestCommands.size() - m_iNowCommandIndex) + m_iNowCommandIndex;
				}
				break;
			case SEQUENCE:
				m_iNextCommandIndex = m_iNowCommandIndex;	//��Ϊԭ��������ɾ������ǰindex��Ϊ��һ�������index	
				break;
//			case PRE_SEQUENCE:
//				m_iNextCommandIndex = m_iNowCommandIndex - 1;
//				break;
			}
			
			
			if (m_iNextCommandIndex < 0 )		//��ֹ���һ��ָ��ȡ��-1
			{
				m_iNextCommandIndex = 0;
			}
			else if (m_iNextCommandIndex >= m_RestCommands.size())
			{
				m_iNextCommandIndex = m_RestCommands.size() - 1;
			}
			m_iNextCommandNumber = m_RestCommands.get(m_iNextCommandIndex);
		}
		else
		{
			//ִ�н���
			m_iNextCommandIndex = -1;
			m_iNextCommandNumber = -1;
		}
	}
	
	public void executeCommand()
	{
		//ִ��ָ��
		
		if (m_iNextCommandIndex != -1)
		{
			m_iNowCommandIndex = m_iNextCommandIndex;
			m_iNowCommandNumber = m_iNextCommandNumber;
			m_RestCommands.remove(m_iNextCommandIndex);
			
			//���ɺ���ָ��
			setNextMode();
			setNextCommand();
			
			//notify observer
			dataChangeedEnd();
		}
	}
	
	public void executeCommandToEnd()
	{
		while ( m_RestCommands.size() != 0 )
		{
			executeCommand();
		}
	}
}
