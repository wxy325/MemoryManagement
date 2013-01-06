import java.util.*;

public class PhysicsMemory extends Observable implements Observer 
{
	//常量
	static public final int MEMORY_SIZE = 4;
	
	//private
	private PageReplacementMode m_Mode = PageReplacementMode.FIFO_MODE;		//默认FIFO
	public void setPageReplacementMode(PageReplacementMode newMode)
	{
		m_Mode = newMode;
	}
	private Vector<Integer> m_FIFOModeVector = new Vector<Integer>();
	private Vector<Integer> m_LRUModeVector = new Vector<Integer>();
	private int m_iPageMissingNumber;
	public int getPageMissingNumber(){return m_iPageMissingNumber;}
	private float m_flPageMissingRate;
	public float getPageMissingRate(){return m_flPageMissingRate;}
	private int m_ariMemeoryDatas[] = new int[MEMORY_SIZE];
	public int getPageInFrame(int frameNumber){ return m_ariMemeoryDatas[frameNumber];};
	
	private int m_iNowCommandPage;
	private int m_iNextCommandPage;
	private int m_iNowFrame;
	
	private CommandData m_CommandData;
	
	private void dataChangedEnd()				//notify observer
	{
		setChanged();
		notifyObservers();
	}
	
	
	//调页
	//返回所置换的frame号
	private int changePage()			
	{
		int iRemovePage = -1;
		if (m_Mode == PageReplacementMode.FIFO_MODE)	//FIFO 
		{
			if (m_FIFOModeVector.size() < MEMORY_SIZE)
			{
				iRemovePage = -1;
			}
			else
			{
				iRemovePage = m_FIFOModeVector.firstElement();
			}
		}
		else											//LRU
		{
			if (m_LRUModeVector.size() < MEMORY_SIZE ) 
			{
				iRemovePage = -1;
			}
			else
			{
				iRemovePage = m_LRUModeVector.firstElement();
			}
		}

		int i = -1;
		for (i = 0; i < m_ariMemeoryDatas.length; i++)
		{
			if (m_ariMemeoryDatas[i] == iRemovePage)
			{
				break;
			}
		}
		m_ariMemeoryDatas[i] = m_iNowCommandPage;
		
		
		//刷新FIFO与LRU Vector
		if (m_FIFOModeVector.size() == MEMORY_SIZE) 
		{	
			if (m_Mode == PageReplacementMode.FIFO_MODE)
			{
				
				for (int j = 0; j < m_LRUModeVector.size(); j++)
				{	
					if (m_LRUModeVector.get(j) == m_FIFOModeVector.get(0))
					{
						m_LRUModeVector.remove(j);
						break;
					}
				}
				m_FIFOModeVector.remove(0);
			}
			else
			{
				for (int j = 0; j < m_FIFOModeVector.size(); j++)
				{
					if(m_FIFOModeVector.get(j) == m_LRUModeVector.get(0))
					{
						m_FIFOModeVector.remove(j);
					}
				}
				m_LRUModeVector.remove(0);
			}
		}

		m_FIFOModeVector.add(m_iNowCommandPage);
		m_LRUModeVector.add(m_iNowCommandPage);
		
//		dataChangedEnd();
//		setChanged();
		
//		iRemovePage,,m_iNowCommandPage,,i
//		Vector<Integer> passVector = new Vector<Integer>();
//		passVector.add(iRemovePage);
//		passVector.add(m_iNowCommandPage);
//		passVector.add(i);
//		notifyObservers(passVector);
		return i;
	}
	
	private void refreshLRUModeVector()
	{
		int index = m_LRUModeVector.indexOf(m_iNowCommandPage);
		if (index != (m_LRUModeVector.size() - 1)) 
		{
			int tempPage = m_LRUModeVector.get(index);
			m_LRUModeVector.remove(index);
			m_LRUModeVector.add(tempPage);
		}
	}
	
	private void executeCommand()
	{
		
		int i = 0;
		for (i = 0; i < m_ariMemeoryDatas.length; i++)
		{
			if (m_iNowCommandPage == m_ariMemeoryDatas[i])
			{
				break;
			}
		}
		
		if (i == m_ariMemeoryDatas.length)		//缺页
		{
			i = changePage();
			
			m_iPageMissingNumber++;
			
		}
		
		m_iNowFrame = i;

		refreshLRUModeVector();
		
		m_flPageMissingRate = (float)m_iPageMissingNumber / m_CommandData.getExeCount();
		dataChangedEnd();
	}
	
	
	
	
	//public
	public int getNowFrame()
	{
		return m_iNowFrame;
	}
	public PhysicsMemory(CommandData commandData) 
	{
		m_CommandData = commandData;
		m_CommandData.addObserver(this);
		
		restart();
	}
	
	public void changeMode(PageReplacementMode newMode)
	{
		m_Mode = newMode;
	}
	public void restart()	//重置函数
	{
		m_FIFOModeVector.removeAllElements();
		m_LRUModeVector.removeAllElements();
		m_iPageMissingNumber = 0;
		m_flPageMissingRate = 0.f;
		m_iNowFrame = -1;
		for (int i = 0; i < m_ariMemeoryDatas.length ; i++)
		{
			m_ariMemeoryDatas[i] = -1;
			
		}
		m_iNextCommandPage = m_CommandData.getNextCommandNumber() / m_CommandData.COMMAND_NUMBER_PER_PAGE;
		dataChangedEnd();
	}


	
	@Override
	public void update(Observable o, Object arg)
	{
		if ( o instanceof CommandData )
		{
			CommandData datas = (CommandData) o;
			if (datas.getNowCommandNumber() == -1)	//重置
			{
				restart();
			}
			else									//执行
			{
				m_iNowCommandPage = m_iNextCommandPage;
				m_iNextCommandPage = m_CommandData.getNextCommandNumber() / m_CommandData.COMMAND_NUMBER_PER_PAGE;
				executeCommand();
			}
		}
	}
}
