package b.main;

import b.service.GameService;

public class Main {		// VO 및 interface 사용X, 
						// 스플릿, 더블, 에이스 카드 1/11 구현 안 됨

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		GameService gs  = new GameService();
			
		
		try {
			gs.GameSetting();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
