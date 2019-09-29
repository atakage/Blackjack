package b.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GameService {

	private List<String> cardPackList; // 카드가 담겨 있는 List
	private Scanner scan;
	private int playerMoney = 1000;
	private int bet = 0;
	private String WDL; // 게임 마무리 전 정산 위한 게임 결과 판별 뵨수

	private int playerTotalP;
	private int dealerTotalP;

	private List<Integer> playerField; // 플레이어가 뽑은 카드의 점수가 저장돼 있는 정수형 리스트
	private List<Integer> dealerField;

	private List<String> playerCardStock; // 플레이어가 뽑은 카드 카드의 문양이 저장돼 있는 문자열 리스트
	private List<String> dealerCardStock;

	public GameService() {
		// TODO Auto-generated constructor stub

		playerField = new ArrayList<Integer>();
		dealerField = new ArrayList<Integer>();

		scan = new Scanner(System.in);
	}

	public void createCard() { // 카드 생성 메서드

		cardPackList = new ArrayList<String>();
		playerCardStock = new ArrayList<String>();
		dealerCardStock = new ArrayList<String>();

		String[] strArrSDHC = { "♠", "◆", "♣", "♥" };
		String strCards = null;

		for (int i = 0; i < strArrSDHC.length; i++) { // 외부 for에서 스페이드, 다이아, 클로버, 하트 돌아가며 생성

			strCards = strArrSDHC[i] + ":" + "J";
			cardPackList.add(strCards);
			strCards = strArrSDHC[i] + ":" + "K";
			cardPackList.add(strCards);
			strCards = strArrSDHC[i] + ":" + "Q";
			cardPackList.add(strCards);

			for (int j = 1; j < 11; j++) { // 내부 for에서 2~10까지 숫자 저장

				strCards = strArrSDHC[i] + ":" + j;
				if (j < 2) {
					strCards = strArrSDHC[i] + ":" + "A";
				}

				cardPackList.add(strCards);
			}

		}

		Collections.shuffle(cardPackList); // 끝나기 전 카드 섞기

	}

	public void GameSetting() throws Exception { // 메인 서비스

		while (true) {
			this.createCard();
			playerTotalP = 0;
			dealerTotalP = 0;

			System.out.println("☆-----------블랙잭 게임--------☆");
			System.out.println("딜:아무 키나 입력");
			System.out.println("게임 종료:0");
			System.out.println("------------------");
			System.out.println("소지 금액:" + playerMoney);
			System.out.println("------------------");
			System.out.print(">> ");
			String strCommand = scan.nextLine();

			if (strCommand.equals("0")) {
				System.out.println("종료");
				break;
			}

			this.betAccept(); // 배팅 금액 입력 및 오류 처리 메서드

			System.out.println("======================");
			System.out.println("배팅 금액:" + bet + "으로 게임 시작");
			System.out.println("소지 금액:" + playerMoney);
			System.out.println("======================");

			Thread.sleep(1000); // 콘솔 출력에 딜레이 주기
			System.out.println("3...");
			Thread.sleep(1000);
			System.out.println("2..");
			Thread.sleep(1000);
			System.out.println("1");

			this.playerCardDTB(); // 카드 뽑기 메서드
			this.playerCardDTB();
			System.out.println("플레이어 점수합" + playerTotalP);
			this.dealerCardDTB(); // 딜러 카드 뽑기 메서드 변수명 외에 플레이어어와 동일
			this.dealerCardDTB();
			System.out.println("딜러 점수합" + dealerTotalP);

			boolean bjc = this.pointJBJ(); // 블랙잭 판별 메서드

			if (bjc == true) { // 블랙잭 시 while문 되돌아가기
				continue;
			}

			boolean result = this.playerCardDTBN(); // 히트 or 스탠드 메서드

			if (result == false) {

				this.pointJS(); // 스탠드 시 발동

			} else {

				this.pointJH(); // 히트 시 발동
			}

			if (WDL.equals("L")) { // 게임 리셋 전 최종 결과에 따른 금액 처리
				System.out.println("-----------");
				System.out.println("배팅 금액  -" + bet);
				bet = 0;

			} else if (WDL.equals("D")) {
				playerMoney += bet;
				bet = 0;
			} else if (WDL.equals("W")) {
				bet = bet + bet;
				System.out.println("-----------");
				System.out.println("배팅 금액  +" + bet);
				playerMoney += bet;
				bet = 0;
			}
		}

	}

	public void pointJH() {

		while (true) {

			System.out.println("플레이어가 카드를 뽑습니다");
			this.playerCardDTB();
			System.out.println("플레이(" + playerTotalP + "):" + playerCardStock);
			System.out.println("------------------------------------------------");
			if (playerTotalP > 21) {
				System.out.println("딜러(" + dealerTotalP + "):" + dealerCardStock);
				System.out.println("플레이(" + playerTotalP + "):" + playerCardStock);

				System.out.println("플레이어 버스트! 딜러 승");
				WDL = "L";
				return;
			} else if (playerTotalP == 21) {
				this.pointJS();
				return;
			}

			System.out.println("---------------------------");
			System.out.println("  히트:Any Key     스탠드:S ");
			System.out.println("---------------------------");
			System.out.println(">> ");
			String strCommand = scan.nextLine();

			if (strCommand.equalsIgnoreCase("s")) {

				this.pointJS();
				return;
			} else {

				this.pointJH();
				return;
			}

		}
	}

	public void pointJS() { // 조건, 반복문이 난잡
							// 승무패에 따라 지역 변ㅅ수 조작, 금액 정산은 마지막에

		if (dealerTotalP > 16) {
			System.out.println("딜러(" + dealerTotalP + "):" + dealerCardStock);
			System.out.println("플레이(" + playerTotalP + "):" + playerCardStock);

			if (dealerTotalP == playerTotalP) {

				System.out.println("DRAW");
				WDL = "D";
				return;
			} else if (dealerTotalP > playerTotalP) {
				System.out.println("플레이어 패배");
				WDL = "L";
				return;
			} else {
				System.out.println("플레이어 승");
				WDL = "W";
				return;

			}
		} else {
			System.out.println("딜러(" + dealerTotalP + "):" + dealerCardStock);
			System.out.println("플레이(" + playerTotalP + "):" + playerCardStock);

			while (dealerTotalP <= 16) {
				System.out.println("딜러가 카드를 뽑습니다");
				this.dealerCardDTB();

				System.out.println("딜러(" + dealerTotalP + "):" + dealerCardStock);
				System.out.println("플레이(" + playerTotalP + "):" + playerCardStock);

				if (dealerTotalP > 21) {
					System.out.println("딜러 버스트! 플레이어 승");
					WDL = "W";
					return;
				}

			}

			if (dealerTotalP == playerTotalP) {

				System.out.println("DRAW");
				WDL = "D";
				return;
			} else if (dealerTotalP > playerTotalP) {
				System.out.println("플레이어 패배");
				WDL = "L";
				return;
			} else {
				System.out.println("플레이어 승");
				WDL = "W";
				return;

			}

		}

	}

	public boolean pointJBJ() { // 한 번도 안 걸려서 테스트 못 해봄

		boolean bjc = false;

		if (playerTotalP == 21) {

			if (dealerTotalP == 21) {
				bet = 0;
				System.out.println("DRAW");

				// continue;
			}
			System.out.println("플레이어 블랙잭!");
			playerMoney -= bet;
			bet *= 1.5;
			bjc = true;
			// continue;
		}

		if (dealerTotalP == 21) {

			if (playerTotalP == 21) {
				System.out.println("DRAW");
				playerMoney += bet;
				bet = 0;
				// continue;
			}
			System.out.println("딜러 블랙잭!");
			bet = 0;
			bjc = true;
			// continue;
		
		}

		return bjc;

	}

	public boolean playerCardDTBN() {
		boolean HoS = true;
		System.out.println("---------------------------");
		System.out.println("  히트:Any Key     스탠드:S ");
		System.out.println("---------------------------");
		System.out.print(">> ");
		String strCommand = scan.nextLine();

		if (strCommand.equalsIgnoreCase("s")) {
			System.out.println("스탠드!");
			return HoS = false;
		}

		System.out.println("히트!");
		return HoS;

	}

	public void playerCardDTB() {

		int cardPackSize = cardPackList.size();
		String[] pStrCardArr = null;

		// for (int i = 0; i < 2; i++) {
		int fieldValue;

		String strCardIndext = cardPackList.get(0); // 카드 리스트를 섞어 놓은 상태이기 때문에
		// 0번 자리에서 계속 뽑음

		playerCardStock.add(strCardIndext);
		cardPackList.remove(0); // 뽑은 카드 제거
		pStrCardArr = strCardIndext.split(":");

		System.out.println("플레이어 카드" + pStrCardArr[0] + ":" + pStrCardArr[1]);

		if (pStrCardArr[1].equals("K") || pStrCardArr[1].equals("J") || pStrCardArr[1].equals("Q")) {

			fieldValue = 10;
			playerField.add(fieldValue);
			// continue;

		} else if (pStrCardArr[1].equals("A")) {

			fieldValue = 1;
			playerField.add(fieldValue);
			// continue;
		} else {

			fieldValue = Integer.valueOf(pStrCardArr[1]);

			playerField.add(fieldValue);
			// cardPackList.remove(0);

		}

		playerTotalP += fieldValue;

	}

	public void dealerCardDTB() {

		String[] dStrCardArr = null;
		int cardPackSize = cardPackList.size();

		// for (int i = 0; i < 2; i++) {
		int fieldValue;

		String strCardIndext = cardPackList.get(0);
		dealerCardStock.add(strCardIndext);
		cardPackList.remove(0);
		dStrCardArr = strCardIndext.split(":");
		System.out.println("딜러 카드" + dStrCardArr[0] + dStrCardArr[1]);

		if (dStrCardArr[1].equals("K") || dStrCardArr[1].equals("J") || dStrCardArr[1].equals("Q")) {

			fieldValue = 10;
			dealerField.add(fieldValue);

		} else if (dStrCardArr[1].equals("A")) {

			fieldValue = 1;
			dealerField.add(fieldValue);

		} else {

			fieldValue = Integer.valueOf(dStrCardArr[1]);

			dealerField.add(fieldValue);

		}

		dealerTotalP += fieldValue;

	}

	public boolean isStringDouble(String s) {
		try {
			Double.parseDouble(s);
			return true; // true일 시 정수
		} catch (Exception e) {
			return false; // 문자열
		}
	}

	public void betAccept() {

		String strMC;
		int commandBet;

		while (true) {

			System.out.print("배팅 금액 입력>> ");
			strMC = scan.nextLine();

			while (!this.isStringDouble(strMC)) { // String에 저장된 데이터가 정수인지 문자인지 판별해주는
				// 메소드 활용, 수업 때 배운 예외 처리 내용 찾을 수 없어서
				// 인터넷 검색 활용

				System.out.println("배팅 금액은 숫자로만 입력!!");
				System.out.print("배팅 금액 입력>> ");
				strMC = scan.nextLine();
			}

			commandBet = Integer.valueOf(strMC);

			int betcheck = playerMoney - commandBet;

			if (betcheck < 0) {
				System.out.println("배팅 가능한 금액이 아님");
				continue;
			}

			break;
		}

		playerMoney -= commandBet;
		bet += commandBet;

	}

}
