package com.example.springmvctoys;

import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import dev.story.framework.exception.DevEntException;
import dev.story.framework.exception.DevPrcException;
import dev.story.framework.ftp.domain.FtpTransferFile;
import dev.story.framework.handler.Constant;
import dev.story.framework.handler.Message;
import dev.story.framework.mip.client.support.EDSFactory;
import dev.story.framework.model.DSData;
import dev.story.framework.model.DSMultiData;
import dev.story.framework.model.EDSManager;
import dev.story.framework.model.EntityDataSet;
import dev.story.framework.model.FieldInfoSet;
import dev.story.framework.rsp.common.dataset.RSPDataSet;
import dev.story.framework.utils.DateUtils;
import dev.story.framework.utils.StringUtils;
import dev.story.framework.web.convert.support.DevBeanUtils;
import gshseai.GSH_S4C_PRD02.WS.PRD_EAI_Internal_Product.ws.wsProvider.PRD_EAI_Internal_Product_P.PRD_EAI_Res;
import gshseai.GSH_S4C_PRD02.WS.PRD_EXP_Internal_Product.ws.wsProvider.PRD_EXP_Internal_Product_P.PRD_EXP_Internal_Product_P_PortType;
import gshseai.GSH_S4C_PRD02.WS.PRD_EXP_Internal_Product.ws.wsProvider.PRD_EXP_Internal_Product_P.PRD_EXP_Internal_Product_P_PortTypeProxy;
import gshseai.GSH_S4C_PRD02.WS.PRD_EXP_Internal_Product.ws.wsProvider.PRD_EXP_Internal_Product_P.PRD_EXP_Req;
import gshseai.GSH_S4C_PRD02.WS.PRD_EXP_Internal_Product.ws.wsProvider.PRD_EXP_Internal_Product_P.PRD_EXP_Request;
import gshseai.GSH_S4C_PRD02.WS.PRD_EXP_Internal_Product.ws.wsProvider.PRD_EXP_Internal_Product_P.PRD_EXP_Res;
import kr.co.gshs.iris.IrisClientImage;
import kr.co.gshs.iris.rpc.rabbitmq.RpcClientImage;
import smart4c.modules.ast.domain.ProcRsltInfo;
import smart4c.modules.atp.domain.BroadBefMngInfo;
import smart4c.modules.brd.domain.BroadFormPrd;
import smart4c.modules.brd.entity.BroadFormPrdEntity;
import smart4c.modules.cmm.domain.CalndQryCond;
import smart4c.modules.cmm.domain.CmmCdQryCond;
import smart4c.modules.cmm.domain.ProhibitWordCond;
import smart4c.modules.cmm.entity.CalndCdEntity;
import smart4c.modules.cmm.entity.CmmCdEntity;
import smart4c.modules.cmm.entity.MailSndEntity;
import smart4c.modules.cmm.entity.SmsTnsBaseEntity;
import smart4c.modules.cpn.domain.CpnLimitPrd;
import smart4c.modules.cpn.entity.CpnApplyPrdEntity;
import smart4c.modules.ecd.ord.entity.EcOrdOverEntity;
import smart4c.modules.ecd.prd.entity.EcPrdNewPoolInEntity;
import smart4c.modules.ecd.prd.entity.EcScmMngEntity;
import smart4c.modules.ecd.prd.entity.EcShopEntity;
import smart4c.modules.ecd.prd.entity.EcdForgnTmPrdEntity;
import smart4c.modules.ecd.prd.entity.EcdPrdImpQryDEntity;
import smart4c.modules.ods.atp.entity.OdsBroadBefMngInfoEntity;
import smart4c.modules.ord.constant.OrdConstant;
import smart4c.modules.prd.domain.*;
import smart4c.modules.prd.entity.AttrPrdEndInfoEntity;
import smart4c.modules.prd.entity.AttrPrdEntity;
import smart4c.modules.prd.entity.AttrPrdStockShtInfoEntity;
import smart4c.modules.prd.entity.BrandEntity;
import smart4c.modules.prd.entity.CntrbProfitMngEntity;
import smart4c.modules.prd.entity.ForgnPrdEntity;
import smart4c.modules.prd.entity.ImgEntity;
import smart4c.modules.prd.entity.ItemCdEntity;
import smart4c.modules.prd.entity.ItemCmposInfoEntity;
import smart4c.modules.prd.entity.MultiCdEntity;
import smart4c.modules.prd.entity.OrdPsblQtyEntity;
import smart4c.modules.prd.entity.PrdAgreeDocEntity;
import smart4c.modules.prd.entity.PrdChanlEntity;
import smart4c.modules.prd.entity.PrdClsBaseEntity;
import smart4c.modules.prd.entity.PrdClsChkAprvEntity;
import smart4c.modules.prd.entity.PrdClsSpecInfoEntity;
import smart4c.modules.prd.entity.PrdColChgLogEntity;
import smart4c.modules.prd.entity.PrdDescdHtmlDEntity;
import smart4c.modules.prd.entity.PrdEndLogEntity;
import smart4c.modules.prd.entity.PrdEntity;
import smart4c.modules.prd.entity.PrdExplnEntity;
import smart4c.modules.prd.entity.PrdGenrlDescdEntity;
import smart4c.modules.prd.entity.PrdHtmlDescdEntity;
import smart4c.modules.prd.entity.PrdImpQryInfoEntity;
import smart4c.modules.prd.entity.PrdLimitChgLogEntity;
import smart4c.modules.prd.entity.PrdMetaInfoEntity;
import smart4c.modules.prd.entity.PrdMontrnExcptEntity;
import smart4c.modules.prd.entity.PrdPmoEntity;
import smart4c.modules.prd.entity.PrdPrcEntity;
import smart4c.modules.prd.entity.PrdPrdDtrEntity;
import smart4c.modules.prd.entity.PrdSkuMngEntity;
import smart4c.modules.prd.entity.PrdSpecInfoEntity;
import smart4c.modules.prd.entity.PrdStockInfoEntity;
import smart4c.modules.prd.entity.PrdSuplyPlanEntity;
import smart4c.modules.prd.entity.PrdUdaEntity;
import smart4c.modules.prd.entity.ShtprdOrdEntity;
import smart4c.modules.prd.entity.SubSupEntity;
import smart4c.modules.prd.entity.SupDlvcEntity;
import smart4c.modules.prd.entity.SupEntity;
import smart4c.modules.prd.entity.SupPenltQryEntity;
import smart4c.modules.prd.entity.VendrEntity;
import smart4c.modules.qas.domain.PaiInfo;
import smart4c.modules.qas.domain.QaExposAtachFile;
import smart4c.modules.qas.entity.PaiInfoEntity;
import smart4c.modules.qas.entity.QaAtachFileConctInfoEntity;
import smart4c.services.cmm.filemng.process.ImageFileUploadProcess;
import smart4c.services.cmm.filemng.process.impl.ReUploadImageFileInfo;
import smart4c.services.cmm.hompg.process.CtiMngProcess;
import smart4c.services.cmm.syscmm.util.SysUtil;
import smart4c.services.prd.PrdConstants;
import smart4c.services.prd.prdaprv.process.PrdAprvMngProcess;
import smart4c.services.prd.prdaprv.process.PrdQaBefReqProcess;
import smart4c.services.prd.prdcmm.process.AttrPrdMngCmmProcess;
import smart4c.services.prd.prdcmm.process.PrdMngCmmProcess;
import smart4c.services.prd.prdcmm.process.PrdPrcMngCmmProcess;
import smart4c.services.prd.prdif.util.PrdValidUtil;
import smart4c.services.prd.prdif.ws.WsPrdBisPrdDescdRegModProcess;
import smart4c.services.prd.prdif.ws.WsPrdBisPrdItemMappnSyncProcess;
import smart4c.services.prd.prdif.ws.WsPrdEaiAttrPrdSyncProcess;
import smart4c.services.prd.prdif.ws.WsPrdEaiPrdChanlSyncProcess;
import smart4c.services.prd.prdif.ws.WsPrdEaiPrdPriceRegModProcess;
import smart4c.services.prd.prdif.ws.WsPrdEaiPrdSyncProcess;
import smart4c.services.prd.prdif.ws.WsPrdEaiQaAprvProcess;
//import smart4c.services.prd.prdif.ws.WsPrdScoProdItemMapProcess;
// [GS리테일 SAP 재무시스템 구축에 따른 Legacy 대응 개발 프로젝트][2023.09.01][최태웅] SAP 통합에 따른 신규 EAI 호출
import smart4c.services.prd.prdif.ws.WsPrdHcoProdItemMapProcess;
import smart4c.services.prd.prdmng.process.PrdAttrPrdSkuMngProcess;
import smart4c.services.prd.prdmng.process.PrdPrmmBrandMngProcess;
import smart4c.services.prd.supmng.process.SupDlvcMngProcess;
import smart4c.utils.calc.BigDecimalUtil;
import smart4c.utils.cmm.StringUtil;
import smart4c.utils.http.HttpClientService;
import smart4c.utils.http.domain.HttpInfoSet;
import smart4c.utils.log.SMTCLogger;
import smart4c.utils.prd.PrdClsUdaUtils;

/**
 * <pre>
 *
 * description : 상품정보를 상품영역 또는 타영역에서 공통으로 이용하는 경우,
 * 공통프로세스로 정의하고, 통합하여 구현한다.
 *
 * Generated by CodeGenerator. You can freely modify this generated file.
 * Copyright &amp;copy 2010 by GSHS. All rights reserved.
 * </pre>
 *
 * @author lgcns-213eab4ac
 * @since 1.0 2010-09-16 06:41:20
 *
 *        <pre>
 *
 * history :
 *[SR02141226076][2015.02.16][유수경]:모바일 상품권/티켓 관련 상품분류 재정비의 건
 *[2015-03-03][유수경]:도서몰 인터파크(1028208 ) 추가 
 *[SR02150327087][2015-04-27][유수경]: MC/PC상품 재고범위내 판매여부
 *[PD-2015-007][2015.05.20][정동국]:패널-직송개선 
 *[SR02150803016]:매장리스트 배너 이미지 추가
 *[SR02150818070][2015.08.21][유수경]:ca/dm 제외한 TC만 추가시에는 유료배송 가능하도록 
 *[패널 2015_014_제도개선 변경]김영준(kyjhouse) 2015-09-14 상품기본생성 -상품의 계약서 관련 정보 생성(상품등록주체, 프로모션합의여부) 추가
 *[SR02160118132][2016.01.15][김영현]:판매중 상품 주문가능수량 0입력 제한
 *[SR02160115126][2016.01.19][백동현]API 주문가능수량과 상품 판매상태 불일치에 대한 규칙 변경의 건
 *[SR02160118172][2016.03.15][백동현]:패밀리세일을 위한 상품코드 복사등록시 요청사항
 *[SR02160405072][2016.04.05][백동현]:API - 상품정보변경 : 노출매장 수정 기능 추가
 *[SR02160531123][2016.06.01][백동현]:어린이제품 안전 특별법 시행에 따른 어린이 도서 안전기준 인증값 설정의 건
 *[SR02160614077][2016.06.13][김영현]:상품판매상태 변경 EAI 과다 호출 로직 개선 및 배송/수거 방어 제한 로직 추가
 *[SR02160614099][2016.06.17][김영현]:해외직구 환불유형 선택 목록 추가 요청 
 *[SR02160630101][2016.0629][김영현]:편의점반품 설정 로직 추가
 *[SR02160729022][2016.08.31][김영현]:경품 공동부담 개발요청 - 업체부담 경품 과세 제한 로직 제거
 *[SR02161025031][2016.10.25][백동현]:API 상품연동 시 이중 가격 등록 발생 이슈 해결
 *[SR02161108033][2016.11.14][김영현]:골드바 판매제한에 따른 개발요청 
 *[SR02161118015][2016.11.24][김영현]:경품공동부담 프로세스 개발 관련 로직 변경의 건 
 *[SR02161117016][2016.11.28][백동현]:[기간계/채널] 제주/도서불가 설정시 지역 확인 로직 변경
 *[SR02161216019][2016.12.16][백동현]:(API)제주/도서 추가배송비 개발(M상품운영파트 공대연차장 요청건)
 *[SR02161024027][2016.12.19][백동현]:배송 형태별 배송조건 변경 요청 건(조건에 따라 다름)
 *[SR02170125085][2017.02.21][전영준]:[M상품운영파트] 화장품 전성분 관련 시스템 개발요청(0125)
 *[SR02170306112][2017.03.09][백동현]:직송관리대행 다중 출고/반송지 개발요청건 (정식버전 오픈에 대한 CSR)
 *[SR02170413078][2017-04-14][고경환] 직송관리대행 상품에대한 요청 데이터 추가 validation 체크  gowinix
 *[PD_2017_001_여행프로세스개선][2017.03.02][최미영] : 여행정보 복사
 *[SR02170516901][2017.05.24][백동현]:(내부개선과제) 사은품 등록시 상품 채널 확인 체크로직 외 2건
 *[SR02170711940][2017.08.17][백동현]:재고부족알람배치 건에 대한 수정 개발 건
 *[SR02170818129][2017.08.21][백동현]:재고부족알람배치 건에 대한 수정 개발 건
 *[PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거
 *[SR02171027963][2017.11.17][이용문]:신버젼 API 연동 대량상품등록 인터페이스 항목 추가의 건 (협력사 속성상품코드 문자입력)
 *[PD_2017_017_속도개선][2017.11.30][김은희] :속도개선 수정건
 *[SR02170913526][2017.09.14][최미영]:전통주기본프로세스 체크로직 추가
 *[PD_2017_017_속도개선][2017.12.14][장종일] : 속도개선 Session Info 추가
 *[SR02171208490][2018.01.03][추연철]:이미지서버업로드 - 변경 작업 
 *[SR02180316175][2018.03.26]:세일즈원 WAS Thread Hang으로 인한 CPU 과점 조치
 *[SR02180405155][2018.04.05][백동현]-상품신규등록시 PRD_PRD_M에 협력사+협력사상품코드가 중복으로 생성되는현상
 *[SR02180316175] 2018.04.23:이용문 세일즈원 WAS Thread Hang으로 인한 CPU 과점 조치
 *[SR02180510025][2018.05.15][추연철]:세일즈원과 위드넷 자동주문상품명 길이 일치 처리 
 *[SR02180323545][2018.06.25][고경환]:위드넷 이미지 실시간 리사이징 영역 확대 적용 요청의 건
 *[PD_2018_005_GUCCI 입점][2018.07.05][김성훈] : 원본(확대) 이미지 처리
 *[SR02180911580][2018.09.10][이용문]:세일즈원 신규상품승인_아이템 가격범위초과 (가격정보가 없다보니 아이템가격범위초과 항목에 Y로 노출되는 현상)
 *[SR02180727185][2018.10.01][이용문]: 지정일(설치포함) 프로세스 개선을 위한 상품등록 화면 수정
 *[SR02180828038][2018.10.22][온지영]:상품 자동승인 제외 로직 일부 제거 건 -> 체크로직 위치 변경
 *[SR02190227985][2019.03.11][이용문]:상품 분류검증 추천확대
 *[SR02190319198][2019.03.19][백동현]:즉시적립금할인 설정 제한 처리
 *[SR02190411725][2019.04.12][이용문]:상품분류 솔루션_자동승인 기준 확대
 *[SR02190426489][2019.06.07][백동현]:[직매입개선]GS초이스 상품등록제어 및 재고범위내 판매기능 개발요청
 *[SR02190619568][2019.07.11][박민수]:상품코드 임포트 임시저장 후, 멀티 상품코드 조건으로 대상조회
 *[SR02191115633] 2019.11.19 이용문 : 백화점 연동시 OP코드 일치 여부 포함 및 데이터 정합성 요청
 *[SR02200210723][2020.02.17][백동현]:일심품절 해제 시 오류 건 처리
 *[SR02200323382] 2020.04.13 이용문 : 카테고리 개편] 기간계/API 개발 요청 
 *[SR02200506079][2020.05.08][김영현]:정기배송 프로세스 3차 개선 관련 개발 요청
 *2020-09-10 김태엽 수정 - HANGBOT-3880_GSITMBO-1778 - WAS Log 정리
 *[HANGBOT-2201_DEVOPS-1805][2020.10.19][김영현]상품코드 복사등록 시 상품평 공유설정유형 추가
 *[HANGBOT-3880_GSITMBO-2890][2020.10.26][백동현]:WAS Log 정리
 *[HANGBOT-8465_GSITMBO-4059] 2020.12.28 이용문 : GS리테일_와인25플러스 상품/주문 API 연동 진행건
 *[HANGBOT-9096 GSITMBO-4581] 2021.04.21 이태호  위수탁 반품 수거형태 직반출 Default 변경관련
 *[HANGBOT-16987_GSITMBO-9804][2021.07.13][백동현]:지정택배수거 종료 
 *[HANGBOT-23372_GSITMBO-12450][2021.11.09][백동현]:표준출고일 관리건 문의
 *[HANGBOT-28953_GSITMBO-15549][2022.01.10][김진석]:[상품]SAP로 상품코드가 내려오지 않는 현상
 *[HANGBOT-30716] 2022.01.27 이용문 : 프레시몰 새벽센터 폐점으로 인한 시스템 수정요청
 *[HANGBOT-31738 GSITMBO-17570] 2022.02.21 이태호 표준출고일관리화면 상품코드추가후 조회시 조회안되는 현상 개선요청
 *[HANGBOT-27157_GSITMBO-17413][2022.02.23][백동현]:협력사 신규 API 진행_엘롯데
 *[HANGBOT-28095_GSITMBO-17845][2022.03.07][백동현]:경품 제세공과금 계산로직 및 신고데이터 반영기준 변경요청
 *[HANGBOT-32562 GSITMBO-19050] 2022.04.04 이태호 세일즈원의 상품 신규등록 수정요청 - 상품 가로,세로,높이,무게 정보 복사 되지 않게 수정
 *[GRIT-42210][2022.12.28][백동현]:[세일즈원] CDN - PURGE 서비스 변경에 따른 개발
 *[GRIT-47719][2023.01.31][백동현]:[세일즈원] (내부개선과제) 상품 SMTC->SAP 연동 시 정보 누락 이슈
 *[GRIT-53312][2023.02.28][백동현]:(내부개선과제) 상품 SMTC->SAP 연동 시 정보 누락 개선 개발
 *[GRIT-65674][2023.05.30][백동현]:모바일 전용 이미지 저장 기능 삭제 요청
 *[GRIT-71897][2023.06.27][백동현]:(내부개선과제) 사은픔/배송품과 경품 상품 환불유형 값 신규등록 시 유지 작업
 *[GRIT-89267] 2023.10.25 이태호  [프레시몰내] 와인25+& GS SHOP 위수탁상품 연동종료 요청의 건 (세일즈원-상품)
 *[GRIT-90644] 2023.11.20 TV상품 유료반품 시행관련 시스템변경 요청(2차 : 세일즈원-상품영역)
 *[GRIT-100715][2024.01.08][백동현]:image.gsshop.com 일몰 작업 - 상품복사 등록 시 대표이미지를 복사 방식 변경
 *[GRIT-105476][2024.01.31][백동현]:상품분류 검증 솔루션 제거 작업 (세일즈원-상품)
 *[GRIT-112159][2024.02.21]:[세일즈원 상품] 상품대표이미지 aws 전환을 위한 개발
 *
 * </pre>
 * @version 1.0 2010-09-16 06:41:20
 */
public class PrdMngCmmProcessImpl implements PrdMngCmmProcess {

	private static Log logger = LogFactory.getLog(PrdMngCmmProcessImpl.class);

	@Autowired
	private PrdGenrlDescdEntity prdGenrlDescdEntity;

	@Autowired
	private EcOrdOverEntity ecOrdOverEntity;

	@Autowired
	private AttrPrdEndInfoEntity attrPrdEndInfoEntity;

	@Autowired
	private AttrPrdEntity attrPrdEntity;
	
	@Autowired
	private EcPrdNewPoolInEntity ecPrdNewPoolInEntity;

	@Autowired
	private AttrPrdMngCmmProcess attrPrdMngCmmProcess;

	@Autowired
	private AttrPrdStockShtInfoEntity attrPrdStockShtInfoEntity;

	@Autowired
	private BrandEntity brandEntity;

	@Autowired
	private BroadFormPrdEntity broadFormPrdEntity;

	@Autowired
	private CntrbProfitMngEntity cntrbProfitMngEntity;

	@Autowired
	private EcScmMngEntity ecScmMngEntity;

	@Autowired
	private EcShopEntity ecShopEntity;

	@Autowired
	private ForgnPrdEntity forgnPrdEntity;

	@Autowired
	private PrdPrcEntity prdPrcEntity;

	@Autowired
	private  PrdDescdHtmlDEntity prdDescdHtmlDEntity;

	@Autowired
	private WsPrdBisPrdDescdRegModProcess wsPrdBisPrdDescdRegModProcess;

	@Autowired
	private SupDlvcEntity supDlvcEntity;

	@Autowired
	private SupDlvcMngProcess supDlvcMngProcess;

	@Autowired
	private HttpClientService httpClientService;
	
//	@Autowired
//	private DealMngEntity dealMngEntity;
	// #ImportedClassName(PRD 상품.PRD Entity.Smart4C.1 Entity.PrdEntity )
	// #ImportedClassName(PRD 상품.PRD Entity.Smart4C.1 Entity.PrdImpQryInfoEntity )
	// #ImportedClassName(PRD 상품.PRD00 상품공통.5 Message.ImpQryList )
	// #ImportedClassName(PRD 상품.PRD00 상품공통.5 Message.ImpQrySeq )
	// #ImportedClassName(PRD 상품.PRD00 상품공통.5 Message.SaleEndClr )

	@Autowired
	private ImgEntity imgEntity;

	@Autowired
	private ImageFileUploadProcess imgFileUploadProcess;

	@Autowired
	private ItemCdEntity itemCdEntity;

	@Autowired
	private ItemCmposInfoEntity itemCmposInfoEntity;

	@Autowired
	private MultiCdEntity multiCdEntity;

	@Autowired
	private OrdPsblQtyEntity ordPsblQtyEntity;

	@Autowired
	private PrdAprvMngProcess prdAprvMngProcess;

	@Autowired
	private PrdChanlEntity prdChanlEntity;

	@Autowired
	private PrdClsBaseEntity prdClsBaseEntity;

	@Autowired
	private PrdEndLogEntity prdEndLogEntity;

	@Autowired
	private PrdEntity prdEntity;

	@Autowired
	private PrdHtmlDescdEntity prdHtmlDescdEntity;

	@Autowired
	private PrdImpQryInfoEntity prdImpQryInfoEntity;

	@Autowired
	private PrdMetaInfoEntity prdMetaInfoEntity;

	@Autowired
	private PrdPmoEntity prdPmoEntity;

	@Autowired
	private PrdSpecInfoEntity prdSpecInfoEntity;

	@Autowired
	private PrdStockInfoEntity prdStockInfoEntity;

	@Autowired
	private PrdSuplyPlanEntity prdSuplyPlanEntity;

	@Autowired
	private PrdUdaEntity prdUdaEntity;

	@Autowired
	private ShtprdOrdEntity shtprdOrdEntity;

	@Autowired
	private SubSupEntity subSupEntity;

	@Autowired
	private SupEntity supEntity;

	@Autowired
	private WsPrdEaiAttrPrdSyncProcess wsPrdEaiAttrPrdSyncProcess;

	@Autowired
	private WsPrdEaiPrdSyncProcess wsPrdEaiPrdSyncProcess;

	@Autowired
	private WsPrdEaiPrdPriceRegModProcess wsPrdEaiPrdPriceRegModProcess;

	@Autowired
	private WsPrdEaiPrdChanlSyncProcess wsPrdEaiPrdChanlSyncProcess;

	@Autowired
	private WsPrdBisPrdItemMappnSyncProcess wsPrdBisPrdItemMappnSyncProcess;

	// 해외상품
	@Autowired
	private EcdForgnTmPrdEntity ecdForgnTmPrdEntity;

	@Autowired
	private PrdPrdDtrEntity prdPrdDtrEntity;		//상품물류 확장정보, sap 재구축 (2013/01/17 안승훈)

	@Autowired
	private PrdQaBefReqProcess prdQaBefReqProcess;		// QA의뢰, sap 재구축 (2013/01/29 안승훈)

	@Autowired
	private PrdColChgLogEntity  prdColChgLogEntity;

	@Autowired
	private OdsBroadBefMngInfoEntity odsBroadBefMngInfoEntity;
	
	@Autowired
	private PrdMontrnExcptEntity prdMontrnExcptEntity;

	private TransactionTemplate transactionTemplate;

	@Autowired
	public void init(PlatformTransactionManager smtcTransactionManager) {
		this.transactionTemplate = new TransactionTemplate(smtcTransactionManager);
	}

	@Autowired
	private PrdClsChkAprvEntity prdClsChkAprvEntity;
	public void setPrdClsChkAprvEntity(PrdClsChkAprvEntity prdClsChkAprvEntity) {
		this.prdClsChkAprvEntity = prdClsChkAprvEntity;
	}

	@Autowired
	private PrdClsSpecInfoEntity prdClsSpecInfoEntity;

	@Autowired
	private PrdPrcMngCmmProcess prdPrcMngCmmProcess;

	@Autowired
	private CpnApplyPrdEntity cpnApplyPrdEntity;
//	@Autowired
//	private PrdClsDescdItmInfoEntity prdClsDescdItmInfoEntity ;

	@Autowired
	//private WsPrdScoProdItemMapProcess wsPrdScoProdItemMapProcess ;		// 상품 item sap 동기화 (sap 재구축 2013/02/04 안승훈)
	private WsPrdHcoProdItemMapProcess wsPrdHcoProdItemMapProcess ;		// 상품 item sap 동기화 ([GS리테일 SAP 재무시스템 구축에 따른 Legacy 대응 개발 프로젝트][2023.09.01][최태웅] )

	@Autowired
	private WsPrdEaiQaAprvProcess wsPrdEaiQaAprvProcess;

	public void setCpnApplyPrdEntity(CpnApplyPrdEntity cpnApplyPrdEntity) {
		this.cpnApplyPrdEntity = cpnApplyPrdEntity;
	}


	@Autowired
	private PrdExplnEntity prdExplnEntity;	//2013-10-22 / [일반기술서 정보고시 통합] / kimky73

	public void setPrdExplnEntity(PrdExplnEntity prdExplnEntity) {
		this.prdExplnEntity = prdExplnEntity;
	}
	
//	@Autowired
//	private ImgMngProcess imgMngProcess;
//	public void setImgMngProcess(ImgMngProcess imgMngProcess ) {
//		this.imgMngProcess = imgMngProcess;
//	}

	@Autowired
	private VendrEntity vendrEntity;		//[현대백화점제휴-패널] 추가 sagekim	2014.08.21 
	
	public void setVendrEntity(VendrEntity vendrEntity) {
		this.vendrEntity = vendrEntity;
	}
	
	@Autowired
	private CmmCdEntity cmmCdEntity;
	

	@Autowired
	private SupPenltQryEntity supPenltQryEntity; //[PD-2015-007] 정동국 2015-05-12 패널 직송개선
	
	//[S][패널 2015_014_제도개선 변경]김영준(kyjhouse) 2015-10-05
	@Autowired
	private PrdAgreeDocEntity prdAgreeDocEntity;		 
	
	public void setPrdAgreeDocEntity(PrdAgreeDocEntity prdAgreeDocEntity) {
		this.prdAgreeDocEntity = prdAgreeDocEntity;
	}
	//[E][패널 2015_014_제도개선 변경]김영준(kyjhouse) 2015-10-05
	
	//[S][PD_2016_009] 도서산간 배송비추가 ljb 2016.07.13
//	@Autowired
//	private DlvInfoEntity dlvInfoEntity; 
	//[E][PD_2016_009] 도서산간 배송비추가 ljb 2016.07.13
	
	
    @Autowired
    private PaiInfoEntity paiInfoEntity;
    public void setPaiInfoEntity( PaiInfoEntity paiInfoEntity) {
        this.paiInfoEntity = paiInfoEntity;
    }
    
	@Autowired
	private CalndCdEntity calndCdEntity;
    
    @Autowired
    private QaAtachFileConctInfoEntity qaAtachFileConctInfoEntity;
    public void setQaAtachFileConctInfoEntity( QaAtachFileConctInfoEntity qaAtachFileConctInfoEntity) {
        this.qaAtachFileConctInfoEntity = qaAtachFileConctInfoEntity;
    }

	// [PD_2018_005_Gucci 입점] 2018-07-19, 김철현 : 프리미엄브랜드인 경우 주문가능여부 판단기준값 셋팅처리
    @Autowired
    private PrdPrmmBrandMngProcess prdPrmmBrandMngProcess;
    
    @Autowired
    private EcdPrdImpQryDEntity ecdPrdImpQryDEntity;
    
    ////[SR02200506079][2020.05.08][김영현]:정기배송 프로세스 3차 개선 관련 개발 요청
    @Autowired
    private PrdAttrPrdSkuMngProcess prdAttrPrdSkuMngProcess;
    
    /*[SKU][2021.01.05]:SKU 프로젝트*/ 
    @Autowired
	private PrdSkuMngEntity prdSkuMngEntity;

//    @Autowired
//    private PrdClsUdaEntity prdClsUdaEntity;

    /**
	 * <pre>
	 *
	 * desc : 상품채널 정보를 저장한다.(연계용)
	 *
	 * </pre>
	 * 
	 * 
	 * @author
	 * @date
	 * @param List
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> savePrdChanlList(
		                                         	List<PrdChanlInfo> addPrdChanlInfoPrd,
		                                         	List<PrdChanlInfo> modifyPrdChanlInfoPrd,
		                                         	List<PrdChanlInfo> removePrdChanlInfoPrd
											         ) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		Date date = SysUtil.getCurrTime();
   	 	String sysdtm = DateUtils.format(date,"yyyyMMddHHmmss");
		if (addPrdChanlInfoPrd.size() > 0) {
			for (int i = 0; i < addPrdChanlInfoPrd.size(); i++) {
				List<PrdChanlSyncInfo> prdChanlSyncInfoList = new ArrayList<PrdChanlSyncInfo>();
				// 상품채널정보 eai 인터페이스
				PrdChanlSyncInfo pPrdChanlSyncInfoID = null;

		    	pPrdChanlSyncInfoID = new PrdChanlSyncInfo();
		    	pPrdChanlSyncInfoID.setJobType("I");
		    	pPrdChanlSyncInfoID.setPrdCd(addPrdChanlInfoPrd.get(i).getPrdCd());
		    	pPrdChanlSyncInfoID.setChanlCd(addPrdChanlInfoPrd.get(i).getChanlCd());
		    	pPrdChanlSyncInfoID.setChanlMdId(addPrdChanlInfoPrd.get(i).getChanlMdId());
		    	pPrdChanlSyncInfoID.setSalePsblYn(addPrdChanlInfoPrd.get(i).getSalePsblYn());
		    	pPrdChanlSyncInfoID.setQaInspYn(addPrdChanlInfoPrd.get(i).getQaInspYn());
		    	pPrdChanlSyncInfoID.setStdRelsDdcnt(addPrdChanlInfoPrd.get(i).getStdRelsDdcnt());
		    	pPrdChanlSyncInfoID.setStdRelsDdcnt(addPrdChanlInfoPrd.get(i).getStdRelsDdcnt());
		    	pPrdChanlSyncInfoID.setRepMdUserId(addPrdChanlInfoPrd.get(i).getRepMdUserId());
		    	pPrdChanlSyncInfoID.setRegDtm(sysdtm);
		    	pPrdChanlSyncInfoID.setRegrId(addPrdChanlInfoPrd.get(i).getSessionUserId());
		    	pPrdChanlSyncInfoID.setModDtm(sysdtm);
		    	pPrdChanlSyncInfoID.setModrId(addPrdChanlInfoPrd.get(i).getSessionUserId());

				prdChanlSyncInfoList.add(pPrdChanlSyncInfoID);

				if (prdChanlSyncInfoList.size() > 0) {
						wsPrdEaiPrdChanlSyncProcess.prdChanlSyncProcess(prdChanlSyncInfoList);
				}
			}
		}

		if (modifyPrdChanlInfoPrd.size() > 0) {
			for (int i = 0; i < modifyPrdChanlInfoPrd.size(); i++) {
				List<PrdChanlSyncInfo> prdChanlSyncInfoList = new ArrayList<PrdChanlSyncInfo>();
				// 상품채널정보 eai 인터페이스
				PrdChanlSyncInfo pPrdChanlSyncInfoID = null;

		    	pPrdChanlSyncInfoID = new PrdChanlSyncInfo();
		    	pPrdChanlSyncInfoID.setJobType("U");
		    	pPrdChanlSyncInfoID.setPrdCd(modifyPrdChanlInfoPrd.get(i).getPrdCd());
		    	pPrdChanlSyncInfoID.setChanlCd(modifyPrdChanlInfoPrd.get(i).getChanlCd());
		    	pPrdChanlSyncInfoID.setChanlMdId(modifyPrdChanlInfoPrd.get(i).getChanlMdId());
		    	pPrdChanlSyncInfoID.setSalePsblYn(modifyPrdChanlInfoPrd.get(i).getSalePsblYn());
		    	pPrdChanlSyncInfoID.setQaInspYn(modifyPrdChanlInfoPrd.get(i).getQaInspYn());
		    	pPrdChanlSyncInfoID.setStdRelsDdcnt(modifyPrdChanlInfoPrd.get(i).getStdRelsDdcnt());
		    	pPrdChanlSyncInfoID.setStdRelsDdcnt(modifyPrdChanlInfoPrd.get(i).getStdRelsDdcnt());
		    	pPrdChanlSyncInfoID.setRepMdUserId(modifyPrdChanlInfoPrd.get(i).getRepMdUserId());
		    	pPrdChanlSyncInfoID.setRegDtm(sysdtm);
		    	pPrdChanlSyncInfoID.setRegrId(modifyPrdChanlInfoPrd.get(i).getSessionUserId());
		    	pPrdChanlSyncInfoID.setModDtm(sysdtm);
		    	pPrdChanlSyncInfoID.setModrId(modifyPrdChanlInfoPrd.get(i).getSessionUserId());

				prdChanlSyncInfoList.add(pPrdChanlSyncInfoID);

				if (prdChanlSyncInfoList.size() > 0) {
						wsPrdEaiPrdChanlSyncProcess.prdChanlSyncProcess(prdChanlSyncInfoList);
				}
			}
		}

		if (removePrdChanlInfoPrd.size() > 0) {
			for (int i = 0; i < removePrdChanlInfoPrd.size(); i++) {
				List<PrdChanlSyncInfo> prdChanlSyncInfoList = new ArrayList<PrdChanlSyncInfo>();
				// 상품채널정보 eai 인터페이스
				PrdChanlSyncInfo pPrdChanlSyncInfoID = null;

		    	pPrdChanlSyncInfoID = new PrdChanlSyncInfo();
		    	pPrdChanlSyncInfoID.setJobType("D");
		    	pPrdChanlSyncInfoID.setPrdCd(removePrdChanlInfoPrd.get(i).getPrdCd());
		    	pPrdChanlSyncInfoID.setChanlCd(removePrdChanlInfoPrd.get(i).getChanlCd());
		    	pPrdChanlSyncInfoID.setChanlMdId(removePrdChanlInfoPrd.get(i).getChanlMdId());
		    	pPrdChanlSyncInfoID.setSalePsblYn(removePrdChanlInfoPrd.get(i).getSalePsblYn());
		    	pPrdChanlSyncInfoID.setQaInspYn(removePrdChanlInfoPrd.get(i).getQaInspYn());
		    	pPrdChanlSyncInfoID.setStdRelsDdcnt(removePrdChanlInfoPrd.get(i).getStdRelsDdcnt());
		    	pPrdChanlSyncInfoID.setStdRelsDdcnt(removePrdChanlInfoPrd.get(i).getStdRelsDdcnt());
		    	pPrdChanlSyncInfoID.setRepMdUserId(removePrdChanlInfoPrd.get(i).getRepMdUserId());
		    	pPrdChanlSyncInfoID.setRegDtm(sysdtm);
		    	pPrdChanlSyncInfoID.setRegrId(removePrdChanlInfoPrd.get(i).getSessionUserId());
		    	pPrdChanlSyncInfoID.setModDtm(sysdtm);
		    	pPrdChanlSyncInfoID.setModrId(removePrdChanlInfoPrd.get(i).getSessionUserId());

				prdChanlSyncInfoList.add(pPrdChanlSyncInfoID);

				if (prdChanlSyncInfoList.size() > 0) {
					wsPrdEaiPrdChanlSyncProcess.prdChanlSyncProcess(prdChanlSyncInfoList);
				}
			}
		}
		return returnMap;
	}
	/**
	 * <pre>
	 *
	 * desc : 멀티코드목록을 저장한다.
	 *   -.상품등록을 신규로 하는 경우,
	 *    ca등록 : 멀티코드 = 상품코드, 채널그룹코드 = 'CC', 채널상세코드 = 'CA'
	 *    ec등록 : 멀티코드 = '8'||상품코드, 채널그룹코드 = 'CE', 채널상세코드 = 'PA'
	 *    지정하여 등록한다.
	 *   -.tc채널이 추가되는 경우 (멀티코드.채널그룹코드 = 'H')
	 *    tc등록 : 멀티코드 = '7'||상품코드, 채널그룹코드 = 'CT', 채널상세코드 = 'HB'
	 *    지정하여 등록한다.
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG (kbog2089)
	 * @date 2011-01-08 02:07:11
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> addMultiCdList(List<MultiCd> multiCdList) throws DevEntException{
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		DSData resultData = new DSData();
		// 멀티코드목록등록
		multiCdEntity.addMultiCdList(multiCdList);
		resultData.put("retCd", "S");
		returnMap.put("addMultiCdList", EDSFactory.create(EsbCmm.class, resultData));
		return returnMap;
	}

	@Override
	public OrdPsblQty addOrdQtyTmp(OrdPsblQty pOrdPsblQty) throws DevEntException {
		Map<String, Object> inputMap = new HashMap<String, Object>();

		OrdPsblQtyQryCond pOrdPsblQtyQryCond = new OrdPsblQtyQryCond();
		pOrdPsblQtyQryCond.setAttrPrdCd(pOrdPsblQty.getAttrPrdCd());
		pOrdPsblQtyQryCond.setPrdCd(pOrdPsblQty.getPrdCd());
		pOrdPsblQtyQryCond.setMultiCd(pOrdPsblQty.getMultiCd());
		pOrdPsblQtyQryCond.setChanlCd(pOrdPsblQty.getChanlCd());
		/** 2020-06-25 김태엽 수정 - HANGBOT-439_GSITMBO-61 - AK몰 제휴입점 */
		pOrdPsblQtyQryCond.setOrdQty(pOrdPsblQty.getOrdQty());

		PrdSuplyPlanInfo pPrdSuplyPlanInfo = new PrdSuplyPlanInfo();
		pPrdSuplyPlanInfo.setAttrPrdRepCd(pOrdPsblQty.getAttrPrdRepCd());
		pPrdSuplyPlanInfo.setSuplyPlanQty(pOrdPsblQty.getOrdQty());

		inputMap.put("inOrdPsblQtyQryCond", pOrdPsblQtyQryCond);
		inputMap.put("inOrdPsblQty", pOrdPsblQty);
		inputMap.put("inPrdSuplyPlanInfo", pPrdSuplyPlanInfo);

		OrdPsblQty ordPsblQty = addOrdQtyTmpList(inputMap);

		return ordPsblQty;
	}

	/**
	 * <pre>
	 *
	 * desc : 주문가능수량.재고수량.공급계획수량을 차감/환원하기 위하여 주문수량임시테이블에 등록한다.
	 * (Y : 주문가능, N : 주문불가, O : 수량부족, G : 사은품부족, S : 재고부족, B : 공급계획수량부족)
	 * 	 - 수량구분코드가 'ORD'인 경우 (주문가능수량 차감/환원요청)
	 * 	  주문가능수량조회를 수행한다.
	 * 	  IF (주문가능여부가 'Y' &amp; 조회후 결과값의 주문가능수량이 요청한 주문수량보다 크다)
	 * 	  임시주문가능수량목록을 등록한다.
	 * 	 사은품수량 < 주문수량 이면 주문가능여부를 'G'로 세팅한다.
	 * 	  ELSE
	 * 	  주문가능여부가 'Y'이면 주문가능여부를 'O'로 세팅하고 이벤트를 종료한다.
	 * 	 - 수량구분코드가 'STK'인 경우 (재고수량 차감/환원요청)
	 * 	 주문가능수량조회를 수행한다.
	 * 	 임시주문가능수량목록을 등록한다.
	 * 	 재고수량< 주문수량 이면 주문가능여부를 'S'로 세팅한다.
	 * 	 - 수량구분코드가 'BRD'인 경우 (공급계획수량 차감/환원요청)
	 * 	 주문상품공급계획정보조회를 수행한다.
	 * 	 임시주문가능수량목록을 등록한다.
	 * 	 공급계획수<주문수량 이면 주문가능여부를 'B'로 세팅한다.
	 *
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-11-23 12:48:44
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public List<OrdPsblQty> addOrdQtyTmpList(List<OrdPsblQty> pOrdPsblQtyList) throws DevEntException {
		for (int i = 0; i < pOrdPsblQtyList.size(); i++) {
			Map<String, Object> inputMap = new HashMap<String, Object>();

			OrdPsblQtyQryCond pOrdPsblQtyQryCond = new OrdPsblQtyQryCond();
			pOrdPsblQtyQryCond.setAttrPrdCd(pOrdPsblQtyList.get(i).getAttrPrdCd());
			pOrdPsblQtyQryCond.setPrdCd(pOrdPsblQtyList.get(i).getPrdCd());
			pOrdPsblQtyQryCond.setMultiCd(pOrdPsblQtyList.get(i).getMultiCd());
			pOrdPsblQtyQryCond.setChanlCd(pOrdPsblQtyList.get(i).getChanlCd());
			/** 2020-06-25 김태엽 수정 - HANGBOT-439_GSITMBO-61 - AK몰 제휴입점 */
			pOrdPsblQtyQryCond.setOrdQty(pOrdPsblQtyList.get(i).getOrdQty());

			PrdSuplyPlanInfo pPrdSuplyPlanInfo = new PrdSuplyPlanInfo();
			pPrdSuplyPlanInfo.setAttrPrdRepCd(pOrdPsblQtyList.get(i).getAttrPrdRepCd());
			pPrdSuplyPlanInfo.setSuplyPlanQty(pOrdPsblQtyList.get(i).getOrdQty());

			inputMap.put("inOrdPsblQtyQryCond", pOrdPsblQtyQryCond);
			inputMap.put("inOrdPsblQty", pOrdPsblQtyList.get(i));
			inputMap.put("inPrdSuplyPlanInfo", pPrdSuplyPlanInfo);

			pOrdPsblQtyList.set(i, addOrdQtyTmpList(inputMap));

		}

		return pOrdPsblQtyList;
	}

	/* [PD_2018_005_Gucci 입점] 2018-07-19, 김철현 : 프리미엄브랜드인 경우 주문가능수량정보를 해당 회사의 API
    를 통하여 가져오도록 한다. */	
	@Override
	public OrdPsblQty addOrdQtyTmpList(Map<String, Object> inputMap) throws DevEntException {
		// Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		OrdPsblQtyQryCond pOrdPsblQtyQryCond = (OrdPsblQtyQryCond) inputMap.get("inOrdPsblQtyQryCond");
		OrdPsblQty pOrdPsblQty = (OrdPsblQty) inputMap.get("inOrdPsblQty");
		// OrdPsblQtyQryCond pOrdPsblQtyQryCond1 = (OrdPsblQtyQryCond) inputMap.get("inOrdPsblQtyQryCond1");
		// OrdPsblQty pOrdPsblQty1 = (OrdPsblQty) inputMap.get("inOrdPsblQty1");
		// PrdSuplyPlanInfo pPrdSuplyPlanInfo = (PrdSuplyPlanInfo) inputMap.get("inPrdSuplyPlanInfo");

		logger.debug("pOrdPsblQty::" + pOrdPsblQty.getAttrPrdRepCd());
		// logger.debug("pOrdPsblQty1::" + pOrdPsblQty1.getAttrPrdRepCd());
		// logger.debug("pPrdSuplyPlanInfo::" + pPrdSuplyPlanInfo.getAttrPrdRepCd());

		// if(수량구분코드가 'ORD'인 경우) {
		if (pOrdPsblQty.getQtyGbnCd().toString().equals("ORD")) {
			// 주문가능수량조회
			EntityDataSet<DSData> getOrdPsblQty = ordPsblQtyEntity.getOrdPsblQty(pOrdPsblQtyQryCond);

			/* [PD_2018_005_Gucci 입점] 2018-07-19, 김철현 : 프리미엄브랜드인 경우 주문가능수량정보를 해당 회사의 API
            를 통하여 가져오도록 한다. 
            EntityDataSet<DSData> 타입을 그대로 사용하던 것을 OrdPsblQty 유형으로 변경후 처리한다.*/			
			
			// MJCHON 110210 주문가능수량 > 주문수량 => 주문가능수량 >= 주문수량으로 조건 변경
			// if(주문가능여부 = 'Y' & 주문가능수량 >= 주문수량) {
			if (getOrdPsblQty != null && getOrdPsblQty.getValues() != null) {
				if (getOrdPsblQty.size() > 0) {
					// [PD_2018_005_Gucci 입점] OrdPsblQty 타입으로 변경
					OrdPsblQty ordPsblQtyTmp = DevBeanUtils.wrappedMapToBean(getOrdPsblQty.getValues(), OrdPsblQty.class);
					
					/* [PD_2018_005_Gucci 입점] 2018-07-19, 김철현 : 프리미엄브랜드인 경우 주문가능수량정보를 해당 회사의 API
	                   를 통하여 가져오도록 한다.
	                   setPrmmBrandOrdPsblQty 함수내에서 변경될수 있는 값은 
	                   주문가능수량(OrdPsblQty) 과 주문가능여부(OrdPsblYn) 이다. */
					if (OrdConstant.PRMM_BRAND_GUCCI_SUP_CD.equals(ObjectUtils.toString(ordPsblQtyTmp.getSupCd()))) {
						//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
						SMTCLogger.infoPrd("======>프리미엄 브랜드 주문가능수량 체크 시작:PrdMngCmmProcessInpl.addOrdQtyTmpList");
						prdPrmmBrandMngProcess.setPrmmBrandOrdPsblQty(ordPsblQtyTmp);
						
						//2018-09-04 채널 주문서 진입시 프리미엄브랜드API 체크가 제외됨에(속도이슈) 따라
						//주문생성시 체크하는 이곳에서 관련정보 셋팅하여 채널ESB 주문생성인 
						//OrdCmmProcessImpl.modifyGsshopPrdOrdPsblQty 에서 주문가능수량0 인 경우 결품처리 되도록 한다.
						pOrdPsblQty.setSupCd(ordPsblQtyTmp.getSupCd());					//업체코드
						pOrdPsblQty.setOrdPsblQty(ordPsblQtyTmp.getOrdPsblQty());	    //주문가능수량
						pOrdPsblQty.setSafeStockQty(ordPsblQtyTmp.getSafeStockQty());	//안전재고수량
					}
					
					if ((pOrdPsblQty.getOrdQty().intValue() < 0) ||
							("Y".equals(ordPsblQtyTmp.getOrdPsblYn())
					        && ordPsblQtyTmp.getOrdPsblQty().intValue() >= pOrdPsblQty.getOrdQty().intValue())) {

						// LIH 2011.01.30 추가
						logger.debug(" 임시주문가능수량목록등록 ");
						pOrdPsblQty.setAttrPrdRepCd(ordPsblQtyTmp.getAttrPrdRepCd());
						// 임시주문가능수량목록등록
						ordPsblQtyEntity.addTmpOrdPsblQtyList(pOrdPsblQty);
						// 사은품수량 < 주문수량 이면 주문가능여부를 'G'로 세팅한다.
						/*
						 * if(getOrdPsblQty.getValues().getInt("gftRemanQty") < pOrdPsblQty.getOrdQty().intValue()) {
						 * pOrdPsblQty.setOrdPsblYn("G"); }
						 */
						pOrdPsblQty.setOrdPsblYn("Y");
					} else if ("E".equals(ordPsblQtyTmp.getOrdPsblYn())) {
						pOrdPsblQty.setOrdPsblYn("E");
					} else if ("T".equals(ordPsblQtyTmp.getOrdPsblYn())) {
						pOrdPsblQty.setOrdPsblYn("T");
					} else {
						if ("Y".equals(ordPsblQtyTmp.getOrdPsblYn())) {
							pOrdPsblQty.setOrdPsblYn("O");
						} else {
							pOrdPsblQty.setOrdPsblYn("N");
						}
					}
				}
			}
			/* [PD_2018_005_Gucci 입점] END */
		}

		// if(수량구분코드가 'STK'인 경우) {
		if (pOrdPsblQty.getQtyGbnCd().toString().equals("STK")) {
			/*
			 * 전민정 수정, 김주영D 재검토 필요 // 주문가능수량조회 ordPsblQtyEntity.getOrdPsblQty(pOrdPsblQtyQryCond); // 임시주문가능수량목록등록
			 * ordPsblQtyEntity.addTmpOrdPsblQtyList(pOrdPsblQty); // 주문가능수량조회 EntityDataSet<DSData> getOrdPsblQty =
			 * ordPsblQtyEntity.getOrdPsblQty(pOrdPsblQtyQryCond); // 재고수량< 주문수량 이면 주문가능여부를 'S'로 세팅한다. if (getOrdPsblQty
			 * != null && getOrdPsblQty.getValues() != null) { if( getOrdPsblQty.size() > 0 ) { // if(
			 * getOrdPsblQty.getValues().getBigDecimal("stockQty").intValue() < pOrdPsblQty.getOrdQty().intValue()) {
			 * if( getOrdPsblQty.getValues().getInt("stockQty") < pOrdPsblQty.getOrdQty().intValue()) {
			 * pOrdPsblQty.setOrdPsblYn("S"); } } }
			 */
			// 주문가능수량조회
			EntityDataSet<DSData> getOrdPsblQty = ordPsblQtyEntity.getOrdPsblQty(pOrdPsblQtyQryCond);

			if (getOrdPsblQty != null && getOrdPsblQty.getValues() != null) {
				if (getOrdPsblQty.size() > 0) {
					if (pOrdPsblQty.getOrdQty().intValue() > 0
							&& getOrdPsblQty.getValues().getInt("stockQty") > 0
							&& getOrdPsblQty.getValues().getInt("stockQty") >= pOrdPsblQty.getOrdQty().intValue()) {
						// 임시주문가능수량목록등록
						ordPsblQtyEntity.addTmpOrdPsblQtyList(pOrdPsblQty);
						pOrdPsblQty.setOrdPsblYn("Y");

					} else {
						pOrdPsblQty.setOrdPsblYn("S");
					}
				}
				
				/** 2013-10-28 김태엽 추가 - SR02131025085 - 입고택배 배송예정일 오안내건 로그 추가 */
				/** 2013-11-04 김태엽 추가 - SR02131025085 - 입고택배 배송예정일 오안내건 로그 추가 */
				/** 2020-09-10 김태엽 수정 - HANGBOT-3880_GSITMBO-1778 - WAS Log 정리 */
				SMTCLogger.infoOrd("센터재고수량차감 - STK - "
						+ "ordItemId : " + pOrdPsblQty.getOrdItemId() + ", "
						+ "ordPsblQty : " + getOrdPsblQty.getValues().getInt("ordPsblQty") + ", "
						+ "prdStockQty : " + getOrdPsblQty.getValues().getInt("prdStockQty") + ", "
						+ "stockQty : " + getOrdPsblQty.getValues().getInt("stockQty") + ", "
						+ "ordQty : " + pOrdPsblQty.getOrdQty().intValue() + ", "
						+ "attrPrdCd : " + pOrdPsblQty.getAttrPrdRepCd());
				
			}
		}
		/*
		 * - 수량구분코드가 'BRD'인 경우 (공급계획수량 차감/환원요청) 주문상품공급계획정보조회를 수행한다. 임시주문가능수량목록을 등록한다. 공급계획수<주문수량 이면 주문가능여부를 'B'로 세팅한다.
		 */
		if (pOrdPsblQty.getQtyGbnCd().toString().equals("BRD")) {
			// 주문상품공급계획정보조회
			PrdSuplyPlanInfo pPrdSuplyPlanInfo = new PrdSuplyPlanInfo();
			pPrdSuplyPlanInfo.setQryGbn("3");
			pPrdSuplyPlanInfo.setSuplyPlanMngNo(pOrdPsblQty.getSuplyPlanMngNo());
			pPrdSuplyPlanInfo.setSuplyPlanQty(pOrdPsblQty.getOrdQty());
			pPrdSuplyPlanInfo.setAttrPrdRepCd(pOrdPsblQty.getAttrPrdRepCd());
			pPrdSuplyPlanInfo.setSuplySeq(pOrdPsblQty.getPmoNo()==null?"":pOrdPsblQty.getPmoNo().toString());
			/** 2015-07-06 김태엽 수정 - SR02150703026 - 데이터홈쇼핑 공급계획 적용 */
			pPrdSuplyPlanInfo.setBroadMnfcTypCd(pOrdPsblQty.getBroadMnfcTypCd());
			
			EntityDataSet<DSData> getOrdPsblQty = prdSuplyPlanEntity.getOrdPrdSuplyPlanInfo(pPrdSuplyPlanInfo);

			logger.debug(" 공급계획수량차감 getOrdPsblQty : " + getOrdPsblQty);

			if (getOrdPsblQty != null && getOrdPsblQty.getValues() != null) {
				if (getOrdPsblQty.size() > 0) {
					if (getOrdPsblQty.getValues().getInt("suplyPlanQty") < pOrdPsblQty.getOrdQty().intValue()) {
						pOrdPsblQty.setOrdPsblYn("B");
					} else {
						// 원상품코드란에 공급계획관리번호, 사은품번호란에 공급순번 등록 --> 공급계획수량 계산시 이용  20110618 JOO
						pOrdPsblQty.setPrdCd(new BigDecimal(getOrdPsblQty.getValues().getString("suplyPlanMngNo")));
						pOrdPsblQty.setPmoNo(new BigDecimal(getOrdPsblQty.getValues().getString("suplySeq")));
						// 임시주문가능수량목록등록
						ordPsblQtyEntity.addTmpOrdPsblQtyList(pOrdPsblQty);
						pOrdPsblQty.setOrdPsblYn("Y");
					}
				}
				
				/** 2015-07-06 김태엽 수정 - SR02150703026 - 데이터홈쇼핑 공급계획 적용 */
				/** 2020-09-10 김태엽 수정 - HANGBOT-3880_GSITMBO-1778 - WAS Log 정리 */
				SMTCLogger.infoOrd("공급계획수량차감 - BRD - "
						+ "ordItemId : " + pOrdPsblQty.getOrdItemId() + ", "
						+ "ordPsblQty : " + getOrdPsblQty.getValues().getInt("ordPsblQty") + ", "
						+ "suplyPlanQty : " + getOrdPsblQty.getValues().getInt("suplyPlanQty") + ", "
						+ "suplyPlanMngNo : " + getOrdPsblQty.getValues().getInt("suplyPlanMngNo") + ", "
						+ "ordQty : " + pOrdPsblQty.getOrdQty().intValue() + ", "
						+ "attrPrdCd : " + pOrdPsblQty.getAttrPrdRepCd() + ", "
						+ "broadMnfcTypCd : " + pOrdPsblQty.getBroadMnfcTypCd()
						);					
				
			}
		}
		
		return pOrdPsblQty;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> addOrdQtyTmpList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		String key = "inAddOrdQtyTmpList";
		OrdPsblQtyQryCond pOrdPsblQtyQryCond = dataSet.getDataset4InsertObjFirst(key, OrdPsblQtyQryCond.class);
		OrdPsblQty pOrdPsblQty = dataSet.getDataset4InsertObjFirst(key, OrdPsblQty.class);
		// OrdPsblQtyQryCond pOrdPsblQtyQryCond1 = dataSet.getDataset4InsertObjFirst(key, OrdPsblQtyQryCond.class);
		// OrdPsblQty pOrdPsblQty1 = dataSet.getDataset4InsertObjFirst(key, OrdPsblQty.class);
		PrdSuplyPlanInfo pPrdSuplyPlanInfo = dataSet.getDataset4InsertObjFirst(key, PrdSuplyPlanInfo.class);

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("inOrdPsblQtyQryCond", pOrdPsblQtyQryCond);
		inputMap.put("inOrdPsblQty", pOrdPsblQty);
		// inputMap.put("inOrdPsblQtyQryCond1", pOrdPsblQtyQryCond1);
		// inputMap.put("inOrdPsblQty1", pOrdPsblQty1);
		inputMap.put("inPrdSuplyPlanInfo", pPrdSuplyPlanInfo);

		returnMap.put("outPsblQtyEntity", EDSFactory.createObj(OrdPsblQty.class, addOrdQtyTmpList(inputMap)));

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품기본정보를 입력한다.(연계용)
	 * 20110428 상품기술서 정보, HTML기술서 정보 추가
	 * 
	 * [HANGBOT-9096 GSITMBO-4581] 2021.04.21 이태호  위수탁 반품 수거형태 직반출 Default 변경관련
	 * 
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-11-21 10:49:08
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	/*
	 * PrdmMain prdmMain // 기본 및 배송 정보 List<PrdAttrPrdMinsert> pattrPrdM // 속성상품정보 PrdStockDinsert prdAttrPrdm // 상품재고정보
	 * 속성상품 정보에 따른 list PrdprcHinsert prdprcHinsert // 상품가격이력 List<PrdChanlDinsert> prdChanlDinsert // 상품채널정보
	 * PrdOrdPsblQtyDinsert prdOrdPsblQtyD // 상품주문가능수량정보 재고 정보에 2배 (ad, az) list List<PrdChanlMappnDinsert>
	 * prdChanlMappnD // 채널매핑정보 PrdPrdDinsert prdPrdD // 상품확장정보 List<PrdNmChgHinsert> prdNmChg // 상품명변경이력
	 * List<EcdSectPrdlstInfo> ecdSectPrdlstInfo // 카테고리 매장 SafeCertPrd psafeCertPrd // 안전인증내역
	 * bpr개선사항 추가 : 유료배송비 금액만 입력시 유료배송비코드가 없는경우 배송비코드를 생성한후 해당 배송비코드 set처리
	 * [SR02160630101][2016.0629][김영현]:편의점반품 설정 로직 추가
	 * [SR02161108033][2016.11.14][김영현]:골드바 판매제한에 따른 개발요청
	 * [HANGBOT-32562 GSITMBO-19050] 2022.04.04 이태호 세일즈원의 상품 신규등록 수정요청 - 상품 가로,세로,높이,무게 정보 복사 되지 않게 수정
	 */
	// 상품물류 확장 정보 추가 :  sap 재구축 (2013/01/17 안승훈)
	
	public Map<String, EntityDataSet> addPrdBaseInfoList(PrdmMain prdmMain, // 상품마스터
	        List<PrdAttrPrdMinsert> pattrPrdM, // 상품속성
	        List<PrdStockDinsert> prdAttrPrdm, // 재고속성
	        PrdprcHinsert prdprcHinsert, // 상품가격
	        List<PrdChanlInfo> prdChanlDinsert, // 상품채널
	        List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD, // 상품주문가능수량
	        List<PrdChanlMappnDinsert> prdChanlMappnD, // 채널매핑정보
	        PrdPrdDinsert prdPrdD, // 상품확장정보
	        List<PrdNmChgHinsert> prdNmChg, // 상품명변경
	        List<EcdSectPrdlstInfo> ecdSectPrdlstInfo, // 카테고리매장
	        SafeCertPrd psafeCertPrd, // 인증정보
	        List<PrdSchdDInfo> prdSchdDInfo, // 상품예정정보
	        List<PrdNumvalDinfo> prdNumvalDinfo, // 상품수치정보
	        List<PrdSpecVal> prdSpecInfo, // 상품사양정보
	        List<PrdUdaDtl> prdUdaDtl, // 상품UDA상세
	        PrdNtcInfo prdNtcInfo, // 상품공지
	        List<PrdMetaDInfo> prdMetaDInfo ,// 상품메타정보
	        List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList, // 상품기술서 정보
	        List<PrdDescdHtmlDInfo> prdDescdHtmlDInfoList, //상품 HTML 기술서 정보
	        ForgnTmPrdMng forgnTmPrdMng, // 해외상품
	        List<BundlPrdCmposInfo> bundlPrdCmposInfoList, // 해외상품세트 구성품
	        PrdDtrD prdDtrD	// 상품물류 확장정보
	        ) throws Exception {
		
		List<PrdFrenenvInfo> prdFrenenvInfoList = new ArrayList<PrdFrenenvInfo>();
		return this.addPrdBaseInfoList(prdmMain, pattrPrdM, prdAttrPrdm, prdprcHinsert, prdChanlDinsert, prdOrdPsblQtyD, prdChanlMappnD, prdPrdD, prdNmChg, ecdSectPrdlstInfo, psafeCertPrd, prdSchdDInfo, prdNumvalDinfo, prdSpecInfo, prdUdaDtl, prdNtcInfo, prdMetaDInfo, prdDesceGenrlDInfoList, prdDescdHtmlDInfoList, forgnTmPrdMng, bundlPrdCmposInfoList, prdDtrD, prdFrenenvInfoList);
	}
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> addPrdBaseInfoList(PrdmMain prdmMain, // 상품마스터
	        List<PrdAttrPrdMinsert> pattrPrdM, // 상품속성
	        List<PrdStockDinsert> prdAttrPrdm, // 재고속성
	        PrdprcHinsert prdprcHinsert, // 상품가격
	        List<PrdChanlInfo> prdChanlDinsert, // 상품채널
	        List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD, // 상품주문가능수량
	        List<PrdChanlMappnDinsert> prdChanlMappnD, // 채널매핑정보
	        PrdPrdDinsert prdPrdD, // 상품확장정보
	        List<PrdNmChgHinsert> prdNmChg, // 상품명변경
	        List<EcdSectPrdlstInfo> ecdSectPrdlstInfo, // 카테고리매장
	        SafeCertPrd psafeCertPrd, // 인증정보
	        List<PrdSchdDInfo> prdSchdDInfo, // 상품예정정보
	        List<PrdNumvalDinfo> prdNumvalDinfo, // 상품수치정보
	        List<PrdSpecVal> prdSpecInfo, // 상품사양정보
	        List<PrdUdaDtl> prdUdaDtl, // 상품UDA상세
	        PrdNtcInfo prdNtcInfo, // 상품공지
	        List<PrdMetaDInfo> prdMetaDInfo ,// 상품메타정보
	        List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList, // 상품기술서 정보
	        List<PrdDescdHtmlDInfo> prdDescdHtmlDInfoList, //상품 HTML 기술서 정보
	        ForgnTmPrdMng forgnTmPrdMng, // 해외상품
	        List<BundlPrdCmposInfo> bundlPrdCmposInfoList, // 해외상품세트 구성품
	        PrdDtrD prdDtrD,	// 상품물류 확장정보
	        List<PrdFrenenvInfo> prdFrenenvInfoList // 상품친환경인증 정보 
	        ) throws Exception {
		
		
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		DSData returnDsData = new DSData();
		ScmMd scmMd = new ScmMd();
		// chkDsData = getPrdSaveChk(dataSet);
		SupDtlInfo supDtlInfo = new SupDtlInfo();

		EntityDataSet<DSMultiData> supDtlInfo1 = prdEntity.getPrdSupInfo(prdmMain);
		List<PrdDescdSyncInfo> addPrdDescdEaiList = new ArrayList<PrdDescdSyncInfo>();
		if (supDtlInfo1 == null) {
			returnDsData.put("prdCd", "");
			returnDsData.put("retCd", "-1");
			returnDsData.put("retMsg", Message.getMessage("cmm.msg.027", new String[] { "상품", "협력사 정보" })); // 해당 {0}의 {1}가(이) 존재하지 않습니다.
			returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
			return returnMap;
		}
		// 패널티등급을 조회해서 건수가 있으면 신규등록을 할 수 없다.
		scmMd.setSupCd(prdmMain.getSupCd());
		scmMd.setMdId(prdmMain.getOperMdId());
		scmMd.setSubSupCd(prdmMain.getSubSupCd());
		scmMd.setGradeSt("I");
		// 신규상품등록 시 방송상품인 경우에는 패널티 제한을 하지 않는다. (2011/08/03 OSM)
		int tvChanlCheck = 0;
		for (PrdChanlInfo prdChanlInfo : prdChanlDinsert) {
			if ("C".equals(prdChanlInfo.getChanlCd())) {
				tvChanlCheck = 1;
				scmMd.setChanlCd(prdChanlInfo.getChanlCd());
				break;
			}
		}
		if (tvChanlCheck == 0) {
			//DSData outstkOrdRegCnt = ecScmMngEntity.getGsSuppGradeCnt(scmMd);
			DSData outstkOrdRegCnt = supPenltQryEntity.getSupPenltGradeCnt(scmMd);//[PD-2015-007] 정동국 2015-05-12 : 협력사패널티조회 수정
            
			if (outstkOrdRegCnt.getInt("cnt")  > 0){
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", Message.getMessage("prd.msg.381"));
				returnMap.put("outSavePrdBase", EDSFactory.create(EsbCmm.class, returnDsData));
				return returnMap;
			}
		}
		
		//상품신규등록시 아이템별 라이센스 만료 여부 조회하여 만료된경우 상품 등록 불가 처리[SR02140425037]
	    ItemReg itemCdInfo = new ItemReg();
		itemCdInfo.setPrdClsCd(prdmMain.getPrdClsCd());
		itemCdInfo.setBrandCd(prdmMain.getBrandCd());
		
		EntityDataSet<DSData> itemLicentWarnInfo =  itemCdEntity.getItemLicensWarnChkInfo(itemCdInfo, prdSpecInfo);
		
		if( itemLicentWarnInfo != null && itemLicentWarnInfo.getValues() != null ) {
	    	if("Y".equals(itemLicentWarnInfo.getValues().getString("licensChkYn"))) {
	    		returnDsData.put("retCd", "E");
	    		String itemCd = itemLicentWarnInfo.getValues().getString("itemCd");
	    		if("10".equals(itemLicentWarnInfo.getValues().getString("itemMngItmCd")) ) {
	    			returnDsData.put("retMsg", "해당 아이템은 라이센스가 만료되어 상품을 신규등록 할 수 없습니다.("+itemCd+")");
		    	} else {
		    		returnDsData.put("retMsg", "해당 아이템은 경고장이 접수되어 상품을 신규등록 할 수 없습니다.("+itemCd+")");
		    	}
		    	
				returnMap.put("outSavePrdBase", EDSFactory.create(EsbCmm.class, returnDsData));
				
				return returnMap;
		    }
	    }
		
		supDtlInfo.setSupCd(supDtlInfo1.getValues().getBigDecimal(0, "supCd").toString()); // 협력사 코드
		supDtlInfo.setContYn(supDtlInfo1.getValues().getString("contYn")); // 협력사 코드 계약여부
		supDtlInfo.setCondtlPrchPmsnYn(supDtlInfo1.getValues().getString("condtlPrchPmsnYn")); // 협력사.조건부매입허용여부
		supDtlInfo.setDirTakoutYn(supDtlInfo1.getValues().getString("dirTakoutYn")); // 직반출여부
		supDtlInfo.setCvsRtpYn(supDtlInfo1.getValues().getString("cvsRtpYn")); // 협력사.편의점반품여부
		supDtlInfo.setTxnEndDt(supDtlInfo1.getValues().getString("txnEndDtChk")); // 협력사.거래종료일자
		supDtlInfo.setPrdOboxCd(supDtlInfo1.getValues().getString("prdOboxCd")); // 협력사.상품합포장코드
		supDtlInfo.setDirdlvEntrstDlvYn(supDtlInfo1.getValues().getString("dirdlvEntrstDlvYn")); // 협력사.직송위탁배송여부
		supDtlInfo.setDirdlvEntrstPickYn(supDtlInfo1.getValues().getString("dirdlvEntrstPickYn")); // 협력사.직송위탁수거여부
		supDtlInfo.setApntDlvsPickTypCd(supDtlInfo1.getValues().getString("apntDlvsPickTypCd")); // 협력사.지정택배수거유형코드
		supDtlInfo.setThplUseYn(supDtlInfo1.getValues().getString("thplUseYn")); // 3PL사용여부 (sap 재구축 추가 : 2013/01/23 안승훈 )
		
//		supDtlInfo.setDlvsPickPsblYn(supDtlInfo1.getValues().getString("dlvsPickPsblYn"));  //택배수거가눙여부
		
		if (prdmMain.getOrdMnfcYn().equals("1") || prdmMain.getOrdMnfcYn().equals("Y")) {
			prdmMain.setOrdMnfcYn("Y");
		}else {
			prdmMain.setOrdMnfcYn("N");
		}
		//[SR02180911580][2018.09.10][이용문]:세일즈원 신규상품승인_아이템 가격범위초과 (가격정보가 없다보니 아이템가격범위초과 항목에 Y로 노출되는 현상) 
		if( prdmMain.getSalePrc() == null || prdmMain.getSalePrc().intValue() <= 0 ){
			prdmMain.setSalePrc(prdprcHinsert.getSalePrc());
		}
		
		//[S][SR02170913526][2017.09.14][최미영]:전통주기본프로세스
	    PrdClsUda prdClsUda1 = new PrdClsUda(); 
	    prdClsUda1.setUdaNo(new BigDecimal( "230" ));
	    prdClsUda1.setPrdClsCd(prdmMain.getPrdClsCd());      
	    if(PrdClsUdaUtils.isPrdClsUdaFlag(prdClsUda1)){
	    	// 등록시 선택없음으로 자동 셋팅
	    	if (prdmMain.getGsnpntNoGivYn() == null || "".equals(prdmMain.getGsnpntNoGivYn())) {
	    		prdmMain.setGsnpntNoGivYn("Y"); // GS엔포인트적립제한
	    	}
	        prdmMain.setVipCpnLimitPrdYn("Y");  //슈퍼/더블쿠폰 제한(값을 줘야 CPN_LIMIT_PRD_D 테이블에 등록됨)
	         
	        //위드넷에서 직접 들어온 경우 쿠폰적용여부 쿠폰 미적용으로 변경
	        if ("WITH".equals(prdmMain.getSessionUserId()) ){
	            prdmMain.setCpnApplyTypCd("09");
	            prdmMain.setAutoOrdPsblYn("N"); //자동주문가능여부
				prdmMain.setImmAccmDcLimitYn("Y"); // 적립금즉시제한
				prdmMain.setAliaSpclsalLimitYn("Y");// 제휴특판제한
	        }     
	          
	        //전통주 체크로직 반영
	        prdmMain = checkTraditionalLiquorPrd(prdmMain);
	        if (prdmMain.getRetCd().equals("-1")) {
	            returnDsData.put("prdCd", "");
	            returnDsData.put("retCd", "-1");
	            returnDsData.put("retMsg", prdmMain.getRetMsg());
	            returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
	            return returnMap;
	        }
	    } 
	    //[E][SR02170913526][2017.09.14][최미영]:전통주기본프로세스

		// 1200 입고택배(귀금)-택배, 1500 입고택배(귀금)-직반 은 센터추가포장 필수 설정로 처리 [bpr 2013.03.03]
		if (("1200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) || "1500".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())))
				&& !"Y".equals(StringUtils.NVL(prdmMain.getCentAddPkgYn()))) {
			prdmMain.setCentAddPkgYn("Y");
		}
		//[SR02160614077][2016.06.13][김영현]:상품판매상태 변경 EAI 과다 호출 로직 개선 및 배송/수거 방어 제한 로직 추가
		//직송 - 택배수거 등록 제한		
		if ("3000".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))){
			returnDsData.put("prdCd", "");
			returnDsData.put("retCd", "-1");
			//returnDsData.put("retMsg", "배송형태 직송은(는) 업체수거이어야 합니다. "); //prd.msg.224={0}은(는) {1}이어야합니다
			returnDsData.put("retMsg", Message.getMessage("prd.msg.224", new String[]{"배송형태 직송", "업체수거"}));
			returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
			return returnMap;
		}

		/*유료배송인경우 배송비 코드가 없고 유료배송비금액이 있는경우 기존에 배송비 코드가 없으면 배송비 코드 생성.[bpr 박현신]2013.01.30*/
		if( "Y".equals(prdmMain.getChrDlvYn()) &&
			prdmMain.getChrDlvcCd() == null &&
			prdmMain.getChrDlvcAmt() != null &&
			prdmMain.getChrDlvcAmt().compareTo(new BigDecimal("0")) > 0) {
			SupDlvcPrd supDlvcPrd = new SupDlvcPrd();

			supDlvcPrd.setDlvcAmt(prdmMain.getChrDlvcAmt());
			supDlvcPrd.setSupCd(prdmMain.getSupCd());
			supDlvcPrd.setDlvcLimitAmt(prdmMain.getShipLimitAmt());
			
			// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
			// 직송이 아닐경우 당사배송비를 조회 하기 위해 배송비구분코드에 'GS', 협력사코드에 '989081' 를 세팅한다.
			if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){
				supDlvcPrd.setDlvcGbnCd("GS") ; // 배송비구분코드 [ 당사 배송비 : 'GS'(협력사는 당사협력사코드 : 989081) ]
				supDlvcPrd.setSupCd(new BigDecimal("989081")) ; // 당사협력사코드
			}
			
			DSData dlvcCd = supDlvcEntity.getCntSupDlvcCd(supDlvcPrd);
			//배송비코드가 없으면 생성한다.
			if (dlvcCd.size()  <= 0 ) {
				List<SupDlvcPrd> supDlvcPrdRegList = new ArrayList<SupDlvcPrd>();
				List<SupDlvcPrd> supDlvcPrdModList = new ArrayList<SupDlvcPrd>();

				supDlvcPrd.setSessionObject(prdmMain);
				supDlvcPrd.setMdId("60027");
				supDlvcPrd.setStdAmtYn(prdmMain.getShipLimitYn());
				supDlvcPrdRegList.add(supDlvcPrd);
				PrdEaiPrdReTnsRslt result = supDlvcMngProcess.saveSupDlvcPrdList2(supDlvcPrdRegList, supDlvcPrdModList);
				if ("S".equals(result.getResponseResult())) {
					prdmMain.setChrDlvcCd( new BigDecimal(result.getResponseMessage()));
				}


			} else{
				prdmMain.setChrDlvcCd(dlvcCd.getBigDecimal("dlvcCd"));
			}

		 }

		SupPrdAprvQryCond pSupPrdAprvQryCond = new SupPrdAprvQryCond();
		pSupPrdAprvQryCond.setSupCd(prdmMain.getSupCd());
		pSupPrdAprvQryCond.setSuppGoodsCode(StringUtils.NVL(prdmMain.getSupPrdCd()));
		pSupPrdAprvQryCond.setSupPrdCd(StringUtils.NVL(prdmMain.getSupPrdCd()));
		// 상품승인상태조회 -> 해외상품일 경우에는 확인하지 않도록 수정 (2011/05/29 OSM)
		if (forgnTmPrdMng == null || forgnTmPrdMng.getFrTmSeq() == null) {
			if (prdmMain.getPrdAprvDataYn() != null && StringUtils.NVL(prdmMain.getPrdAprvDataYn()).equals("Y")) {
				DSMultiData supPrdAprvSt = ecScmMngEntity.getSupPrdAprvSt(pSupPrdAprvQryCond).getValues();

				if (supPrdAprvSt != null && supPrdAprvSt.size() > 0 && !("00".equals(StringUtils.NVL(supPrdAprvSt.get(0).getString("prdAprvStCd"))))) {
					String prdAprvStNm = StringUtils.NVL(supPrdAprvSt.get(0).getString("prdAprvStNm"));
					returnDsData.put("prdCd", "");
					returnDsData.put("retCd", "-1");
					returnDsData.put("retMsg", Message.getMessage("prd.msg.286", new String[]{prdAprvStNm}));
					returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
					return returnMap;
				}
			}
		}

		// 해외상품 로직추가 (2011/05/03 OSM)
		if (forgnTmPrdMng != null && forgnTmPrdMng.getFrTmSeq() != null) {
			prdmMain.setForgnPrdNormPrc((forgnTmPrdMng.getForgnPrdNormPrc() == null) ? null : forgnTmPrdMng.getForgnPrdNormPrc());
			prdmMain.setPrdGbnCd("10");
			prdmMain.setFrTmSeq(forgnTmPrdMng.getFrTmSeq());
		}

		// prdmMain = BusComp_PreSetFieldValue("" , "", prdmMain, supDtlInfo, prdprcHinsert);
		
		// [새벽배송2차] 새벽배송비 세팅 [S]
		prdPrdD = setDawnDlvInfo(prdmMain, prdPrdD);
		// [새벽배송2차] 새벽배송비 세팅 [E]

		// 필수 입력 값 Check
		/* [딜구조개선-패널] 2014-11-06, 김상철, 딜더미상품인 경우 체크안하게  */ 
		if(!"88".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))){
			//SMTCLogger.writePrd("필수 입력 값 Check");
			prdmMain = checkPrdmPK(supDtlInfo, prdmMain, prdprcHinsert, prdPrdD, psafeCertPrd, prdSpecInfo, prdUdaDtl, prdNumvalDinfo,
					prdChanlDinsert, prdNmChg);
			logger.debug("prdmMain ==>" + prdmMain);
			//SMTCLogger.writePrd("prdmMain ==>" + prdmMain);
			if (prdmMain.getRetCd().equals("-1")) {
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", prdmMain.getRetMsg());
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
		
			// prdmMain = BusComp_PreWriteRecord("", prdmMain, supDtlInfo, prdprcHinsert, psafeCertPrd, prdPrdD);

			// 연관 입력 값 Check
			prdmMain = checkPrdmRelativeValue(supDtlInfo, prdmMain, prdprcHinsert, prdPrdD, psafeCertPrd, prdSpecInfo,
					prdUdaDtl, prdNmChg);
			if (prdmMain.getRetCd().equals("-1")) {
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", prdmMain.getRetMsg());
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
		
			// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
			/*
			 * 입고택배, 직택배도 유료배송 가능하도록 수정 (채널코드 'C', 'U', 'D', 'H' 제외) 
			 *  -> checkPrdmRelativeValue 에서 유료배송비 부분을 checkPrdchrDlvInsp 이동..
			 *     채널코드 도 체크해야 하기 때문에 기존함수 사용이 어려워 분리. 
			 */
			List<PrdChanlInfo> prdChanlDupdate = new ArrayList<PrdChanlInfo>() ;
			prdmMain = checkPrdchrDlvInsp(prdmMain, prdChanlDinsert, prdChanlDupdate) ; // 유료배송검사
			if (prdmMain.getRetCd().equals("-1")) {
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", prdmMain.getRetMsg());
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			
			logger.debug("checkPrdmRelativeValue 체크 완료");
		}
		/* [딜구조개선-패널] END */ 
		
		// 연관값 Default Setting
		Map valueMap = new HashMap();
		valueMap = setPrdmRelativeValue(supDtlInfo, prdmMain, prdprcHinsert, prdPrdD, psafeCertPrd, prdSpecInfo,
				prdUdaDtl);
		prdmMain = (PrdmMain) valueMap.get("prdmMain");
		prdPrdD = (PrdPrdDinsert) valueMap.get("prdPrdD");
		logger.debug("setPrdmRelativeValue 체크 완료");

		// 상품 물류확장 정보의 validation check , sap 재구축 (2013/01/18 안승훈)
		//SR02170413078]  2017-04-14 직송관리대행 상품에대한 요청 데이터 추가 validation 체크  gowinix start
		//배송유형이 직송(택배)업체수거인것만(code : 3200)이 아니면 직송관리대행여부는 N으로 설정함.
		if (!"3200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))){ //상품 배송수거형태가 직송(택배)업체수거인것만(code : 3200)
			prdDtrD.setDirdlvMngAgncyYn("N"); //직송관리대행은 무조건 N
		}
		//SR02170413078]  2017-04-14 직송관리대행 상품에대한 요청 데이터 추가 validation 체크  gowinix end
		
		prdmMain = checkPrdDtrdRelativeValue(prdmMain, prdDtrD);

		if (prdmMain.getRetCd().equals("-1")) {
			returnDsData.put("prdCd", "");
			returnDsData.put("retCd", "-1");
			returnDsData.put("retMsg", prdmMain.getRetMsg());
			returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
			return returnMap;
		}

		// 상품코드 채번에 해외상품 관련 수정 (2011/05/26 OSM)
		EntityDataSet<DSData> sprdcd = new EntityDataSet<DSData>();
		
		/* [딜구조개선-패널] 2014-10-10, 김상철, 딜더미상품인 경우 프런트에서 넘어온 신규코드로 세팅  */ 
		if("88".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))){
			DSData pDsd = new DSData();
			pDsd.put("prdCd", prdmMain.getPrdCd());
			sprdcd.setValues(pDsd);
		}else{
			if (forgnTmPrdMng != null && forgnTmPrdMng.getPrdid() != null) {
				// 해외세트상품 등록의 경우 prdid가 마이너스로 전송 (2011/11/03 OSM).
				if (forgnTmPrdMng.getPrdid().compareTo(new BigDecimal("0")) < 0) {
					sprdcd = prdEntity.setPrdCd(); // 상품코드 채번
					forgnTmPrdMng.setPrdid(sprdcd.getValues().getBigDecimal("prdCd"));
				} else {
					FieldInfoSet fis = new FieldInfoSet();
					fis.add("prdCd", BigDecimal.class.getName(), 255);
					sprdcd.setFieldInfoSet(fis);
					DSData dsd = new DSData();
					dsd.put("prdCd", forgnTmPrdMng.getPrdid());
					sprdcd.setValues(dsd);
				}
			} else {
				sprdcd = prdEntity.setPrdCd(); // 상품코드 채번
			}
		}
		/* [딜구조개선-패널] END */ 
		
		// 상품코드 세팅
		prdmMain.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
		if (psafeCertPrd != null) {
			psafeCertPrd.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
		}
		prdPrdD.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
		//상품권분류인경우 주문유의사항 문구 카드제한추가 2013.09.04 박현신 SR02130902043
		//[SR02141226076] : B310111 추가 
		/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 _485 Start */
		PrdClsUda param = new PrdClsUda();
		param.setUdaNo(new BigDecimal( "130" ));
		param.setPrdClsCd(prdmMain.getPrdClsCd());		
        /*if("B310105".equals(prdmMain.getPrdClsCd().substring(0, 7)) ||
        	"B310111".equals(prdmMain.getPrdClsCd().substring(0, 7))) {*/
		if(PrdClsUdaUtils.isPrdClsUdaFlag(param)){
			/** 2019-10-29 김태엽 수정 - SR02191010960 - 유가증권(상품권) 구매시 무통장 결제 제한 팝업문구 수정 */
			// prdPrdD.setOrdAttndCntnt("상품권은 카드 결제가 제한되는 상품입니다.\n결제수단이 카드인 경우 주문을 할 수 없습니다.\n(단, 법인카드는 주문이 가능합니다.) \n※ 법인카드 등록 시 비고란에 사업자번호를 반드시 기재해 주세요.");
			prdPrdD.setOrdAttndCntnt("상품권은 환금성 상품으로 결제수단이 제한되는 상품입니다.\n신용카드는 법인카드(카드 비고란 사업자 번호 기재 必)로만 결제가능하며, \n현금결제(무통장/예치금)는 주문자 고객 본인에 한해 주문 가능합니다 ");

        }
		/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 End */

		/* 원천세와 판매가 계산 (2011/04/10 OSM) */
		if ("00".equals(prdmMain.getGftTypCd())) { // 판매상품
			/*판매상품의 경우 원천세와 경품단가는 null 20110416 joo
			prdprcHinsert.setSupProprdUprc(new BigDecimal(0));
			prdprcHinsert.setProprdWthtax(new BigDecimal(0));
			*/
		} else {
			PrdCalcProprdWthtax prdCalcProprdWthtax = new PrdCalcProprdWthtax();
			// 상품코드
			prdCalcProprdWthtax.setPrdCd((prdmMain.getPrdCd() == null) ? new BigDecimal(0) : prdmMain.getPrdCd());
			// 사은품유형코드(매입유형)
			prdCalcProprdWthtax.setGftTypCd((prdmMain.getGftTypCd() == null) ? "" : prdmMain.getGftTypCd());
			// 세금유형코드
			prdCalcProprdWthtax.setTaxTypCd((prdmMain.getTaxTypCd() == null) ? "" : prdmMain.getTaxTypCd());
			// 매입금액
			prdCalcProprdWthtax.setPrchPrc((prdprcHinsert.getPrchPrc() == null) ? new BigDecimal(0) : prdprcHinsert.getPrchPrc());
			logger.debug("prdmMain.getGshsGftcertYn()=>" + prdmMain.getGshsGftcertYn() );
			logger.debug("prdmMain.getGftcertFacePrc()=>" + prdmMain.getGftcertFacePrc());
			if ("Y".equals(prdmMain.getGshsGftcertYn())) { // 당사상품권이면 액면가로 원천세계산 2013.06.17 SAP재구축 정동국
				prdCalcProprdWthtax.setPrchPrc(prdmMain.getGftcertFacePrc());
				logger.debug("prdCalcProprdWthtax.getPrchPrc()=>" + prdCalcProprdWthtax.getPrchPrc());
			}
			// 경품단가
			prdCalcProprdWthtax.setSupProprdUprc((prdprcHinsert.getSupProprdUprc() == null) ? new BigDecimal(0) : prdprcHinsert.getSupProprdUprc());
			// 원천세
			prdCalcProprdWthtax.setProprdWthtax((prdprcHinsert.getProprdWthtax() == null) ? new BigDecimal(0) : prdprcHinsert.getProprdWthtax());
			// 판매가
			prdCalcProprdWthtax.setSalePrc((prdprcHinsert.getSalePrc() == null) ? new BigDecimal(0) : prdprcHinsert.getSalePrc());

			// 원천세와 판매가 계산
			prdCalcProprdWthtax = calcProprdWthtax(prdCalcProprdWthtax);

			if (prdCalcProprdWthtax.getProprdWthtax().compareTo(new BigDecimal(-1)) == 0 ||
					prdCalcProprdWthtax.getSalePrc().compareTo(new BigDecimal(-1)) == 0) {
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", Message.getMessage("prd.msg.370")); //매입유형(사은품유형)이나 세금구분을 확인하세요.
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}

			prdprcHinsert.setProprdWthtax(prdCalcProprdWthtax.getProprdWthtax());
			prdprcHinsert.setSalePrc(prdCalcProprdWthtax.getSalePrc());

			logger.debug("1=>" + prdmMain.getGftTypCd() );

			prdCalcProprdWthtax = null; // 메모리 초기화
		}
		/* 원천세와 판매가 계산 (2011/04/10 OSM) 끗! */

		//경품_업체제공이 아닌경우  경품단가는 null 20110420 jdk
		if (!prdmMain.getGftTypCd().equals("07")
				&& !prdmMain.getGftTypCd().equals("08")) {
			prdprcHinsert.setSupProprdUprc(null);
		}
		//경품_업체제공(고객부담), 경품_당사제공(당사부담), 경품_당사제공(고객부담)이 아닌경우 원천세는 null 20110420 jdk
		//[유수경][2014.12.04]: 경품_업체제공(업체부담) 
		if (!prdmMain.getGftTypCd().equals("07")
				&& !prdmMain.getGftTypCd().equals("03")
				&& !prdmMain.getGftTypCd().equals("04")
				&& !prdmMain.getGftTypCd().equals("08")
				) {
			prdprcHinsert.setProprdWthtax(null);
		}

		//완전매입상품이면 업체지급액  null 20110420 jdk
		if (prdmMain.getPrchTypCd().equals("01")) {
			prdprcHinsert.setSupGivRtamt(null);
		}
		//수수료매입상품이면 매입가  null 20110420 jdk
		if (prdmMain.getPrchTypCd().equals("03")) {
			prdprcHinsert.setPrchPrc(null);
		}

		/* 판매상품이면서 판매가0원 상품이면 판매가와 원가를 0원 처리 (2011/04/12 OSM) */
		/* 판매상품이면서 판매가0원 상품이면 원가는 입력 가능 (2011/04/16 joo) */
		if ("00".equals(prdmMain.getGftTypCd()) && "Y".equals(prdmMain.getZrwonSaleYn())) {
			prdprcHinsert.setSalePrc(new BigDecimal(0));
			//prdprcHinsert.setPrchPrc(new BigDecimal(0));
		}
		logger.debug("addPrdBaseInfoList=>1");

		/*검사상품일 경우 협력사의 qa 무검사여부를 판단하여 무검사 처리한다.*/
		if(!"M".equals(prdPrdD.getClsChkAftAprvCd())&& !"Q".equals(prdPrdD.getClsChkAftAprvCd()) ) {
			DSData supQaInfo = prdClsBaseEntity.getSupQaInfo(prdmMain);
			if( supQaInfo != null && "N".equals(supQaInfo.getString("qaInspYn"))) {
				// BD 가 추가되어 조건절에 추가 (sap 재구축 추가 : 2013/01/24 안승훈 )
				if (prdmMain.getQaGrdCd().equals("BC") || prdmMain.getQaGrdCd().equals("BD")) {
					prdPrdD.setClsChkAftAprvCd("Q");
				}else{
					prdPrdD.setClsChkAftAprvCd("M");
				}

				for (int i = 0; i < prdChanlDinsert.size(); i++) {
					PrdChanlInfo prdChanlInfo = prdChanlDinsert.get(i);

					if(!"N".equals(prdChanlInfo.getQaInspYn()) ) {
						prdChanlInfo.setQaInspYn("N");
						prdChanlDinsert.set(i, prdChanlInfo);
					}
				}
			}

		}

		/* QA검사 무검사일 경우 사양값의 QA검사여부를 한번 더 체크 (2011/09/20 KJH) */
		if("M".equals(prdPrdD.getClsChkAftAprvCd()) || "Q".equals(prdPrdD.getClsChkAftAprvCd()) ) {
			for( int idx = 0; idx < prdSpecInfo.size(); idx++) {
				PrdSpecVal pSpecValQryCond = prdSpecInfo.get(idx);
				DSData qaInspYnDS = prdClsSpecInfoEntity.getPrdSpecQaInspYn(pSpecValQryCond);

				//선택된 사양값 중 한개라도 qa검사여부가 Y일 경우
				//1) 분류검증후결재코드를 ' '로 바꿔 줌   2) 채널리스트 QA검사여부 Y로 바꿔줌
				if( qaInspYnDS != null && "Y".equals(qaInspYnDS.getString("qaInspYn")) ) {
					prdPrdD.setClsChkAftAprvCd(" ");

					for (int i = 0; i < prdChanlDinsert.size(); i++) {
						PrdChanlInfo prdChanlInfo = prdChanlDinsert.get(i);

						if(!"Y".equals(prdChanlInfo.getQaInspYn()) ) {
							prdChanlInfo.setQaInspYn("Y");
							prdChanlDinsert.set(i, prdChanlInfo);
						}
					}

					break;
				}
			}
		}
		//-QA검사 무검사일 경우 사양값체크 END


		//2011/11/28 KJH - 매입형태가 완전매입(01)이고 배송수거형태코드가 1로 시작하는 상품은 무조건 QA하도록 수정
		String dlvPickMthodCd = StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1);
		logger.debug("매입형태>>>"+prdmMain.getPrchTypCd()+"   배송수거형태>>>"+dlvPickMthodCd);
		//20120625 SR02120621092  완전매입은 무조건 qa검사로
		if("01".equals(prdmMain.getPrchTypCd()) ){
			prdPrdD.setClsChkAftAprvCd(" ");

			// 완전매입인 경우에는 QA 등급을 무조건 TV등급으로 한다. (sap 재구축 추가 : 2013/02/04 안승훈 )
			prdmMain.setQaGrdCd("BC");

			for (int i = 0; i < prdChanlDinsert.size(); i++) {
				PrdChanlInfo prdChanlInfo = prdChanlDinsert.get(i);

				if(!"Y".equals(prdChanlInfo.getQaInspYn()) ) {
					prdChanlInfo.setQaInspYn("Y");
					prdChanlDinsert.set(i, prdChanlInfo);
				}
				logger.debug("QA여부>>>"+prdChanlInfo.getQaInspYn());
			}
		}
		//-완전매입, 배송수거형태에 따른 QA검사여부셋팅 END

		EntityDataSet<DSMultiData> checkPrdClsChkAprv =  prdClsChkAprvEntity.checkPrdClsChkAprv(prdmMain);
		String expAutoClsYn = checkPrdClsChkAprv.getValues().getString(0, "expAutoClsYn");
		prdPrdD.setClsChkStCd("00");
		//협력사 승인인 경우 분류검증 요청상태로 변경 [bpr]
		if (prdmMain.getPrdAprvDataYn() != null && ("Y").equals(prdmMain.getPrdAprvDataYn())) {
			prdPrdD.setClsChkStCd("10");
		}
		//위드넷 상품분류 성공 여부 [SR02190227985]
		boolean isWithRePrdClsChk = false; //StringUtils.NVL(prdmMain.getWithClsChkSuccYn())
		//위드넷에서 직접 들어온 경우 분류검증 요청상태로 변경 [bpr]
		if ("WITH".equals(prdmMain.getSessionUserId()) 
				|| ( "N".equals(expAutoClsYn) && "VENDR".equals(prdmMain.getSessionUserId()) ) ){ //2022.08.19. hwangsc 분류검증예외 등록 하지 않는  셀러툴 협력사도 분류검증하기 위하여 조건 추가.){

			//상품분류 검증 [SR02190227985]
			if( "M".equals(prdPrdD.getClsChkAftAprvCd()) // 분류검증후 md팀장합격일 때만 처리. 
					&& isRePrdClsChk(prdmMain) ){  //상품분류 추천분류여부 체크
				isWithRePrdClsChk = true;
			}
			
			//위드넷 데이터 분류검증 성공시
			if( isWithRePrdClsChk ){
				prdPrdD.setClsChkStCd("20");
			}else{
				prdPrdD.setClsChkStCd("10");
			}
		}
		
		///[GRIT-105476]:상품분류 검증 솔루션 제거 작업 (세일즈원-상품)
		//강제 분류검증 합격 처리
		if ("VENDR".equals(prdmMain.getSessionUserId())){
			prdPrdD.setClsChkStCd("20");
		}

		/* [딜구조개선-패널] 2014-10-10, 김상철, 딜더미상품인 경우 분류검증상태코드를 20으로 세팅(MD팀장합격처리 로직을 처리하기위해) */ 
		if("88".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))){
			prdPrdD.setClsChkStCd("20");
		}
		/* [딜구조개선-패널] END */ 
		/* [SR02150327087]  mc/pc 상품인경우 재고범위내주문여부필수 */
		if("DC02".equals(StringUtils.NVL(prdmMain.getDtctCd())) ){ 
			if(!"Y".equals(StringUtils.NVL(prdPrdD.getStockInSaleYn()))){
			  prdPrdD.setStockInSaleYn("Y");
			}
		}
		
		/*
		 * [SR02161108033][2016.11.14][김영현]:골드바 판매제한에 따른 개발요청
		 * 	카드사용제한,연간할인권, 자산사용, 제휴포인트 제한
		 *  B69011101 골드바(금괴)
		*/
		 if("B69011101".equals(prdmMain.getPrdClsCd())){
			 prdmMain.setCardUseLimitYn("Y");	//카드사용제한
			 prdmMain.setImmAccmDcLimitYn("Y");	//적립금즉시제한
			 prdmMain.setGsnpntNoGivYn("Y");	//GS엔포인트적립제한
			 prdmMain.setVipCpnLimitPrdYn("Y");		//슈퍼/더블쿠폰 제한
			 prdmMain.setAutoOrdPsblYn("N");	//자동주문가능여부
			 
			 //연간할인권 제한
			 boolean chkUdaVal = true;
			 if(prdUdaDtl!=null && prdUdaDtl.size()>0){
				 for(int i=0; i<prdUdaDtl.size(); i++){
					 PrdUdaDtl subPrdUdaDtl = prdUdaDtl.get(i);
					 //기등록 체크
					 if(subPrdUdaDtl !=null && subPrdUdaDtl.getUdaNo() != null && subPrdUdaDtl.getUdaNo().compareTo(new BigDecimal("22")) == 0){
						 chkUdaVal = false;
					 }					 
				 }
			 }
			 if(chkUdaVal){
				 PrdUdaDtl prdUdaDtlL = new PrdUdaDtl();
				 prdUdaDtlL.setUdaGbnCd("10");
				 prdUdaDtlL.setUdaNo(new BigDecimal("22"));
				 prdUdaDtlL.setUdaVal("Y");
				 prdUdaDtlL.setUseYn("1");
				 prdUdaDtlL.setChk("Y");
				 prdUdaDtlL.setValidEndDtm("29991231235959");
				 prdUdaDtlL.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
				 prdUdaDtlL.setSessionUserId(prdmMain.getSessionUserId());
				 if(prdUdaDtl==null){
					 prdUdaDtl = new ArrayList<PrdUdaDtl>();
				 }
				 prdUdaDtl.add(prdUdaDtlL);
			 }
			 
		 }
		 
		 //[S][SR02170913526][2017.09.14][최미영]:전통주기본프로세스 => 연간할인권, 제휴카드적립제한
	     PrdClsUda prdClsUda2 = new PrdClsUda(); 
	     prdClsUda2.setUdaNo(new BigDecimal( "230" ));
	     prdClsUda2.setPrdClsCd(prdmMain.getPrdClsCd()); 
	     if(PrdClsUdaUtils.isPrdClsUdaFlag(prdClsUda2)){       
	         //연간할인권 제한
	         boolean chkUdaVal1 = true;
	         boolean chkUdaVal2 = true;
	         if(prdUdaDtl!=null && prdUdaDtl.size()>0){
	             for(int i=0; i<prdUdaDtl.size(); i++){
	                 PrdUdaDtl subPrdUdaDtl = prdUdaDtl.get(i);
	                 //기등록 체크
	                 if(subPrdUdaDtl !=null && subPrdUdaDtl.getUdaNo() != null && subPrdUdaDtl.getUdaNo().compareTo(new BigDecimal("22")) == 0){
	                     chkUdaVal1 = false;
	                 }  
	                 if(subPrdUdaDtl !=null && subPrdUdaDtl.getUdaNo() != null && subPrdUdaDtl.getUdaNo().compareTo(new BigDecimal("16")) == 0){
	                     chkUdaVal2 = false;
	                 }  
	             }
	         }
	         if(chkUdaVal1 || chkUdaVal2){  
	             if(prdUdaDtl==null){
	                 prdUdaDtl = new ArrayList<PrdUdaDtl>();
	             }
	             //연간할인권제한
	             if(chkUdaVal1){
	                 PrdUdaDtl prdUdaDtlL22 = new PrdUdaDtl();
	                 prdUdaDtlL22.setUdaGbnCd("10");
	                 prdUdaDtlL22.setUdaVal("Y");
	                 prdUdaDtlL22.setUseYn("1");
	                 prdUdaDtlL22.setChk("Y");
	                 prdUdaDtlL22.setUdaNo(new BigDecimal("22"));
	                 prdUdaDtlL22.setValidEndDtm("29991231235959");
	                 prdUdaDtlL22.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
	                 prdUdaDtlL22.setSessionUserId(prdmMain.getSessionUserId());
	                 prdUdaDtl.add(prdUdaDtlL22);
	             }
	             //제휴카드적립제한
	             if(chkUdaVal2){
	                 PrdUdaDtl prdUdaDtlL16 = new PrdUdaDtl();
	                 prdUdaDtlL16.setUdaGbnCd("10");
	                 prdUdaDtlL16.setUdaVal("Y");
	                 prdUdaDtlL16.setUseYn("1");
	                 prdUdaDtlL16.setChk("Y");
	                 prdUdaDtlL16.setUdaNo(new BigDecimal("16"));
	                 prdUdaDtlL16.setValidEndDtm("29991231235959");
	                 prdUdaDtlL16.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
	                 prdUdaDtlL16.setSessionUserId(prdmMain.getSessionUserId());
	                 prdUdaDtl.add(prdUdaDtlL16);          
	            }       
	            //ogger.debug("prdUdaDtl 모든자료 ====> " +prdUdaDtl);
	         }      
	     }	
	     //[E][SR02170913526][2017.09.14][최미영]:전통주기본프로세스
	     
		//[HANGBOT-27157_GSITMBO-17413] 협력사 신규 API 진행_엘롯데
		//상품등록 공통에서 처리하도록 로직 변경
	     if(prdmMain.getSupCd().compareTo(new BigDecimal("1046873")) == 0 && !"".equals(StringUtils.NVL(prdmMain.getSupPrdCd(), ""))) { 
	    	 SupPrdCdQryCond pSupPrdCdQryCond = new SupPrdCdQryCond();
	    	 pSupPrdCdQryCond.setSupCd(prdmMain.getSupCd().toString());
	    	 pSupPrdCdQryCond.setSuppGoodsCode(prdmMain.getSupPrdCd());
	    	 EntityDataSet<DSMultiData> supPrdAddInfo = ecScmMngEntity.getSupPrdAddInfo(pSupPrdCdQryCond);
	    	 
	    	 if (supPrdAddInfo.getValues() != null && supPrdAddInfo.getValues().size()>0){
	    		String rtnSupVal = StringUtils.NVL(supPrdAddInfo.getValues().getString(0, "rtnSupVal"), "");
	    		if(!"".equals(rtnSupVal)){
	    			PrdUdaDtl prdUdaDtl1111 = new PrdUdaDtl();
	    			prdUdaDtl1111.setUdaGbnCd("10");
	    			prdUdaDtl1111.setUdaVal(StringUtil.nvl(rtnSupVal));
	    			prdUdaDtl1111.setUseYn("1");
	    			prdUdaDtl1111.setChk("Y");
	    			prdUdaDtl1111.setUdaNo(new BigDecimal("1111"));
	    			prdUdaDtl1111.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
	    			prdUdaDtl1111.setValidEndDtm("29991231235959");
	    			prdUdaDtl1111.setSessionUserId(prdmMain.getSessionUserId());
	    			prdUdaDtl.add(prdUdaDtl1111);
	    		}
	    	 }
		}
	     
	     //즉시적립금할인 설정 제한 처리
	     //[SR02190319198]:즉시적립금할인 설정 제한 처리
	     prdmMain.setImmAccmDcLimitYn("Y");
	     
	     //[SR02190426489]:[직매입개선]GS초이스 상품등록제어 및 재고범위내 판매기능 개발요청
	     if (prdPrdD.getGschoiceYn() != null && ("Y").equals(prdPrdD.getGschoiceYn())) {
	    	 prdmMain.setSaleEndDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
	    	 prdmMain.setSaleEndPrsnId(prdmMain.getSessionUserId());
	    	 prdmMain.setSaleEndRsnCd("55");
	     }
	     
	     //[SR02200210723]:일심품절 해제 시 오류 건 처리 
	     //등록된 속성 내 주문가능수량이 없는 경우 본품에 대한 일시품절 처리 진행
		if (!"88".equals(StringUtils.NVL(prdmMain.getPrdGbnCd())) && !"WITH".equals(prdmMain.getSessionUserId()) ) {
			String tempoutYn = "Y";
			String tempoutChgDtm = DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss");
			for (int i = 0; i < prdOrdPsblQtyD.size(); i++) {
				if (BigDecimalUtil.NVL(prdOrdPsblQtyD.get(i).getOrdPsblQty()).compareTo(BigDecimal.ZERO) > 0) {
					tempoutYn = "N";
					tempoutChgDtm = null;
					break;
				}
			}
			prdmMain.setTempoutYn(tempoutYn);
			prdmMain.setTempoutChgDtm(tempoutChgDtm);
		}

		//[SR02180405155][2018.04.05][백동현]-상품신규등록시 PRD_PRD_M에 협력사+협력사상품코드가 중복으로 생성되는현상
	    //상품승인이 필요한 대상에 대해서만 이력적재 테이블에 등록한다.
	    //이력적재 테이블에 중복 등록 된 경우 강제 오류로 리턴한다.
		if (prdmMain.getPrdAprvDataYn() != null && ("Y").equals(prdmMain.getPrdAprvDataYn())) {
			try{
				SupPrdAprvDInfo supPrdAprvDInfo = new SupPrdAprvDInfo();
				supPrdAprvDInfo.setSupCd(prdmMain.getSupCd());
				supPrdAprvDInfo.setSupPrdCd(prdmMain.getSupPrdCd());
				supPrdAprvDInfo.setGbnCd("M");
				supPrdAprvDInfo.setSessionObject(prdmMain);
				ecScmMngEntity.addSupPrdAprvD(supPrdAprvDInfo);
			}catch(Exception e){
				throw new DevEntException("\n신규상품승인 이력적재 시 오류가 발생하였습니다.\n협력사코드:" + prdmMain.getSupCd() + " 협력사상품코드:" + prdmMain.getSupPrdCd() + "\nErrorCode:addSupPrdAprvD");				
			}
		}

		// 상품마스터 insert
		int insertok = prdEntity.setPrdBase(prdmMain);
		
		/*
		 * 디토 서비스 종료로 삭제 처리
		// Ditto상품 GS_DITTO_BBS insert
		if(StringUtils.NVL(prdmMain.getDittoYn(), "N").equals("Y")){
			DSData outDittoCnt = ecScmMngEntity.getDittoCnt(prdmMain);
			if(outDittoCnt.getInt("llDittoCnt") == 0){
				ecScmMngEntity.setDittoBbs(prdmMain);
			}
			//2013.12.02 SR02131002090 DITTO상품인경 DITTO쿠폰에 등록처리
			PrdCpn prdCpn = new PrdCpn();
			prdCpn.setSessionObject(prdmMain);
			prdCpn.setPrdCd(prdmMain.getPrdCd());
			cpnApplyPrdEntity.addCpnDittoPrd(prdCpn);

		}
		*/
		
		//[S][PD_2016_009] 도서산간추가배송비 2016.08.19 ljb		
		//자동주문인 경우 추가배송비는 무료여부 확인 
		if( "Y".equals(prdmMain.getAutoOrdPsblYn())){
			if ("Y".equals(prdPrdD.getJejuChrDlvYn())   
				|| (prdPrdD.getJejuChrDlvcCd() != null && !prdPrdD.getJejuChrDlvcCd().equals("")) 
				|| (prdPrdD.getJejuChrDlvcAmt() != null && !prdPrdD.getJejuChrDlvcAmt().equals("")) ) {

				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", Message.getMessage("prd.msg.224", new String[]{"제주도 배송비", "무료"}));
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			
			if ("Y".equals(prdPrdD.getJejuExchRtpChrYn())   
				|| (prdPrdD.getJejuRtpDlvcCd() != null && !prdPrdD.getJejuRtpDlvcCd().equals("")) 
				|| (prdPrdD.getJejuRtpOnewyRndtrpCd() != null && !prdPrdD.getJejuRtpOnewyRndtrpCd().equals("")) 
				|| (prdPrdD.getJejuExchOnewyRndtrpCd() != null && !prdPrdD.getJejuExchOnewyRndtrpCd().equals("")) ) {

				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", Message.getMessage("prd.msg.224", new String[]{"제주도 반품교환비", "무료"}));
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			if ("Y".equals(prdPrdD.getIlndChrDlvYn())   
				|| (prdPrdD.getIlndChrDlvcCd() != null && !prdPrdD.getIlndChrDlvcCd().equals("")) 
				|| (prdPrdD.getIlndChrDlvcAmt() != null && !prdPrdD.getIlndChrDlvcAmt().equals("")) ) {

				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", Message.getMessage("prd.msg.224", new String[]{"도서지방 배송비", "무료"}));
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			
			if ("Y".equals(prdPrdD.getIlndExchRtpChrYn())   
				|| (prdPrdD.getIlndRtpDlvcCd() != null && !prdPrdD.getIlndRtpDlvcCd().equals("")) 
				|| (prdPrdD.getIlndRtpOnewyRndtrpCd() != null && !prdPrdD.getIlndRtpOnewyRndtrpCd().equals("")) 
				|| (prdPrdD.getIlndExchOnewyRndtrpCd() != null && !prdPrdD.getIlndExchOnewyRndtrpCd().equals("")) ) {

				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", Message.getMessage("prd.msg.224", new String[]{"도서지방 반품교환비", "무료"}));
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
		}
		
		if ("3000".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) || "3200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) ){
			//제주도 배송비 코드 생성
			if( "Y".equals(prdPrdD.getJejuDlvPsblYn()) && "Y".equals(prdPrdD.getJejuChrDlvYn()) &&  
				(prdPrdD.getJejuChrDlvcCd() == null || "".equals(prdPrdD.getJejuChrDlvcCd())) &&
				prdPrdD.getJejuChrDlvcAmt() != null && !"".equals(prdPrdD.getJejuChrDlvcAmt())) {
				
				SupDlvcPrd supDlvcPrd = new SupDlvcPrd();	
				supDlvcPrd.setDlvcAmt(new BigDecimal(prdPrdD.getJejuChrDlvcAmt()));
				supDlvcPrd.setSupCd(prdmMain.getSupCd());
				supDlvcPrd.setDlvcLimitAmt(prdmMain.getShipLimitAmt());
				
				// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
				// 직송이 아닐경우 당사배송비를 조회 하기 위해 배송비구분코드에 'GS', 협력사코드에 '989081' 를 세팅한다.
				if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){
					supDlvcPrd.setDlvcGbnCd("GS") ; // 배송비구분코드 [ 당사 배송비 : 'GS'(협력사는 당사협력사코드 : 989081) ]
					supDlvcPrd.setSupCd(new BigDecimal("989081")) ; // 당사협력사코드
				}
				
				DSData dlvcCd = supDlvcEntity.getCntSupDlvcCd(supDlvcPrd);
				//배송비코드가 없으면 생성한다.
				if (dlvcCd.size()  <= 0 ) {
					List<SupDlvcPrd> supDlvcPrdRegList = new ArrayList<SupDlvcPrd>();
					List<SupDlvcPrd> supDlvcPrdModList = new ArrayList<SupDlvcPrd>();
	
					supDlvcPrd.setSessionObject(prdmMain);
					supDlvcPrd.setMdId("60027");
					supDlvcPrd.setStdAmtYn(prdmMain.getShipLimitYn());
					supDlvcPrdRegList.add(supDlvcPrd);
					PrdEaiPrdReTnsRslt result = supDlvcMngProcess.saveSupDlvcPrdList2(supDlvcPrdRegList, supDlvcPrdModList);
					if ("S".equals(result.getResponseResult())) {
						prdPrdD.setJejuChrDlvcCd(result.getResponseMessage());
					}	
	
				} else{
					prdPrdD.setJejuChrDlvcCd(dlvcCd.getBigDecimal("dlvcCd").toString());
				}
	
			 }
			
			//[SR02170516901][2017.05.24][백동현]:(내부개선과제) 사은품 등록시 상품 채널 확인 체크로직 외 2건
			//제주지방배송가능여부가 N으로 들어오는 경우 제주와 관련된 금액 필드는 모두 N 및 Null 처리한다.
			if("N".equals(prdPrdD.getJejuDlvPsblYn())){
				 prdPrdD.setJejuChrDlvYn("N") ;   				//제주도유료배송여부;
				 prdPrdD.setJejuChrDlvcCd("") ;   				//제주도유료배송비코드;
				 prdPrdD.setJejuChrDlvcAmt("") ;   			//제주도유료배송금액;
				 prdPrdD.setJejuExchRtpChrYn("N") ;   			//제주도교환반품유료여부;
				 prdPrdD.setJejuRtpDlvcCd("") ;   				//제주도반품배송비코드;
				 prdPrdD.setJejuRtpOnewyRndtrpCd("") ;   	//제주도반품편도왕복코드;
				 prdPrdD.setJejuExchOnewyRndtrpCd("") ;   	//제주도교환편도왕복코드;
			 }
			
			//도서지방 배송비 코드 생성
			if( "Y".equals(prdPrdD.getIlndDlvPsblYn()) &&  "Y".equals(prdPrdD.getIlndChrDlvYn()) &&  
				(prdPrdD.getIlndChrDlvcCd() == null || prdPrdD.getIlndChrDlvcCd().equals("")) &&
				prdPrdD.getIlndChrDlvcAmt() != null && !prdPrdD.getIlndChrDlvcAmt().equals("")) {
				
				SupDlvcPrd supDlvcPrd = new SupDlvcPrd();	
				supDlvcPrd.setDlvcAmt(new BigDecimal(prdPrdD.getIlndChrDlvcAmt()));
				supDlvcPrd.setSupCd(prdmMain.getSupCd());
				supDlvcPrd.setDlvcLimitAmt(prdmMain.getShipLimitAmt());
				
				// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
				// 직송이 아닐경우 당사배송비를 조회 하기 위해 배송비구분코드에 'GS', 협력사코드에 '989081' 를 세팅한다.
				if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){
					supDlvcPrd.setDlvcGbnCd("GS") ; // 배송비구분코드 [ 당사 배송비 : 'GS'(협력사는 당사협력사코드 : 989081) ]
					supDlvcPrd.setSupCd(new BigDecimal("989081")) ; // 당사협력사코드
				}
				
				DSData dlvcCd = supDlvcEntity.getCntSupDlvcCd(supDlvcPrd);
				//배송비코드가 없으면 생성한다.
				if (dlvcCd.size()  <= 0 ) {
					List<SupDlvcPrd> supDlvcPrdRegList = new ArrayList<SupDlvcPrd>();
					List<SupDlvcPrd> supDlvcPrdModList = new ArrayList<SupDlvcPrd>();
	
					supDlvcPrd.setSessionObject(prdmMain);
					supDlvcPrd.setMdId("60027");
					supDlvcPrd.setStdAmtYn(prdmMain.getShipLimitYn());
					supDlvcPrdRegList.add(supDlvcPrd);
					PrdEaiPrdReTnsRslt result = supDlvcMngProcess.saveSupDlvcPrdList2(supDlvcPrdRegList, supDlvcPrdModList);
					if ("S".equals(result.getResponseResult())) {
						prdPrdD.setIlndChrDlvcCd(result.getResponseMessage());
					}	
	
				} else{
					prdPrdD.setIlndChrDlvcCd(dlvcCd.getBigDecimal("dlvcCd").toString());
				}
	
			}
			
			//[SR02170516901][2017.05.24][백동현]:(내부개선과제) 사은품 등록시 상품 채널 확인 체크로직 외 2건
			//도서지방배송가능여부가 N으로 들어오는 경우 도서와 관련된 금액 필드는 모두 N 및 Null 처리한다.
			if("N".equals(prdPrdD.getIlndDlvPsblYn())){
				prdPrdD.setIlndChrDlvYn("N") ;  				// 도서지방유료배송여부;
				prdPrdD.setIlndChrDlvcCd("") ;  				// 도서지방유료배송비코드;
				prdPrdD.setIlndChrDlvcAmt("") ;  				// 도서지방유료배송금액;
				prdPrdD.setIlndExchRtpChrYn("N") ;  			// 도서지방교환반품유료여부;
				prdPrdD.setIlndRtpDlvcCd("") ;  				// 도서지방반품배송비코드;
				prdPrdD.setIlndRtpOnewyRndtrpCd("") ;   	//도서지방반품편도왕복코드;
				prdPrdD.setIlndExchOnewyRndtrpCd("") ;   	//도서지방교환편도왕복코드;
			}
			
			//배송불가지역 자동 데이터 등록
			//[SR02161117016] [기간계/채널] 제주/도서불가 설정시 지역 확인 로직 변경 - 배송불가 자동 처리 로직 삭제
			//setPrdDlvNoadmtRegonList(prdPrdD);
			
		}else{
			prdPrdD.setIlndDlvPsblYn("") ;  				//도서지방배송가능여부;
			prdPrdD.setJejuDlvPsblYn("") ;  				// 제주도배송가능여부;
			prdPrdD.setDd3InDlvNoadmtRegonYn("") ;  // 72시간내배송불가지역여부;
			prdPrdD.setIlndChrDlvYn("") ;  				// 도서지방유료배송여부;
			prdPrdD.setIlndChrDlvcCd("") ;  				// 도서지방유료배송비코드;
			prdPrdD.setIlndChrDlvcAmt("") ;  				// 도서지방유료배송금액;
			prdPrdD.setIlndExchRtpChrYn("") ;  			// 도서지방교환반품유료여부;
			prdPrdD.setIlndRtpDlvcCd("") ;  				// 도서지방반품배송비코드;
			prdPrdD.setIlndRtpOnewyRndtrpCd("") ;   	//도서지방반품편도왕복코드;
			prdPrdD.setIlndExchOnewyRndtrpCd("") ;   	//도서지방교환편도왕복코드;
			prdPrdD.setJejuChrDlvYn("") ;   				//제주도유료배송여부;
			prdPrdD.setJejuChrDlvcCd("") ;   				//제주도유료배송비코드;
			prdPrdD.setJejuChrDlvcAmt("") ;   			//제주도유료배송금액;
			prdPrdD.setJejuExchRtpChrYn("") ;   			//제주도교환반품유료여부;
			prdPrdD.setJejuRtpDlvcCd("") ;   				//제주도반품배송비코드;
			prdPrdD.setJejuRtpOnewyRndtrpCd("") ;   	//제주도반품편도왕복코드;
			prdPrdD.setJejuExchOnewyRndtrpCd("") ;   	//제주도교환편도왕복코드;
		}
		//[E][PD_2016_009] 도서산간추가배송비 2016.08.19 ljb
		
		//일반상품으로 들어오지 않은 경우에는 추가배송비 관련 데이터를 전부 널처리한다.
		if(!"00".equals(StringUtils.NVL(prdmMain.getGftTypCd()))){
			prdPrdD.setIlndDlvPsblYn("") ;  				//도서지방배송가능여부;
			prdPrdD.setJejuDlvPsblYn("") ;  				// 제주도배송가능여부;
			prdPrdD.setDd3InDlvNoadmtRegonYn("") ;  // 72시간내배송불가지역여부;
			prdPrdD.setIlndChrDlvYn("") ;  				// 도서지방유료배송여부;
			prdPrdD.setIlndChrDlvcCd("") ;  				// 도서지방유료배송비코드;
			prdPrdD.setIlndChrDlvcAmt("") ;  				// 도서지방유료배송금액;
			prdPrdD.setIlndExchRtpChrYn("") ;  			// 도서지방교환반품유료여부;
			prdPrdD.setIlndRtpDlvcCd("") ;  				// 도서지방반품배송비코드;
			prdPrdD.setIlndRtpOnewyRndtrpCd("") ;   	//도서지방반품편도왕복코드;
			prdPrdD.setIlndExchOnewyRndtrpCd("") ;   	//도서지방교환편도왕복코드;
			prdPrdD.setJejuChrDlvYn("") ;   				//제주도유료배송여부;
			prdPrdD.setJejuChrDlvcCd("") ;   				//제주도유료배송비코드;
			prdPrdD.setJejuChrDlvcAmt("") ;   			//제주도유료배송금액;
			prdPrdD.setJejuExchRtpChrYn("") ;   			//제주도교환반품유료여부;
			prdPrdD.setJejuRtpDlvcCd("") ;   				//제주도반품배송비코드;
			prdPrdD.setJejuRtpOnewyRndtrpCd("") ;   	//제주도반품편도왕복코드;
			prdPrdD.setJejuExchOnewyRndtrpCd("") ;   	//제주도교환편도왕복코드;
		}
		
		//[SR02161216019] (API)제주/도서 추가배송비 개발
		//협력사매핑관리 내 제주/도서산간 배송 제한으로 설정된 협력사의 경우에는 자동으로 불가처리 진행한다. 
		if (("3000".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) || "3200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))) 
			&& "00".equals(StringUtils.NVL(prdmMain.getGftTypCd()))){
			//API 추가배송비 제한 협력사 여부
			SupUdaDtl supUdaDtl = new SupUdaDtl();
			supUdaDtl.setUdaNo(new BigDecimal(55));
			supUdaDtl.setSupCd(prdmMain.getSupCd());
			supUdaDtl.setUdaVal("Y");
			supUdaDtl.setUseYn("Y");
			
			//협력사 UDA정보 조회
			DSData supUdaDtlInfo = supEntity.getSupUdaDtlInfo(supUdaDtl);
			
			//추가배송비 등록 제한 여부 확인 후 신규 입력 상품에 대해서 업데이트 진행
			if(supUdaDtlInfo != null && supUdaDtlInfo.size() >= 1){
				
				prdPrdD.setIlndDlvPsblYn("N") ;  				//도서지방배송가능여부;
				prdPrdD.setJejuDlvPsblYn("N") ;  				// 제주도배송가능여부;
				prdPrdD.setDd3InDlvNoadmtRegonYn("N") ;  // 72시간내배송불가지역여부;
				prdPrdD.setIlndChrDlvYn("N") ;  				// 도서지방유료배송여부;
				prdPrdD.setIlndChrDlvcCd("") ;  				// 도서지방유료배송비코드;
				prdPrdD.setIlndChrDlvcAmt("") ;  				// 도서지방유료배송금액;
				prdPrdD.setIlndExchRtpChrYn("N") ;  			// 도서지방교환반품유료여부;
				prdPrdD.setIlndRtpDlvcCd("") ;  				// 도서지방반품배송비코드;
				prdPrdD.setIlndRtpOnewyRndtrpCd("") ;   	//도서지방반품편도왕복코드;
				prdPrdD.setIlndExchOnewyRndtrpCd("") ;   	//도서지방교환편도왕복코드;
				prdPrdD.setJejuChrDlvYn("N") ;   				//제주도유료배송여부;
				prdPrdD.setJejuChrDlvcCd("") ;   				//제주도유료배송비코드;
				prdPrdD.setJejuChrDlvcAmt("") ;   			//제주도유료배송금액;
				prdPrdD.setJejuExchRtpChrYn("N") ;   			//제주도교환반품유료여부;
				prdPrdD.setJejuRtpDlvcCd("") ;   				//제주도반품배송비코드;
				prdPrdD.setJejuRtpOnewyRndtrpCd("") ;   	//제주도반품편도왕복코드;
				prdPrdD.setJejuExchOnewyRndtrpCd("") ;   	//제주도교환편도왕복코드;
			}
		}
		
		// 상품확장정보 insert
		prdEntity.setInsertPrdPrdD(prdPrdD);

		// 상품물류확장정보 insert  , sap 재구축 (2013/01/18 안승훈)
		// default 값을 세팅한다.
		logger.debug("sapr============>" + prdDtrD.getPrdCd());
		prdDtrD.setPrdCd(prdmMain.getPrdCd());		// 상품코드 입력
		prdDtrD.setSessionUserId(prdmMain.getSessionUserId());		// 세션 아이디 입력
		// 상품운임그룹코드 default 세팅 , 직송이 아니면 'SM0230' 세팅 : 무조건 SM0230 으로 변경 (sap 재구축 추가 : 2013/05/07 안승훈 )
		//if (prdDtrD.getPrdChrGrpCd() == null && !StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1).equals("3")) {
			prdDtrD.setPrdChrGrpCd("SM0230");
		//}
			
		//[HANGBOT-32562 GSITMBO-19050] 2022.04.04 이태호 세일즈원의 상품 신규등록 수정요청 - 상품 가로,세로,높이,무게 정보 복사 되지 않게 수정
		//가로,세로 높이는 일반상품등록시 최조 값에는 세팅 하지 않는다.(복사시에도 세팅 안함)	
		prdDtrD.setWidVal(null);
		prdDtrD.setVertVal(null);
		prdDtrD.setHightVal(null);
		prdDtrD.setWeihtVal(null);
			
			
		// 평가감은  update 되지않도록 null로 설정한다.
		prdDtrD.setWrtdnYn(null);
		
		// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
		// --> 수정시작
		// 상품물류확장정보 에 묶음배송에 따른 합포장가능여부를 입력한다.
		if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
			&& "Y".equals(StringUtils.NVL(prdmMain.getChrDlvYn()))){
			if("A01".equals(StringUtils.NVL(prdmMain.getBundlDlvCd()))){
				prdDtrD.setOboxPsblYn("Y") ; // 직송이 아니고 묶음배송이 가능(A01)이면 합포장가능여부 = 'Y'
			}else if("A03".equals(StringUtils.NVL(prdmMain.getBundlDlvCd()))){
				prdDtrD.setOboxPsblYn("N") ; // 직송이 아니고 묶음배송이 불가능[배송비상품수만큼결제](A03) 이면 합포장가능여부 = 'N' 
			}else {
				prdDtrD.setOboxPsblYn("N") ; // default = 'N'
			}
		}
		// --> 수정종료
		
		/*[직송관리대행] 직송관리대행서비스구축ㅣ 2015.07 패널프로젝트 김효석(hs_style) Start*/  
		prdDtrD.setDlvPickMthodCd(prdmMain.getDlvPickMthodCd());
		prdDtrD.setDlvsCoCd(prdmMain.getDlvsCoCd());
		
		DlvsCoCdCond dlvsCoCdCond = new DlvsCoCdCond();
		dlvsCoCdCond.setSupCd(String.valueOf(prdmMain.getSupCd()));
		dlvsCoCdCond.setDlvsCoCd(prdmMain.getDlvsCoCd());
		dlvsCoCdCond.setPrdRelspAddrCd(prdmMain.getPrdRelspAddrCd());
		dlvsCoCdCond.setPrdRetpAddrCd(prdmMain.getPrdRetpAddrCd());
		
		//[SR02170306112][2017.03.09][백동현]:직송관리대행 다중 출고/반송지 개발요청건 (정식버전 오픈에 대한 CSR)
		EntityDataSet<DSData> dirMngYn = prdEntity.getSupAddrDirdlvMngAgncyYn(dlvsCoCdCond);
		
		if("Y".equals(prdDtrD.getDirdlvMngAgncyYn()) || "1".equals(prdDtrD.getDirdlvMngAgncyYn())){
			if("1".equals(prdDtrD.getDirdlvMngAgncyYn())) prdDtrD.setDirdlvMngAgncyYn("Y");
			int ckd = Integer.parseInt(dirMngYn.getValues().getString("rtnVal"));
			if(ckd == 0){
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", "직송관리대행 협력사가 아닙니다.");
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			
			/* HANGBOT-16987_GSITMBO-9804 지정택배수거 종료
			if("Y".equals(prdmMain.getApntDlvsImplmYn())){
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", "지정택배수거와 직송관리대행을 동시에 사용하실 수 없습니다.");
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}*/
			
			if(prdmMain.getBundlDlvCd() == null || "".equals(prdmMain.getBundlDlvCd())){
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", "직송관리대행 상품의 묶음배송여부는 필수입니다."); 
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			
			if("A01".equals(prdmMain.getBundlDlvCd())){
				if(prdDtrD.getBundlDlvPsblQty() == null || "".equals(prdDtrD.getBundlDlvPsblQty())){
					returnDsData.put("prdCd", "");
					returnDsData.put("retCd", "-1");
					returnDsData.put("retMsg", "직송관리대행 상품의 묶음배송가능수량은 필수입니다."); 
					returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
					return returnMap;
				}
				
				if(Integer.parseInt(prdDtrD.getBundlDlvPsblQty()) < 2){
					returnDsData.put("prdCd", "");
					returnDsData.put("retCd", "-1");
					returnDsData.put("retMsg", "묶음배송가능수량은 2개이상 입력 가능합니다."); 
					returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
					return returnMap;
				}
			}else{
				prdDtrD.setBundlDlvPsblQty("");
			}
		}
		/*[직송관리대행] 직송관리대행서비스구축ㅣ 2015.07 패널프로젝트 김효석(hs_style) End*/
		
		prdPrdDtrEntity.addPrdPrdDtr(prdDtrD);		

		// EC노출여부 수정
		// prdEntity.modifyIntrntExposYn(prdPrdD);   // 2011.06.24 EC노출 건들지마!

		// 상품매장노출정보 insert
		prdEntity.addPrdShopExposDInfo(prdmMain);

		// 상품수치정보 insert
		if (prdNumvalDinfo != null) {
			logger.debug("setPrdBase==>9");
			//SMTCLogger.writePrd("prdNumvalDinfo 수치정보");
			for (int i = 0; i < prdNumvalDinfo.size(); i++) {
				if (StringUtils.NVL(prdNumvalDinfo.get(i).getGubun()).equals("Y")) {
					prdNumvalDinfo.get(i).setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
					prdNumvalDinfo.get(i).setSessionUserId(prdmMain.getSessionUserId()); /* 세션정보 [PD_2017_017_속도개선][jongil] */
					prdEntity.setInsertPrdNumvalD(prdNumvalDinfo.get(i));
				}
			}
		}

		// 상품가격정보
		prdprcHinsert.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
		if (prdprcHinsert.getPrdAttrGbnCd() == null){
			prdprcHinsert.setPrdAttrGbnCd("P");
		}

		Date date = SysUtil.getCurrTime();
   	 	String sysdtm = DateUtils.format(date,"yyyyMMddHHmmss");

		prdprcHinsert.setSessionUserId(prdmMain.getSessionUserId()); /* 세션정보 [PD_2017_017_속도개선][jongil] */
		prdprcHinsert.setValidStrDtm(sysdtm);
		prdprcHinsert.setRegDtm(sysdtm);
		prdprcHinsert.setModDtm(sysdtm);

		// 해외상품 로직추가 (2011/05/03 OSM)
		if (forgnTmPrdMng != null) {
			// 결정무게값
			prdprcHinsert.setDetrmWeihtVal((forgnTmPrdMng.getDetrmWeihtVal() == null) ? null : forgnTmPrdMng.getDetrmWeihtVal().toString());
			// 현지운임비용
			prdprcHinsert.setOnsiteChrCost((forgnTmPrdMng.getOnsiteChrCost() == null) ? null : forgnTmPrdMng.getOnsiteChrCost());
			// 현지상품가격
			prdprcHinsert.setOnsitePrdPrc((forgnTmPrdMng.getOnsitePrdPrc() == null) ? null : forgnTmPrdMng.getOnsitePrdPrc());
			// 현지할인가격
			prdprcHinsert.setOnsiteDcPrc((forgnTmPrdMng.getOnsiteDcPrc() == null) ? null : forgnTmPrdMng.getOnsiteDcPrc());
			// 창고코드
			prdprcHinsert.setWhsCd((forgnTmPrdMng.getWhsCd() == null) ? null : forgnTmPrdMng.getWhsCd());
		}
		prdEntity.setInsertPrdPrcH(prdprcHinsert);

		// [JANUS1] 프레쉬몰 채널/EC매장 등록여부 체크
		this.checkFreshMallUda(prdmMain, prdChanlDinsert, ecdSectPrdlstInfo);
		
		// 상품채널정보 insert
		
		if (prdChanlDinsert != null) {
			
			for (int i = 0; i < prdChanlDinsert.size(); i++) {
				prdChanlDinsert.get(i).setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
				prdChanlDinsert.get(i).setSessionUserId(prdmMain.getSessionUserId()); /* 세션정보 [PD_2017_017_속도개선][jongil] */
				//logger.info("#ymlee STD7 : setStdRelsDdcnt: " + prdChanlDinsert.get(i).getStdRelsDdcnt());
			}
			List<PrdChanlInfo> modifyPrdChanlInfoPrd = new ArrayList<PrdChanlInfo>();
			List<PrdChanlInfo> removePrdChanlInfoPrd = new ArrayList<PrdChanlInfo>();
			// 상품채널목록저장
			prdChanlEntity.savePrdChanlList(prdChanlDinsert, modifyPrdChanlInfoPrd, removePrdChanlInfoPrd);

			// 상품멀티코드정보 insert
			PrdCdQryCond prdCdQryCond = new PrdCdQryCond();
			prdCdQryCond.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
			DSMultiData multiCdList = prdEntity.getPrdMultiCodeList(prdCdQryCond).getValues();

			if (multiCdList != null) {
				List<MultiCd> prdMultiList = new ArrayList<MultiCd>();
				for (int i = 0; i < multiCdList.size(); i++) {
					MultiCd prdMulti = new MultiCd();
					prdMulti.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd")); // 상품코드
					prdMulti.setMultiCd(multiCdList.get(i).getString("multiCd")); // 멀티코드
					prdMulti.setChanlCd(multiCdList.get(i).getString("chanlCd")); // 채널코드
					prdMulti.setChanlGrpCd(multiCdList.get(i).getString("chanlGrpCd")); // 채널그룹코드
					prdMulti.setChanlDtlCd(multiCdList.get(i).getString("chanlDtlCd")); // 채널상세코드
					prdMulti.setSessionUserIp(prdmMain.getSessionUserIp());
					prdMulti.setSessionUserId(prdmMain.getSessionUserId());
					prdMulti.setSessionUserNm(prdmMain.getSessionUserNm());
					prdMultiList.add(prdMulti);
				}
				multiCdEntity.addMultiCdList(prdMultiList); // 멀티코드목록등록
			}
		}
		logger.debug("insertok=>" + insertok);

		//SMTCLogger.writePrd("addPrdBaseInfoList 수치정보");
        // 속성상품 Insert
		if (insertok > 0) {
			logger.debug("pattrPrdM.size()=>" + pattrPrdM.size());
			if (pattrPrdM.size() > 0) {
				logger.debug("addPrdBaseInfoList=>5");
				for (int i = 0; i < pattrPrdM.size(); i++) {
					pattrPrdM.get(i).setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
				}
				for (int j = 0; j < prdAttrPrdm.size(); j++) {
					prdAttrPrdm.get(j).setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
				}

				if("01".equals(prdmMain.getOrdPrdTypCd())) { // 주문형태 : 속성 대표 상품주문 인경우 처리
					String attrPrdRepCd ="";

					for (int stockIndex = 0; stockIndex != prdAttrPrdm.size(); stockIndex++) {
						PrdStockDinsert stock = prdAttrPrdm.get(stockIndex);
						String tempAttrPrdRepCd = stock.getAttrPrdRepCd();

						if(tempAttrPrdRepCd == null || tempAttrPrdRepCd.length() < 5) {  // 속성대표코드가 P1인경우 주문형태코드 생성 및 속성상품/상품재고에 연결함
							//attrPrdRepCd = prdEntity.getAttrPrdRepCd(prdmMain);
						    //'P' || NVL(TO_CHAR(TO_NUMBER(REPLACE(MAX(SUBSTR(ATTR_PRD_REP_CD, 2, 3)), 'P', '')) + 1, 'fm000'), '001') || :prdCd
							attrPrdRepCd = tempAttrPrdRepCd + prdmMain.getPrdCd().toString();

							stock.setPrdCd(prdmMain.getPrdCd());
							stock.setAttrPrdRepCd(attrPrdRepCd);

							for (int attrPrdIndex = 0; attrPrdIndex != pattrPrdM.size(); attrPrdIndex++) {
								PrdAttrPrdMinsert attrPrd = pattrPrdM.get(attrPrdIndex);

								logger.debug("tempAttrPrdRepCd =>" + tempAttrPrdRepCd);
								logger.debug("attrPrd.getAttrPrdRepCd() =>" + attrPrd.getAttrPrdRepCd());
								logger.debug("attrPrdRepCd =>" + attrPrdRepCd);
								if (tempAttrPrdRepCd.equals(attrPrd.getAttrPrdRepCd())) {
									attrPrd.setAttrPrdRepCd(attrPrdRepCd);
									attrPrd.setPrdCd(prdmMain.getPrdCd());
								}
							}
							for (int possQtyIndex = 0; possQtyIndex != prdOrdPsblQtyD.size(); possQtyIndex++) {
								// 임시 속성코드와 동일하면 채번 속성코드로 변경
								PrdOrdPsblQtyDinsert possQty = prdOrdPsblQtyD.get(possQtyIndex);

								if(tempAttrPrdRepCd.equals(possQty.getAttrPrdRepCd())) {
									possQty.setAttrPrdRepCd(attrPrdRepCd);
								}
							}
						}
					}
				}

				//[SR02180727185][2018.09.17][이용문]: 지정일(설치포함) 프로세스 개선을 위한 상품등록 화면 수정
				String apntDtDlvTyp = "";
				if( "Y".equals(StringUtils.NVL(prdPrdD.getApntDtDlvYn())) || "1".equals(StringUtils.NVL(prdPrdD.getApntDtDlvYn())) ){
					apntDtDlvTyp = StringUtils.NVL(prdPrdD.getApntDtDlvTyp());
					
					//지정일 배송유형이 날짜지정이라면 날짜타입 및 공휴일 체크
					if( "D".equals(apntDtDlvTyp) ){
						DSData tmpApntCheck = isValidateApntAttrDate(pattrPrdM, "");
						if( tmpApntCheck != null && !"1".equals(tmpApntCheck.getString("retCd")) ){
							returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, tmpApntCheck));
						}
					}
				}
				
				int e = 1;
				// 속성상품 INSERT
				for (int i = 0; i < pattrPrdM.size(); i++) {
					PrdAttrPrdMinsert attrPrd = pattrPrdM.get(i);
					BigDecimal prdCd = pattrPrdM.get(i).getPrdCd();

					if (prdCd == null || prdCd.toString().length() < 6) { // 여기서 상품코드가없는 속성상품코드처리
						attrPrd.setPrdCd(prdmMain.getPrdCd());
					}

					// 임시 채번 속성코드
					BigDecimal tempAttrPrdCd = attrPrd.getAttrPrdCd();

					// 속성코드 채번
					attrPrd.setAttrPrdCd(prdEntity.getAttrPrdCd(prdmMain));
					// 속성값코드가 모두 'None'인 경우 속성값코드4에 신규채번함 => E001 ~  E999
					if(attrPrd.getAttrValCd1().equals("None") && attrPrd.getAttrValCd2().equals("None")
						&& attrPrd.getAttrValCd3().equals("None") && attrPrd.getAttrValCd4().equals("None") ) {
						String attrValCd4 = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(e), 3, '0');
						attrPrd.setAttrValCd4("E" + attrValCd4);
						e = e + 1;
					}

					// 속성대표/속성일반(ordPrdTypCd)
					// 01 : 속성 대표 상품별 주문
					// 02 : 일반주문/상품속성별주문
					if (!"01".equals(prdmMain.getOrdPrdTypCd())) { // 주문형태 : 속성일반 주문인경우 처리
						// 속성 대표코드 동일 코드로 처리
						attrPrd.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());

						for (int stockIndex = 0; stockIndex != prdAttrPrdm.size(); stockIndex++) {
							// 임시 속성코드와 동일하면 채번 속성코드로 변경
							PrdStockDinsert stock = prdAttrPrdm.get(stockIndex);
							if(stock.getAttrPrdRepCd() != null){
								if (tempAttrPrdCd.toString().equals(stock.getAttrPrdRepCd())) {
									stock.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());
									stock.setPrdCd(attrPrd.getPrdCd());
								}
							}else{
								stock.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());
								stock.setPrdCd(attrPrd.getPrdCd());
							}
						}
						for (int possQtyIndex = 0; possQtyIndex != prdOrdPsblQtyD.size(); possQtyIndex++) {
							// 임시 속성코드와 동일하면 채번 속성코드로 변경
							PrdOrdPsblQtyDinsert possQty = prdOrdPsblQtyD.get(possQtyIndex);
							if(possQty.getAttrPrdRepCd() != null){
								if(tempAttrPrdCd.toString().equals(possQty.getAttrPrdRepCd())) {
									possQty.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());
								}
							}else{
								possQty.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());
							}
						}
					}

					if("Y".equals(expAutoClsYn)) {
						if("Q".equals(prdPrdD.getClsChkAftAprvCd())) {
							attrPrd.setAttrPrdAprvStCd("25");
						}
					}
					
					pattrPrdM.get(i).setSessionUserId(prdmMain.getSessionUserId()); /* 세션정보 [PD_2017_017_속도개선][jongil] */
					// 속성상품코드 insert
					prdEntity.setInsertPrdAttrPrdM(pattrPrdM.get(i));
					
					/*[SKU][2021.01.05]:SKU 프로젝트*/
					if("Y".equals(prdPrdD.getSkuYn())){
						if(attrPrd.getCopySrcAttrPrdCd() != null){
							prdSkuMngEntity.copyInsertPrdSkuMapD(pattrPrdM.get(i));
						}
					}	
				}

				// 재고수량 저장
				for (int i = 0; i < prdAttrPrdm.size(); i++) {
					prdAttrPrdm.get(i).setSessionUserId(prdmMain.getSessionUserId()); /* 세션정보 [PD_2017_017_속도개선][jongil] */
					int upAttrval = attrPrdEntity.modifyStockD(prdAttrPrdm.get(i));
					if (upAttrval < 1) {
						prdEntity.setInsertPrdStockD(prdAttrPrdm.get(i));
					}
				}

				// 주문가능 수량 저장
				for (int i = 0; i < prdOrdPsblQtyD.size(); i++) {
					// 주문가능 수량 로그 정보  속성대표상품코드
					OrdPsblQty  pOrdPsblQty = new OrdPsblQty();
					pOrdPsblQty.setAttrPrdRepCd(prdOrdPsblQtyD.get(i).getAttrPrdRepCd());
					//SMTCLogger.writePrd("getDtctCd"+i+"==>"+prdmMain.getDtctCd());
					//SMTCLogger//SMTCLogger.writePrd(nSaleYn"+i+"==>"+prdPrdD.getStockInSaleYn());
					/* [SR02150327087]  mc/pc 상품인경우 재고범위내주문여부필수 */
					if("DC02".equals(StringUtils.NVL(prdmMain.getDtctCd())) && "Y".equals(StringUtils.NVL(prdPrdD.getStockInSaleYn()) ) ){
					   pOrdPsblQty.setOrdPsblQty(new BigDecimal(0));
					   prdOrdPsblQtyD.get(i).setOrdPsblQty(new BigDecimal(0));
					}else{
						pOrdPsblQty.setOrdPsblQty(prdOrdPsblQtyD.get(i).getOrdPsblQty());
					}
					pOrdPsblQty.setSessionObject(prdOrdPsblQtyD.get(i));
					prdOrdPsblQtyD.get(i).setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
					// SAP 전송 주문가능 수량 로그  수정
					if("AZ".equals(prdOrdPsblQtyD.get(i).getChanlGrpCd())){
					   ordPsblQtyEntity.savePrdOrdPsblQtyLog(pOrdPsblQty);
					}

					int upAttrval = attrPrdEntity.modifyOrdPsblQtyD(prdOrdPsblQtyD.get(i));
					if (upAttrval < 1) {

						// 2011/12/20 KJH - 주문가능수량로그등록 추가
						pOrdPsblQty.setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
						pOrdPsblQty.setChanlGrpCd(prdOrdPsblQtyD.get(i).getChanlGrpCd());
						ordPsblQtyEntity.addOrdPsblQtyLog(pOrdPsblQty);
						//-end

						prdEntity.setInsertPrdOrdPsblQtyD(prdOrdPsblQtyD.get(i));
					}
				}
				prdEntity.modifyPrdAttrHead(prdmMain);
			}
		}

		// 상품명변경이력
		for (int i = 0; i < prdNmChg.size(); i++) {
			if (prdNmChg.get(i).getChanlCd() == null){
				prdNmChg.get(i).setChanlCd("P");

				/* 오아후몰 신규/수정 메소드
		     	BPR 배포시 해당 메소드 주석처리 필요함
				 */
				if("33".equals(prdmMain.getPrdGbnCd().toString()))	prdNmChg.get(i).setChanlCd("S");	// 일본상품은 채널 S isKang 20130215
			}
			prdNmChg.get(i).setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
			prdNmChg.get(i).setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
			prdEntity.setInsertPrdNmChgH(prdNmChg.get(i));

		}
		logger.debug("setPrdBase==>6");
		//SMTCLogger.writePrd("상품명변경이력");
		// 상품노출매장 입력
		if (ecdSectPrdlstInfo != null) {
			for (int i = 0; i < ecdSectPrdlstInfo.size(); i++) {
				ecdSectPrdlstInfo.get(i).setPrdid(sprdcd.getValues().getBigDecimal("prdCd"));
				ecdSectPrdlstInfo.get(i).setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
				ecShopEntity.insertsectPrdlst(ecdSectPrdlstInfo.get(i));
			}
		}
		// 안전인증정보 입력 -> 해외상품은 제외하도록 수정 (2011/06/27 OSM)
		/* [딜구조개선-패널] 2014-11-06, 김상철, 딜더미상품인 경우 체크안하게  */ 
		if(!"88".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))){
			if (psafeCertPrd != null && (forgnTmPrdMng == null || forgnTmPrdMng.getFrTmSeq() == null)) {
				logger.debug("setPrdBase==>7" + psafeCertPrd.getSafeCertOrgCd());
				String SafeCertFileNm = StringUtils.NVL(psafeCertPrd.getSafeCertFileNm());
	
				if (!StringUtils.NVL(psafeCertPrd.getSafeCertGbnCd()).equals("")) {
					//안전인증파일명이 있을경우 파일등록
					if (!SafeCertFileNm.equals("") && !SafeCertFileNm.equals("Y") && !SafeCertFileNm.equals("N")){
						String prdCd = psafeCertPrd.getPrdCd().toString();
						String fileNm = psafeCertPrd.getSafeCertFileNm();
						String newFileNm = prdCd +fileNm.substring(psafeCertPrd.getSafeCertFileNm().indexOf("."));
	
						//[SR02171208490][2018.01.03][추연철]:이미지서버업로드 - 변경 작업
						String oldUrl =null;
						String newUrl = "/echoContents/FUPRD06/" + prdCd.substring(0, 2) + "/" + prdCd.substring(2, 4) + "/";
						if ( prdmMain.getPrdAprvDataYn() != null && prdmMain.getPrdAprvDataYn().equals("Y")){ //승인일경우
							oldUrl = Constant.getString("upload.sup.image.server.path");
							List<ReUploadImageFileInfo> Files = new ArrayList<ReUploadImageFileInfo>();
							ReUploadImageFileInfo ftpTransferFile = new ReUploadImageFileInfo();
							ftpTransferFile.setDownloadRemotePath(oldUrl);
							ftpTransferFile.setDownloadRemoteFileName(psafeCertPrd.getSafeCertFileNm());
							ftpTransferFile.setUplaodRemoteFilePath(newUrl);
							ftpTransferFile.setUploadRemoteFileName(newFileNm);
							Files.add(ftpTransferFile);
							try{
								imgFileUploadProcess.transferImgFilesServer2Server(Files);
							}catch(DevPrcException e) {
								//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
								SMTCLogger.errorPrd(Message.getMessage("util.msg.006"), e);
							}
						}else if( (prdmMain.getCopyPrdYn() != null && prdmMain.getCopyPrdYn().equals("Y") && psafeCertPrd.getSafeCertFileNm().indexOf(".")==8 &&  prdmMain.getCopySrcPrdCd().toString().equals(fileNm.substring(0,8))) ){ //복사등록일경우
							String copyPrdCd = prdmMain.getCopySrcPrdCd().toString();
							oldUrl = "/echoContents/FUPRD06/" +copyPrdCd.substring(0, 2) + "/" + copyPrdCd.substring(2, 4) + "/";
							List<ReUploadImageFileInfo> Files = new ArrayList<ReUploadImageFileInfo>();
							ReUploadImageFileInfo ftpTransferFile = new ReUploadImageFileInfo();
							ftpTransferFile.setDownloadRemotePath(oldUrl);
							ftpTransferFile.setDownloadRemoteFileName(psafeCertPrd.getSafeCertFileNm());
							ftpTransferFile.setUplaodRemoteFilePath(newUrl);
							ftpTransferFile.setUploadRemoteFileName(newFileNm);
							Files.add(ftpTransferFile);
							try{
								imgFileUploadProcess.transferImgFilesServer2Server(Files);
							}catch(DevPrcException e) {
								//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
								SMTCLogger.errorPrd(Message.getMessage("util.msg.006"), e);
							}
						}else{ //신규등록일경우
							List<FtpTransferFile> Files = new ArrayList<FtpTransferFile>();
							String CntntExtnsNm = fileNm.substring(psafeCertPrd.getSafeCertFileNm().indexOf(".")+1);
						    FtpTransferFile ftpTransferFile = new FtpTransferFile();
							ftpTransferFile.setFileUplodPathNm(Constant.getString("upload.image.temp"));
							ftpTransferFile.setFileNm(psafeCertPrd.getSafeCertFileNm());
							ftpTransferFile.setFileExtnsNm(CntntExtnsNm);
							ftpTransferFile.setRemotePath(newUrl);
							ftpTransferFile.setRemoteFileNm(newFileNm);
							
							//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
							SMTCLogger.infoPrd("ftpTransferFile=>이미지처리1" + ftpTransferFile.getFileUplodPathNm());
							SMTCLogger.infoPrd("ftpTransferFile=>이미지처리1" + ftpTransferFile.getFileNm());
							SMTCLogger.infoPrd("ftpTransferFile=>이미지처리1" + ftpTransferFile.getFileExtnsNm());
							SMTCLogger.infoPrd("ftpTransferFile=>이미지처리1" + ftpTransferFile.getFileUplodFullName());
							
							Files.add(ftpTransferFile);
							try {
//								List<FtpTransferFile> rslt = imgFileUploadProcess.transferImgFilesToServer(Files);
								List<FtpTransferFile> rslt = imgFileUploadProcess.transferImgFilesToServerNew(Files);
								logger.debug("setPrdBase==>rslt" + rslt);
							} catch (Exception e) {
								logger.debug(Message.getMessage("util.msg.006"),e);
							}
						}
	
						psafeCertPrd.setSafeCertFileNm(newFileNm);
					}
					
					psafeCertPrd.setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
					prdEntity.setInsertPrdSafeCertD(psafeCertPrd);
				}
			}
		}
		/* [딜구조개선-패널] END */
		
		// 상품예정정보 입력
		if (prdSchdDInfo != null) {
			logger.debug("setPrdBase==>8");
			for (int i = 0; i < prdSchdDInfo.size(); i++) {
				if (StringUtils.NVL(prdSchdDInfo.get(i).getGubun()).equals("Y")) {
					prdSchdDInfo.get(i).setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
					prdSchdDInfo.get(i).setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
					prdEntity.setInsertPrdSchdD(prdSchdDInfo.get(i));
				}
			}
		}
		// UDA정보 입력

		// 상품의 소분류가 상품권일경우 gs&point 제한으로 설정 한다. uda_no = 21 2012-03-15로직제외
		/*
		if (prdmMain.getPrdClsCd().substring(0, 7).equals("B310105")){
			PrdUdaDtl prdUdaDtlL = new PrdUdaDtl();
			prdUdaDtlL.setUdaGbnCd("10");
			prdUdaDtlL.setUdaNo(new BigDecimal("21"));
			prdUdaDtlL.setUdaVal("Y");
			prdUdaDtlL.setUseYn("1");
			prdUdaDtlL.setChk("Y");
			prdUdaDtlL.setValidEndDtm("29991231235959");
			prdUdaDtlL.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
			prdUdaDtlL.setSessionUserId(prdmMain.getSessionUserId());
			prdUdaDtl.add(prdUdaDtlL);
		}
		*/
		//2013-11-21 KJH 구매제한설정 분류이며, 인터넷채널일 경우 구매제한으로 설정한다.
		PrdClsByBaseValQryCond pPrdClsByBaseValQryCond = new PrdClsByBaseValQryCond();
		pPrdClsByBaseValQryCond.setSupCd(prdmMain.getSupCd().toString());
		pPrdClsByBaseValQryCond.setDtlClsCd(prdmMain.getPrdClsCd());
		pPrdClsByBaseValQryCond.setBrandCd(prdmMain.getBrandCd().toString());
		pPrdClsByBaseValQryCond.setUseYn("Y");
		pPrdClsByBaseValQryCond.setClsLvlNo("4");
		pPrdClsByBaseValQryCond.setGbnNo("%");
		EntityDataSet<DSMultiData> pPrdClsBaseValListEDS = prdClsBaseEntity.getPrdClsBaseValList(pPrdClsByBaseValQryCond);

		if( pPrdClsBaseValListEDS.getValues() != null && pPrdClsBaseValListEDS.getValues().size() > 0  ) {
			if("1".equals(pPrdClsBaseValListEDS.getValues().get(0).getString("prchLimitYn")) && "GE".equals(prdmMain.getRegChanlGrpCd()) ) {
				boolean regYn = false;	//구매제한 사용자 등록데이터 존재 여부
				
				for( int i = 0; i < prdUdaDtl.size(); i++) {
					PrdUdaDtl cmpPrdUdaDtl = prdUdaDtl.get(i);
					if(regYn) { break; }
					
					if( "10".equals(cmpPrdUdaDtl.getUdaGbnCd()) && cmpPrdUdaDtl.getUdaNo().intValue() == 15 ) {
						//입력된 구매제한수량이 분류기준의 구매제한수량보다 클 경우 분류기준의 구매제한수량으로 셋팅함.
						if(Integer.parseInt(cmpPrdUdaDtl.getUdaVal()) > pPrdClsBaseValListEDS.getValues().get(0).getInt("prchLimitQty") ) {
							cmpPrdUdaDtl.setUdaVal(pPrdClsBaseValListEDS.getValues().get(0).getString("prchLimitQty"));
						}
						regYn = true;
					}
				}
				
				if(!regYn) {
					PrdUdaDtl prdUdaDtlL = new PrdUdaDtl();
					prdUdaDtlL.setUdaGbnCd("10");
					prdUdaDtlL.setUdaNo(new BigDecimal("15"));
					prdUdaDtlL.setUdaVal(pPrdClsBaseValListEDS.getValues().get(0).getString("prchLimitQty"));
					prdUdaDtlL.setUseYn("1");
					prdUdaDtlL.setChk("Y");
					prdUdaDtlL.setValidEndDtm("29991231235959");
					//기간별 구매제한 - 기본 제한 일수 누락 시 입력 추가
					prdUdaDtlL.setUdaVal1("30");
					prdUdaDtlL.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
					prdUdaDtlL.setSessionUserId(prdmMain.getSessionUserId());
					prdUdaDtl.add(prdUdaDtlL);
				}
				
			}
		}
		//-end

		if (prdUdaDtl != null) {
			logger.debug("setPrdBase==>10");
			for (int i = 0; i < prdUdaDtl.size(); i++) {
				if (StringUtils.NVL(prdUdaDtl.get(i).getChk()).equals("Y")) {
					prdUdaDtl.get(i).setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
					prdUdaDtl.get(i).setSessionUserId(prdmMain.getSessionUserId()); /* 세션정보 [PD_2017_017_속도개선][jongil] */
					prdUdaEntity.setInsertPrdUda(prdUdaDtl.get(i));
				}
			}
		}

		//2012/03/16 신규등록 시 VIP쿠폰제한 협력사에 등록되어 있을 경우 업체전용쿠폰 발행시만 적용가능하도록 VIP 및 더블쿠폰도 적용 불가능하도록 쿠폰제한상품목록 초기설정
		DSData limitTgtCnt = supEntity.getSupVipCpnLimitTgtCnt(prdmMain);
		
		/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 _486 Start */
		param = new PrdClsUda();
		param.setUdaNo(new BigDecimal( "171" ));
		param.setPrdClsCd(prdmMain.getPrdClsCd());

		if( limitTgtCnt != null && limitTgtCnt.getInt("vipCpnLimitTgtCnt") > 0 ) {
			logger.debug("vip cpn limit >>>");
			CpnLimitPrd cpnLimitPrd = new CpnLimitPrd();
			cpnLimitPrd.setPrdCd(prdmMain.getPrdCd());
			cpnLimitPrd.setCpnGbnCd("V");
			cpnLimitPrd.setUseYn("Y");
			cpnLimitPrd.setValidStrDtm(sysdtm);
			cpnLimitPrd.setValidEndDtm("29991231235959");
			cpnLimitPrd.setChk("1");

			cpnLimitPrd.setSessionObject(prdmMain);

			cpnApplyPrdEntity.addCpnLimitPrdList(cpnLimitPrd);
		}
		//2012/03/16 -END
		//소분류가상품권(B310105)분류인경우 vip쿠폰 제한 설정 SR02130812120  2013.08.16
		//[SR02141226076][2015.02.16][유수경]:모바일 상품권/티켓 관련 상품분류 재정비의 건,  'B310109', 'B310111', 'B310707' 추가 처리
		// [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거		
		/*else if ("B310105".equals(prdmMain.getPrdClsCd().substring(0, 7)) ||
				 "B310109".equals(prdmMain.getPrdClsCd().substring(0, 7)) ||
				 "B310111".equals(prdmMain.getPrdClsCd().substring(0, 7)) ||
				 "B310707".equals(prdmMain.getPrdClsCd().substring(0, 7)) 
				) {*/
		else if(PrdClsUdaUtils.isPrdClsUdaFlag(param)){	
		/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 _486 End */
			logger.debug("vip cpn 등록처리");
			CpnLimitPrd cpnLimitPrd = new CpnLimitPrd();
			cpnLimitPrd.setPrdCd(prdmMain.getPrdCd());
			cpnLimitPrd.setCpnGbnCd("V");
			cpnLimitPrd.setUseYn("Y");
			cpnLimitPrd.setValidStrDtm(sysdtm);
			cpnLimitPrd.setValidEndDtm("29991231235959");
			cpnLimitPrd.setChk("1");

			cpnLimitPrd.setSessionObject(prdmMain);

			cpnApplyPrdEntity.addCpnLimitPrdList(cpnLimitPrd);
		}

		// 브랜드의 사양정보 등록 (브랜드 사양을 반드시 먼저처리해야 함) (2011/05/01 JOO)
		PrdSpecVal prdSpecVal = new PrdSpecVal();
		prdSpecVal.setPrdSpecSeq(prdmMain.getBrandCd());
		prdSpecVal.setPrdCd(prdmMain.getPrdCd());
		prdSpecVal.setPrdSpecCd("0");
		prdSpecVal.setPrdSpecStCd("1");
		prdSpecVal.setSessionUserId((prdmMain.getSessionUserId() == null) ? "" : prdmMain.getSessionUserId()); /* sessionUserId (사용자ID) */
		//setPrdSpec(prdSpecVal);
		prdSpecInfo.add(0, prdSpecVal);
		// 상품사양값목록저장
		if (prdSpecInfo != null) {
			logger.debug("setPrdBase==>11");
			for (int i = 0; i < prdSpecInfo.size(); i++) {
				prdSpecInfo.get(i).setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
				prdSpecInfo.get(i).setSessionUserId((prdmMain.getSessionUserId() == null) ? "" : prdmMain.getSessionUserId());  /* 세션정보 [PD_2017_017_속도개선][jongil] */
			}
			prdSpecInfoEntity.insertPrdSpecInfoList(prdSpecInfo);
		}

		// 아이템코드 업데이트 (2011/03/30 OSM)
		ItemReg pItemReg = new ItemReg();
		pItemReg.setPrdCd(prdmMain.getPrdCd());	/* 상품코드 */
		pItemReg.setSessionObject(prdmMain);    /* 세션정보 */

		//2013-09-02 방송알림 공유 추가
		if( "Y".equals(prdmMain.getPrdPhnalamYn()) ) {
			pItemReg.setPrdPhnalamYn(prdmMain.getPrdPhnalamYn());
		}

		modifyPrdMstItem(pItemReg);

		// 상품공지정보등록
		if (prdNtcInfo != null) {
			logger.debug("setPrdBase==>12");
			if (StringUtils.NVL(prdNtcInfo.getGubun()).equals("Y")) {
				prdNtcInfo.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
				prdNtcInfo.setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
				prdEntity.addPrdNtcInfo(prdNtcInfo);
			}
		}
		// 상품메타정보등록
		if (prdMetaDInfo != null) {
			logger.debug("setPrdBase==>13");
			for (int i = 0; i < prdMetaDInfo.size(); i++) {
				prdMetaDInfo.get(i).setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
				prdMetaDInfo.get(i).setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
				//[PD_2017_001_여행프로세스개선][2017.03.02][최미영]:여행은 패스
				if (prdMetaDInfo.get(i).getPrdMetaTypCd().equals("50")){
					continue;
				}
				prdMetaInfoEntity.addPrdMetaD(prdMetaDInfo.get(i));
			}
		}
		
		//[PD_2017_017_속도개선] 2017.11.30 김은희 :  이미지 복사 에러 메시지 
		String noLargeImgCopyMsg = "";
		String noBannerImgCopyMsg = "";
		// 상품복사 프로세스
		if (prdmMain.getCopyPrdYn() != null && prdmMain.getCopyPrdYn().equals("Y")) {
			// 무이자할부프로모션 복사
			if (prdmMain.getPrdCopyNoIntPmoYn() != null && prdmMain.getPrdCopyNoIntPmoYn().equals("Y")) {
				PrcPmoQryCond prcPmoQryCond = new PrcPmoQryCond();
				prcPmoQryCond.setPrdCd(prdmMain.getCopySrcPrdCd());
				DSMultiData copyNoIntPmo = prdPmoEntity.getPrdCopyNoIntPmoList(prcPmoQryCond).getValues();
				PrdPmoStore prdPmoStore = new PrdPmoStore();
				BigDecimal prdPmoSeq = new BigDecimal(0);

				if (copyNoIntPmo != null) {
					for (int i = 0; i < copyNoIntPmo.size(); i++) {
						prdPmoStore.setPmoNo(copyNoIntPmo.get(i).getBigDecimal("pmoNo")); // 프로모션번호
						prdPmoSeq = prdPmoEntity.getPrdPmoSeq(); // 상품프로모션순번채번
						prdPmoStore.setPrdPmoSeq(prdPmoSeq); // 상품프로모션순번
						prdPmoStore.setTgtGbnCd(copyNoIntPmo.get(i).getString("tgtGbnCd")); // 대상구분코드
						prdPmoStore.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd")); // 상품코드
						prdPmoStore.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss")); // 유효시작일시
						prdPmoStore.setValidEndDtm("29991231235959"); // 유효종료일시
						prdPmoStore.setChanlCd(copyNoIntPmo.get(i).getString("chanlCd")); // 채널코드
						prdPmoStore.setApplyPriorRank(copyNoIntPmo.get(i).getString("applyPriorRank")); // 적용우선순위
						prdPmoStore.setOfferTypCd(copyNoIntPmo.get(i).getString("offerTypCd")); // 오퍼유형코드
						prdPmoStore.setNoIntMmCnt(copyNoIntPmo.get(i).getBigDecimal("noIntMmCnt")); // 무이자개월수
						prdPmoStore.setAprvStCd("30"); // 승인상태코드
						prdPmoStore.setRtAmtCd(copyNoIntPmo.get(i).getString("rtamtCd")); //일시불유형
						prdPmoStore.setGshsApplyRtAmt(copyNoIntPmo.get(i).getBigDecimal("gshsApplyRtamt")); //당사일시불적용금액 
						prdPmoStore.setSupApplyRtAmt(copyNoIntPmo.get(i).getBigDecimal("supApplyRtamt")); //협력사일시불적용금액
						prdPmoStore.setNoIntInsmFeeSupShrRt(copyNoIntPmo.get(i).getBigDecimal("noIntInsmFeeSupShrRt")); //무이자협력사부담율
						prdPmoStore.setLongNoIntTgtCardVal(copyNoIntPmo.get(i).getString("longNoIntTgtCardVal")); //무이자대상카드
						prdPmoStore.setSessionUserIp(prdmMain.getSessionUserIp());
						prdPmoStore.setSessionUserId(prdmMain.getSessionUserId());
						prdPmoStore.setSessionUserNm(prdmMain.getSessionUserNm());

						prdPmoEntity.addPrdPmo(prdPmoStore); // 상품프로모션등록
					}
				}
			}
			// 기술서 복사
			if (prdmMain.getPrdCopyDescYn() != null && prdmMain.getPrdCopyDescYn().equals("Y")) {
				PrdCdQryCond prdCdQryCond = new PrdCdQryCond();
				prdCdQryCond.setPrdCd(prdmMain.getCopySrcPrdCd());

				// HTML기술서 복사
				DSMultiData copyHtml = prdEntity.getPrdCopyDescHtmlList(prdCdQryCond).getValues();
				if (copyHtml != null) {
					List<PrdHtmlDescd> prdHtmlList = new ArrayList<PrdHtmlDescd>();
					for (int i = 0; i < copyHtml.size(); i++) {
						PrdHtmlDescd prdHtml = new PrdHtmlDescd();
						prdHtml.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd")); // 상품코드
						prdHtml.setChanlCd(copyHtml.get(i).getString("chanlCd")); // 채널코드
						prdHtml.setRegGbnCd(copyHtml.get(i).getString("regGbnCd")); // 등록구분코드
						prdHtml.setDescdExplnCntnt(copyHtml.get(i).getString("descdExplnCntnt")); // 기술서설명내용
						prdHtml.setRcmdSntncCntnt(copyHtml.get(i).getString("rcmdSntncCntnt")); // 추천문구내용
						prdHtml.setWritePrevntYn(copyHtml.get(i).getString("writePrevntYn")); // 쓰기방지여부
						prdHtml.setEcExposYn(copyHtml.get(i).getString("ecExposYn")); // EC노출여부
						prdHtml.setSessionUserIp(prdmMain.getSessionUserIp());
						prdHtml.setSessionUserId(prdmMain.getSessionUserId());
						prdHtml.setSessionUserNm(prdmMain.getSessionUserNm());
						prdHtmlList.add(prdHtml);
					}
					prdHtmlDescdEntity.addHtmlDescdList(prdHtmlList); // 상품HTML기술서등록
					
					//[SR02140514059][2014.06.20][김지혜] : MC 단품 기술서 캡쳐 솔루션 적용
					//[GRIT-65674]:모바일 전용 이미지 저장 기능 삭제 요청
					//for( int i = 0 ; i < prdHtmlList.size(); i++) {
						//PrdDescdHtmlDInfo prdDescdHtmlDInfo = new PrdDescdHtmlDInfo();  
						//DevBeanUtils.wrappedObjToObj(prdDescdHtmlDInfo, prdHtmlList.get(i));
						//logger.debug("변환되었느냐~"+prdDescdHtmlDInfo.getPrdCd());
						
						//imgMngProcess.connectMobileDescdHtmlDInfo(prdDescdHtmlDInfo);
					//}
					//-end
				}
			}
			// 상품평 공유
			if (prdmMain.getPrdCopyKnowYn() != null) {
				//[HANGBOT-2201_DEVOPS-1805][2020.10.19][김영현]상품코드 복사등록 시 상품평 공유설정유형 추가
				if(prdmMain.getPrdCopyKnowYn().equals("Y")){		//단방향 공유
					EcGsKnow ecGsKnow = new EcGsKnow();
					ecGsKnow.setActivePrdid(sprdcd.getValues().getBigDecimal("prdCd")); // 공유한상품코드
					ecGsKnow.setPassivePrdid(prdmMain.getCopySrcPrdCd()); // 공유된상품코드
					ecGsKnow.setSessionUserId(prdmMain.getSessionUserId());
					ecShopEntity.insertPrdCopyKnowList(ecGsKnow);
				}else if(prdmMain.getPrdCopyKnowYn().equals("M")){	//상호 공유
					EcGsKnow ecGsKnow = new EcGsKnow();
					ecGsKnow.setActivePrdid(sprdcd.getValues().getBigDecimal("prdCd")); // 공유한상품코드
					ecGsKnow.setPassivePrdid(prdmMain.getCopySrcPrdCd()); // 공유된상품코드
					ecGsKnow.setSessionUserId(prdmMain.getSessionUserId());
					ecShopEntity.insertPrdCopyKnowList(ecGsKnow);
					
					EcGsKnow ecGsKnow2 = new EcGsKnow();
					ecGsKnow2.setActivePrdid(prdmMain.getCopySrcPrdCd()); // 공유된상품코드
					ecGsKnow2.setPassivePrdid(sprdcd.getValues().getBigDecimal("prdCd")); // 공유한상품코드
					ecGsKnow2.setSessionUserId(prdmMain.getSessionUserId());
					ecShopEntity.insertPrdCopyKnowList(ecGsKnow2);
				}
			}
			// 이미지 복사
			if (prdmMain.getPrdCopyImgYn() != null && prdmMain.getPrdCopyImgYn().equals("Y")) {
				Cntnt pCntnt = new Cntnt();
				pCntnt.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd")); // 상품코드
				pCntnt.setOrgPrdCd(prdmMain.getCopySrcPrdCd()); // 복사할상품코드
				pCntnt.setCntntRegGbnCd("I"); // 컨텐츠등록구분코드
				pCntnt.setSessionUserId(prdmMain.getSessionUserId());
				//[PD_2020_002_모바일배너이력관리] 상품이력테이블 등록 시 필요
				pCntnt.setSessionUserIp(prdmMain.getSessionUserIp());
				
				PrdQryCond pPrdQryCond = new PrdQryCond();
				pPrdQryCond.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
				pPrdQryCond.setSessionUserId(prdmMain.getSessionUserId());

				//[2016.03.15][김영현]:상품 복사 시 이미지 등록 개선(대표이미지 누락 처리)
				pCntnt.setImgL1AddYn("Y");
								
				//[SR02140924103] [2014.10.27][유수경] : 이미지 복사 개선
				String cpChannel = "O";
				CmmCdQryCond pCmmCdQryCond = new CmmCdQryCond(); // 공통코드조회조건
				pCmmCdQryCond.setCmmGrpCd("PRD165");
				EntityDataSet<DSMultiData> cdListEntityDataSet = cmmCdEntity.getCdList(pCmmCdQryCond); // 코드목록조회
				if( cdListEntityDataSet.getValues() != null  && cdListEntityDataSet.getValues().size() > 0) {
					cpChannel = cdListEntityDataSet.getValues().getString(0, "cmmCd");
				}	
				if(cpChannel == null || "".equals(cpChannel)){
					cpChannel = "O";
				}
				

				//[PD_2017_017_속도개선][2017.11.08][김은희] :속도개선 수정건
				pCntnt.setCntntTypCdChk(true);
				pCntnt.setCntntTypCd("LARGE");
				//대표 이미지 조회 
				DSMultiData largeImg = imgEntity.getCntntCopy(pCntnt).getValues();
				
				if (largeImg.size() > 0) {
					
					//[PD_2020_003_상품이미지이력관리] 상품이미지 복사 등록, 2020.03.17 김영일 시작
					for (int i = 0; i < largeImg.size(); i++) {
						PrdImgChgLog prdImgChgLog = new PrdImgChgLog();
						prdImgChgLog.setPrdCd(pCntnt.getPrdCd());
						prdImgChgLog.setCntntFileNm(largeImg.get(i).getString("newFileNm"));
						prdImgChgLog.setChgBefVal(largeImg.get(i).getString("oldFileNm"));   //카피원본컨텐츠파일명
						prdImgChgLog.setChgAftVal(largeImg.get(i).getString("newFileNm"));  //카피대상컨텐츠파일명
						prdImgChgLog.setSessionUserId(pCntnt.getSessionUserId());
						prdImgChgLog.setSessionUserIp(pCntnt.getSessionUserIp());
						//상품이미지변경로그 등록
					    imgEntity.addPrdImgChgLog(prdImgChgLog);
					}
					//[PD_2020_003_상품이미지이력관리] 상품이미지 복사 등록, 2020.03.17 김영일 끝
					Map<String, String> result = imgFileCopy(prdmMain.getCopySrcPrdCd(), sprdcd.getValues().getBigDecimal("prdCd"), largeImg, true);
					String retCd = (String)result.get("retCd");
					if(!"S".equals(retCd)){
						String retMsg = (String)result.get("retMsg");
						noLargeImgCopyMsg = "\n" + retMsg;
					}
				}else{
					//대표 이미지 정보가 없습니다.
					noLargeImgCopyMsg ="\n"+Message.getMessage("prd.msg.547");
				}
				//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
				SMTCLogger.infoPrd("returnDsData::"+returnDsData);
				
				//배너이미지만 조회 - 기존 프로세스와 동일하게 처리 

				pCntnt.setCntntTypCdChk(true);
				pCntnt.setCntntTypCd("B");
				DSMultiData bannerImg = imgEntity.getCntntCopy(pCntnt).getValues();
				if (bannerImg.size() > 0) {
					
					// 이미지복사등록
					imgEntity.addCntntCopy(pCntnt);

					// 이미지파일 업로드
					//모바일배너 등록 변경이력 생성, [PD_2020_모바일배너이력관리] 2020.02.20 김영일
					for (int i = 0; i < bannerImg.size(); i++) {
						PrdColChgLog prdColChgLog = new PrdColChgLog();
						prdColChgLog.setPrdCd(pCntnt.getPrdCd());
						prdColChgLog.setChgColNm("MOBILE_BANNER");
						prdColChgLog.setChgAftVal(bannerImg.get(i).getString("newFileNm"));
						prdColChgLog.setSessionUserId(pCntnt.getSessionUserId());
						prdColChgLog.setSessionUserIp(pCntnt.getSessionUserIp());
						//상품이력 등록
						prdColChgLogEntity.addColChgLog(prdColChgLog);
					}

					Map<String, String> result = imgFileCopy(prdmMain.getCopySrcPrdCd(),sprdcd.getValues().getBigDecimal("prdCd"), bannerImg, false);
					String retCd = (String)result.get("retCd");
					if(!"S".equals(retCd)){
						noBannerImgCopyMsg = "\n\n" + "배너이미지 저장중 오류가 발생하였습니다. \n배너이미지를 다시 등록해주세요.";
					}
				}
				
				//[PD_2017_017_속도개선][2017.11.30][김은희] : 복사이미지가 있을경우에만 이미지 변경로그 및 확인여부 수정처리 
				if(bannerImg.size() > 0 || largeImg.size() > 0){
					// 상품이미지확인여부수정
					prdEntity.modifyPrdImgCnfYn(pPrdQryCond);
					// 이미지변경로그등록
					imgEntity.addCntntChgLog(pCntnt);
				}
			}
		}

		// 상품승인 프로세스
		if (prdmMain.getPrdAprvDataYn() != null && ("Y").equals(prdmMain.getPrdAprvDataYn())) {
			returnDsData.put("prdStep", "OK");
			pSupPrdAprvQryCond.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
			if (prdmMain.getNoIntMmCnt() != null){
				pSupPrdAprvQryCond.setNoIntMmCnt(prdmMain.getNoIntMmCnt());
			}
			logger.debug("forgnTmPrdMng >+++++@> " + forgnTmPrdMng);
			// 프로모션 승인 등록 -> 해외상품관련 수정 (2011/05/27 OSM)
			if (forgnTmPrdMng != null && forgnTmPrdMng.getFrTmSeq() != null
					&& prdmMain.getNoIntMmCnt() != null && prdmMain.getNoIntMmCnt().compareTo(new BigDecimal("0")) > 0) {
				logger.debug("해외상품 프로모션 등록 ==> [" + prdmMain.getNoIntMmCnt() + "] 개월 무이자");
				PrdPmoStore prdPmoStore = new PrdPmoStore();
				prdPmoStore.setPmoNo(new BigDecimal("1")); 			// 프로모션번호
				BigDecimal prdPmoSeq = prdPmoEntity.getPrdPmoSeq(); // 상품프로모션순번채번
				prdPmoStore.setPrdPmoSeq(prdPmoSeq);				// 상품프로모션순번
				prdPmoStore.setTgtGbnCd("P"); 						// 대상구분코드
				prdPmoStore.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd")); // 상품코드
				prdPmoStore.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss")); // 유효시작일시
				prdPmoStore.setValidEndDtm("29991231235959"); 		// 유효종료일시
				prdPmoStore.setChanlCd("P"); 						// 채널코드
				prdPmoStore.setApplyPriorRank("5"); 				// 적용우선순위
				prdPmoStore.setOfferTypCd("10"); 					// 오퍼유형코드
				prdPmoStore.setNoIntMmCnt(prdmMain.getNoIntMmCnt());// 무이자개월수
				prdPmoStore.setAprvStCd("30"); 						// 승인상태코드
				prdPmoStore.setSessionUserIp(prdmMain.getSessionUserIp());
				prdPmoStore.setSessionUserId(prdmMain.getSessionUserId());
				prdPmoStore.setSessionUserNm(prdmMain.getSessionUserNm());
				prdPmoEntity.addPrdPmo(prdPmoStore); // 상품프로모션등록
			} else {
				DSMultiData copyNoIntPmo = ecScmMngEntity.getSupPrdAprvPmoSimpleDList(pSupPrdAprvQryCond).getValues();
				if (copyNoIntPmo != null) {
					for (int i = 0; i < copyNoIntPmo.size(); i++) {
						PrdPmoStore prdPmoStore = new PrdPmoStore();
						prdPmoStore.setPmoNo(copyNoIntPmo.get(i).getBigDecimal("pmoNo")); // 프로모션번호
						BigDecimal prdPmoSeq = prdPmoEntity.getPrdPmoSeq(); // 상품프로모션순번채번
						prdPmoStore.setPrdPmoSeq(prdPmoSeq); // 상품프로모션순번
						prdPmoStore.setTgtGbnCd(copyNoIntPmo.get(i).getString("tgtGbnCd")); // 대상구분코드
						prdPmoStore.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd")); // 상품코드
						prdPmoStore.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss")); // 유효시작일시
						prdPmoStore.setValidEndDtm("29991231235959"); // 유효종료일시
						prdPmoStore.setChanlCd(copyNoIntPmo.get(i).getString("chanlCd")); // 채널코드
						prdPmoStore.setApplyPriorRank(copyNoIntPmo.get(i).getString("applyPriorRank")); // 적용우선순위
						prdPmoStore.setOfferTypCd(copyNoIntPmo.get(i).getString("offerTypCd")); // 오퍼유형코드
						prdPmoStore.setNoIntMmCnt(copyNoIntPmo.get(i).getBigDecimal("noIntMmCnt")); // 무이자개월수
						prdPmoStore.setAprvStCd("30"); // 승인상태코드(작성중에서 확정으로 변경 bpr
						prdPmoStore.setSessionUserIp(prdmMain.getSessionUserIp());
						prdPmoStore.setSessionUserId(prdmMain.getSessionUserId());
						prdPmoStore.setSessionUserNm(prdmMain.getSessionUserNm());
						prdPmoEntity.addPrdPmo(prdPmoStore); // 상품프로모션등록
					}
				}
			}
			// 20110425 일반기술서 받아서 처리
			// prdDesceGenrlDInfoList, // 상품기술서 정보
		    // prdDescdHtmlDInfoList //상품 HTML 기술서 정보

//----- 주석처리 후 변경[S] 상품 승인 저장 / 2013-11-05 / [일반기술서 정보고시 통합] / kimky73 [S] -----//
			//addPrdDescdEaiList = this.addPrdDescdS4c(prdmMain,prdDesceGenrlDInfoList, prdDescdHtmlDInfoList );
			addPrdDescdEaiList = this.addPrdDescdS4cNew(prdmMain,prdDesceGenrlDInfoList, prdDescdHtmlDInfoList );
//----- 주석처리 후 변경[S] 상품 승인 저장 / 2013-11-05 / [일반기술서 정보고시 통합] / kimky73 [S] -----//

			// 이미지 승인 등록 -> 해외상품이 아닌 경우 (2011/05/29 OSM)
			//[SR02180316175]세일즈원 WAS Thread Hang으로 인한 CPU 과점 조치
			//이미지 전송 호출 변경
			if (forgnTmPrdMng == null || forgnTmPrdMng.getFrTmSeq() == null) {
				// 이미지 승인 등록
				DSMultiData copyImg = ecScmMngEntity.getSupPrdAprvImgList(pSupPrdAprvQryCond).getValues();
				if (copyImg != null && copyImg.size() > 0) {
					//[GRIT-112159]:[세일즈원 상품] 상품대표이미지 aws 전환을 위한 개발
					String oldUrl = Constant.getString("upload.sup.new.image.server.path");
					//String newUrl = Constant.getString("upload.image.server.path");
					
					//[SR02180323545][2018.06.25][고경환]:위드넷 이미지 실시간 리사이징 영역 확대 적용 요청의 건
					//String newUrl =  "/multimedia/exform/imagecreate_bulk/";
					String newUrl =  Constant.getString("upload.salesone.image.server.path");
					for (int i = 0; i < copyImg.size(); i++) {					
						
						List<ReUploadImageFileInfo> Files = new ArrayList<ReUploadImageFileInfo>();

						ReUploadImageFileInfo ftpTransferFile = new ReUploadImageFileInfo();
						ftpTransferFile.setDownloadRemotePath(oldUrl);
						ftpTransferFile.setDownloadRemoteFileName(copyImg.get(i).getString("oldFileNm"));
						ftpTransferFile.setUplaodRemoteFilePath(newUrl);
						ftpTransferFile.setUploadRemoteFileName(copyImg.get(i).getString("newFileNm"));
						Files.add(ftpTransferFile);

						//색상이미지등록
						String colorNm  = copyImg.get(i).getString("colorNm");
						String colorVal = copyImg.get(i).getString("colorVal");
						if (colorNm != null && !"".equals(colorNm)){
							ColorByImg pColorByImg = new ColorByImg();
							pColorByImg.setPrdCd(pSupPrdAprvQryCond.getPrdCd());
							pColorByImg.setColorNm(colorNm);
							pColorByImg.setColorVal(colorVal);
							pColorByImg.setImgFileNm(copyImg.get(i).getString("newFileNm"));
							pColorByImg.setSessionObject(prdmMain);       /* 세션정보 */

							List<ColorByImg> pColorByImgList = new ArrayList<ColorByImg>();
							pColorByImgList.add(pColorByImg);
							attrPrdEntity.addColorByImg(pColorByImgList);
						}
						
						//[PD_2020_003_상품이미지이력관리] 상품이미지 신규 등록, 2020.03.17 김영일 시작
						PrdImgChgLog prdImgChgLog = new PrdImgChgLog();
						prdImgChgLog.setPrdCd(pSupPrdAprvQryCond.getPrdCd());
						prdImgChgLog.setCntntFileNm(copyImg.get(i).getString("newFileNm"));
						prdImgChgLog.setChgAftVal(copyImg.get(i).getString("newFileNm"));
						prdImgChgLog.setSessionUserId(prdmMain.getSessionUserId());
						prdImgChgLog.setSessionUserIp(prdmMain.getSessionUserIp());
						//상품이미지변경로그 등록
					    imgEntity.addPrdImgChgLog(prdImgChgLog);
					    //[PD_2020_003_상품이미지이력관리] 상품이미지 신규 등록, 2020.03.17 김영일 끝

						try {
							List<FtpTransferFile> rslt = imgFileUploadProcess.transferImgFilesServer3Server(Files);
							logger.debug("setPrdBase==>rslt" + rslt);
						} catch (DevPrcException e) {
							returnDsData.put("prdStep", "ImageError");
							logger.debug(Message.getMessage("util.msg.006"),e);
						}
					}
					
				}

				// SR02150720061 배너이미지처리
				Cntnt pCntnt = new Cntnt();
				pCntnt.setSupCd(pSupPrdAprvQryCond.getSupCd());
				pCntnt.setSupPrdCd(pSupPrdAprvQryCond.getSuppGoodsCode() );
				//[PD_2020_002_모바일배너이력관리] 상품이력테이블 등록 시 필요
				pCntnt.setSessionUserId(prdmMain.getSessionUserId());
				pCntnt.setSessionUserIp(prdmMain.getSessionUserIp());
				
				EntityDataSet<DSMultiData> bnnrImg = ecScmMngEntity.getPrdWthBnnrImgList(pCntnt);
				int chk = 0; 
				if (bnnrImg != null && bnnrImg.size() > 0) {
					List<ReUploadImageFileInfo> Files = new ArrayList<ReUploadImageFileInfo>();
					List<ReUploadImageFileInfo> FilesImg = new ArrayList<ReUploadImageFileInfo>();
					
					String oldUrl = Constant.getString("upload.sup.image.server.path");
					String newCd = sprdcd.getValues().getBigDecimal("prdCd").toString();
					String newUrl = "/multimedia/image/" + newCd.substring(0, 2) + "/" + newCd.substring(2, 4) + "/";
					String newFileNm = "";
					//이미지 복사 등록시 상품코드 자리수가 8자리이상인 경우  이미지경로 변경.
					if (newCd.length() >= 8) {
						newUrl = newUrl + newCd.substring(4, 6) + "/";
					} 
					for (int i = 0; i < bnnrImg.size(); i++) {
						//pc/mobile인지?
						if (bnnrImg.getValues().get(i).getString("cntntFileNm") != null && !"".equals(bnnrImg.getValues().get(i).getString("cntntFileNm")) ){
							logger.debug("setPrdBase==>bnnrImg cntntFileNm" + bnnrImg.getValues().get(i).getString("cntntFileNm"));
							//[PD_2018_005_GUCCI 입점] 2018.07.05 김성훈 : 원본(확대) 이미지 처리
							if("ENLARGEMENT".equals(bnnrImg.getValues().get(i).getString("cntntTypCd"))) {
								newFileNm = newCd+"_E1_NORESIZE."+"jpg";
							}else if ( "N".equals(bnnrImg.getValues().get(i).getString("cntntTypCd")) ){
								newFileNm = newCd+"_N1."+bnnrImg.getValues().get(i).getString("cntntExtnsNm");
							}else{
								newFileNm = newCd+"_B1."+bnnrImg.getValues().get(i).getString("cntntExtnsNm");
							}
							ReUploadImageFileInfo ftpTransferFile = new ReUploadImageFileInfo();
							ReUploadImageFileInfo ftpTransferFileImg = new ReUploadImageFileInfo();
							ftpTransferFile.setDownloadRemotePath(oldUrl);
							ftpTransferFile.setDownloadRemoteFileName(bnnrImg.getValues().get(i).getString("cntntFileNm"));
							ftpTransferFile.setUplaodRemoteFilePath(newUrl);
							ftpTransferFile.setUploadRemoteFileName(newFileNm);
							Files.add(ftpTransferFile);
							//이미지캐시를 위해
							ftpTransferFileImg.setUplaodRemoteFilePath( newCd.substring(0, 2) + "/" + newCd.substring(2, 4) + "/");
							ftpTransferFileImg.setUploadRemoteFileName(newFileNm);
							FilesImg.add(ftpTransferFileImg);
							chk ++;
							
							//prd_cntnt_d log
							Cntnt cntnt = new Cntnt();
							cntnt.setPrdCd(new BigDecimal(newCd));
							cntnt.setCntntRegGbnCd(bnnrImg.getValues().get(i).getString("cntntTypCd"));
							cntnt.setCntntTypCd(bnnrImg.getValues().get(i).getString("cntntTypCd"));
							cntnt.setCntntNm(newFileNm);
							cntnt.setFileSize(new BigDecimal(0));
							cntnt.setCntntExtnsNm(bnnrImg.getValues().get(i).getString("cntntExtnsNm"));
							cntnt.setCntntGbnCd("img");
							cntnt.setCntntRegGbnCd("10");
							cntnt.setEcOrSiebelYn("E");
							cntnt.setSessionObject(prdmMain);
							imgEntity.addCntnt(cntnt);
							
							//모바일배너 등록 변경이력 생성, [PD_2020_모바일배너이력관리] 2020.02.20 김영일
							if("B".equals(bnnrImg.getValues().get(i).getString("cntntTypCd"))) {								
								PrdColChgLog prdColChgLog = new PrdColChgLog();
								prdColChgLog.setPrdCd(new BigDecimal(newCd));
								prdColChgLog.setChgColNm("MOBILE_BANNER");
								prdColChgLog.setChgAftVal(newFileNm);
								prdColChgLog.setSessionUserId(pCntnt.getSessionUserId());
								prdColChgLog.setSessionUserIp(pCntnt.getSessionUserIp());
								//상품이력 등록
								prdColChgLogEntity.addColChgLog(prdColChgLog);
							}
						}	
						
					}
					
					logger.debug("setPrdBase==>bnnrImg Files" + Files);
					try {
						if(chk > 0 && Files != null && Files.size() > 0 ){
						   List<FtpTransferFile> rslt = imgFileUploadProcess.transferImgFilesServer2Server(Files);
						   logger.debug("setPrdBase==>rslt" + rslt);
						}  
						
					} catch (DevPrcException e) {
						returnDsData.put("prdStep", "ImageBnnrError");
						logger.debug(Message.getMessage("util.msg.006"),e);
					}
					
					
					//이미지캐쉬 호출
					
					/* [GRIT-42210][2022.12.28][백동현]:[세일즈원] CDN - PURGE 서비스 변경에 따른 개발
					 * 신규상품 등록시에는 CDN 퍼지 호출하지 않도록 함
					try {
						if(chk > 0 && FilesImg != null && FilesImg.size() > 0 ){
							List<String> purgeList = new ArrayList<String>();
							for (ReUploadImageFileInfo files : FilesImg) {
								String imgUrl = Constant.getString("prd.img.server.url");
								purgeList.add(imgUrl+files.getUplaodRemoteFilePath()+files.getUploadRemoteFileName());
								
								//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
								//SMTCLogger.infoPrd("http://async.wisen.gscdn.com/rsync?id=1103&files=image.gsshop.com/image/"+files.getUplaodRemoteFilePath()+files.getUploadRemoteFileName());
								//URL url = new URL("http://async.wisen.gscdn.com/rsync?id=1103&files=image.gsshop.com/image/"+files.getUplaodRemoteFilePath()+files.getUploadRemoteFileName());
								
								//@SuppressWarnings("unused")
								//HttpURLConnection http = (HttpURLConnection) url.openConnection();
								
					     	}
							if(purgeList.size() > 0){
								//이미지 퍼지 호출
								cdnPurgeProcess.execPurge(purgeList);
							}
						}	
							
					} catch (Exception e) {
						// TODO Auto-generated catch block
					//	e.printStackTrace();
					}*/
					
					//호출끝
				}
				//SR02150720061 처리끝
				
				// 상품승인정보 반영
				pSupPrdAprvQryCond.setPrdStep(returnDsData.getString("prdStep"));
				pSupPrdAprvQryCond.setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
				ecScmMngEntity.modifySupPrdAprv(pSupPrdAprvQryCond);				
			}
			
			

			// 속성상품승인정보 반영
			// [SR02171027963] 2017-11-17 이용문 : 신버젼 API 연동 대량상품등록 인터페이스 항목 추가의 건 (협력사 속성상품코드 문자입력) 
			// 협력사속성상품코드 잘못 update 되는 로직 수정.
			if (pattrPrdM.size() > 0 &&  !"".equals(StringUtils.NVL(prdmMain.getSupPrdCd())) ) {
				for (int i = 0; i < pattrPrdM.size(); i++) {
					pattrPrdM.get(i).setSupCd(prdmMain.getSupCd());
					pattrPrdM.get(i).setSupPrdCd(prdmMain.getSupPrdCd());
					pattrPrdM.get(i).setSessionObject(prdmMain); /* 세션정보 [PD_2017_017_속도개선][jongil] */
					ecScmMngEntity.modifySupAttrPrdAprv(pattrPrdM.get(i));
				}
			}
		}
		// 2012.06.29 일반기술서가 없고 일반그룹 상품일 경우 AS/반품정보를 등록한다.SR02120627032
/*----- 삭제[S] 상품 승인 저장 / 2013-11-05 / [일반기술서 정보고시 통합] / kimky73 [S] -----//

		logger.debug("일반기술서 승인등록");
		if (prdDesceGenrlDInfoList.size() == 0 && addPrdDescdEaiList.size() == 0 && "GE".equals(prdmMain.getRegChanlGrpCd())) {
			PrdClsQryCond prdClsQryCond = new PrdClsQryCond();
			prdClsQryCond.setPrdClsCd(prdmMain.getPrdClsCd().substring(0, 7));
			prdClsQryCond.setClsLvlNo(new BigDecimal(3));
			EntityDataSet<DSMultiData> PrdDesceGenrlDInfoL = prdClsDescdItmInfoEntity.getPrdClsASDescList(prdClsQryCond);
			//분류별 as/교환정보를 가져온다.
			List<PrdDesceGenrlDInfo> prdDesceGenrlAsInfoList = DevBeanUtils.wrappedListMapToBeans(
					PrdDesceGenrlDInfoL.getValues(), PrdDesceGenrlDInfo.class);

			for (int i = 0; i < prdDesceGenrlAsInfoList.size(); i++) {
				PrdDesceGenrlDInfo prdDesceGenrlDInfo = prdDesceGenrlAsInfoList.get(i);
				prdDesceGenrlDInfo.setItmHiddnYn("1");
				prdDesceGenrlDInfo.setSessionObject(prdmMain);
				prdDesceGenrlAsInfoList.set(i, prdDesceGenrlDInfo);
			}

			addPrdDescdEaiList = this.addPrdDescdASS4c(prdmMain,prdDesceGenrlAsInfoList);

		}
//----- 삭제[E] 상품 승인 저장 / 2013-11-05 / [일반기술서 정보고시 통합] / kimky73 [E] -----*/
		
		/*[S][SR02180109959][2018.01.09][최미영]:네이버가격비교=>아이템정보저장*/
		if (!"".equals(StringUtils.NVL(prdmMain.getSupPrdCd())) ) {			
			if (prdmMain.getItemByPrcCnfYn() == null || "".equals(prdmMain.getItemByPrcCnfYn())) {
				//자동등록 또는 단일 등록 대
				ItemReg pItemStdReg = new ItemReg();
				pItemStdReg.setPrdClsCd(prdmMain.getPrdClsCd());
				pItemStdReg.setBrandCd(prdmMain.getBrandCd());
				pItemStdReg.setSalePrc(prdmMain.getSalePrc());
				EntityDataSet<DSData> itemPrcInfo = null;
				itemPrcInfo = itemCdEntity.getItemSalePrcRegInfo(pItemStdReg);	
				
				pSupPrdAprvQryCond.setItemByPrcCnfYn(itemPrcInfo.getValues().getString("salePrcRegYn"));
				pSupPrdAprvQryCond.setSaveItemCd(itemPrcInfo.getValues().getString("itemCd"));
				pSupPrdAprvQryCond.setItemStdHighPrc(itemPrcInfo.getValues().getString("itemStdHighPrc"));
				pSupPrdAprvQryCond.setItemStdLowPrc(itemPrcInfo.getValues().getString("itemStdLowPrc"));
			} else {				
				pSupPrdAprvQryCond.setItemByPrcCnfYn(StringUtils.NVL(prdmMain.getItemByPrcCnfYn()));
				pSupPrdAprvQryCond.setSaveItemCd(StringUtils.NVL(prdmMain.getSaveItemCd()));
				pSupPrdAprvQryCond.setItemStdHighPrc(StringUtils.NVL(prdmMain.getItemStdHighPrc()));
				pSupPrdAprvQryCond.setItemStdLowPrc(StringUtils.NVL(prdmMain.getItemStdLowPrc()));	
			}
			ecScmMngEntity.modifySupPrdDtl(pSupPrdAprvQryCond);
		}
		/*[E][SR02180109959][2018.01.09][최미영]:네이버가격비교=>아이템정보저장*/
		
		logger.debug("insertok ==>" + insertok);
		logger.debug("prdmMain.getPrchTypCd() ==>" + prdmMain.getPrchTypCd());
		logger.debug("prdmMain.getPrdClsCd() ==>" + prdmMain.getPrdClsCd());
		logger.debug("prdPrdD.getClsChkAftAprvCd() ==>" + prdPrdD.getClsChkAftAprvCd());
		logger.debug("prdPrdD.getClsChkStCd() ==>" + prdPrdD.getClsChkStCd());

	 	//MD팀장합격처리
		//if (!prdmMain.getPrchTypCd().equals("01") && !prdmMain.getPrchTypCd().equals("04")) { // 완전매입상품이 아닌 경우		
		if(!prdmMain.getPrchTypCd().equals("01") ) {
			/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 _487 Start */
			param = new PrdClsUda();
			param.setUdaNo(new BigDecimal( "160" ));
			param.setPrdClsCd(prdmMain.getPrdClsCd());	
			
			//if(!(prdmMain.getPrchTypCd().equals("04") && prdmMain.getPrdClsCd().equals("B67010705"))) {
			if(!(prdmMain.getPrchTypCd().equals("04") && PrdClsUdaUtils.isPrdClsUdaFlag(param))) {
			/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 End */
			 	// ec채널 상품인경우 MD팀장합격처리 ( 분류검증완료 + 검증후 결재상태가 'M'이고 완전매입상품이 아닌 경우
				//if (prdPrdD.getClsChkAftAprvCd().equals("M") && prdPrdD.getClsChkStCd().equals("20") ) {
				if (prdPrdD.getClsChkAftAprvCd() != null && prdPrdD.getClsChkAftAprvCd().equals("M") && prdPrdD.getClsChkStCd().equals("20") ) { // prdPrdD.getClsChkAftAprvCd() != null &&  추가함 isKang 20130114
					//SMTCLogger.writePrd("ec채널 상품인경우 MD팀장합격처리");
					List<MdTeamldrAprvTgt> pMdTeamldrAprvTgtList = new ArrayList<MdTeamldrAprvTgt>();
					MdTeamldrAprvTgt pMdTeamldrAprvTgt = null;
					pMdTeamldrAprvTgt = new MdTeamldrAprvTgt();

					pMdTeamldrAprvTgt.setChk("1");
					pMdTeamldrAprvTgt.setPrdCd(prdmMain.getPrdCd());
					pMdTeamldrAprvTgt.setPrdAprvStCd("30");
					pMdTeamldrAprvTgt.setPrdTypCd("P");
					pMdTeamldrAprvTgt.setPrdAttrGbnCd("P");
					pMdTeamldrAprvTgt.setClsChkAftAprvCd("M");
					pMdTeamldrAprvTgt.setStyleDirEntYn(prdmMain.getStyleDirEntYn());
					pMdTeamldrAprvTgt.setSessionUserId(prdmMain.getSessionUserId());
					
					/* [딜구조개선-패널] 2014-10-29, 김상철, 딜더미상품은 상품구분코드 88로 넘겨 기술서 존재여부 체크 skip 처리  */
					if("88".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))){
						pMdTeamldrAprvTgt.setPrdGbnCd("88");
					}
					/* [딜구조개선-패널] END  */
					
					logger.debug("메인저장시 MD 팀장 승인 prdCd===>"+pMdTeamldrAprvTgt.getPrdCd());
					//SMTCLogger.writePrd("메인저장시 MD 팀장 승인 prdCd===>"+pMdTeamldrAprvTgt.getPrdCd());
					pMdTeamldrAprvTgtList.add(pMdTeamldrAprvTgt);

					ProcRsltInfo procRsltInfo = prdAprvMngProcess.modifyMdTeamldrPassToSm4c(pMdTeamldrAprvTgtList);
					if (!procRsltInfo.getRetCd().equals("S")) {
						returnDsData.put("prdCd", prdmMain.getPrdCd());
						returnDsData.put("retCd", "E");
						returnDsData.put("retMsg", procRsltInfo.getRetMsg());
						returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
						return returnMap;
					}
				}
			}
		}

		// 해외상품저장 (2011/05/02 OSM)
		if (forgnTmPrdMng != null && forgnTmPrdMng.getFrTmSeq() != null) {
			logger.debug("해외상품 저장시작 ====================");
			String frSetGbn = "N" ;
			EntityDataSet<DSData> sFrprdcd = new EntityDataSet<DSData>();
			if ( "Y".equals(forgnTmPrdMng.getForgnSetYn()) && bundlPrdCmposInfoList.size() > 0) {
				frSetGbn = "Y";
			}
			//세트 상품일 경우 해외임시상품코드 채번
			if ("Y".equals(frSetGbn)) {
				sFrprdcd = ecdForgnTmPrdEntity.getFrPrdCd(); // 상품코드 채번
				forgnTmPrdMng.setFrTmSeq(sFrprdcd.getValues().getBigDecimal("frTmSeq"));
			}
			insertok = ecdForgnTmPrdEntity.setForgnPrdBase(prdmMain, prdprcHinsert, forgnTmPrdMng, "I");
			logger.debug("setForgnPrdBase Insert Result ===> " + insertok);

			// 해외세트 처리
			if ("Y".equals(frSetGbn)) {
				for (BundlPrdCmposInfo bundlPrdCmposInfo : bundlPrdCmposInfoList) {
					// 상품코드 채번
					if (bundlPrdCmposInfo.getPrdCd() == null) {
						bundlPrdCmposInfo.setPrdCd(prdmMain.getPrdCd());
						logger.debug("해외상품 세트정보 ====================>" + bundlPrdCmposInfo);
					}
					ecdForgnTmPrdEntity.setForgnPrdSetBase(bundlPrdCmposInfo);
				}
				forgnTmPrdMng.setPrdNm(prdmMain.getPrdNm());
				//해외임시상품코드 등록
				ecdForgnTmPrdEntity.saveForgnPrdTmPrd(forgnTmPrdMng);

				ecdForgnTmPrdEntity.saveForgnPrdTmPrdHis(forgnTmPrdMng);

			}
			//플레인 정보고시항목 등록 처리 2012.10.31
			EntityDataSet<DSMultiData> frTmGovPublsInfoL = ecdForgnTmPrdEntity.getTmGovPublsInfo(forgnTmPrdMng);
			List<PrdGovPubls> frTmGovPublsList = DevBeanUtils.wrappedListMapToBeans(
					frTmGovPublsInfoL.getValues(), PrdGovPubls.class);

			PrdGovPubls prdGovPubls = new PrdGovPubls();
			for (int i = 0; i < frTmGovPublsList.size(); i++) {
				prdGovPubls = frTmGovPublsList.get(i);
				prdGovPubls.setPrdCd(prdmMain.getPrdCd());
				prdGovPubls.setSessionObject(prdmMain);
				prdEntity.modifyPrdGovPublsInfo(prdGovPubls);
			}

			// 위에서 등록한 데이터가 EC에 전송되는 것을 기다리기 위해 3초 대기함.
			try {
				Thread.sleep(3000);
			} catch (Exception ex) {
			}

			// 해외상품 자동매장 프로시져 호출 (2011/06/11 OSM)
			final BigDecimal forgnPrdCd = forgnTmPrdMng.getPrdCd();
			final String sUserId = forgnTmPrdMng.getSessionUserId();
			Map outputMap = this.transactionTemplate.execute(new TransactionCallback<Map>() {
				Map outputMap;
		        @Override
		        public Map doInTransaction(TransactionStatus status) {
		        	outputMap = ecdForgnTmPrdEntity.setFrPrdFrSectAuto(forgnPrdCd, sUserId);
					logger.debug("해외상품 자동매장 Result =>" + outputMap);
					return outputMap;
		        }
			});
			//Map outputMap = ecdForgnTmPrdEntity.setFrPrdFrSectAuto(forgnTmPrdMng.getPrdCd(), forgnTmPrdMng.getSessionUserId());
			if (outputMap == null) {
				//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
				SMTCLogger.infoPrd("해외상품 자동매장 생성실패!! Result is Null!!");
			} else if ("0".equals(outputMap.get("rtnValue").toString())) {
				logger.debug("해외상품 자동매장 생성완료!!");
			} else {
				//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
				SMTCLogger.infoPrd("해외상품 자동매장 생성실패!! result ==> " + outputMap.get("rtnValue"));
			}
			logger.debug("해외상품 저장끝 ====================");
		}


		// 상품승인 해당 상품이고, 검사상품이면 QA사전의뢰를 진행한다. (sap 재구축 2013/02/01 안승훈)
		if (//prdmMain.getPrdAprvDataYn() != null && ("Y").equals(prdmMain.getPrdAprvDataYn()) &&
				StringUtils.NVL(prdPrdD.getClsChkStCd()).equals("20")
				&& !("Q".equals(prdPrdD.getClsChkAftAprvCd()) || "M".equals(prdPrdD.getClsChkAftAprvCd()))) {

			// 결재의뢰 process 호출
			returnDsData = prdQaBefReqProcess.requestMdAprvQaBefReq(prdmMain, pattrPrdM);
			if (returnDsData.getString("retCd").equals("-1")) {
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
		}

		
		if("Y".equals(expAutoClsYn) 
				|| isWithRePrdClsChk ) { // 위드넷에서 분류검증을 통과한 경우 분류검증 자동으로 합격 처리함 [SR02181119924][2018.12.07][이용문] : 상품 분류 검증 기준 변경
			
			/*prdPrdD.setClsChkStCd("20");
			if("Q".equals(prdPrdD.getClsChkAftAprvCd())) {
				prdmMain.setPrdAprvStCd("25");
			}*/
			// 2013-08-30 분류검증자동화
			ClsChkChg pClsChkChg = new ClsChkChg();
			pClsChkChg.setPrdCd(prdmMain.getPrdCd());
			pClsChkChg.setSessionObject(prdmMain);

			this.modifyPrdClsChkAutoPass(pClsChkChg);
			
		}
		
		//[SR02200506079][2020.05.08][김영현]:정기배송 프로세스 3차 개선 관련 개발 요청
		//지정일자배송유형이 정기배송인 경우 SKU정보 생성 
		if(prdPrdD.getApntDtDlvTyp() != null && "R".equals(prdPrdD.getApntDtDlvTyp())){
			//저장 대상 리스트 조회
			PrdprcHinsert prdPrcH = new PrdprcHinsert();
			prdPrcH.setPrdCd(prdmMain.getPrdCd());
			prdPrcH.setSessionObject(prdmMain);
					
			prdAttrPrdSkuMngProcess.saveAttrPrdSkuInfo(prdPrcH);
		}

		//[GRIT-53312]:(내부개선과제) 상품 SMTC->SAP 연동 시 정보 누락 개선 개발
		//상품API(VENDR)의 경우 트랜잭션 처리로 인해 EAI 영역에서 데이터가 조회되지 않음
		//저장 완료 후 EAI 호출 처리 진행함
		if( !"VENDR".equals(prdmMain.getSessionUserId())){
			//SMTCLogger.writePrd("EAI호출 시작------------------------------------------------------------------->");
			// eai 호출로직 시작
			PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
			pPrdEaiPrdSyncInfo.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
			PRD_EAI_Res pRD_EAI_Res = new PRD_EAI_Res();
			// 상품 동기화
			if( //prdmMain.getPrdCd().toString().length() >= 9 || [HANGBOT-28953_GSITMBO-15549][2022.01.10][김진석]:[상품]SAP로 상품코드가 내려오지 않는 현상
					"20".equals(prdmMain.getBundlPrdGbnCd())){
				pPrdEaiPrdSyncInfo.setSapSaleBlockYn("Y");
			}
			pRD_EAI_Res = wsPrdEaiPrdSyncProcess.prdEaiPrdSyncProcess(pPrdEaiPrdSyncInfo);
			
			//[GRIT-47719][2023.01.31][백동현]:[세일즈원] (내부개선과제) 상품 SMTC->SAP 연동 시 정보 누락 이슈
			if(pRD_EAI_Res != null && !pRD_EAI_Res.getResponseResult().isEmpty()){
				PrdMetaDInfo prdMetaDEaiInfo = new PrdMetaDInfo();
				prdMetaDEaiInfo.setPrdCd(sprdcd.getValues().getBigDecimal("prdCd"));
				prdMetaDEaiInfo.setSessionObject(prdmMain);
				prdMetaDEaiInfo.setPrdMetaTypCd("94");
				prdMetaDEaiInfo.setAttrCharVal1(pRD_EAI_Res.getResponseResult());
				prdMetaDEaiInfo.setAttrCharVal2(StringUtil.nvl(pPrdEaiPrdSyncInfo.getSapSaleBlockYn()));
				prdMetaInfoEntity.addPrdMetaD(prdMetaDEaiInfo);
			};
			
			logger.debug("wsPrdEaiPrdSyncProcess val=>" + pRD_EAI_Res.getResponseResult());

			// 속성상품 동기화
			for (int i = 0; i < pattrPrdM.size(); i++) {
				pPrdEaiPrdSyncInfo.setPrdCd(pattrPrdM.get(i).getAttrPrdCd());
				logger.debug("pattrPrdM.get(i).getAttrPrdCd()=>" + pattrPrdM.get(i).getAttrPrdCd());
				// 속성 동기화
				gshseai.GSH_S4C_PRD02.WS.PRD_EAI_Attribute_Product.ws.wsProvider.PRD_EAI_Attribute_Product_P.PRD_EAI_Res resval = wsPrdEaiAttrPrdSyncProcess
						.prdEaiAttrPrdSyncProcess(pPrdEaiPrdSyncInfo);
				logger.debug("resval=>" + resval.getResponseResult());
			}

			//SMTCLogger.writePrd("속성 EAI호출 ");
			// 상품가격 eai 인터페이스

			List<PrdEaiPrcSyncInfo> pPrdEaiPrcSyncInfoList = new ArrayList<PrdEaiPrcSyncInfo>();
			PrdEaiPrcSyncInfo pPrdEaiPrcSyncInfoID = null;

			pPrdEaiPrcSyncInfoID = new PrdEaiPrcSyncInfo();
			pPrdEaiPrcSyncInfoID.setJobTyp("I");
			pPrdEaiPrcSyncInfoID.setPrdCd(prdprcHinsert.getPrdCd());
			pPrdEaiPrcSyncInfoID.setPrdAttrGbnCd(prdprcHinsert.getPrdAttrGbnCd());
			pPrdEaiPrcSyncInfoID.setValidEndDtm("29991231235959");
			pPrdEaiPrcSyncInfoID.setValidStrDtm(sysdtm);
			pPrdEaiPrcSyncInfoID.setEnterDate(prdprcHinsert.getRegDtm());
			pPrdEaiPrcSyncInfoID.setEnterId(prdprcHinsert.getSessionUserId());
			pPrdEaiPrcSyncInfoID.setModifyDate(prdprcHinsert.getModDtm());
			pPrdEaiPrcSyncInfoID.setModifyId(prdprcHinsert.getSessionUserId());
			pPrdEaiPrcSyncInfoID.setSalePrc(prdprcHinsert.getSalePrc());

			logger.debug("1=>" );
			//경품_업체제공이 아닌경우  경품단가는 null 20110420 jdk
			if ( !prdmMain.getGftTypCd().equals("03")
					&& !prdmMain.getGftTypCd().equals("04")) {
				pPrdEaiPrcSyncInfoID.setSupProprdUprc(null);
			}else{
				pPrdEaiPrcSyncInfoID.setSupProprdUprc(prdprcHinsert.getSupProprdUprc());
			}
			pPrdEaiPrcSyncInfoID.setProprdWthtax(prdprcHinsert.getProprdWthtax());
			pPrdEaiPrcSyncInfoID.setSupGivRtamtCd(prdprcHinsert.getSupGivRtamtCd());
			pPrdEaiPrcSyncInfoID.setPrchPrc(prdprcHinsert.getPrchPrc());
			if ( prdmMain.getPrchTypCd().equals("03") ) {
				if ("02".equals(prdprcHinsert.getSupGivRtamtCd())) {
					pPrdEaiPrcSyncInfoID.setSupGivRtamt(prdprcHinsert.getSupGivRtamt());
				} else {
					pPrdEaiPrcSyncInfoID.setSupGivRtamt(prdprcHinsert.getSupGivRtamt().setScale(0, BigDecimal.ROUND_DOWN));
				}
			}
			pPrdEaiPrcSyncInfoID.setInstlCost(prdprcHinsert.getInstlCost());
			pPrdEaiPrcSyncInfoID.setVipDlvYn(prdprcHinsert.getVipDlvYn());
			pPrdEaiPrcSyncInfoID.setVipDlvStdPrc(prdprcHinsert.getVipDlvStdPrc());
			pPrdEaiPrcSyncInfoID.setOnsitePrdPrc(prdprcHinsert.getOnsitePrdPrc());
			pPrdEaiPrcSyncInfoID.setOnsiteDcPrc(prdprcHinsert.getOnsiteDcPrc());
			pPrdEaiPrcSyncInfoID.setDetrmWeihtVal(prdprcHinsert.getDetrmWeihtVal());
			pPrdEaiPrcSyncInfoID.setOnsiteChrCost(prdprcHinsert.getOnsiteChrCost());
			pPrdEaiPrcSyncInfoID.setWhsCd(prdprcHinsert.getWhsCd());
			pPrdEaiPrcSyncInfoID.setOtherSysTnsYn(prdprcHinsert.getOtherSysTnsYn());
			pPrdEaiPrcSyncInfoID.setNoteCntnt(prdprcHinsert.getNoteCntnt());

			pPrdEaiPrcSyncInfoList.add(pPrdEaiPrcSyncInfoID);

			if (pPrdEaiPrcSyncInfoList.size() > 0) {
				wsPrdEaiPrdPriceRegModProcess.prdEaiPrdPricdRegModProcess(pPrdEaiPrcSyncInfoList);
			}

			// 상품채널정보 eai 호출
			if (prdChanlDinsert != null) {
				List<PrdChanlInfo> modifyPrdChanlInfoPrd = new ArrayList<PrdChanlInfo>();
				List<PrdChanlInfo> removePrdChanlInfoPrd = new ArrayList<PrdChanlInfo>();

				savePrdChanlList(prdChanlDinsert, modifyPrdChanlInfoPrd, removePrdChanlInfoPrd);
			}

			// 상품기술서 EAI 호출
			if( addPrdDescdEaiList.size() > 0 ) {
				addPrdDescdEai( addPrdDescdEaiList );
			}
		}
		

		//수출입상품 등록시 EAI I/F호출 기술서 임의값등록
		if("Y".equals(prdDtrD.getExprtPrdYn()) ){
			//PRD_DESCD_HTML_D 테이블 인서트
			PrdDescdHtmlDInfo prdDescdHtmlDInfo = new PrdDescdHtmlDInfo();
			prdDescdHtmlDInfo.setPrdCd(prdmMain.getPrdCd());
			prdDescdHtmlDInfo.setChanlCd("P");
			prdDescdHtmlDInfo.setRegGbnCd("M");
			prdDescdHtmlDInfo.setSessionUserId(prdmMain.getSessionUserId());
			//PRD_EXPLN_ITM_D테이블 인서트
			PrdDesceGenrlDInfo prdDesceGenrlDInfo = new PrdDesceGenrlDInfo();
			prdDesceGenrlDInfo.setPrdCd(prdmMain.getPrdCd());
			prdDesceGenrlDInfo.setChanlCd("A");
			prdDesceGenrlDInfo.setSessionUserId(prdmMain.getSessionUserId());
			prdDesceGenrlDInfo.setPrdExplnItmNm("수출상품");
			prdDesceGenrlDInfo.setPrdExplnGbnCd("G");
			prdDesceGenrlDInfo.setPrdExplnCntnt("수출상품");
			prdDesceGenrlDInfo.setExposSeq(new BigDecimal("0"));
			prdDesceGenrlDInfo.setExposYn("N");
			prdDesceGenrlDInfo.setGovPublsPrdGrpCd("10");
			prdDesceGenrlDInfo.setPrdExplnItmCd("10005");
			
			//PRD_DESCD_GENRL_D  테이블 인서트
			PrdDescd prdDescd = new PrdDescd();
			prdDescd.setPrdCd(prdmMain.getPrdCd());
			prdDescd.setChanlCd("A");
			prdDescd.setDescdItmSeq(new BigDecimal("1"));
			prdDescd.setSessionUserId(prdmMain.getSessionUserId());
			prdDescd.setDescdExplnCntnt("수출상품");
			prdDescd.setDescdItmCd("10040");
			prdDescd.setDescdItmNm("수출상품");
			prdDescd.setSortSeq(new BigDecimal("0"));
			
			
			prdGenrlDescdEntity.addGenrlDescd(prdDescd);
			
			prdExplnEntity.setPrdDescdGenrlDNew(prdDesceGenrlDInfo);
			//상품기술서 html 테이블 입력
			//System.out.println("\n\n chk here prdDescdHtmlDInfo="+ prdDescdHtmlDInfo.toString());
			logger.debug("prdDescdHtmlDInfo="+ prdDescdHtmlDInfo.toString());
			prdDescdHtmlDEntity.setPrdDescdHtmlD(prdDescdHtmlDInfo);
			
			exprtPrdRegEai(sprdcd.getValues().getBigDecimal("prdCd").toString());
			
			if("30".equals(prdmMain.getPrdAprvStCd() ) ){
				prdPrdD.setClsChkStCd("20");
				prdEntity.setUpdatePrdPrdD(prdPrdD);
			}
			
		}
		
	 	//완전매입상품인 경우 메시지 처리
		//if (prdmMain.getPrchTypCd().equals("01") || prdmMain.getPrchTypCd().equals("04")) { // 완전매입상품인경우
		/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 _488 Start */
		param = new PrdClsUda();
		param.setUdaNo(new BigDecimal( "160" ));
		param.setPrdClsCd(prdmMain.getPrdClsCd());		
		
		//if(prdmMain.getPrchTypCd().equals("01") || (prdmMain.getPrchTypCd().equals("04") && prdmMain.getPrdClsCd().equals("B67010705"))) {
		if(prdmMain.getPrchTypCd().equals("01") || (prdmMain.getPrchTypCd().equals("04") && PrdClsUdaUtils.isPrdClsUdaFlag(param))) {
		/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 End */
			/* SR02140728068 2014.09.29  완전매입 미승인 도입*/
			if(!"1".equals(prdmMain.getPrchTypAprvCd()) && !"30".equals(prdmMain.getPrdAprvStCd())){
				returnDsData.put("prdCd", prdmMain.getPrdCd());
				returnDsData.put("retCd", "0");
				returnDsData.put("retMsg", Message.getMessage("prd.msg.024"));
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}			
		}

		//주문제작요청사유내용 저장. 2013.09.05 SR02130902043
		if (!StringUtils.NVL(prdmMain.getOrdMnfcReqCntnt()).equals("")) {

			PrdNoteDInfo prdNoteDInfo = new PrdNoteDInfo();
			prdNoteDInfo.setSessionObject(prdmMain);
			prdNoteDInfo.setPrdCd(prdmMain.getPrdCd());
			prdNoteDInfo.setNoteTypCd("OMR");
			prdNoteDInfo.setNoteCntnt1(prdmMain.getOrdMnfcReqCntnt());
			logger.debug("prdNoteDInfo==>"+prdNoteDInfo);
			prdEntity.setPrdReportMstInfo(prdNoteDInfo);
		}
		
		//[SR02160118172][2016.03.15][백동현]:패밀리세일을 위한 상품코드 복사등록시 요청사항
		//슈퍼/더블쿠폰 제한 로직 등록
		if(prdmMain.getVipCpnLimitPrdYn() != null && prdmMain.getVipCpnLimitPrdYn().equals("Y")){
			CpnLimitPrd cpnLimitPrd = new CpnLimitPrd();
			cpnLimitPrd.setPrdCd(prdmMain.getPrdCd());
			cpnLimitPrd.setCpnGbnCd("V");
			cpnLimitPrd.setValidStrDtm(sysdtm);
			cpnLimitPrd.setValidEndDtm("29991231235959");
			cpnLimitPrd.setSessionUserId(prdmMain.getSessionUserId());
			cpnLimitPrd.setUseYn("Y");
			cpnLimitPrd.setCpnLimitAprvStCd("20");		//승인완료
			cpnLimitPrd.setCpnLimitAprvReqCntnt("");	//승인요청내용
			logger.debug("cpnLimitPrd==>"+cpnLimitPrd);
			cpnApplyPrdEntity.addCpnLimitPrdList(cpnLimitPrd);
		}
		
		if(prdmMain.getMontrExcepPrdYn() != null && prdmMain.getMontrExcepPrdYn().equals("Y")){
			MontrExcpt montrExcptInfo = new MontrExcpt();
			montrExcptInfo.setConctNo(prdmMain.getPrdCd());
			montrExcptInfo.setMontrnExcptTgtGbnCd("10");
			montrExcptInfo.setValidStrDtm(sysdtm);
			montrExcptInfo.setValidEndDtm("29991231235959");
			montrExcptInfo.setSessionUserId(prdmMain.getSessionUserId());
			montrExcptInfo.setMontrnExcptAprvReqCntnt("임직원상품 신규등록");
			
			montrExcptInfo.setMontrnGbnCd("10");	//적자상품 예외등록
			logger.debug("montrExcptInfo==>"+montrExcptInfo);		
			prdMontrnExcptEntity.addMontrnExcptList(montrExcptInfo);
			
			montrExcptInfo.setMontrnGbnCd("20"); //판매량급증 예외등록
			prdMontrnExcptEntity.addMontrnExcptList(montrExcptInfo);
		}
		
		//[SR02160630101][2016.0629][김영현]:편의점반품 설정 로직 추가
		/*
		1) 상품분류별(대,중,소) 편의점 반품여부 → 현행유지 
   		2) 제외대상 로직 제외 및 수정 → 다,라 : 조건에서 제외 , 마 : 조건수정
       		가. 해외상품
       		나. 직송이면서 합포장 상품
       		마. 가격 50만원 초과 또는 총무게 5kg 초과 또는 총길이 120cm초과 ▶ 조건변경 가격 50만원 초과 또는 총무게 20kg초과 또는 총길이 160cm초과 
       		바. 묶음배송여부가 불가능/불가능(배송비상품수만큼결제)인 경우
       		사. 입고택배(의류)-택배 1100, 입고택배(의류)-직반 1400, 직송(설치)-업체 3100, 원재료/나석/매장상품 5000 편의점반품불가 
		 */
		//일반 상품이면서 해외상품이 아닌 경우에 처리 -- 세일즈원, 위드넷, API 공통 처리
		if ((forgnTmPrdMng == null || forgnTmPrdMng.getFrTmSeq() == null) 
				&& "00".equals(StringUtils.NVL(prdmMain.getGftTypCd())) 
					&& !"88".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))) {
			prdEntity.modifyCvsDlvsRtpYn(prdmMain);			
		}
		
		//[S][PD_2017_001_여행프로세스개선][2017.03.02][최미영] : 여행정보 복사
		//if (prdmMain.getCopyPrdYn() != null && prdmMain.getCopyPrdYn().equals("Y")) {
		if (prdmMain.getCopySrcPrdCd() != null) {//getCopyPrdYn 이것 비교는 문제 있음
			PrdMetaDInfo travlPrdMetaDInfo = ecScmMngEntity.getTravlPrdMetaDInfo(prdmMain);
			//logger.debug("travlPrdMetaDInfo :" +travlPrdMetaDInfo);
		    //메타정보를 등록한다.	
		    if (travlPrdMetaDInfo!=null){
		        travlPrdMetaDInfo.setSessionUserId(prdmMain.getSessionUserId());
		        prdMetaInfoEntity.addPrdMetaD(travlPrdMetaDInfo);
		    }
		    //복사정보를 등록한다.
		    prdEntity.insertPrdCopyD(prdmMain);
		}
		//[E][PD_2017_001_여행프로세스개선][2017.03.02][최미영] : 여행정보 복사
		
		//SMTCLogger.writePrd("저장성공"+sprdcd.getValues().getBigDecimal("prdCd").toString());
		returnDsData.put("prdCd", sprdcd.getValues().getBigDecimal("prdCd").toString());
		returnDsData.put("retCd", "0");
		returnDsData.put("retMsg", Message.getMessage("prd.esb.msg.006", new String[]{"상품"}) + noLargeImgCopyMsg + noBannerImgCopyMsg);
		returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
		return returnMap;
	}
	
	/**
	 * <pre>
	 * 상품분류 추천분류여부 체크. [SR02190227985] 
	 * </pre>
	 *
	 * @author ymlee
	 * @date 2019. 3. 11.
	 * @param prdmMain
	 * @return
	 * @throws Exception
	 */
	private boolean isRePrdClsChk(PrdmMain prdmMain) throws Exception {
		
		boolean returnValue = false;
		
		SupPrdCdQryCond pSupPrdCdQryCond = new SupPrdCdQryCond();
		pSupPrdCdQryCond.setSupCd(prdmMain.getSupCd().toString());
		pSupPrdCdQryCond.setSuppGoodsCode(StringUtils.NVL(prdmMain.getSupPrdCd()));
		pSupPrdCdQryCond.setSupPrdCd(StringUtils.NVL(prdmMain.getSupPrdCd()));
		
		// 협력사 상품기본조회
		EntityDataSet<DSMultiData> supPrdBase = ecScmMngEntity.getSupPrdBase(pSupPrdCdQryCond); 

		if (supPrdBase != null 
				&& supPrdBase.getValues() != null 
				&& supPrdBase.getValues().size() > 0) {

			DSData supPrdInfo = supPrdBase.getValues().getDSData(0);
			
			String clsChkAutoCond = StringUtils.NVL(prdmMain.getPrdNm()) + " ";	//상품명
			clsChkAutoCond += StringUtils.NVL(supPrdInfo.getString("brandNm")) + " ";		//브랜드명
			clsChkAutoCond += StringUtils.NVL(prdmMain.getMnfcCoNm()) + " ";	//제조사명
			clsChkAutoCond += StringUtils.NVL(supPrdInfo.getString("prdBaseCmposCntnt")) + " ";	//기본구성정보
			clsChkAutoCond += StringUtils.NVL(supPrdInfo.getString("prdAddCmposCntnt")) + " ";	//추가구성정보

			
			//분류검증 상품분류 목록 조회
			int autoCnt = 0;
			CmmCdQryCond pCmmCdQryCond = new CmmCdQryCond(); 
			pCmmCdQryCond.setCmmGrpCd("PRD142");
			EntityDataSet<DSMultiData> autoPrdClsExplnTgtCdList = cmmCdEntity.getCdList(pCmmCdQryCond);
			if( autoPrdClsExplnTgtCdList.getValues() != null  && autoPrdClsExplnTgtCdList.getValues().size() > 0) {
				autoCnt = autoPrdClsExplnTgtCdList.getValues().size();
			}

			//정보 고시 정보 조회
			List<PrdGovPublsList> supGovPublsList = DevBeanUtils.wrappedListMapToBeans(ecScmMngEntity.getSupPrdGovPublsListNew(pSupPrdCdQryCond).getValues(), PrdGovPublsList.class);
			
			String govPublsCntnt = "";
			for (int i = 0; i < supGovPublsList.size(); i++) {
				for (int k = 0; k < autoCnt; k++){
					if ("G".equals(supGovPublsList.get(i).getPrdExplnGbnCd()) 
							&& autoPrdClsExplnTgtCdList.getValues().getString(k, "cmmCd").equals(supGovPublsList.get(i).getGovPublsItmCd())) {
						govPublsCntnt += StringUtils.NVL(supGovPublsList.get(i).getGovPublsItmNm()) + ":";
						govPublsCntnt += StringUtils.NVL(supGovPublsList.get(i).getGovPublsItmCntnt()) + " ";
					}
				}
			}
//			logger.info("[isRePrdClsChk] clsChkAutoCond (1) : " + clsChkAutoCond);
			if (!StringUtils.isEmpty(govPublsCntnt)) {
				clsChkAutoCond += govPublsCntnt;
			}
//			logger.info("[isRePrdClsChk] govPublsCntnt : " + govPublsCntnt);
//			logger.info("[isRePrdClsChk] clsChkAutoCond (2) : " + clsChkAutoCond);
			//분류기 태우기
		    PrdQryCond pPrdQryCond = new PrdQryCond();
		    pPrdQryCond.setClsChkAutoCond(clsChkAutoCond);			//직접 data셋팅
		    
		    //아래 로직 변경처리 [SR02190411725] 2019.04.12 이용문 : 상품분류 솔루션_자동승인 기준 확대
		    returnValue = prdAprvMngProcess.getPrdClsChkAutoPassYn(pPrdQryCond, prdmMain.getPrdClsCd());
//		    DSMultiData returnPrdClsChkList = prdAprvMngProcess.getPrdClsChkAutoPassList(pPrdQryCond);
//		    //추천분류에 상품분류가 맞을 경우 자동 합격처리
//		    for( DSData rePrdClsChkInfo : returnPrdClsChkList ) {
//		    	if( prdmMain.getPrdClsCd().equals(rePrdClsChkInfo.getString("reCommPrdClsCd")) ) {
//		    		returnValue = true;
//		    		break;
//		    	}
//		    }
		    
//		}else{
//			logger.info("[isRePrdClsChk] 위드넷 상품기본정보 없음.");
		}
		return returnValue;
	}

	public PRD_EXP_Res exprtPrdUpdate(String prdCd){
		return exprtPrdRegEai(prdCd);
	}

	private PRD_EXP_Res exprtPrdRegEai(String prdCd) {
		PRD_EXP_Res res = new PRD_EXP_Res();
		PRD_EXP_Req req = new PRD_EXP_Req();
		PRD_EXP_Request request = new PRD_EXP_Request();
		PRD_EXP_Internal_Product_P_PortType dtrWmsShipOrderCancelTargetTransPPorttype = new PRD_EXP_Internal_Product_P_PortTypeProxy();
		
		try {

			request.setPRD_CD(prdCd);
			

			req.setSender_ID("PRD");
			req.setReceiver_ID("EXP");
			req.setService_ID("PRD_EXP_Internal_Product");
			req.setTransactionDate(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
			req.setPRD_EXP_Request(request);

			res = dtrWmsShipOrderCancelTargetTransPPorttype.mainProcess(req);



			logger.debug("##### Web SERVICE CALL END RESULT :: " + res.getSender_ID() + " :: END");
			logger.debug("##### Web SERVICE CALL END RESULT :: " + res.getReceiver_ID() + " :: END");
			logger.debug("##### Web SERVICE CALL END RESULT :: " + res.getResponseMessage() + " :: END");
			logger.debug("##### Web SERVICE CALL END RESULT :: " + res.getResponseResult() + " :: END");			
		} catch (Exception e) {
			res.setResponseResult("E");
			//res.setResponseMessage(e.toString());
			res.setResponseMessage("수출입 상품 등록/수정 중 오류가 발생하였습니다.");
			//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
			SMTCLogger.errorPrd("수출입 상품 등록/수정 중 오류가 발생하였습니다.", e);
			
		}finally{
		}
		return res;
	}

	// 해외상품 세트가 추가된 상품정보 저장 메소드 (2011/11/04 OSM)  : sap 재구축 (2013/01/17 안승훈)
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> addPrdBaseInfoList(PrdmMain prdmMain, // 상품마스터
	        List<PrdAttrPrdMinsert> pattrPrdM, // 상품속성
	        List<PrdStockDinsert> prdAttrPrdm, // 재고속성
	        PrdprcHinsert prdprcHinsert, // 상품가격
	        List<PrdChanlInfo> prdChanlDinsert, // 상품채널
	        List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD, // 상품주문가능수량
	        List<PrdChanlMappnDinsert> prdChanlMappnD, // 채널매핑정보
	        PrdPrdDinsert prdPrdD, // 상품확장정보
	        List<PrdNmChgHinsert> prdNmChg, // 상품명변경
	        List<EcdSectPrdlstInfo> ecdSectPrdlstInfo, // 카테고리매장
	        SafeCertPrd psafeCertPrd, // 인증정보
	        List<PrdSchdDInfo> prdSchdDInfo, // 상품예정정보
	        List<PrdNumvalDinfo> prdNumvalDinfo, // 상품수치정보
	        List<PrdSpecVal> prdSpecInfo, // 상품사양정보
	        List<PrdUdaDtl> prdUdaDtl, // 상품UDA상세
	        PrdNtcInfo prdNtcInfo, // 상품공지
	        List<PrdMetaDInfo> prdMetaDInfo ,// 상품메타정보
	        List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList, // 상품기술서 정보
	        List<PrdDescdHtmlDInfo> prdDescdHtmlDInfoList, //상품 HTML 기술서 정보
	        ForgnTmPrdMng forgnTmPrdMng, // 해외상품
	        List<BundlPrdCmposInfo> bundlPrdCmposInfoList // 해외상품세트 구성품
	        ) throws Exception {

		// 물류확장 기본 정보 객체 선언
		PrdDtrD prdDtrD = new PrdDtrD();
		
		/*[직송관리대행] 직송관리대행서비스구축ㅣ 2015.07 패널프로젝트 김효석(hs_style) Start*/
		prdDtrD.setDirdlvMngAgncyYn(prdmMain.getDirdlvMngAgncyYn());
		prdDtrD.setBundlDlvPsblQty(prdmMain.getBundlDlvPsblQty());
		if("1".equals(prdDtrD.getDirdlvMngAgncyYn())) prdDtrD.setDirdlvMngAgncyYn("Y");
		/*[직송관리대행] 직송관리대행서비스구축ㅣ 2015.07 패널프로젝트 김효석(hs_style) End*/
		
		return addPrdBaseInfoList(prdmMain, pattrPrdM, prdAttrPrdm, prdprcHinsert,
		        prdChanlDinsert, prdOrdPsblQtyD, prdChanlMappnD, prdPrdD, prdNmChg, ecdSectPrdlstInfo, psafeCertPrd,
		        prdSchdDInfo, prdNumvalDinfo, prdSpecInfo, prdUdaDtl, prdNtcInfo, prdMetaDInfo, prdDesceGenrlDInfoList, prdDescdHtmlDInfoList,
		        forgnTmPrdMng, bundlPrdCmposInfoList, prdDtrD);
	}


	// 해외상품 추가된 상품정보 저장 메소드 (2011/05/02 OSM)
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> addPrdBaseInfoList(PrdmMain prdmMain, // 상품마스터
	        List<PrdAttrPrdMinsert> pattrPrdM, // 상품속성
	        List<PrdStockDinsert> prdAttrPrdm, // 재고속성
	        PrdprcHinsert prdprcHinsert, // 상품가격
	        List<PrdChanlInfo> prdChanlDinsert, // 상품채널
	        List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD, // 상품주문가능수량
	        List<PrdChanlMappnDinsert> prdChanlMappnD, // 채널매핑정보
	        PrdPrdDinsert prdPrdD, // 상품확장정보
	        List<PrdNmChgHinsert> prdNmChg, // 상품명변경
	        List<EcdSectPrdlstInfo> ecdSectPrdlstInfo, // 카테고리매장
	        SafeCertPrd psafeCertPrd, // 인증정보
	        List<PrdSchdDInfo> prdSchdDInfo, // 상품예정정보
	        List<PrdNumvalDinfo> prdNumvalDinfo, // 상품수치정보
	        List<PrdSpecVal> prdSpecInfo, // 상품사양정보
	        List<PrdUdaDtl> prdUdaDtl, // 상품UDA상세
	        PrdNtcInfo prdNtcInfo, // 상품공지
	        List<PrdMetaDInfo> prdMetaDInfo ,// 상품메타정보
	        List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList, // 상품기술서 정보
	        List<PrdDescdHtmlDInfo> prdDescdHtmlDInfoList, //상품 HTML 기술서 정보
	        ForgnTmPrdMng forgnTmPrdMng // 해외상품
	) throws Exception {
		List<BundlPrdCmposInfo> bundlPrdCmposInfoInfoList = new ArrayList<BundlPrdCmposInfo>();
		return addPrdBaseInfoList(prdmMain, pattrPrdM, prdAttrPrdm, prdprcHinsert,
		        prdChanlDinsert, prdOrdPsblQtyD, prdChanlMappnD, prdPrdD, prdNmChg, ecdSectPrdlstInfo, psafeCertPrd,
		        prdSchdDInfo, prdNumvalDinfo, prdSpecInfo, prdUdaDtl, prdNtcInfo, prdMetaDInfo, prdDesceGenrlDInfoList, prdDescdHtmlDInfoList,
		        forgnTmPrdMng, bundlPrdCmposInfoInfoList);
	}

	// 기존 상품정보 저장 메소드
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> addPrdBaseInfoList(PrdmMain prdmMain, // 상품마스터
	        List<PrdAttrPrdMinsert> pattrPrdM, // 상품속성
	        List<PrdStockDinsert> prdAttrPrdm, // 재고속성
	        PrdprcHinsert prdprcHinsert, // 상품가격
	        List<PrdChanlInfo> prdChanlDinsert, // 상품채널
	        List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD, // 상품주문가능수량
	        List<PrdChanlMappnDinsert> prdChanlMappnD, // 채널매핑정보
	        PrdPrdDinsert prdPrdD, // 상품확장정보
	        List<PrdNmChgHinsert> prdNmChg, // 상품명변경
	        List<EcdSectPrdlstInfo> ecdSectPrdlstInfo, // 카테고리매장
	        SafeCertPrd psafeCertPrd, // 인증정보
	        List<PrdSchdDInfo> prdSchdDInfo, // 상품예정정보
	        List<PrdNumvalDinfo> prdNumvalDinfo, // 상품수치정보
	        List<PrdSpecVal> prdSpecInfo, // 상품사양정보
	        List<PrdUdaDtl> prdUdaDtl, // 상품UDA상세
	        PrdNtcInfo prdNtcInfo, // 상품공지
	        List<PrdMetaDInfo> prdMetaDInfo ,// 상품메타정보
	        List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList, // 상품기술서 정보
	        List<PrdDescdHtmlDInfo> prdDescdHtmlDInfoList //상품 HTML 기술서 정보
	) throws Exception {
		ForgnTmPrdMng forgnTmPrdMng = null; // 해외상품
		
		//[S][패널 2015_014_제도개선 변경]김영준(kyjhouse) 2015-09-14
		//상품의 계약서 관련 정보 생성(상품등록주체, 프로모션합의여부)
		PrdInfoSupAprv agreeDocInfoCond = new PrdInfoSupAprv();
		agreeDocInfoCond.setRegSubjCd("SUP");
		agreeDocInfoCond.setSupCd(prdmMain.getSupCd().toString());
		agreeDocInfoCond.setSupPrdCd(prdmMain.getSupPrdCd());
		//합의서정보조회 MAX(agreeDocNo)
		DSData prdSaleMaxAgreeDocNoInfo = prdAgreeDocEntity.getPrdSaleMaxAgreeDocNoInfo(agreeDocInfoCond);
		
		PrdMetaDInfo prdMetaD = new PrdMetaDInfo();
		prdMetaD.setPrdMetaTypCd("70");
		prdMetaD.setAttrCharVal1("SUP");
		prdMetaD.setAttrCharVal2(StringUtils.NVL(prdSaleMaxAgreeDocNoInfo.getString("agreeDocNo"), ""));
		prdMetaD.setAttrCharVal3("N");
		prdMetaD.setSessionObject(prdmMain);
		prdMetaDInfo.add(prdMetaD);
		//[E][패널 2015_014_제도개선 변경]김영준(kyjhouse) 2015-09-14
		
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		
		returnMap = addPrdBaseInfoList(prdmMain, pattrPrdM, prdAttrPrdm, prdprcHinsert,
		        prdChanlDinsert, prdOrdPsblQtyD, prdChanlMappnD, prdPrdD, prdNmChg, ecdSectPrdlstInfo, psafeCertPrd,
		        prdSchdDInfo, prdNumvalDinfo, prdSpecInfo, prdUdaDtl, prdNtcInfo, prdMetaDInfo, prdDesceGenrlDInfoList, prdDescdHtmlDInfoList,
		        forgnTmPrdMng);
		
		DSData dsata = (DSData) returnMap.get("outSavePrdBase").getValues();
		
		//[S][패널 2015_014_제도개선 변경]김영준(kyjhouse) 2015-10-05
		PrdInfoSupAprv pmoSupAprvInfo = new PrdInfoSupAprv();
		pmoSupAprvInfo.setSessionUserId("WITH");
		pmoSupAprvInfo.setPrdCd(dsata.getString("prdCd"));
		pmoSupAprvInfo.setSubSupCd(prdmMain.getSubSupCd());
		pmoSupAprvInfo.setQryGbn("MP");
		pmoSupAprvInfo.setRegSubjCd("SUP");
		pmoSupAprvInfo.setSupCd(prdmMain.getSupCd().toString());
		pmoSupAprvInfo.setSupPrdCd(prdmMain.getSupPrdCd());
		//위수탁거래합의서 상세 수정
		prdAgreeDocEntity.modifyPrdSaleAgreeDocD(pmoSupAprvInfo);
		//[E][패널 2015_014_제도개선 변경]김영준(kyjhouse) 2015-10-05
		
		// [SR02170125085][2017.02.21][전영준]:[M상품운영파트] 화장품 전성분 관련 시스템 개발요청(0125)
		if (dsata.getString("prdCd") != null && dsata.getString("prdCd").length() > 0) {
			
			// MD승인시 상품승인검사필요서류결과정보 상품코드 누락 수정 - 20170220. yjjeon
			PaiInfo paiInfo = new PaiInfo();
			paiInfo.setQaPrdCd(prdmMain.getPrdCd().toString());
			paiInfo.setSessionUserId(prdmMain.getSessionUserId());
			paiInfoEntity.modifyQaMandDocPrdCd(paiInfo);
			
			//쉐어플랙스 테이블 상품 코드 업데이트
	 	    QaExposAtachFile qaExposAtachFile = new QaExposAtachFile();
			qaExposAtachFile.setSessionUserId(prdmMain.getSessionUserId());
			qaExposAtachFile.setPrdCd(prdmMain.getPrdCd().toString()); 
			qaExposAtachFile.setSupCd(prdmMain.getSupCd().toString());
			qaExposAtachFile.setSupPrdCd(prdmMain.getSupPrdCd());
			qaAtachFileConctInfoEntity.updateQaExposAtachFilePrdcd(qaExposAtachFile);
		}

		
		return returnMap;
		
		
	}

	// 협력사 일반기술서 셋팅 메소드 추가함 isKang
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : addSupPrdDescdS4c
	 */
//	public List<PrdDescdSyncInfo> addSupPrdDescdS4c( PrdmMain prdmMain, List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList) {
//		List<PrdDescdSyncInfo> prdDescdSyncInfoList = new ArrayList<PrdDescdSyncInfo>();
//		PrdDesceGenrlDInfo prdDesceGenrlDInfo1  = new PrdDesceGenrlDInfo(); // 심의요청 용
//		PrdDescdHtmlDInfo  prdDescdHtmlDInfo  = new PrdDescdHtmlDInfo();
//		EntityDataSet<DSData> entityDataSet = null;
//
//		// 일반기술서 승인 등록
//		logger.debug("qryGbn==="+prdmMain.getQryGbn());
//		if("Y".equals(StringUtils.NVL(prdmMain.getQryGbn()))) {
//			// 기술서를 조회
//			SupPrdAprvQryCond pSupPrdAprvQryCond = new SupPrdAprvQryCond();
//			pSupPrdAprvQryCond.setPrdCd(prdmMain.getPrdCd());
//			pSupPrdAprvQryCond.setSupCd(prdmMain.getSupCd());
//			pSupPrdAprvQryCond.setSupPrdCd(prdmMain.getSupPrdCd());
//
//			pSupPrdAprvQryCond.setSuppGoodsCode(prdmMain.getSupPrdCd());
//
//			DSMultiData copyDesc = ecScmMngEntity.getSupPrdAprvDescdGenrlDList(pSupPrdAprvQryCond).getValues();
//
//			if (copyDesc != null) {
//				for (int i = 0; i < copyDesc.size(); i++) {
//					PrdDesceGenrlDInfo prdDesceGenrlDInfo = new PrdDesceGenrlDInfo();
//					// 상품기술서 정보를 INSERT -> 시퀀스 채번 후 입력방식으로 변경 (2011/03/26 OSM)
//					entityDataSet = prdEntity.getDescdItmSeq();
//					prdDesceGenrlDInfo.setDescdItmSeq(entityDataSet.getValues().getBigDecimal("descdItmSeq"));	//기술서항목순번
//					prdDesceGenrlDInfo.setPrdCd(prdmMain.getPrdCd()); 											// 상품코드
//					prdDesceGenrlDInfo.setChanlCd(copyDesc.get(i).getString("chanlCd")); 						// 채널코드
//					prdDesceGenrlDInfo.setDescdItmNm(copyDesc.get(i).getString("descdItmNm"));	 				// 기술서항목명
//					prdDesceGenrlDInfo.setDescdExplnCntnt(copyDesc.get(i).getString("descdExplnCntnt")); 		// 기술서설명내용
//					prdDesceGenrlDInfo.setDescdItmCd(copyDesc.get(i).getString("descdItmCd")); 					// 기술서항목코드
//					prdDesceGenrlDInfo.setSortSeq(copyDesc.get(i).getBigDecimal("sortSeq")); 					// 정렬순서
//					prdDesceGenrlDInfo.setItmHiddnYn(copyDesc.get(i).getString("itmHiddnYn")); 					// 항목숨김여부
//					prdDesceGenrlDInfo.setWrapYn(copyDesc.get(i).getString("wrapYn")); 							// 줄바꿈여부
//					prdDesceGenrlDInfo.setLineInsertYn(copyDesc.get(i).getString("lineInsertYn")); 				// 줄삽입여부
//					prdDesceGenrlDInfo.setFlckrYn(copyDesc.get(i).getString("flckrYn")); 						// 점멸여부
//					prdDesceGenrlDInfo.setLtrColorNm(copyDesc.get(i).getString("ltrColorNm")); 					// 글자색상명
//					prdDesceGenrlDInfo.setIntrntExposYn(copyDesc.get(i).getString("intrntExposYn")); 			// 인터넷노출여부
//					prdDesceGenrlDInfo.setEaiLinkYn(copyDesc.get(i).getString("eaiLinkYn")); 					// EAI연동여부
//					prdDesceGenrlDInfo.setSessionUserIp(prdmMain.getSessionUserIp());
//					prdDesceGenrlDInfo.setSessionUserId(prdmMain.getSessionUserId());
//					prdDesceGenrlDInfo.setSessionUserNm(prdmMain.getSessionUserNm());
//
//					prdEntity.saveSupPrdDescdGenrlInfo(prdDesceGenrlDInfo);
//				}
//			}
//		} else if("N".equals(StringUtils.NVL(prdmMain.getQryGbn()))) { // 직접받은 데이터
//			// 일반기술서가 비어 있지 않을때
//			if(prdDesceGenrlDInfoList.size() > 0 ) {
//				logger.debug("size>>>>"+prdDesceGenrlDInfoList.size());
//				for(PrdDesceGenrlDInfo prdDesceGenrlDInfo : prdDesceGenrlDInfoList) {
//					prdDesceGenrlDInfo.setPrdCd(prdmMain.getPrdCd());
//
//					// 협력사 일반기술서 저장 추가함. isKang 20130321
//					prdDesceGenrlDInfo.setSupCd(prdmMain.getSupCd());
//					prdDesceGenrlDInfo.setSupPrdCd(prdmMain.getSupPrdCd());
//					// 상품기술서 정보를 INSERT -> 시퀀스 채번 후 입력방식으로 변경 (2011/03/26 OSM)
//					entityDataSet = prdEntity.getDescdItmSeq();
//					prdDesceGenrlDInfo.setDescdItmSeq(entityDataSet.getValues().getBigDecimal("descdItmSeq"));
//
//					prdEntity.saveSupPrdDescdGenrlInfo(prdDesceGenrlDInfo);
//				}
//			}
//		}
//
//		return prdDescdSyncInfoList;
//	}
	// SOURCE_CLEANSING : END
	
	

	/**
	 * <pre>
	 *
	 * desc : 상품품절등록한다.
	 *     1.품절주문등록건수를 조회한다.
	 *     IF (건수 = 0) THEN
	 *      2.품절등록대상을 조회한다.
	 *      IF (품절등록대상.상태코드 = '11') OR (품절등록대상.상태코드 = '12') OR (품절등록대상.상태코드 = '21') THEN
	 *      alert(CMM085,'해당주문상태의 품절') 에러메시지로 에러를 리턴한다.
	 *      END IF
	 *      3.품절등록을 수행한다.
	 *     ELSE
	 *      IF (품절주문등록.상태코드 = '10') THEN
	 *      alert(PRD160,'품절등록') 에러메시지로 에러를 리턴한다.
	 *      ELSE IF (품절주문등록.상태코드 = '40') OR (품절주문등록.상태코드 = '50') THEN
	 *      alert(CMM085,'주문취소/출고확정된 주문의 품절') 에러메시지로 에러를 리턴한다.
	 *      ELSE IF (품절주문등록.상태코드 = '20') THEN
	 *      4.품절수정을 수행한다.
	 *      END IF
	 *     END IF
	 *
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-12-17 07:49:33
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public DSData addPrdOutstk(OutstkTgt outstkTgt) throws DevEntException {

		// 1.품절주문등록건수를 조회한다.
		DSData outstkOrdRegCnt = shtprdOrdEntity.getOutstkOrdRegCnt(outstkTgt);
		DSData outstkRegTgtResult = null;

		if (outstkOrdRegCnt.getInt("regCnt") == 0) {
			// 건수가 없을 경우

			// 2.품절등록대상을 조회한다.
			outstkRegTgtResult = shtprdOrdEntity.getOutstkRegTgt(outstkTgt);

			// 대상이 존재하지 않음
			if (outstkRegTgtResult.size() == 0) {
				// cmm.msg.027=해당 {0}의 {1}가(이) 존재하지 않습니다.
				throw new DevEntException(Message.getMessage("cmm.msg.027", new String[] { "주문번호", "주문" }));
			}

			String sStCd = StringUtils.NVL(outstkRegTgtResult.getString("stCd")); // 품절등록대상.상태코드
			if (sStCd.equals("10") || sStCd.equals("40") || sStCd.equals("50")) {
				// {0} 은/는 입력할 수 없습니다.
				throw new DevEntException(Message.getMessage("cmm.msg.085", new String[] { "해당 주문상태의 품절" }));

			}

			OutstkOrdReg outstkOrdReg = new OutstkOrdReg();

			outstkOrdReg.setOrdItemId(outstkTgt.getOrdItemId());
			outstkOrdReg.setOrdItemNo(outstkRegTgtResult.getBigDecimal("ordItemNo"));
			outstkOrdReg.setOrdNo(outstkRegTgtResult.getBigDecimal("ordNo"));
			outstkOrdReg.setPrdCd(outstkRegTgtResult.getBigDecimal("prdCd"));
			outstkOrdReg.setAttrPrdCd(outstkRegTgtResult.getBigDecimal("attrPrdCd"));
			outstkOrdReg.setSupCd(outstkRegTgtResult.getBigDecimal("supCd"));
			outstkOrdReg.setStCd(outstkRegTgtResult.getString("stCd"));
			outstkOrdReg.setMdId(outstkRegTgtResult.getString("mdId"));
			outstkOrdReg.setChanlCd(outstkRegTgtResult.getString("chanlCd"));
			outstkOrdReg.setSrChanlCd(outstkRegTgtResult.getString("srChanlCd"));
			outstkOrdReg.setAccmGshsShrYn(outstkTgt.getAccmGshsShrYn()); // [PD-2015-007] 품절지연주체
			// session setting
			outstkOrdReg.setSessionObject(outstkTgt);

			// 3.품절등록을 수행한다.
			shtprdOrdEntity.addOutstk(outstkOrdReg);

		} else {
			// 건수가 있을 경우
			String sStCd = StringUtils.NVL(outstkTgt.getStCd());

			if (sStCd.equals("10")) {
				// 이미 처리되어 {0}할수 없습니다.
				throw new DevEntException(Message.getMessage("prd.error.msg.160", new String[] { "품절등록" }));

			} else if (sStCd.equals("40") || sStCd.equals("50")) {
				// {0} 은/는 입력할 수 없습니다.
				throw new DevEntException(Message.getMessage("cmm.msg.085", new String[] { "주문취소/출고확정된 주문의 품절" }));

			} else if (sStCd.equals("20")) {
				// 4.품절수정을 수행한다.
				OutstkOrdReg outstkOrdReg = new OutstkOrdReg();
				outstkOrdReg.setOrdItemId(outstkTgt.getOrdItemId()); // 주문아이템아이디
				// session setting
				outstkOrdReg.setSessionObject(outstkTgt);

				shtprdOrdEntity.modifyOutstk(outstkOrdReg);
			}

		}

		return outstkRegTgtResult;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품품절등록한다.
	 *     1.품절주문등록건수를 조회한다.
	 *     IF (건수 = 0) THEN
	 *      2.품절등록대상을 조회한다.
	 *      IF (품절등록대상.상태코드 = '11') OR (품절등록대상.상태코드 = '12') OR (품절등록대상.상태코드 = '21') THEN
	 *      alert(CMM085,'해당주문상태의 품절') 에러메시지로 에러를 리턴한다.
	 *      END IF
	 *      3.품절등록을 수행한다.
	 *     ELSE
	 *      IF (품절주문등록.상태코드 = '10') THEN
	 *      alert(PRD160,'품절등록') 에러메시지로 에러를 리턴한다.
	 *      ELSE IF (품절주문등록.상태코드 = '40') OR (품절주문등록.상태코드 = '50') THEN
	 *      alert(CMM085,'주문취소/출고확정된 주문의 품절') 에러메시지로 에러를 리턴한다.
	 *      ELSE IF (품절주문등록.상태코드 = '20') THEN
	 *      4.품절수정을 수행한다.
	 *      END IF
	 *     END IF
	 *
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-12-17 07:49:33
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> addPrdOutstk(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

//		String key = "inAddPrdOutstk";

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 수량부족상품결품등록한다.
	 *      1.속성상품재고부족정보조회를 수행한다.
	 *      IF (조회건이 있으면) THEN
	 * 	   1.속성상품재고부족정보수정을 수행한다.
	 * 	   (속성상품재고부족정보.구매시도횟수 = 1)
	 * 	   2.재고정보메일발송수정을 수행한다.
	 *      (메일전송영역코드 = '029', 문자속성값2 = 'E047',
	 * 	               숫자속성값2 = 속성상품코드, 숫자속성값1 = 1 ELSE
	 *       1.속성상품재고부족정보상품목록조회를 수행한다.
	 * 	 (속성상품재고부족정보 : 협력사메일발송여부 = 'N'
	 * 	                  협력사SMS발송여부 = 'N'  MD메일발송여부 = 'N'
	 * 	  MDSMS발송여부 = 'N'
	 * 	                  구매시도횟수 = 1 )
	 * 	  IF (조회건 &gt; 0) THEN
	 * 	         1.속성상품재고부족정보등록을 수행한다.
	 * 	  ELSE  IF (안전재고수량 &gt; 0)
	 * 	  THEN  1. 메일발송MD연락처조회 수행한다.
	 * 	         2. 메일발송협력사연락처조회 수행한다.
	 * 	  ELSE   1. 메일발송MD연락처조회 수행한다.  END IF  IF ( MD이메일주소 &lt;&gt; '' OR MD이메일주소 &lt;&gt; NULL OR 담당자이메일주소 &lt;&gt; '' OR 담당자이메일주소 &lt;&gt; NULL) THEN  1.메일발송등록 수행한다.  IF (저장결과 &gt;= 1) THEN  IF (MD이메일주소 &lt;&gt; '' OR MD이메일주소 &lt;&gt; NULL) THEN  MD메일발송여부 = 'Y'  END IF  IF (담당자이메일주소 &lt;&gt; '' OR 담당자이메일주소 &lt;&gt; NULL) THEN  협력사메일발송여부 = 'Y'  END IF  END IF  END IF  IF ( MD핸드폰번호 &lt;&gt; '' OR MD핸드폰번호 &lt;&gt; NULL OR 담당자핸드폰번호 &lt;&gt; '' OR 담당자핸드폰번호 &lt;&gt; NULL) THEN  1.전송시간을 확인한다.  IF (전송여부 = 'N') THEN   IF (MD핸드폰번호 &lt;&gt; '' OR MD핸드폰번호 &lt;&gt; NULL) THEN  협력사SMS발송여부 = ''  1.속성상품재고부족정보등록을 수행한다.  END IF  IF (담당자핸드폰번호 &lt;&gt; '' OR 담당자핸드폰번호 &lt;&gt; NULL) THEN  MDSMS발송여부 = ''
	 *       1.속성상품재고부족정보등록을 수행한다.  END IF  ELSE  IF (MD핸드폰번호 &lt;&gt; '' OR MD핸드폰번호 &lt;&gt; NULL) THEN  발송메시지내용 = '[GS SHOP]담당상품인 + prdid + 의 재고가 부족하오니 준비요청 바랍니다.'  1.SMS발송등록 수행한다.  IF (저장결과 &gt;= 1) THEN  MDSMS발송여부 = 'Y'  END IF  2.속성상품재고부족정보등록을 수행한다.  END IF  IF (담당자핸드폰번호 &lt;&gt; '' OR 담당자핸드폰번호 &lt;&gt; NULL) THEN  발송메시지내용 = '[GS SHOP]귀사의 + prdid + 의 재고가 부족하오니 준비/입력 바랍니다..'  1.SMS발송등록 수행한다.  IF (저장결과 &gt;= 1) THEN  협력사SMS발송여부 = 'Y'  END IF  2.속성상품재고부족정보등록을 수행한다.  END IF  END IF  END IF  END IF END IF
	 *
	 * [PD_2018_005_Gucci 입점] 2018-07-19, 김철현 : 프리미엄브랜드의 API 조회결과 주문가능수량이 0 인 경우 PRD_ORD_PSBL_QTY_D 테이블도
	 *                0으로 변경한다.(일시품절배치[PRDBT0370]의 수행 대상이 되도록 하기 위하여) 
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG (kbog2089)
	 * @date 2011-01-13 02:27:57
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> addQtyShtPrdShtprd(AttrPrdStockShtInfo pAttrPrdStockShtInfo)
			throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		EntityDataSet<DSData> pGetAttrPrdStockShtInfo = attrPrdStockShtInfoEntity.getAttrPrdStockShtInfo(pAttrPrdStockShtInfo);

		logger.debug("pGetAttrPrdStockShtInfo====" + pGetAttrPrdStockShtInfo);
		if (pGetAttrPrdStockShtInfo.getValues() != null) {

			// 속성상품재고부족정보수정
			pAttrPrdStockShtInfo.setPrchAtempCnt(new BigDecimal("1"));
			attrPrdStockShtInfoEntity.modifyAttrPrdStockShtInfo(pAttrPrdStockShtInfo);

		} else {
			// 속성상품재고부족정보상품목록조회
				pAttrPrdStockShtInfo.setSupMailSndYn("N");
				pAttrPrdStockShtInfo.setSupSmsSndYn("N");
				pAttrPrdStockShtInfo.setMdMailSndYn("N");
				pAttrPrdStockShtInfo.setMdSmsSndYn("N");
				pAttrPrdStockShtInfo.setPrchAtempCnt(new BigDecimal("1"));
				// 속성상품재고부족정보등록
				attrPrdStockShtInfoEntity.addAttrPrdStockShtInfo(pAttrPrdStockShtInfo);

		}
		
		/* [PD_2018_005_Gucci 입점] 2018-07-19, 김철현 : 프리미엄브랜드의 API 조회결과 주문가능수량이 0 인 경우 PRD_ORD_PSBL_QTY_D 테이블도
	       0으로 변경 및 임시테이블/로그테이블 정리한다. */
		if (OrdConstant.PRMM_BRAND_GUCCI_SUP_CD.equals(ObjectUtils.toString(pAttrPrdStockShtInfo.getSupCd()))) {
			prdPrmmBrandMngProcess.modifyPrdOrdPsblQtyZero(pAttrPrdStockShtInfo);
		}
		/* [PD_2018_005_Gucci 입점] END */				
		
		return returnMap;
	}
	/**
	 *
	 * <pre>
	 *
	 * desc : 재고 부족시 메일 발송
	 *
	 * </pre>
	 * @author kbog2089
	 * @date 2011. 5. 16. 오전 11:37:45
	 * @param ordPslyQty
	 * @return
	 * @throws DevEntException
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> sendMailGsStock(OrdPsblQty ordPslyQty) {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		// 이 로직에 상관없이 주문은 되어야 하므로 여기에서 Exception 처리
		try{
			
			//[PD_2018_010 GS슈퍼] 마트협력사 재고부족알림 방지로직 추가 2018-10-31 김태형
			if(isMart(ordPslyQty.getPrdCd()))
				return returnMap;
			
			// 배송형태에 따라 직송 여부
			EntityDataSet<DSMultiData> supMailCntc = null;
			EntityDataSet<DSMultiData> mdMailCntc = null;
			MailSndCntc   pMailSndCntc = new MailSndCntc();
			pMailSndCntc.setPrdCd(ordPslyQty.getPrdCd());
			if("3".equals(ordPslyQty.getDlvPickMthodCd().substring(0,1))) { // 직송여부  협력사
				// 메일발송협력사연락처조회
				supMailCntc = prdEntity.getMailSndSupCntc(pMailSndCntc);
				/******************************************************
				 *  20110617  김지희 대리 요청   직송일 경우는 MD 에게 발송 금지
				 ******************************************************/
				// MD에게도 발송
				//mdMailCntc = prdEntity.getMailSndMdCntc(pMailSndCntc);
			} else {
				if("2".equals(ordPslyQty.getDlvPickMthodCd().substring(0,1))) { // 직택배/집하택배일경우
					// 메일발송협력사연락처조회
					supMailCntc = prdEntity.getMailSndSupCntc(pMailSndCntc);
				}
				// 메일발송MD연락처조회
				mdMailCntc = prdEntity.getMailSndMdCntc(pMailSndCntc);
			}
			List<PrdStockShtMailDInfo> prdStockShtMailDInfoList = new ArrayList<PrdStockShtMailDInfo>();
			/*****************************************************
			 * 20110617  MD일때 협력사 코드 supCd 데이터 보내지 말라
			 *****************************************************/
			// 메일 발송
			if(mdMailCntc != null ) {
				DSMultiData mdMailCntcMultiDs = mdMailCntc.getValues();

				if( mdMailCntcMultiDs != null && mdMailCntcMultiDs.size() > 0){
					for(int i = 0; i < mdMailCntcMultiDs.size(); i++){

						DSData mdMailCntcDs = mdMailCntcMultiDs.get(i);

						// 메일 발송

						if(!"".equals(StringUtils.NVL(mdMailCntcDs.getString("mdEmailAddr")))) {
							PrdStockShtMailDInfo pPrdMailStock = new PrdStockShtMailDInfo();
							pPrdMailStock.setAttrPrdCd(ordPslyQty.getAttrPrdCd());
							pPrdMailStock.setRcvrId(mdMailCntcDs.getString("mdEmailAddr"));
							pPrdMailStock.setPrdCd(ordPslyQty.getPrdCd());
							pPrdMailStock.setMdId(mdMailCntcDs.getString("operMdId"));
							pPrdMailStock.setMrkPrdNm(mdMailCntcDs.getString("prdNm"));
							pPrdMailStock.setRcvrCelphnNo(mdMailCntcDs.getString("mdCelphnNo"));
							pPrdMailStock.setRcvrEmailAddr(mdMailCntcDs.getString("mdEmailAddr"));
							pPrdMailStock.setRcvrNm(mdMailCntcDs.getString("mdNm"));
							pPrdMailStock.setSupCd(null);
							pPrdMailStock.setMailSmsGbnCd("MAIL");// 구분자 넣기 메일
			 				pPrdMailStock.setSessionObject(ordPslyQty);
							prdStockShtMailDInfoList.add(pPrdMailStock);

						}
						// SMS 발송 데이터
						if(!"".equals(StringUtils.NVL(mdMailCntcDs.getString("mdCelphnNo")))) {
							PrdStockShtMailDInfo pPrdMailStock = new PrdStockShtMailDInfo();
							pPrdMailStock.setAttrPrdCd(ordPslyQty.getAttrPrdCd());
							pPrdMailStock.setRcvrId(mdMailCntcDs.getString("mdCelphnNo"));
							pPrdMailStock.setPrdCd(ordPslyQty.getPrdCd());
							pPrdMailStock.setMdId(mdMailCntcDs.getString("operMdId"));
							pPrdMailStock.setMrkPrdNm(mdMailCntcDs.getString("prdNm"));
							pPrdMailStock.setRcvrCelphnNo(mdMailCntcDs.getString("mdCelphnNo"));
							pPrdMailStock.setRcvrEmailAddr(mdMailCntcDs.getString("mdEmailAddr"));
							pPrdMailStock.setRcvrNm(mdMailCntcDs.getString("mdNm"));
							pPrdMailStock.setSupCd(null);
							pPrdMailStock.setMailSmsGbnCd("SMS");// 구분자 넣기 메일
							pPrdMailStock.setSessionObject(ordPslyQty);
							prdStockShtMailDInfoList.add(pPrdMailStock);
						}
					}
				}

			}

			if(supMailCntc!= null ) {

				DSMultiData supMailCntcMultiDs = supMailCntc.getValues();

				if( supMailCntcMultiDs != null && supMailCntcMultiDs.size() > 0){
					for(int i = 0; i < supMailCntcMultiDs.size(); i++){

						DSData supMailCntcDs = supMailCntcMultiDs.get(i);
						//메일용
						if(!"".equals(StringUtils.NVL(supMailCntcDs.getString("asignrEmailAddr")))) {
							PrdStockShtMailDInfo pPrdMailStock1 = new PrdStockShtMailDInfo();
							pPrdMailStock1.setAttrPrdCd(ordPslyQty.getAttrPrdCd());
							pPrdMailStock1.setPrdCd(ordPslyQty.getPrdCd());
							pPrdMailStock1.setRcvrId(supMailCntcDs.getString("asignrEmailAddr"));
							pPrdMailStock1.setMdId(supMailCntcDs.getString("operMdId"));
							pPrdMailStock1.setMrkPrdNm(supMailCntcDs.getString("prdNm"));
							pPrdMailStock1.setRcvrCelphnNo(supMailCntcDs.getString("asignrCelphnNo"));
							pPrdMailStock1.setRcvrEmailAddr(supMailCntcDs.getString("asignrEmailAddr"));
							pPrdMailStock1.setRcvrNm(supMailCntcDs.getString("asigrNm"));
							pPrdMailStock1.setSupCd(supMailCntcDs.getBigDecimal("supCd"));
							pPrdMailStock1.setMailSmsGbnCd("MAIL");// 구분자 넣기 메일
							pPrdMailStock1.setSessionObject(ordPslyQty);
							prdStockShtMailDInfoList.add(pPrdMailStock1);
						}
					    // SMS 용
						if(!"".equals(StringUtils.NVL(supMailCntcDs.getString("asignrCelphnNo")))) {
							PrdStockShtMailDInfo pPrdMailStock1 = new PrdStockShtMailDInfo();
							pPrdMailStock1.setAttrPrdCd(ordPslyQty.getAttrPrdCd());
							pPrdMailStock1.setPrdCd(ordPslyQty.getPrdCd());
							pPrdMailStock1.setRcvrId(supMailCntcDs.getString("asignrCelphnNo"));
							pPrdMailStock1.setMdId(supMailCntcDs.getString("operMdId"));
							pPrdMailStock1.setMrkPrdNm(supMailCntcDs.getString("prdNm"));
							pPrdMailStock1.setRcvrCelphnNo(supMailCntcDs.getString("asignrCelphnNo"));
							pPrdMailStock1.setRcvrEmailAddr(supMailCntcDs.getString("asignrEmailAddr"));
							pPrdMailStock1.setRcvrNm(supMailCntcDs.getString("asigrNm"));
							pPrdMailStock1.setSupCd(supMailCntcDs.getBigDecimal("supCd"));
							pPrdMailStock1.setMailSmsGbnCd("SMS");// 구분자 넣기 메일
							pPrdMailStock1.setSessionObject(ordPslyQty);
							prdStockShtMailDInfoList.add(pPrdMailStock1);
						}
					}
				}

			}
			if(prdStockShtMailDInfoList.size() > 0) {
				attrPrdStockShtInfoEntity.addPrdMailStock(prdStockShtMailDInfoList);
			}
		}catch(DevEntException de) {
//			logger.error(Message.getMessage("prd.msg.377"), de);
		}catch(Exception e) {
//			logger.error(Message.getMessage("prd.msg.377"), e);
		}
		return returnMap;
	}
	
	/**
	 * Description : 상품코드로 마트협력사 여부체크
	 * @author th503
	 * @date : 2018. 10. 31.
	 * @param prdCd
	 */
	public boolean isMart(BigDecimal prdCd) {
		boolean isMart = false;
		try{
			SupUdaCond supUdaCond = new SupUdaCond();
			supUdaCond.setUdaNo(new BigDecimal("73"));
			supUdaCond.setPrdCd(prdCd);
			supUdaCond.setQryGbn("3");
			DSData dsData = supEntity.getSupUdaDUseYn(supUdaCond);
			if(dsData != null && "Y".equals((String)dsData.get("useYn")))
				isMart = true;
		}catch(Exception e){
			//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
			SMTCLogger.errorPrd("====< isMart.ERROR >====", e);
		}
//		logger.info("====< prdCd : " + prdCd + ">====:====< isMart : " + isMart);
		return isMart;
	}
	
	
	
	
	// function BusComp_PreSetFieldValue (FieldName, FieldValue)
	// supDtlInfo 협력사 정보
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : BusComp_PreSetFieldValue
	 */
//	public PrdmMain BusComp_PreSetFieldValue(String FieldName, String FieldValue, PrdmMain prdmMain,
//			SupDtlInfo supDtlInfo, PrdprcHinsert prdprcHinsert) {
//
//		if (supDtlInfo == null) {
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.315"));
//			return prdmMain;
//		}
//		// EAI_Trans_Check ( FieldName );//인터페이스 여부 확인;
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg(Message.getMessage("prd.msg.332"));
//		logger.debug("BusComp_PreSetFieldValue =>1");
//		// 20050718 분류검증 완료체크
//		/*
//		 * if (FieldName == "Grand Parent Primary Line Name" || // 상품분류(대) --> 상품분류코드로 확인 FieldName == "Parent Primary Line Name" || // 상품분류(중) -->
//		 * 상품분류코드로 확인 FieldName == "Primary Line Name" || // 상품분류(소) --> 상품분류코드로 확인 FieldName == "Primary Product Line Name" || // 상품분류(세) --> 상품분류코드로
//		 * 확인 FieldName == "Grand Parent Primary Line Code" || // 상품분류코드(대) --> 상품분류코드로 확인 FieldName == "Prod Brand Name" ) // 브랜드명 {
//		 *//*
//			 * var sCertiStatus = this.GetFieldValue("Prod Certification Status"); //분류검증상태코드 if (sCertiStatus == "20") {
//			 * TheApplication().RaiseErrorText("분류검증이 완료된 후에는 [상품분류, 브랜드]를 수정할 수 없습니다." ); }
//			 */
//		String sCertiStatus = StringUtils.NVL(prdmMain.getClsChkStCd()); // 분류검증상태
//		if (prdmMain.getPrdCd() != null) {
//			if (sCertiStatus.equals("20")) {
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.333"));
//				return prdmMain;
//			}
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>2");
//		// 20080919 제조사구분이 null일경우에만 수정가능
//
//		/*
//		 * if (FieldName == "Factory Name") //제조사명 { /*var FT = this.GetFieldValue("Factory Type"); //제조사구분코드 if (!FT.equals("") ) {
//		 * TheApplication().RaiseErrorText("기업구분값이 있어 제조사명을 수정할 수 없습니다." ); }
//		 */
//		String FT = StringUtils.NVL(prdmMain.getMnfcCoGbnCd()); // 제조사구분코드
//		if (!FT.equals("")) {
//			// prdmMain.setRetCd("-1");
//			// prdmMain.setRetMsg("기업구분값이 있어 제조사명을 수정할 수 없습니다.");
//			return prdmMain;
//
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>3");
//		// 20090819 수정가능
//		/*
//		 * if (FieldName == "Fixed Delivery Ret Flag") //지정택배시행여부 {
//		 *//*
//			 * var CHECK = this.GetFieldValue("Carrier"); //배송수거방법코드
//			 *
//			 * if (!(CHECK.equals("") || CHECK == "2200" || CHECK == "2500"|| CHECK == "2800"|| CHECK == "3100"|| CHECK == "3200"|| CHECK ==
//			 * "3400")&&FieldValue=="Y") { TheApplication().RaiseErrorText("지정택배 수거시행 여부를 수정할 수 없습니다.배송수거방법을 확인하십시오." ); }
//			 */
//		String CHECK = StringUtils.NVL(prdmMain.getDlvPickMthodCd());
//		if (!(CHECK.equals("") || CHECK.equals("2200") || CHECK.equals("2500") || CHECK.equals("2800")
//				|| CHECK.equals("3100") || CHECK.equals("3200") || CHECK.equals("3400"))
//				&& FieldValue.equals("Y")) {
//			// TheApplication().RaiseErrorText("지정택배 수거시행 여부를 수정할 수 없습니다.배송수거방법을 확인하십시오." );
//
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.334"));
//			return prdmMain;
//		}
//		// }
//
//		logger.debug("BusComp_PreSetFieldValue =>4");
//		// 자동주문여부
//		// if (FieldName == "Auto Order Flg") { //자동주문가능여부
//		/*
//		 * var OFA = this.GetFieldValue("Optional Flag"); //단독주문불가여부 if (OFA == "Y" && FieldValue=="Y") {
//		 * TheApplication().RaiseErrorText("자동주문여부와 단독주문상품여부는 동시에 선택할 수 없습니다.1" ); }
//		 */
//		String OFA = StringUtils.NVL(prdmMain.getAutoOrdPsblYn());
//		if (OFA.equals("Y") && FieldValue.equals("Y")) {
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.335"));
//			return prdmMain;
//		}
//		// }
//
//		// 옴션상품여부
//		// if (FieldName == "Optional Flag") { //단독주문불가여부
//		/*
//		 * var OFAV = this.GetFieldValue("Auto Order Flg"); //자동주문가능여부 if (OFAV == "Y" && FieldValue=="Y") {
//		 * TheApplication().RaiseErrorText("자동주문여부와 단독주문상품여부는 동시에 선택할 수 없습니다.2" ); }
//		 */
//		String OFAV = StringUtils.NVL(prdmMain.getSeparOrdNoadmtYn());
//		if (OFAV.equals("Y") && FieldValue.equals("Y")) {
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.335"));
//			return prdmMain;
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>5");
//
//		// 주문가능수량
//		// if (FieldName == "Orderable Qty All" ) { //주문가능수량
//		/*
//		 * var OQA = this.GetFieldValue("Orderable Qty All"); //기존 등록된 주문가능수량 if (OQA != FieldValue) { this.SetFieldValue("Log Type Flg","T1");//판매종료
//		 * 로그 플래그 }
//		 */
//		// String OQA = StringUtils.NVL(prdmMain.getOrdPsblQty().toString());
//		// if (OQA != FieldValue) {
//		// this.SetFieldValue("Log Type Flg","T1");//판매종료 로그 플
//		// }
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>6");
//		// if ( FieldName == "Carrier" ) { //배송수거방법코드
//		/*
//		 * var sCarrier = this.GetFieldValue("Carrier"); //기존 배송수괄코드
//		 *
//		 * if ( sCarrier == "1100" || sCarrier == "1400" ) { this.SetFieldValue("WareHousing Type","01"); }
//		 */
//		String sCarrier = StringUtils.NVL(prdmMain.getDlvPickMthodCd());
//		if (sCarrier.equals("1100") || sCarrier.equals("1400")) {
//			// this.SetFieldValue("WareHousing Type","01"); // 입고유형코드
//			prdmMain.setIstTypCd("01");
//		}
//
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>7");
//		// 20080905 분류검증시 검증자 남김 LN Approval By TheApplicn().LoginId() Prod Certification Status
//		// if (FieldName == "Prod Certification Status") {//분류검증상태
//
//		/*
//		 * if (FieldValue == "20" ) { var sCertiStatus = this.GetFieldValue("Prod Certification STSTUE"); if (sCertiStatus != "20") {
//		 * this.SetFieldValue("LN Approval By", TheApplication().Lod()); //분류검증자사번ID } }
//		 */
//		if (StringUtils.NVL(prdmMain.getClsChkStCd()).equals("20")) {
//			if (!prdmMain.getClsChkStCd().equals("20")) {
//				prdmMain.setClsChkrEmpnoId(prdmMain.getSessionUserId()); // 분류검증자사번ID
//			}
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>8");
//		/*
//		 * if (FieldName == "Approval Status") //상품결재상태코드 { /* var Order = this.GetFieldValue("Orderable"); //판매가능결재? var cFlg =
//		 * this.GetFieldValue("Vendor Contract Flag"); //협럭渦扇㈉? var cPrice = this.GetFieldValue("Price List"); //판매가격 var sVendor =
//		 * this.GetFieldValue("Vendor"); //협력사코드 var TimeBuf; Clib.strftime(TimeBuf,"%Y/%m/%d %X" ,Clib.localtime(Clib.tim);
//		 *
//		 * var sMdId = GetFieldValue("MD Id"); //운영MDID var sShowInInternet = GetFieldValue("Show In Internet"); // var sCS = Check_Spec(FieldValue);
//		 * //추가펑션확인
//		 */
//		String Order = StringUtils.NVL(prdmMain.getSalePsblAprvYn());
//		String cFlg = StringUtils.NVL(supDtlInfo.getContYn()); // 협력사 계약여부
//		BigDecimal cPrice = prdprcHinsert.getSalePrc();
//		String sVendor = StringUtils.NVL(supDtlInfo.getSupCd());
////		String sMdId = StringUtils.NVL(prdmMain.getOperMdId());
////		String sShowInInternet = ""; // 제거
//		// *************************************************************************// 추 가 펑션임
//		// String sCS = Check_Spec(FieldValue);
//		String sCS = "";
//		if (sCS.equals("N")) {
//			// TheApplication().RaiseErrorText("상품기술서 항목이 등록되씰?않아 승인이 불가합니다. 상품기술서를 등록하셔야 합니다." );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.336"));
//			return prdmMain;
//		}
//		/*
//		 * if (FieldValue == "30" ) //결재상태코드 = 30 { if ( sVendor != "1002235" && sVendor != "1002935" && sVend= "1001913" && sVendor != "123097" &&
//		 * sVendor != "121860" && sVendor != "1001898" && sVendor != "1002273" && sVendor != "1001872" && sVendor != "1002425" && sVendor != "1002368"
//		 * && sVendor != "1001921" && sVendor != "304080" && sVendor != "303900" && sVendor != "123097" && sVendor != "1001898" && sVendor !=
//		 * "1002425" && sVendor != "1001913" && sVendor != "1003087" && sVendor != "121860" && sVendor != "305190" && sVendor != "303170" && sVendor
//		 * != "120009" && sVendor != "989081" && sVendor != "121530" && sVendor != "1013100" && sVendor != "1012703" )//304080 삼성전자(주)-정보기기 추가 2777902
//		 * { if ( not(if( [Purchase Type] = "01" or [Purchase Typ= "04" , if(IfNull ([MD Support Approval Status],"8") ="1","Y","N") , "Y" )) == "Y" )
//		 * //매입유형코드, 매입유형코드, 매입유형승인코드 { TheApplication().RaiseErrorText("완전매입 상품은 CAT지원팀 승인이 필요합니다." ); } } // 20050718 분류검증 상태 체크 if
//		 * (this.GetFieldValue("Prod Certification Status") != " //분류검증상태코드 { TheApplication().RaiseErrorText("분류검증 완료 후 결재가爛求? 분류검증 상태를 확인하십시오." );
//		 * } }
//		 */
//		logger.debug("BusComp_PreSetFieldValue =>9");
//		if (StringUtils.NVL(prdmMain.getPrdAprvStCd()).equals("30")) {
//			if (!sVendor.equals("1002235") && sVendor.equals("1002935") && sVendor.equals("1001913")
//					&& sVendor.equals("123097") && sVendor.equals("121860") && sVendor.equals("1001898")
//					&& sVendor.equals("1002273") && sVendor.equals("1001872") && sVendor.equals("1002425")
//					&& sVendor.equals("1002368") && sVendor.equals("1001921") && sVendor.equals("304080")
//					&& sVendor.equals("303900") && sVendor.equals("123097") && sVendor.equals("1001898")
//					&& sVendor.equals("1002425") && sVendor.equals("1001913") && sVendor.equals("1003087")
//					&& sVendor.equals("121860") && sVendor.equals("305190") && sVendor.equals("303170")
//					&& sVendor.equals("120009") && sVendor.equals("989081") && sVendor.equals("121530")
//					&& sVendor.equals("1013100") && sVendor.equals("1012703"))// 304080 삼성전자(주)-정보기기 추가 2777902
//			{
//				if ((prdmMain.getPrchTypCd().equals("01") || prdmMain.getPrchTypCd().equals("04"))) {
//					if (prdmMain.getPrchTypAprvCd().equals("1")) {
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("prd.msg.337"));
//						return prdmMain;
//					}
//				}
//				// 20050718 분류검증 상태 체크
//
//				if (!StringUtils.NVL(prdmMain.getClsChkStCd()).equals("20")) // 분류검증상태코드
//				{
//					// TheApplication().RaiseErrorText("분류검증 완료 후 결재가 가능합니다. 분류검증 상태를 확인하십시오." );
//					prdmMain.setRetCd("-1");
//					prdmMain.setRetMsg(Message.getMessage("prd.msg.338"));
//					return prdmMain;
//				}
//			}
//		}
//
//		logger.debug("BusComp_PreSetFieldValue =>10");
//		/*
//		 * if ((FieldValue == "30") && (cFlg == "Y") && (cPrice != "")) //결재상태가 MD팀장합격이고 거래처와 계약이 되어 있는 경우 { if(Order == "N") //승인이 나지 않은 경우 날짜와, 승인여부
//		 * Setting { this.SetFormattedFieldValue("Start Date", TimeBuf); //판매시작일시 this.SetFieldValue("Orderable","Y"); //판매가능결재여부 } Var_App_Set();
//		 * //펑션추가 }
//		 */
//		// 현재시간조회
//		Date date = SysUtil.getCurrTime();
//		String sysdate = DateUtils.format(date, "yyyyMMddHHmmss");
//		if ((StringUtils.NVL(prdmMain.getPrdAprvStCd()).equals("30")) && (cFlg.equals("Y")) && (!cPrice.equals(""))) {
//			if (Order.equals("N")) // 승인이 나지 않은 경우 날짜와, 승인여부 Setting
//			{
//				// this.SetFormattedFieldValue("Start Date", TimeBuf); //판매시작일시
//				// this.SetFieldValue("Orderable","Y"); //판매가능결재여부
//				prdmMain.setSaleStrDtm(sysdate);
//				prdmMain.setSalePsblAprvYn("Y");
//			}
//		} else if ((FieldValue.equals("30")) && (!cFlg.equals("Y"))) {
//			// TheApplication().RaiseErrorText("거래처와 계약전입니다.")
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.339"));
//			return prdmMain;
//		} else if ((FieldValue.equals("30")) && (!cPrice.equals(""))) {
//			// TheApplication().RaiseErrorText("상품가격을 확인하세요.")
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.340"));
//			return prdmMain;
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>11");
//		// if (FieldName == "Installation Flg") //설치배송상품여부
//		// {
//		// var sInstallCharge = this.GetFieldValue("Installation Charge//설치비용
//		BigDecimal sInstallCharge = prdprcHinsert.getInstlCost();
//
//		BigDecimal zero = new BigDecimal("0");
//
//		if ((FieldValue.equals("N")) && (sInstallCharge.compareTo(zero) > 0)) {
//			// this.SetFieldValue("Installation Charge", ""); //설치비용
//
//			prdmMain.setInstlCost(zero);
//
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>12");
//		if (((StringUtils.NVL(prdmMain.getSessionChanlCd()).equals("P")) || (StringUtils.NVL(prdmMain
//				.getSessionChanlCd()).equals("GSEC")))
//				|| (StringUtils.NVL(prdmMain.getRegChanlGrpCd()).equals("P") || StringUtils.NVL(
//						prdmMain.getRegChanlGrpCd()).equals("GC"))) {
//			if (FieldName.equals("Primary Product Line Name")
//					|| FieldName.equals("Pct Name")
//					|| // 상품분류코드, 상품명
//					(FieldName.equals("Factory Name") && FieldName.equals("Native Country")
//							|| // 재조사명, 원산지
//							FieldName.equals("Carrier")
//							|| // 배송수거방법코드
//							FieldName.equals("Grand Parent Primary Line Name")
//							|| FieldName.equals("Parent Primary Line Name") || // 상품분류콛,
//							FieldName.equals("Primary Line Name") || FieldName.equals("Grand nt Primary Line Code"))) {
////				String sQA = "";
////				String sApprovalStatus = StringUtils.NVL(prdmMain.getPrdAprvStCd());
////				sQA = "QA";
//			}
//		}
//		/*
//		 * if ( (TheApplication().GetProfileAttr("gReg_Channel") != "E" & TheApplication().GetProfileAttr("gReg_Channel") != "M" ) & (gBroadProd ==
//		 * "N") && (FieldName == "Primary Product Line Name" || FieldName == "Pct Name" || //상품분류코드, 상품명 (FieldName == "Factory Name" &&
//		 * TheApplication().GetProfitr("gReg_Channel") != "B" ) || FieldName == "Native Country" || //재조사명, 원산지 FieldName == "Carrier" || //배송수거방법코드
//		 * FieldName == "Grand Parent Primary Line Name" || FieldNam "Parent Primary Line Name" || //상품분류콛, FieldName == "Primary Line Name" ||
//		 * FieldName == "Grand nt Primary Line Code" )) { var sQA = ""; var sApprovalStatus = this.GetFieldValue("Approval Status");贊같燒瀯纘쩡湄? var
//		 * sShowInInt = this.GetFieldValue("Show In Internet"); //? var sProdUpd_forQA = TheApplication().GetProfileAttr("gProdUorQA"); //
//		 *
//		 * if (sProdUpd_forQA != "Y" || sProdUpd_forQA.equals("")) sQA = "MD"; else sQA = "QA";
//		 *
//		 * if ( sQA != "QA" && sShowInInt != "Y" && sApprovalStatus >= && sApprovalStatus != "31" ) //sApprovalStatus >= "25" && sApprovalStatus !=
//		 * "31" 만 수행 { TheApplication().RaiseErrorText("QA결재 이후에는 [상품분류갭?제조사,원산지,배송수거방법,상품상세,브랜드] 를 변경할 수 없습니다.\n" + "QA팀으로 문의하세요" ); }
//		 *
//		 * gBroadProd = "N"; }
//		 */
//		/*
//		 * if ( FieldName == "Purchase Type" ) //매입유형코드 { if ( (FieldValue == "01" || FieldValue == "04" ) && this.GeldValue("Carrier").charAt(0) ==
//		 * "2") //배송수거방법코드 { TheApplication().RaiseErrorText("매입형태가 완전매입/조건붊인 경우 배송형태 직택배는 사용불가능 합니다." ); } if (FieldValue == "01") { var sCarrier =
//		 * this.GetFieldValue("Carrier"); if ( sCarrier == "1300" || sCarrier == "1400" || sCarrier"1500" || sCarrier == "2000"|| sCarrier == "2100"||
//		 * sCarrier == "2200"|| sCarrier == "2300"|| sCarrier == "2400"|| sCarrier == "2500"|| sCarrier == "2600"|| sCarrier == "2700"|| sCarrier ==
//		 * "2800"|| sCarrier == "5000" ) { TheApplication().RaiseErrorText("완전매입 상품은  직반출〈?합니다." ); } } if ( ( FieldValue == "04" ) &&
//		 * this.GetFieldValue("Specialchase Flag") != "Y") //협력사.조건부매입허용여부 { TheApplication().RaiseErrorText("조건부매입 가능거래처가 암다." ); } }
//		 */
//		logger.debug("BusComp_PreSetFieldValue =>13");
//		// if ( FieldName == "Purchase Type" ) //매입유형코드
//		// {
//		if ((StringUtils.NVL(prdmMain.getPrchTypCd()).equals("01") || StringUtils.NVL(prdmMain.getPrchTypCd()).equals(
//				"04"))
//				&& StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2")) // 배송수거방법코드
//		{
//			// TheApplication().RaiseErrorText("매입형태가 완전매입/조건붊인 경우 배송형태 직택배는 사용불가능 합니다." );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.341"));
//			return prdmMain;
//		}
//		if (prdmMain.getPrchTypCd().equals("01")) {
//			/* var sCarrier = this.GetFieldValue("Carrier"); */
//			sCarrier = StringUtils.NVL(prdmMain.getDlvPickMthodCd());
//			if (sCarrier.equals("1300") || sCarrier.equals("1400") || sCarrier.equals("1500")
//					|| sCarrier.equals("2000") || sCarrier.equals("2100") || sCarrier.equals("2200")
//					|| sCarrier.equals("2300") || sCarrier.equals("2400") || sCarrier.equals("2500")
//					|| sCarrier.equals("2600") || sCarrier.equals("2700") || sCarrier.equals("2800")
//					|| sCarrier.equals("5000")) {
//				// TheApplication().RaiseErrorText("완전매입 상품은  직반출〈?합니다." );
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.342"));
//				return prdmMain;
//			}
//		}
//		if ((StringUtils.NVL(prdmMain.getPrchTypCd()).equals("04") && (!StringUtils.NVL(
//				supDtlInfo.getCondtlPrchPmsnYn()).equals("Y")))) // 협력사.조건부매입허용여부
//		{
//			// TheApplication().RaiseErrorText("조건부매입 가능거래처가 암다." );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.343"));
//			return prdmMain;
//		}
//		// }
//
//		logger.debug("BusComp_PreSetFieldValue =>14");
//		// if ( FieldName.equals("Carrier" )) //배송수거방법코드
//		// {
//		if (prdmMain.getDlvPickMthodCd().substring(0, 1).equals("2")
//				&& (prdmMain.getPrchTypCd().equals("01") || prdmMain.getPrchTypCd().equals("04"))) // 매입유형코드
//		{
//			// TheApplication().RaiseErrorText("매입형태가 완전매입/조건붊인 경우 배송형태 직택배는 사용불가능 합니다." );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.341"));
//			return prdmMain;
//		}
//		String sPurchaseT = StringUtils.NVL(prdmMain.getPrchTypCd()); // 매입코드
//		if (sPurchaseT.equals("01")) {
//			if (StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("1300")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("1400")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("1500")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2000")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2100")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2200")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2300")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2400")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2500")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2600")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2700")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("2800")
//					|| StringUtils.NVL(prdmMain.getDlvPickMthodCd()).equals("5000")) {
//				// TheApplication().RaiseErrorText("완전매입 상품은  직반柰〈?합니다." );
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.342"));
//				return prdmMain;
//			}
//		}
//		logger.debug("BusComp_PreSetFieldValue =>15");
//		if ((prdmMain.getDlvPickMthodCd().equals("1300") || prdmMain.getDlvPickMthodCd().equals("1400")
//				|| prdmMain.getDlvPickMthodCd().equals("1500") || prdmMain.getDlvPickMthodCd().equals("2100")
//				|| prdmMain.getDlvPickMthodCd().equals("2400") || prdmMain.getDlvPickMthodCd().equals("2700"))
//				&& (!supDtlInfo.getCondtlPrchPmsnYn().equals("Y"))) // 협력사.직반출여부
//		{
//			// TheApplication().RaiseErrorText("직반출 불가능 거래처입니?;
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.344"));
//			return prdmMain;
//		}
//
//		if (prdmMain.getDlvPickMthodCd().equals("1100") || prdmMain.getDlvPickMthodCd().equals("1400")
//				|| prdmMain.getDlvPickMthodCd().equals("3100") || prdmMain.getDlvPickMthodCd().equals("5000")) { // 2009.10.08 SNY 반영
//			// var sCVSRetFlag = this.GetFieldValue("CVS Ret Flag"); //편택배반품여부
//			String sCVSRetFlag = StringUtils.NVL(prdmMain.getCvsDlvsRtpYn());
//			if (sCVSRetFlag.equals("Y")) {
//				// this.SetFieldValue("CVS Ret Flag","N");
//				prdmMain.setCvsDlvsRtpYn("N");
//			}
//		}
//		// }
//
//		logger.debug("BusComp_PreSetFieldValue =>16");
//		// if ( FieldName.equals("Special Purchase Flag") ) //협력사.조건부매잼여부
//		// {
//		if ((!StringUtils.NVL(supDtlInfo.getCondtlPrchPmsnYn()).equals("Y"))
//				&& StringUtils.NVL(prdmMain.getPrchTypCd()).equals("04")) // 매입유형코드
//		{
//			// TheApplication().RaiseErrorText("조건부매입 가능거래처가爛求?" );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.345"));
//			return prdmMain;
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>17");
//		// if ( FieldName == "Direct Return Flag" ) //협력사.직반출여부
//		// {
//		// var sCarrier = this.GetFieldValue("Carrier"); //배송수거방법
//
//		sCarrier = StringUtils.NVL(prdmMain.getDlvPickMthodCd());
//
//		if ((!StringUtils.NVL(supDtlInfo.getDirTakoutYn()).equals("Y"))
//				&& (sCarrier.equals("1300") || sCarrier.equals("1400") || sCarrier.equals("1500")
//						|| sCarrier.equals("2100") || sCarrier.equals("2400") || sCarrier.equals("2700"))) {
//			// TheApplication().RaiseErrorText("직반출 가능거래처가 아다." );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.346"));
//			return prdmMain;
//		}
//		// }
//
//		// if ( FieldName == "Gift Check Flg") //일회부여사은품여부
//		// {
//		// var sFreeGiftType = this.GetFieldValue("Free Gift Type") ;//사은품유형코드
//		String sFreeGiftType = StringUtils.NVL(prdmMain.getGftTypCd());
//
//		if (StringUtils.NVL(prdmMain.getOnetmGivGftYn()).equals("Y")
//				&& (sFreeGiftType.equals("00") || sFreeGiftType.equals("05"))) {
//			// TheApplication().RaiseErrorText("상품구분이 사은품이 아닙? );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.347"));
//			return prdmMain;
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>18");
//		// if ( FieldName == "Broad Prod Flg" ) //방송중판매가능여부
//		// {
//		if (StringUtils.NVL(prdmMain.getOnairSalePsblYn()).equals("Y")
//				&& (StringUtils.NVL(prdmMain.getPreOrdTypCd()).equals("R") || StringUtils
//						.NVL(prdmMain.getPreOrdTypCd()).equals("P"))) // 미리주문유형코드
//		{
//			// TheApplication().RaiseErrorText("미리주문상품은 방송중판매상품으로 변경할수 없습니다.\n GS방송넷시스템에서 미리주문 여부를 변경해 주세요." );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.348"));
//			return prdmMain;
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>19");
//		// EC 결품해제 관련 추가
//		// if ( FieldName == "End Date") //판매종료일시
//		// {
//		// var ED = this.GetFieldValue("End Date");
////		String ED = StringUtils.NVL(prdmMain.getSaleEndDtm());
//		/*
//		 * if (ED != FieldValue) { this.SetFieldValue("Log Type Flg","T3");//판매종료 로그 플 }
//		 */
//
//		/*
//		 * var sSource = TheApplication().GetProfileAttr("BtnUndoShge"); if (sSource == "BT" || sSource == "BS") // BS 또는 But통한 수정 {
//		 * TheApplication().SetProfileAttr("BtnUndoShortage","") } else // EC 결품상품의 화면통한 수정 {
//		 */
//		// var sResaleInfo = this.GetFieldValue("Close Reason Typ //판매종료사유코드
//		String sResaleInfo = StringUtils.NVL(prdmMain.getSaleEndRsnCd());
//
//		if (sResaleInfo.equals("30") || sResaleInfo.equals("40")) {
//			// TheApplication().RaiseErrorText("결품으로 등록된 상?EC결품해제] 버튼을 통해 판매종료해제 할 수 있습니다." );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.349"));
//			return prdmMain;
//		}
//		// }
//		/*************************************************************************/
//		// Check_Prod_Closed(FieldValue); //펑션추가
//		/*************************************************************************/
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>20");
//		// if ( FieldName == "Close Reason Type") //판매종료사유코드
//		// {
//		/*
//		 * var sSource = TheApplication().GetProfileAttr("BtnUndoShge"); if (sSource == "BT" || sSource == "BS") // BS 또는 But통한 수정 {
//		 * TheApplication().SetProfileAttr("BtnUndoShortage","") } else // EC 결품상품의 화면통한 수정 {
//		 */
//		// var sResaleInfo = this.GetFieldValue("Close Reason Typ //판매종료사유코드
//		sResaleInfo = StringUtils.NVL(prdmMain.getSaleEndRsnCd());
//		if (sResaleInfo.equals("30") || sResaleInfo.equals("40")) {
//			// TheApplication().RaiseErrorText("결품으로 등록된 상?EC결품해제] 버튼을 통해 판매종료해제 할 수 있습니다." );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.349"));
//			return prdmMain;
//		}
//		// }
//		// }
//
//		// 로그추가 추후반영
//		// 인터페이스가 필요없는 필드가 변경되는 경우
//		/*
//		 * if ( FieldName == "Updated Date" || FieldName == "Updated By" ) { FS_LOG ("FieldName1", FieldName) } // 인터페이스가 필요한 필드가 변경되는 경우 else {
//		 * FS_LOG ("FieldName2", FieldName) gChangedFlagExceptCloseReason = "Y"; } //로그추가 추후반영
//		 */
//		/***********************************************************/
//		// var sNeedSetCloseReason = Need_Set_Close_Reason(FieldName, Fielue) //펑션추가
//		String sNeedSetCloseReason = "";
//
//		if (StringUtils.NVL(sNeedSetCloseReason).equals("NO")) {
//			// TheApplication().RaiseErrorText(sNeedSetCloseReason);
//		}
//
//		// 2007.08.21 jaemin C20070813_16421 소싱MD ID 추가
//		/*
//		 * if(FieldName == "User Sourcing MD Id") //제거 { var isMDId = this.GetFieldValue("Sourcing MD Id"); var isNew = this.GetFieldValue("Part #");
//		 *
//		 * if((isMDId .equals("") && isNew !="") || gCopyProd == "Y" ) { TheApplication().RaiseErrorText("소싱 MDID는 수정 할 수 없습니다." ); } }
//		 *
//		 * if(FieldName == "Sourcing MD Id") //제거 { var isMDId = this.GetFieldValue("Sourcing MD Id"); var isNew = this.GetFieldValue("Part #");
//		 *
//		 * if((isMDId != "" && isNew !="") || gCopyProd == "Y" ) { TheApplication().RaiseErrorText("소싱 MDID 는 수정 할 수 없습다." ); } }
//		 */
//		// 지정택배수거 jewelLee 2009.04.08
//		/*
//		 * if ( FieldName == "Fixed Delivery Ret Flag" ) //지정택배시행여부 { if ( gBeforFixedDeliveryRetFlag == "INIT" ) //저장하기전 필도菅?수정하는거 때문에 {
//		 * gBeforFixedDeliveryRetFlag = this.GetFieldValue("Fixed Dely Ret Flag"); } }
//		 *
//		 * if ( FieldName == "Ret Receipt Center Address Code") //상품반품지주소소코드 { if ( gBeforeRetCenter == "INIT" ) { gBeforeRetCenter =
//		 * this.GetFieldValue("Ret Receipt Center ess Code"); } }
//		 */
//		logger.debug("BusComp_PreSetFieldValue =>21");
//		// 2009.09.25 SNY 편의점반품 및 상품출고지 관련 추가 시작
//		// if ( FieldName == "CVS Ret Flag" ) { ////편의점택배반품여부
//		// var sCarrierCd = this.GetFieldValue("Carrier"); //배송수거방법
//		String sCarrierCd = StringUtils.NVL(prdmMain.getDlvPickMthodCd());
//
//		if (StringUtils.NVL(prdmMain.getCvsDlvsRtpYn()).equals("Y")
//				&& (sCarrierCd.equals("1100") || sCarrierCd.equals("1400") || sCarrierCd.equals("3100") || sCarrierCd
//						.equals("5000"))) { // 2009.10.08 SNY 반영
//			// this.SetFieldValue("CVS Ret Flag","N");
//			prdmMain.setCvsDlvsRtpYn("N");
//		}
//
//		/*
//		 * if ( gBeforeCVSRetFlag == "INIT" ) //저장하기전 필드를 두번 하는거 때문에 { gBeforeCVSRetFlag = this.GetFieldValue("CVS Ret Flag"); }
//		 */
//		// }
//		/*
//		 * if ( FieldName == "Ship Center Address Code") //상품출고지주소 { if ( gBeforeShipCenter == "INIT" ) { gBeforeShipCenter =
//		 * this.GetFieldValue("Ship Center Addresde"); } }
//		 */
//		logger.debug("BusComp_PreSetFieldValue =>22");
//		// 2009.09.25 SNY 편의점반품 및 상품출고지 관련 추가 끝
//		// if ( FieldName == "Free Gift Type" ) /* 090710 MJCHON C2009061708 */ //사은품유형코드
//		// {
//		/* 판매상품이 아니면 0원 여부 N 처리 */
//		// var sZeroFlag = this.GetFieldValue("Zero Flag"); //0원판매여
//		// var sPriceList = this.GetFieldValue("Price List"); //판매가?
//		String sZeroFlag = StringUtils.NVL(prdmMain.getZrwonSaleYn());
//
//		if (!FieldValue.equals("00") && sZeroFlag.equals("Y")) {
//			// this.SetFieldValue("Zero Flag", "N");
//			prdmMain.setZrwonSaleYn("Y");
//		}
//		// }
//		logger.debug("BusComp_PreSetFieldValue =>23");
//		// 2009.10.21 선불이 아닌경우 배송비상품 제한 시블은 이로서 배 상품자체를 저장 못합
//
//		// if ( FieldName == "LGHS_RET_REFUND_TYPE" ) //환불유형코드
//		// {
//		/* 배송비상품 확인 */
//		// var sFGT = this.GetFieldValue("Free Gift Type"); //사은품유헌
//		String sFGT = StringUtils.NVL(prdmMain.getGftTypCd());
//		if ((!sFGT.equals("20")) && StringUtils.NVL(prdmMain.getRfnTypCd()).equals("05")) {
//			// TheApplication().RaiseErrorText("배송비 상품은 즉시환불만 합니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.350"));
//			return prdmMain;
//		}
//		// }
//		/*
//		 * if ( igLogging != "Y" ) { switch ( FieldName ) { case "Approval Status" : case "Auto Order Flg" : case "CG Desc1" : case "CG Desc2" : case
//		 * "CG Desc3" : case "CG Desc4" : case "Carrier" : case "Carrier Method" : case "EC ATP Flg" : case "End Date" : case "Factory Name" : case
//		 * "Native Country" : case "Orderable Qty All" : case "Orderable Qty DM" : case "Product Name" : case "Product Org" : case "Sale Type" : case
//		 * "Software Flg" : case "Start Date" : case "User MD Id" : case "Insale Limit Flag" : case "Family Limit Flag" : case "GS MemberShip Product"
//		 * : case "Ditto Flag" : //Ditto여부 C20091007_36314 mjchon 17
//		 *
//		 * igLogging = "Y"; break; default: break; } } if ( igDLogging != "Y" ) { switch ( FieldName ) { case "deliberation level" : case
//		 * "deliberation Text" :
//		 *
//		 * igDLogging = "Y"; break; default: break; } }
//		 *
//		 * return (ContinueOperation); }
//		 *
//		 * catch (e) { throw e; } }
//		 */
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
//
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
	
	
	
	
	// function BusComp_PreWriteRecord ()
	// { // SafeCertPrd 안전인증
	// PrdStockDinsert prdAttrPrdm // 재고정보
	// PrdPrdDinsert prdPrdD // 상품확장정보
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : BusComp_PreWriteRecord
	 */
//	public PrdmMain BusComp_PreWriteRecord(String FieldName, PrdmMain prdmMain, SupDtlInfo supDtlInfo,
//			PrdprcHinsert prdprcHinsert, SafeCertPrd safeCertPrd, PrdPrdDinsert prdPrdD) {
//		logger.debug("BusComp_PreWriteRecord=>1");
//		/*
//		 * var sProdId = prdmMain.getPrdCd(); //상품코드 var AutoOrder = prdmMain.getAutoOrdPsblYn(); //자동주문가능여부 var AutoName =
//		 * this.GetFieldValue("Prod Kor Name"); //자동주문상품명 var AutoType = this.GetFieldValue("Prod ARS Type"); //자동주문분류코드 var ProdName =
//		 * this.GetFieldValue("Product Name"); //상품명 var PartNum = this.GetFieldValue("Part #"); //상품코드 var AppStatus =
//		 * this.GetFieldValue("Approval Status"); //상품결재상태코드 var ProdType = this.GetFieldValue("Prod Type"); //상품유형코드 var SaleType =
//		 * this.GetFieldValue("Sale Type"); //주문상품유형코드 var OrderQtyAll = this.GetFieldValue("Orderable Qty All"); //주문가능수량 (채널 <> 'D') var OrderQtyDM
//		 * = this.GetFieldValue("Orderable Qty DM"); //주문가능수량 (채널 = 'D') var RInfo = this.GetFieldValue("Resale Info"); //제거 var Order =
//		 * this.GetFieldValue("Orderable"); //판매가능결재여부 var GiftType = this.GetFieldValue("Free Gift Type"); //사은품유형코드 var PurchType =
//		 * this.GetFieldValue("Purchase Type"); //매입유형코드 var TaxType = this.GetFieldValue("Tax Type"); //세금유형코드 var TaxPaper =
//		 * this.GetFieldValue("Tax Paper Flg"); //세금계산서발행여부 var ProdOrg = this.GetFieldValue("Product Org"); //상품정보구성내용 var GiftCertiAmt =
//		 * this.GetFieldValue("Gift Certificate Amt"); //상품권액면가격 var ShipFeeFlg = this.GetFieldValue("Ship Fee Flg"); //유료배송여부 var sECATPFlg =
//		 * this.GetFieldValue("EC ATP Flg"); //배송일자안내코드 var sSoftwareFlg = this.GetFieldValue("Software Flg"); //무형상품유형코드 var sOrderProd =
//		 * this.GetFieldValue("Order Prod"); //주문제작여부 var sReserveProd = this.GetFieldValue("Reserve Prod"); //예약판매상품여부 var sWareHousing =
//		 * this.GetFieldValue("WareHousing Type"); //입고유형코드 var sCarrier = this.GetFieldValue("Carrier"); //배송수거방법코드 var sCarrierMethod =
//		 * this.GetFieldValue("Carrier Method"); //택배사코드 var sGmsProdType = this.GetFieldValue("GMS Prod Type"); //상품구분코드 var sFlg = ""; // var
//		 * sSafeFlg = this.GetFieldValue("Safe Flg"); //상품분류.안전인증대상여부 var sSafeModelName = this.GetFieldValue("Safe Model Name"); //안전인증모델명 var
//		 * sSafeNum = this.GetFieldValue("Safe Num"); //안전인증번호 var sSafeType = this.GetFieldValue("Safe Type"); //안전인증구분코드 var sSafeAuthDate =
//		 * this.GetFieldValue("Safe Auth Date"); //안전인증일자 var sSafeAuthOrgan = this.GetFieldValue("Safe Auth Organ"); //안전인증기관코드 var sPrdType =
//		 * GetFieldValue("Prod Type"); //상품유형코드 var sAOF = GetFieldValue("Auto Order Flg"); //자동주문가능여부 var sSAPCheck = gSAPCheck; // var sSPCareStdt =
//		 * this.GetFormattedFieldValue("SP Care Desc From"); //공지정보.주문유의사항.유효시작일시 var sSPCareEddt = this.GetFormattedFieldValue("SP Care Desc To");
//		 * //공지정보.주문유의사항.유효종료일시 var gSwFlg = this.GetFieldValue("Software Flg"); //무형상품유형코드 var OFA = this.GetFieldValue("Auto Order Flg"); //자동주문가능여부
//		 */
//		logger.debug("BusComp_PreWriteRecord=>1-1");
////		BigDecimal sProdId = prdmMain.getPrdCd(); // 상품코드
//		String AutoOrder = StringUtils.NVL(prdmMain.getAutoOrdPsblYn()); // 자동주문가능여부
//		logger.debug("BusComp_PreWriteRecord=>1-2");
//		String AutoName = StringUtils.NVL(prdmMain.getAutoOrdPrdNm()); // 자동주문상품명
//		String AutoType = StringUtils.NVL(prdmMain.getAutoOrdClsCd()); // 자동주문분류코드
//		logger.debug("BusComp_PreWriteRecord=>1-3");
////		String ProdName = StringUtils.NVL(prdmMain.getPrdNm()); // 상품명
//		String PartNum = "";// StringUtils.NVL(prdmMain.getPrdCd().toString()); //상품코드
//		logger.debug("BusComp_PreWriteRecord=>1-4");
////		String AppStatus = StringUtils.NVL(prdmMain.getPrdAprvStCd()); // 상품결재상태코드
//		String ProdType = StringUtils.NVL(prdmMain.getPrdTypCd()); // 상품유형코드
//		String SaleType = StringUtils.NVL(prdmMain.getOrdPrdTypCd()); // 주문상품유형코드
////		BigDecimal OrderQtyAll = prdmMain.getOrdPsblQty(); // 주문가능수량(채널<>'D')
////		BigDecimal OrderQtyDM = prdmMain.getOrdPsblQty(); // 주문가능수량(채널='D')
//		logger.debug("BusComp_PreWriteRecord=>1-5");
//		// String RInfo = prdmMain.getPrdCd(); //제거
////		String Order = StringUtils.NVL(prdmMain.getSalePsblAprvYn()); // 판매가능결재여부
//		String GiftType = StringUtils.NVL(prdmMain.getGftTypCd()); // 사은품유형코드
//		String PurchType = StringUtils.NVL(prdmMain.getPrchTypCd()); // 매입유형코드
//		String TaxType = StringUtils.NVL(prdmMain.getTaxTypCd()); // 세금유형코드
////		String TaxPaper = StringUtils.NVL(prdmMain.getTaxInvIssueYn()); // 세금계산서발행여부
//		String ProdOrg = StringUtils.NVL(prdPrdD.getPrdInfoCmposCntnt()); // 상품정보구성내용
//		BigDecimal GiftCertiAmt = prdmMain.getGftcertFacePrc(); // 상품권액면가격
//		String ShipFeeFlg = StringUtils.NVL(prdmMain.getChrDlvYn()); // 유료배송여부
//		String sECATPFlg = StringUtils.NVL(prdmMain.getDlvDtGuideCd()); // 배송일자안내코드
//		String sSoftwareFlg = StringUtils.NVL(prdmMain.getFrmlesPrdTypCd()); // 무형상품유형코드
//		String sOrderProd = StringUtils.NVL(prdmMain.getOrdMnfcYn()); // 주문제작여부
//		String sReserveProd = StringUtils.NVL(prdmMain.getRsrvSalePrdYn()); // 예약판매상품여부
//		String sWareHousing = StringUtils.NVL(prdmMain.getIstTypCd()); // 입고유형코드
//		String sCarrier = StringUtils.NVL(prdmMain.getDlvPickMthodCd()); // 배송수거방법코드
//		String sCarrierMethod = StringUtils.NVL(prdmMain.getDlvsCoCd()); // 택배사코드
//		String sGmsProdType = StringUtils.NVL(prdmMain.getPrdGbnCd()); // 상품구분코드
////		String sFlg = "";
//		String sSafeFlg = ""; // 상품분류.안전인증대상여부
//		String sSafeModelName = StringUtils.NVL(safeCertPrd.getSafeCertModelNm()); // 안전인증모델명
//		String sSafeNum = StringUtils.NVL(safeCertPrd.getSafeCertNo()); // 안전인증번호
//		String sSafeType = StringUtils.NVL(safeCertPrd.getSafeCertGbnCd()); // 안전인증구분코드
//		String sSafeAuthDate = StringUtils.NVL(safeCertPrd.getSafeCertDt()); // 안전인증일자
//		String sSafeAuthOrgan = StringUtils.NVL(safeCertPrd.getSafeCertOrgCd()); // 안전인증기관코드
//		String sPrdType = StringUtils.NVL(prdmMain.getPrdTypCd()); // 상품유형코드
//		String sAOF = StringUtils.NVL(prdmMain.getAutoOrdPsblYn()); // 자동주문가능여부
////		String sSAPCheck = ""; //
//		String sSPCareStdt = ""; // 공지정보.주문유의사항.유효시작일시
//		String sSPCareEddt = ""; // 공지정보.주문유의사항.유효종료일시
//		String gSwFlg = StringUtils.NVL(prdmMain.getFrmlesPrdTypCd()); // 무형상품유형코드
//		String OFA = StringUtils.NVL(prdmMain.getAutoOrdPsblYn()); // 자동주문가능여부
//		String gCopyProd = "";
//
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg("write 통과");
//		logger.debug("BusComp_PreWriteRecord=>2");
//		if (sPrdType.equals("C") && sAOF.equals("Y")) {
//			// this.SetFieldValue("Auto Order Flg", "N"); //자동주문가능여부
//			// TheApplication().RaiseErrorText("당사상품권은 카드결재가 불가능하므로\n자동주문에 노출할 수 없습니다.\n\n자동주문여부를 확인하십시오");
//			prdmMain.setAutoOrdPsblYn("N");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.351"));
//			return prdmMain;
//		}
//		logger.debug("BusComp_PreWriteRecord=>2");
//		if (PartNum.equals("")) {
//			if (sSafeFlg.equals("Y")) {
//				if (!(sSafeType.equals("0"))) {
//					if (sSafeModelName.equals("")) {
//						// TheApplication().RaiseErrorText("모델명을 입력하세요");
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("prd.msg.287", new String[]{"안전인증모델명"}));
//						return prdmMain;
//					}
//					if (sSafeNum.equals("")) {
//						// TheApplication().RaiseErrorText("인증번호를 입력하세요");
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("prd.msg.287", new String[]{"인증번호"}));
//						return prdmMain;
//					}
//					if (sSafeAuthDate.equals("")) {
//						// TheApplication().RaiseErrorText("인증일을 입력하세요");
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("prd.msg.287", new String[]{"인증일"}));
//						return prdmMain;
//					}
//					if (sSafeAuthOrgan.equals("")) {
//						// TheApplication().RaiseErrorText("인증기관을 입력하세요");
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("prd.msg.287", new String[]{"인증기관"}));
//						return prdmMain;
//					}
//				}
//			}
//		} else {
//			if (sSafeFlg.equals("Y")) {
//				if (!(sSafeType.equals("0") || sSafeType.equals(""))) {
//					if (sSafeModelName.equals("")) {
//						// TheApplication().RaiseErrorText("안전인증모델명을 입력하세요");
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("prd.msg.287", new String[]{"안전인증모델명"}));
//						return prdmMain;
//					}
//					if (sSafeNum.equals("")) {
//						// TheApplication().RaiseErrorText("인증번호를 입력하세요");
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("prd.msg.287", new String[]{"인증번호"}));
//						return prdmMain;
//					}
//					if (sSafeAuthDate.equals("")) {
//						// TheApplication().RaiseErrorText("인증일을 입력하세요");
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("prd.msg.287", new String[]{"인증일"}));
//						return prdmMain;
//					}
//					if (sSafeAuthOrgan.equals("")) {
//						// TheApplication().RaiseErrorText("인증기관을 입력하세요");
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("prd.msg.287", new String[]{"인증기관"}));
//						return prdmMain;
//					}
//				}
//			}
//		}
//		logger.debug("BusComp_PreWriteRecord=>3");
////		Date date = SysUtil.getCurrTime();
////		String sysdate = DateUtils.format(date, "yyyyMMddHHmmss");
//		if (gCopyProd.equals("Y")) {
//			// this.SetFieldValue("Copy Product", gCopyProd); //복사상품여부
//			prdmMain.setCopyPrdYn("Y");
//		}
//		logger.debug("BusComp_PreWriteRecord=>4");
//		// var sOptionProd = this.GetFieldValue("Option Prod Flag"); //스타일직접입력여부
//		String sOptionProd = "";
//		if (sOptionProd.equals("Y")) {
//			if (!ProdType.equals("S")) {
//				// TheApplication().RaiseErrorText("스타일 직접입력 상품은 상품형태가 스타일 상품만 가능합니다");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.288"));
//				return prdmMain;
//			}
//			if ((!sCarrier.equals("3100")) && (!sCarrier.equals("3200")) && (!sCarrier.equals("3400"))) // 직송(설치)-업체수거, 직송(택배)-업체수거, 직송(우편)-업체수거
//			{
//				// TheApplication().RaiseErrorText("스타일 직접입력 상품은 직송(택배)-업체수거, 직송(설치)-업체수거, 직송(우편)-업체수거 형태만 가능합니다");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.289"));
//				return prdmMain;
//			}
//			if (AutoOrder.equals("Y")) {
//				// TheApplication().RaiseErrorText("스타일 직접입력 상품은 자동주문이 불가합니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.290"));
//				return prdmMain;
//			}
//			if (prdmMain.getRepPrdYn().equals("Y")) {
//				// TheApplication().RaiseErrorText("스타일 직접입력 상품은 대표상품으로 등록할 수 없습니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.291"));
//				return prdmMain;
//			}
//		}
//		logger.debug("BusComp_PreWriteRecord=>5");
//		if (sWareHousing.equals("")) {
//			if (sCarrier.equals("1000") || sCarrier.equals("1100") || sCarrier.equals("1200")
//					|| sCarrier.equals("1300") || sCarrier.equals("1400") || sCarrier.equals("1500")) {
//				// TheApplication().RaiseErrorText("입고유형을 입력하셔야 합니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.292"));
//				return prdmMain;
//			}
//		}
//		if (sCarrier.equals("1000") || sCarrier.equals("1100") || sCarrier.equals("1200") || sCarrier.equals("1300")
//				|| sCarrier.equals("1400") || sCarrier.equals("1500") || sCarrier.equals("2000")
//				|| sCarrier.equals("2100") || sCarrier.equals("2200") || sCarrier.equals("2300")
//				|| sCarrier.equals("2400") || sCarrier.equals("2500") || sCarrier.equals("2600")
//				|| sCarrier.equals("2700") || sCarrier.equals("2800")) {
//			if ((!sCarrierMethod.equals("HJ")) && (!sCarrierMethod.equals("HF")) && (!sCarrierMethod.equals("DH"))) {
//				// TheApplication().RaiseErrorText("현 배송수거 방법은 대한통운과 한진택배만 가능합니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.293"));
//				return prdmMain;
//			}
//		} else if (sCarrier.equals("3400")) {
//			if ((!sCarrierMethod.equals("ER"))) {
//				// TheApplication().RaiseErrorText("현 배송수거 방법은 우체국등기만 가능합니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.294"));
//				return prdmMain;
//			}
//		}
//		if (gSwFlg.equals("R") && OFA.equals("Y")) {
//			// TheApplication().RaiseErrorText("문자쿠폰은 자동주문이 불가능합니다." );
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.295"));
//			return prdmMain;
//		} /* 20100413 KMK 문자쿠폰은 자동주문이 불가능하도록 수정 */
//		logger.debug("BusComp_PreWriteRecord=>6");
//		if (!sWareHousing.equals("")) {
//			if ((!sCarrier.equals("1000") && (!sCarrier.equals("1100")) && (!sCarrier.equals("1200"))
//					&& (!sCarrier.equals("1300")) && (!sCarrier.equals("1400")) && (!sCarrier.equals("1500")))) {
//				// TheApplication().RaiseErrorText("입고유형을 입력하시면 안됩니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.296"));
//				return prdmMain;
//			}
//		}
//		logger.debug("BusComp_PreWriteRecord=>7");
//		// 종료일자 Setting
//		if (PartNum.equals("") && prdmMain.getSaleEndDtm().equals("")) // 판매종료일자
//		{
//			if (supDtlInfo.getTxnEndDt().equals("")) // 협력사.거래종료일자
//			{
//				// this.SetFormattedFieldValue("End Date","2999-12-31 23:59:59");
//				prdmMain.setTxnEndDt("29991231235959");
//			} else {
//				// this.SetFormattedFieldValue("End Date",this.GetFormattedFieldValue("Vendor End Date"));
//				prdmMain.setTxnEndDt(supDtlInfo.getTxnEndDt());
//			}
//		}
//		logger.debug("BusComp_PreWriteRecord=>8");
//		logger.debug("StringUtils.NVL(prdmMain.getPrdClsCd()==" + StringUtils.NVL(prdmMain.getPrdClsCd()));
//		if (StringUtils.NVL(prdmMain.getRegChanlGrpCd()).equals("GE")
//		// TheApplication().GetProfileAttr("gReg_Channel") != "M"
//		) {
//			/*
//			 * var sProdLnName1 = this.GetFieldValue("Grand Parent Primary Line Name") ; //상품분류코드 var sProdLnName2 =
//			 * this.GetFieldValue("Parent Primary Line Name") ; var sProdLnName3 = this.GetFieldValue("Primary Line Name") ; var sProdLnName4 =
//			 * this.GetFieldValue("Primary Product Line Name") ;
//			 */
////			String sProdLnName1 = StringUtils.NVL(prdmMain.getLrgClsList()); // 상품분류코드
////			String sProdLnName2 = StringUtils.NVL(prdmMain.getMidClsList()); // 상품분류코드
////			String sProdLnName3 = StringUtils.NVL(prdmMain.getSmlClsList()); // 상품분류코드
////			String sProdLnName4 = StringUtils.NVL(prdmMain.getDtlClsList()); // 상품분류코드
//			String sProdLnName5 = StringUtils.NVL(prdmMain.getPrdClsCd()); // 상품분류코드
//			// if (sProdLnName1.equals( "") || sProdLnName2.equals( "" )|| sProdLnName3.equals( "" ) || sProdLnName4.equals("") )
//			if (sProdLnName5.equals("")) {
//				// TheApplication().RaiseErrorText("상품분류명을 입력하십시오.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.297"));
//				return prdmMain;
//			}
//			// var sProdBrandName = this.GetFieldValue("Prod Brand Name"); // 브랜드명
//			BigDecimal sProdBrandName = prdmMain.getBrandCd();
//			if (sProdBrandName.equals("")) { // var sProdLineCode = this.GetFieldValue("Primary Product Line Code");
//				// var sProdLineType = sProdLineCode.substring(0,1);
//				String sProdLineCode = StringUtils.NVL(prdmMain.getPrdClsCd());// 상품분류코드
//				String sProdLineType = StringUtils.NVL(sProdLineCode.substring(0, 1));
//
//				if (sProdLineType.equals("A") || sProdLineType.equals("B")) {
//					// TheApplication().RaiseErrorText("브랜드명을 입력하십시오.");
//
//					// return(CancelOperation);
//					// prdmMain.setRetCd("-1");
//					// prdmMain.setRetMsg("브랜드명을 입력하십시오.");
//					return prdmMain;
//				}
//			}
//		}
//		// 상품명, 제조사, 원산지 길이 Checking ,현재는 상품명만 checking
//		/*************************************************/
//		Len_Check(); // 펑션추가
//		/*************************************************/
//		logger.debug("BusComp_PreWriteRecord=>9");
//		// Kit 상품이면 완전매입/조건부매입만 가능하도록
//		// C20090107_81946 2009_001 방송 경품 업무 Process 개선
//		if ((GiftType.equals("07") || GiftType.equals("08")) && (!PurchType.equals("03"))) {
//			// TheApplication().RaiseErrorText("업체부담 경품은 수수료분매입 매입형태만 가능합니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.298"));
//			return prdmMain;
//		}
//		// Style상품이 아닐 경우 Sale type은 상품속성별주문이나 재고범위내 주문
//		if ((!ProdType.equals("S")) && (SaleType.equals("01"))) {
//			// TheApplication().RaiseErrorText("속성대표상품별주문은 스타일상품만 가능합니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.299"));
//			return prdmMain;
//		}
//		// 상품권상품은 상품권 액면가를 필수입력(JewelLee 03.04.07)
//		if (ProdType.equals("C") && GiftCertiAmt.equals("")) // 당사상품권여부
//		{
//			// TheApplication().RaiseErrorText("상품권 상품은 상품권 액면가를 반드시 입력해야합니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.300"));
//			return prdmMain;
//		}
//		// else if ((!ProdType.equals("C")) && (!GiftCertiAmt.equals(""))) //당사상품권여부
//		// {
//		// this.SetFieldValue("Gift Certificate Amt", "");
//		// prdmMain.setGftcertFacePrc(null);
//		// }
//		logger.debug("BusComp_PreWriteRecord=>10");
//		// Style상품의 EC보충Point 관리는 속성별 주문가능수량관리에서
//		// if ((ProdType.equals("S")) && prdAttrPrdm.getSafeStockQty().equals("")) //재고.안전재고수량
//		// {
//		// this.SetFieldValue("EC Safety Qty","");
//		/*
//		 * prdmMain.setSafeStockQty(null); //TheApplication().RaiseErrorText("스타일상품의 EC보충Point는 주문가능수량입력 화면에서 가능합니다."); prdmMain.setRetCd("-1");
//		 * prdmMain.setRetMsg("스타일상품의 EC보충Point는 주문가능수량입력 화면에서 가능합니다."); return prdmMain;
//		 */
//		// }
//		// 배송수거에 따른 필수 필드 및 관련 필드 checking
//		// *************************************//
//		// Carrier_Check(); //펑션추가
//		// *************************************//
//		// IVR주문상품일 경우 필드값 체크
//		if ((AutoOrder.equals("Y")) && (AutoName.equals("") || AutoType.equals(""))) {
//			// TheApplication().RaiseErrorText("자동주문상품명과, 자동주문상품분류를 입력하세요");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.301"));
//			return prdmMain;
//		}
//		// C20090107_81946 2009_001 방송 경품 업무 Process 개선
//		if ((GiftType.equals("00") || GiftType.equals("01") || GiftType.equals("02") || GiftType.equals("05"))
//				&& (TaxType.equals("04") || TaxType.equals("05"))) {
//			// TheApplication().RaiseErrorText("세금구분과 사은품경품구분을 확인하세요");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.302"));
//			return prdmMain;
//		}
//		//[SR02160729022][2016.08.31][김영현]:경품 공동부담 개발요청 - 업체부담 경품 과세 제한 로직 제거
//		/*
//		if ((GiftType.equals("07") || GiftType.equals("08")) && ((!TaxType.equals("05")))) {
//			// TheApplication().RaiseErrorText("업체부담 경품은 경품면세 세금구분만 가능합니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.303"));
//			return prdmMain;
//		}
//		*/
//		logger.debug("BusComp_PreWriteRecord=>11");
//		// 사은품,경품구분에서 매입형태는 완전매입 Chekcing
//		if (GiftType.equals("01") || GiftType.equals("03") || GiftType.equals("04")) {
//			if (PurchType.equals("01") || PurchType.equals("04")) {
//			} else {
//				// TheApplication().RaiseErrorText("매입형태는 완전/조건부매입이어야 합니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.304"));
//				return prdmMain;
//			}
//		}
//		logger.debug("BusComp_PreWriteRecord=>12");
//		// 사은품경품구분이 판매상품이 아니면 세금계산서발행이 불가하도록.
//		if ((!GiftType.equals("00"))) {
//			// this.SetFieldValue("Tax Paper Flg", "N"); //세금계산서발행여부
//			prdmMain.setTaxInvIssueYn("N");
//		}
//		// 조건부매입상품 : EC배송일 안내 안함.
//		if (PurchType.equals("04") && sECATPFlg.equals("Y")) {
//			// TheApplication().RaiseErrorText("조건부매입상품은 EC배송일 안내상품으로 등록할 수 없습니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.305"));
//			return prdmMain;
//		}
//		// 배송비상품 Check --일반, 직송(*)-업체수거, 자동주문불가
//		// var sErrMsg = "";
//		String sErrMsg = "";
//		logger.debug("BusComp_PreWriteRecord=>13");
//		if (GiftType.equals("05")) {
//			// var sCar = GetFieldValue("Carrier");
//			String sCar = StringUtils.NVL(prdmMain.getDlvPickMthodCd());
//
//			if (!ProdType.equals("G")) // 배송비 상품은 일반(Single)으로
//				sErrMsg = Message.getMessage("prd.msg.306");
//			if ((!sCar.equals("3100")) && (!sCar.equals("3200")) && (!sCar.equals("3400"))) // 직송(*)-업체수거만 가능
//				sErrMsg = Message.getMessage("prd.msg.307");
//			if (AutoOrder.equals("Y"))
//				sErrMsg = Message.getMessage("prd.msg.308");
//			if (ShipFeeFlg.equals("Y"))
//				sErrMsg = Message.getMessage("prd.msg.309");
//			if (!sErrMsg.equals("")) {
//				// TheApplication().RaiseErrorText(sErrMsg);
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(sErrMsg);
//				return prdmMain;
//			}
//		}
//		logger.debug("BusComp_PreWriteRecord=>14");
//		// 유료배송상품 Check -- 자동주문불가, 유료배송서비스업체, 직송만...
//		if (ShipFeeFlg.equals("Y")) {
//			if (AutoOrder.equals("Y")) {
//				sErrMsg = Message.getMessage("prd.msg.310");
//			} else {
//				// sErrMsg = Check_Vendor_for_ShipFee();
//			}
//			if ((!sErrMsg.equals(""))) {
//				// TheApplication().RaiseErrorText(sErrMsg);
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(sErrMsg);
//				return prdmMain;
//			}
//		}
//		logger.debug("BusComp_PreWriteRecord=>15");
//		// MP대표상품,e스토어대표상품 Check - 직송, 수수료매입, 일반, 대표상품, 거래처
//		if (sSoftwareFlg.equals("H") || sSoftwareFlg.equals("S")) {
//			sErrMsg = Check_SWFlag(sSoftwareFlg);
//			if (!sErrMsg.equals("")) {
//				// TheApplication().RaiseErrorText(sErrMsg);
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(sErrMsg);
//				return prdmMain;
//			}
//		}
//		// 해외쇼핑 대표상품 상품정보 체크 ( 2007.07.17 C20070620_81547 )
//		if (sGmsProdType.equals("A") || sGmsProdType.equals("B") || sGmsProdType.equals("C")) // 상품구분코드 = '10' ||상품구분코드 = '20'||제거
//		{
//			sErrMsg = Check_GMS_Prod(sGmsProdType); // 펑션추가
//			if (!sErrMsg.equals("")) {
//				// TheApplication().RaiseErrorText(sErrMsg);
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(sErrMsg);
//				return prdmMain;
//			}
//		}
//		logger.debug("BusComp_PreWriteRecord=>16");
//		// --------------
//		// 보험상품인 경우 보험상품보험사명 필수 입력( 2006.4.12 )
//		// ActivateField("Insurance Company Desc");
//		if (sSoftwareFlg.equals("I")) {
//			if (prdmMain.getInsuCoCd().equals("")) // 보험사코드
//			{
//				// TheApplication().RaiseErrorText("보험상품은 반드시 보험상품 보험사명을 입력해야합니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.311"));
//				return prdmMain;
//			}
//		} else {
//			// this.SetFieldValue("Insurance Company Desc","");
//			// this.SetFieldValue("Insurance Company","");
//			prdmMain.setInsuCoCd("");
//		}
//		logger.debug("BusComp_PreWriteRecord=>16");
//		// ---------
//		// C20080710_96596 limhju 08.07.31
//		if (((!sSPCareStdt.equals("")) && sSPCareEddt.equals(""))
//				|| (sSPCareStdt.equals("") && (!sSPCareEddt.equals("")))) {
//			// TheApplication().RaiseErrorText("기간주문유의사항 시작일,종료일은 반드시 같이 설정하세요");
//			// return(CancelOperation);
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.312"));
//			return prdmMain;
//		} else if ((!sSPCareStdt.equals("")) && (!sSPCareEddt.equals(""))
//				&& Integer.parseInt(sSPCareStdt) > Integer.parseInt(sSPCareEddt)) {
//			// TheApplication().RaiseErrorText("기간주문유의사항 시작일은 종료일보다 작아야합니다");
//			// return(CancelOperation);
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.313"));
//			return prdmMain;
//		}
//		logger.debug("BusComp_PreWriteRecord=>17");
//		// EC 표준출고일 계산
//		EC_Prod(); // 펑션추가
//		String sEcDvlyDayCheck = "";
////		String gECDeliveryDayCheck = "";
//		// var iDvryAPTDay ;
//		String iDvryAPTDay = "";
//
//		if (sEcDvlyDayCheck.equals("Y")) {
//			iDvryAPTDay = Set_EC_Dvly_Day2(prdmMain); // 펑션추가
//
//			if (sOrderProd.equals("Y") || sReserveProd.equals("Y")) {
//			} else {
////				gECDeliveryDayCheck = "Y";
//				BigDecimal sas = new BigDecimal(iDvryAPTDay);
//
//				// this.SetFieldValue("EC Delivery ATP Day", iDvryAPTDay); //표준출고일 CHANL_CD = 'P'
//				prdmMain.setStdRelsDdcnt(sas);
//			}
//
//			sEcDvlyDayCheck = "N";
//
//			if (!prdmMain.getRegChanlGrpCd().equals("E")) {
//				/*
//				 * if ( gNoQaModifiedFlagCheck == "Y" && gNoQaModifiedFlag != "") //QA검사여부 = 'N'
//				 *
//				 * { this.SetFieldValue("No QA Modified Flag", gNoQaModifiedFlag ); }
//				 */
//			}
//		}
//
//		// Set_DM_Dvly_Day(); //표준출고일 CHANL_CD = 'D'
//		PrdmMain prdmMaindev = Set_DM_Dvly_Day(prdmMain, supDtlInfo);
//		prdmMain.setStdRelsDdcnt(prdmMaindev.getStdRelsDdcnt());
//		String gProdNewRec = "";
//		String gNoQaModifiedFlag = "";
//		logger.debug("BusComp_PreWriteRecord=>18");
//		if (gProdNewRec.equals("Y") && !StringUtils.NVL(prdmMain.getRegChanlGrpCd()).equals("E")) {
//			// this.SetFieldValue("Show In Internet", gNoQaModifiedFlag ); //제거
//
//			if (gNoQaModifiedFlag.equals("Y")) {
//				// this.SetFieldValue("Prod Certification Post Stage","Q"); //분류검증후결재코드
//				prdmMain.setClsChkAftAprvCd("Q");
//			}
//		}
//		logger.debug("BusComp_PreWriteRecord=>19");
//		// -----------------
//		// iBIS JewelLee
//		/*
//		 * var sProdOrgNew = ""; var sProdOrgBasic = this.GetFieldValue("Product Org Basic"); //상품기본구성내용 var sProdOrgAdd =
//		 * this.GetFieldValue("Product Org Additional"); //상품추가구성내용 var sProdOrgGift = this.GetFieldValue("Product Org Gift"); //상품사은품구성내용 var
//		 * sprodOrgMsg = "";
//		 */
//		String sProdOrgNew = "";
//		String sProdOrgBasic = StringUtils.NVL(prdPrdD.getPrdBaseCmposCntnt()); // 상품기본구성내용
//		String sProdOrgAdd = StringUtils.NVL(prdPrdD.getPrdAddCmposCntnt()); // 상품추가구성내용
//		String sProdOrgGift = StringUtils.NVL(prdPrdD.getPrdGftCmposCntnt()); // 상품사은품구성내용
//		String sprodOrgMsg = "";
//		logger.debug("BusComp_PreWriteRecord=>20");
//		if ((sProdOrgBasic + sProdOrgAdd + sProdOrgGift + sprodOrgMsg).equals("")) {
//			if ((!sProdOrgBasic.equals("")))
//				sProdOrgNew = "[기본]" + sProdOrgBasic;
//			if ((!sProdOrgAdd.equals("")))
//				sProdOrgNew = sProdOrgNew + "[추가]" + sProdOrgAdd;
//			if ((!sProdOrgGift.equals("")))
//				sProdOrgNew = sProdOrgNew + "[사은품]" + sProdOrgGift;
//			if ((!sprodOrgMsg.equals("")))
//				sProdOrgNew = sProdOrgNew + "[기타]" + sprodOrgMsg;
//		}
//
//		if ((!sProdOrgNew.equals("")))
//			ProdOrg = sProdOrgNew;
//		// ---------------------
//
//		// SAP I/F로 구성정보에 Enter값 입력 방지( 2003.01.14 JewelLee )
//		/*
//		 * var inx = 0; var ProdOrgLen = ProdOrg.length; var CheckChar = ""; var FilterProdOrg = "";
//		 */
//		int inx = 0;
//		int ProdOrgLen = ProdOrg.length();
//		String CheckChar = "";
//		String FilterProdOrg = "";
//		logger.debug("BusComp_PreWriteRecord=>21");
//		for (inx = 0; inx < ProdOrgLen; inx++) {
//			CheckChar = ProdOrg.substring(inx, inx + 1);
//			/*
//			 * if (Clib.isascii(CheckChar) && (CheckChar == '\n' || CheckChar == '\r')) { CheckChar = " "; }
//			 */
//			FilterProdOrg = FilterProdOrg + CheckChar;
//		}
//		// this.SetFieldValue("Product Org",FilterProdOrg);
//		prdmMain.setPrdInfoCmposCntnt(FilterProdOrg);
//		// ---------------
//		// C20080220_20249 인터넷 도서몰 구축 2008.04.02 jewelLee
//		Check_BookMall(); // 펑선추가
//
//		// 지정택배수거 2009.04.08 jewelLee
//		Check_Fixed_Delivery(); // 펑선추가
//
//		// 2009.09.25 SNY 편의점 판품 및 지정위탁 배송수거에 따른 주소 관리 chekc 로직
//		Check_CVS_Ret_Consign_Delivery(); // 펑선추가
//
//		// 수정일자, 수정자 Setting
//
//		// this.SetFormattedFieldValue("Updated Date", TimeBuf);
//		// this.SetFieldValue("Updated Id", TheApplication().LoginId());
//		/**//*
//			 * if (TheApplication().GetProfileAttr("gReg_Channel") == "E" ) //eShop운영자에서 등록 { if (TheApplication().GetProfileAttr("gReg_Skip_SAP") ==
//			 * "Y" ) //가격변경 { if (TheApplication().GetProfileAttr("gCVSChangFlag") == "Y") { this.SetFieldValue("IF Ctl Flg",""); //2009.09.25 SNY
//			 * 편의점반품 필드 eShop전송 } else { this.SetFieldValue("IF Ctl Flg","S"); //eShop, SAP 전송방지 } } else { if
//			 * (TheApplication().GetProfileAttr("gCVSChangFlag") == "Y") { this.SetFieldValue("IF Ctl Flg",""); //2009.09.25 SNY 편의점반품 필드 eShop전송 }
//			 * else { this.SetFieldValue("IF Ctl Flg","E"); //eShop 전송방지 } } } else { if (TheApplication().GetProfileAttr("gReg_Skip_SAP") == "Y" ) {
//			 * this.SetFieldValue("IF Ctl Flg","P"); //SAP 전송방지 } else { this.SetFieldValue("IF Ctl Flg",""); //전송 } }
//			 */
//		// gSAPCheck = sSAPCheck; //gSAPCheck 요값의 보존
//		// FS_LOG ("gReg_Channel", TheApplication().GetProfileAttr("gReg_Channel"));
//		// FS_LOG ("gReg_Skip_SAP", TheApplication().GetProfileAttr("gReg_Skip_SAP"));
//		// FS_LOG ("IF Ctl Flg", this.GetFieldValue("IF Ctl Flg"));
//		// return (ContinueOperation);
//		logger.debug("BusComp_PreWriteRecord=>22");
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg(Message.getMessage("prd.msg.314"));
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
	
	
	
	
	
	// function BusComp_SetFieldValue (FieldName)
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : BusComp_SetFieldValue
	 */
//	public PrdmMain BusComp_SetFieldValue(String FieldName, PrdmMain prdmMain, PrdOrdPsblQtyDinsert prdOrdPsblQtyD,
//			SupDtlInfo supDtlInfo, PrdprcHinsert prdprcHinsert) {
//
//		if (supDtlInfo == null) {
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.315"));
//			return prdmMain;
//		}
//
//		if (FieldName.equals("Free Gift Type")) // 사은품유형코드
//		{
//			Limit_Check(); // 펑션추가
//		}
//
//		if (FieldName.equals("Prod Type")) // 상품유형코드
//		{
//			// var sPrdType = GetFieldValue("Prod Type");
//			// var sAOF = GetFieldValue("Auto Order Flg"); //자동주문가능여부
//			String sPrdType = StringUtils.NVL(prdmMain.getPrdTypCd());
//			String sAOF = StringUtils.NVL(prdmMain.getAutoOrdPsblYn());
//
//			if (sPrdType.equals("C") && sAOF.equals("Y")) // 당사상품권여부 = 'Y' && 자동주문가능여부 = 'Y'
//			{
//				// this.SetFieldValue("Auto Order Flg", "N");
//
//				// TheApplication().RaiseErrorText("당사상품권은 카드결재가 불가능하므로\n자동주문에 노출할 수 없습니다.\n\n자동주문여부를 확인하십시오");
//				// return(CancelOperation);
//				prdmMain.setAutoOrdPsblYn("N");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.316"));
//				return prdmMain;
//			} else if (sPrdType.equals("C")) // 당사상품권여부 = 'Y'
//			{
//				// this.SetFieldValue("Auto Order Flg", "N");
//				prdmMain.setAutoOrdPsblYn("N");
//
//			}
//		}
//		// 20071210 인증정보
//		/*
//		 * if( FieldName == "Safe Type" ) //안전인증구분코드 { //var sSafeType = GetFieldValue("Safe Type"); String sSafeType = if(sSafeType=="") {
//		 * this.SetFieldValue("Safe Model Name", ""); //안전인증모델명 this.SetFieldValue("Safe Auth Organ", ""); //안전인증기관코드
//		 * this.SetFieldValue("Safe Auth Date", ""); //안전인증일자 this.SetFieldValue("Safe Num", ""); //안전인증번호
//		 *
//		 * }else if(sSafeType=="0") { this.SetFieldValue("Safe Model Name", ""); this.SetFieldValue("Safe Auth Organ", "0");
//		 * this.SetFieldValue("Safe Auth Date", ""); this.SetFieldValue("Safe Num", "");
//		 *
//		 * }else { this.SetFieldValue("Safe Auth Organ Desc", "");
//		 *
//		 * } } if (FieldName == "Safe Auth Date")//안전인증일자 {
//		 *
//		 * var dNow = new Date(); var cDate = Clib.rsprintf("%04d-%02d-%02d %02d:%02d:%02d", //sysdate dNow.getFullYear(),
//		 * dNow.getMonth()+Now.getDate()+1, 0,0,0);
//		 *
//		 * var dExpDay = GetFormattedFieldValue("Safe Auth Date"); if (dExpDay > cDate) //SYSDATE { this.SetFieldValue("Safe Auth Date", "");
//		 * TheApplication().RaiseErrorText("인증일은 현재일보다 미래일 수 없습니다."); return(CancelOperation); } }
//		 */
//
//		/*****/
//		String gECDeliveryDayCheck = "";
//
//		// ******//
//		// if ( gCopyProdId != "") //복사소스상품코드
//		if (prdmMain.getCopySrcPrdCd() != null) {
//			// sEcDvlyDayCheck = "Y";
//		} else {
//			if (FieldName.equals("Primary Product Line Name") || // 상품분류코드
//					FieldName.equals("MD Id")) // 운영MDID
//			{
//				// sEcDvlyDayCheck = "Y";
//				// gNoQaModifiedFlagCheck = "Y"; //QA검사여부 = 'N'
//			} else if (FieldName.equals("Carrier") || // 배송수거방법코드
//					FieldName.equals("Representative Prod Flg") || // 대표상품여부
//					FieldName.equals("Software Flg") || // 무형상품유형코드
//					FieldName.equals("Reserve Prod") || // 예약판매상품여부
//					FieldName.equals("Order Prod")) // 주문제작여부
//			{
//				// sEcDvlyDayCheck = "Y";
//			}
//		}
//
//		if (FieldName.equals("EC Delivery ATP Day")) // 표준출고일수 (CHANL= 'P')
//		{
//			// var sOrder = this.GetFieldValue( "Order Prod" ); //주문제작여부
//			// var sReserve = this.GetFieldValue( "Reserve Prod" ); //예약판매상품여부
//			String sOrder = StringUtils.NVL(prdmMain.getOrdMnfcYn());
//			String sReserve = StringUtils.NVL(prdmMain.getRsrvSalePrdYn());
//
//			if (sReserve.equals("Y")) {
//				gECDeliveryDayCheck = "Y";
//			}
//
//			if (gECDeliveryDayCheck.equals("N") && (!sOrder.equals("Y"))) {
//				// TheApplication().RaiseErrorText("EC표준출고일은 주문제작상품만 수정할 수 있습니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.317"));
//				return prdmMain;
//
//			}
//
//		}
//
//		if (FieldName.equals("Broad Prod Flg")) // 방송중판매가능여부
//		{
//			/*****/
//			// Check_Owner( ); //펑션추가
//			/*****/
//		}
//
//		// 2009.09.25 SNY 편의점택배 관련 시작
//		// 상품분류(세)가 선택될때 상품분류의 편의점 반품 여부를 상품의 편의점 반품 여부에 Setting한다.
//		if (FieldName.equals("Primary Product Line CVS Ret Flag")) // 상품분류.편의점반품여부
//		{
//			/*
//			 * var sPL_CVSRetFlag = ""; //상품세분류 편의점 반품여부 var sAC_CVSRetFlag = ""; //거래처 편의점 반품여부 var sVendorCode = ""; //협력사코드 var sProdLnCode = "";
//			 * //상품분류코드 var sCarrier = ""; //배송수거방법코드
//			 */
//
//			String sPL_CVSRetFlag = ""; // 상품세분류 편의점 반품여부
//			String sAC_CVSRetFlag = ""; // 거래처 편의점 반품여부
//			String sVendorCode = ""; // 협력사코드
////			String sProdLnCode = ""; // 상품분류코드
//			String sCarrier = ""; // 배송수거방법코드
//
//			sPL_CVSRetFlag = StringUtils.NVL(prdmMain.getCvsDlvsRtpYn()); // 상품분류.편의점반품여부
//			sAC_CVSRetFlag = StringUtils.NVL(supDtlInfo.getCvsRtpYn()); // 협력사.편의점반품여부
//			sVendorCode = StringUtils.NVL(supDtlInfo.getSupCd()); // 협력사코드
//			sCarrier = StringUtils.NVL(prdmMain.getDlvPickMthodCd()); // 배송수거방법코드
//
//			if (sPL_CVSRetFlag.equals("Y")) {
//				if (sAC_CVSRetFlag.equals("Y")
//						&& !(sCarrier.equals("1100") || sCarrier.equals("1400") || sCarrier.equals("3100") || sCarrier
//								.equals("5000"))) {
//					// this.SetFieldValue("CVS Ret Flag", "Y"); //편의점반품여부
//					prdmMain.setCvsDlvsRtpYn("Y");
//				} else if (sVendorCode.equals("")
//						&& !(sCarrier.equals("1100") || sCarrier.equals("1400") || sCarrier.equals("3100") || sCarrier
//								.equals("5000"))) {
//					// this.SetFieldValue("CVS Ret Flag", "Y");
//					prdmMain.setCvsDlvsRtpYn("Y");
//				} else {
//					// this.SetFieldValue("CVS Ret Flag", "N");
//					prdmMain.setCvsDlvsRtpYn("N");
//				}
//			} else {
//				// this.SetFieldValue("CVS Ret Flag", "N");
//				prdmMain.setCvsDlvsRtpYn("N");
//			}
//
//		}
//
//		// 거래처가 선택될때 거래처의 편의점 반품 여부를 상품의 편의점 반품여부에 Setting한다.
//		if (FieldName.equals("Account CVS Ret Flag")) // 협력사.편의점반품여부
//		{
//			/*
//			 * var sPL_CVSRetFlag = ""; //상품세분류 편의점 반품여부 var sAC_CVSRetFlag = ""; //거래처 편의점 반품여부 var sVendorCode = ""; var sProdLnCode = ""; var
//			 * sCarrier = "";
//			 */
//			String sPL_CVSRetFlag = ""; // 상품세분류 편의점 반품여부
//			String sAC_CVSRetFlag = ""; // 거래처 편의점 반품여부
////			String sVendorCode = "";
//			String sProdLnCode = "";
//			String sCarrier = "";
//
//			sAC_CVSRetFlag = StringUtils.NVL(supDtlInfo.getCvsRtpYn());// 협력사.편의점반품여부
//			sPL_CVSRetFlag = StringUtils.NVL(prdmMain.getCvsDlvsRtpYn()); // 상품분류.편의점반품여부
//			sProdLnCode = StringUtils.NVL(prdmMain.getPrdClsCd());// 상품분류코드
//			sCarrier = prdmMain.getDlvPickMthodCd(); // 배송수거방법코드
//
//			if (sAC_CVSRetFlag.equals("Y")) {
//				if (sPL_CVSRetFlag.equals("Y")
//						&& !(sCarrier.equals("1100") || sCarrier.equals("1400") || sCarrier.equals("3100") || sCarrier
//								.equals("5000"))) {
//					// this.SetFieldValue("CVS Ret Flag", "Y");
//					prdmMain.setCvsDlvsRtpYn("Y");
//				} else if (sProdLnCode.equals("")
//						&& !(sCarrier.equals("1100") || sCarrier.equals("1400") || sCarrier.equals("3100") || sCarrier
//								.equals("5000"))) {
//					// this.SetFieldValue("CVS Ret Flag", "Y");
//					prdmMain.setCvsDlvsRtpYn("Y");
//				} else {
//					// this.SetFieldValue("CVS Ret Flag", "N");
//					prdmMain.setCvsDlvsRtpYn("N");
//				}
//			} else {
//				// this.SetFieldValue("CVS Ret Flag", "N");
//				prdmMain.setCvsDlvsRtpYn("N");
//			}
//		}
//		// 2009.09.25 SNY 편의점택배 관련 끝
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
	
	

	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : BusComp_WriteRecord
	 */
//	public PrdmMain BusComp_WriteRecord(PrdmMain prdmMain, PrdOrdPsblQtyDinsert prdOrdPsblQtyD, SupDtlInfo supDtlInfo,
//			PrdprcHinsert prdprcHinsert, SafeCertPrd safeCertPrd, PrdStockDinsert prdAttrPrdm, PrdPrdDinsert prdPrdD,
//			PrdAttrPrdMinsert prdAttrPrdMinsert) {
//		// function BusComp_WriteRecord ()
//		// {
//
//		// FS_LOG("Internal Sales Price Pre", this.GetFieldValue("Internal Sales Price Pre"));
//		// try {
//
//		/*
//		 * var sProdId = this.GetFieldValue("Id"); //상품코드 var sProdLnId = this.GetFieldValue("Primary Product Line Id"); //상품분류코드 var sProdLnIdC =
//		 * this.GetFieldValue("Primary Product Line Id for Custom"); //상품분류코드 var sPartNo = this.GetFieldValue("Part #"); //상품코드 var sName =
//		 * this.GetFieldValue("Name"); //제거 var sProdName = this.GetFieldValue("Product Name"); //상품명 var sNewCheck =
//		 * this.GetFieldValue("New Create"); //등록자ID var sProdType = this.GetFieldValue("Prod Type"); //상품유형코드 var oBusObj =
//		 * TheApplication().GetBusObject("Admin Product Definition"); // var iDvryAPTDay ;
//		 */
////		BigDecimal sProdId = prdmMain.getPrdCd(); // 상품코드
////		String sProdLnId = prdmMain.getPrdClsCd(); // 상품분류코드
////		String sProdLnIdC = prdmMain.getPrdClsCd(); // 상품분류코드
////		BigDecimal sPartNo = prdmMain.getPrdCd(); // 상품코드
//		// String sName = this.GetFieldValue("Name"); //제거
////		String sProdName = prdmMain.getPrdNm(); // 상품명
////		String sNewCheck = prdmMain.getSessionUserId(); // 등록자ID
////		String sProdType = prdmMain.getPrdTypCd(); // 상품유형코드
////		String oBusObj = ""; //
////		String iDvryAPTDay;
//
//		return prdmMain;
//
//	}
	// SOURCE_CLEANSING : END
	
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Carrier_Check
	 */
//	public void Carrier_Check() {
//
//	}
	// SOURCE_CLEANSING : END
	
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Carrier_Check
	 */
//	public PrdmMain Carrier_Check(PrdmMain prdmMain, PrdOrdPsblQtyDinsert prdOrdPsblQtyD, SupDtlInfo supDtlInfo,
//			PrdprcHinsert prdprcHinsert, SafeCertPrd safeCertPrd, PrdStockDinsert prdAttrPrdm, PrdPrdDinsert prdPrdD,
//			PrdAttrPrdMinsert prdAttrPrdMinsert) {
//		// function Carrier_Check()
//		// {
//
//		/*
//		 * var ProdType = this.GetFieldValue("Prod Type"); //상품유형코드 var Car = this.GetFieldValue("Carrier"); //배송수거방법코드 var CarType =
//		 * this.GetFieldValue("Carrier Method"); //택배사코드 var CarCheck = ""; // var PackFlg = this.GetFieldValue("Packing Flg"); //센터추가포장여부 var sCnt =
//		 * this.GetFieldValue("Sufficient Cnt"); //상품재고.안전재고 var MinOrd = this.GetFieldValue("Min Order"); //상품재고.최소발주수량 var CarCal = Car.charAt(0);
//		 * // var PLCode = this.GetFieldValue("Primary Product Line Code"); //상품분류코드 var PLCal = PLCode.charAt(0); var sWrappingFlag =
//		 * this.GetFieldValue("Wrapping Flg"); //제거 var sShipFeeFlag = this.GetFieldValue("Ship Fee Flg"); //유료배송여부
//		 */
//		String ProdType = prdmMain.getPrdTypCd(); // 상품유형코드
//		String Car = prdmMain.getDlvPickMthodCd(); // 배송수거방법코드
//		String CarType = prdmMain.getDlvsCoCd(); // 택배사코드
//		String CarCheck = ""; //
//		String PackFlg = prdmMain.getCentAddPkgYn(); // 센터추가포장여부
//		BigDecimal sCnt = prdAttrPrdm.getSafeStockQty(); // 상품재고.안전재고
//		BigDecimal MinOrd = prdAttrPrdMinsert.getMinOrdngQty(); // 상품재고.최소발주수량
//		String CarCal = Car.substring(0, 1); //
////		String PLCode = prdmMain.getPrdClsCd(); // 상품분류코드
////		String PLCal = PLCode.substring(0, 1);
//		String sShipFeeFlag = prdmMain.getChrDlvYn(); // 유료배송여부
//
//		// Carrier type이 직택배,직송택배인인경우 택배사입력
//		if (((CarCal.equals("2")) || (Car.equals("3000")) || (Car.equals("3200"))) && (CarType.equals(""))) {
//			// TheApplication().RaiseErrorText("택배사를 입력하세요");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.287", new String[]{"택배사"}));
//		}
//
//		// 직택배인 경우, 한진, 현대, 대한통운.
//		if (CarType.equals("HD") || CarType.equals("HJ") || CarType.equals("HF") || CarType.equals("DH")
//				|| CarType.equals("EP")) {
//			CarCheck = "Y";
//		}
//
//		if (CarCal.equals("2") && !CarCheck.equals("Y")) {
//			// TheApplication().RaiseErrorText("직택배상품은 택배사가 한진, 현대, 대한통운입니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.318"));
//		}
//		String CheckCnt = "";
//		String CheckOrd = "";
//		// Carrier type이 직택배,직송택배인인경우 최소발주수량, 안전재고 입력 불가, 수 0은 가능.
//		if (sCnt.equals(new BigDecimal("0"))) {
//			CheckCnt = "Y";
//		}
//
//		if (MinOrd.equals(new BigDecimal("0"))) {
//			CheckOrd = "Y";
//		}
//
//		if ((CarCal.equals("2") || CarCal.equals("3")) && (!CheckCnt.equals("Y") || !CheckOrd.equals("Y"))) {
//			// TheApplication().RaiseErrorText("직택배,직송택배 상품은 안전재고, 최소발주수량이 0이거나, 입력불가합니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.319"));
//			return prdmMain;
//		}
//
//		// Prod Type이 당사상품권인 경우 Carrier는 1200인지 Checking
//		if ((ProdType.equals("C")) && (!Car.equals("1200"))) // 당사상품권여부 = 'Y'
//		{
//			// TheApplication().RaiseErrorText("당사상품권의 배송수거는 택배귀금속-택배수거입니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.320"));
//			return prdmMain;
//		}
//
//		// 배송수거방법이 귀금속일 때 센터추가포장했는지 check
//		if ((Car.equals("1200") || Car.equals("1500")) && (!PackFlg.equals("Y"))) {
//			// TheApplication().RaiseErrorText("귀금속은 센터추가포장을 해야합니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.321"));
//			return prdmMain;
//		}
//
//		// 선물포장상품은 택배만 가능
//		if (!CarCal.equals("1")) {
//			// TheApplication().RaiseErrorText("선물포장은 택배상품만 가능합니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.322"));
//			return prdmMain;
//		}
//
//		// 유료배송 상품은 직송만 가능
//		if (!CarCal.equals("3") && sShipFeeFlag.equals("Y")) {
//			// TheApplication().RaiseErrorText("유료배송상품은 직송만 가능합니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.323"));
//			return prdmMain;
//		}
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
			
			
			
			
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_BookMall
	 */
//	public void Check_BookMall() {
//
//	}
	// SOURCE_CLEANSING : END
	
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_BookMall
	 */
//	public PrdmMain Check_BookMall(PrdmMain prdmMain, PrdOrdPsblQtyDinsert prdOrdPsblQtyD, SupDtlInfo supDtlInfo,
//			PrdprcHinsert prdprcHinsert, SafeCertPrd safeCertPrd, PrdStockDinsert prdAttrPrdm, PrdPrdDinsert prdPrdD,
//			PrdAttrPrdMinsert prdAttrPrdMinsert) {
//		// function Check_BookMall()
//		// {
//		/*
//		 * C20080220_20249 인터넷 도서몰 구축 jewelLee 2008.04.02 1. 도서업체만 도서상품 등록 가능 2. EC표준출고일 ( Set_EC_Dvly_Day2 에서 처리 ) 3. 분류선택 4. ISBN코드 등록
//		 */
//
//		/*
//		 * var sBundlePackagingType = this.GetFieldValue("Bundle Packaging Type"); //합포장코드 var sVendorPackGroupCode =
//		 * this.GetFieldValue("Vendor Package Group Code"); //협력사.상품합포장코드 var sLineCode = GetFieldValue("Grand Parent Primary Line Code"); //대분류코드 var
//		 * sISBN = GetFieldValue("ISBN"); //도서ISBN번호
//		 */
//		String sBundlePackagingType = prdmMain.getOboxCd(); // 합포장코드
//		String sVendorPackGroupCode = supDtlInfo.getPrdOboxCd(); // 협력사.상품합포장코드
//		String sLineCode = prdmMain.getPrdClsCd(); // 대분류코드
//		// String sISBN = ""; //GetFieldValue("ISBN"); //도서ISBN번호
//
//		if (sBundlePackagingType.equals("2")) {
//			if (!sVendorPackGroupCode.equals("2")) {
//				// TheApplication().RaiseErrorText("도서합포장 업체만 도서상품을 등록할 수 있습니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.325"));
//				return prdmMain;
//			}
//			if (!(sLineCode.equals("A43") || sLineCode.equals("B11"))) {
//				// TheApplication().RaiseErrorText("도서상품은 도서 분류를 선택해야 합니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.326"));
//				return prdmMain;
//			}
//		} else {
//			if (sLineCode.equals("A43") || sLineCode.equals("B11")) {
//				// TheApplication().RaiseErrorText("도서상품이 아닌 경우 도서 분류를 선택할 수 없습니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.327"));
//				return prdmMain;
//			}
//		}
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
			
			
			
	@Override
	public void setPrdSpec(PrdSpecVal prdSpecVal) {
		// 상품사양정보 삭제 로직 호출
		List<PrdSpecVal> prdSpecRemove = new ArrayList<PrdSpecVal>(); // 상품사양삭제정보
		prdSpecRemove.add(prdSpecVal);
		// 상품사양정보 저장 로직 호출
		List<PrdSpecVal> prdSpecInfo = new ArrayList<PrdSpecVal>(); // 상품사양정보
		prdSpecInfo.add(prdSpecVal);
		setPrdSpec(prdSpecRemove, prdSpecInfo);
	}

	@Override
	public void setPrdSpec(List<PrdSpecVal> prdSpecRemove, List<PrdSpecVal> prdSpecInfo) {
		if (prdSpecRemove != null) {
			logger.debug("prdSpecRemove++**==>" + prdSpecRemove);
			prdSpecInfoEntity.removePrdSpecStList(prdSpecRemove); // 상품사양목록삭제
		}
		if (prdSpecInfo != null && prdSpecInfo.size() > 0) {
			List<PrdSpecVal> prdSpecInfoList = new ArrayList<PrdSpecVal>();
			PrdSpecVal prdSpecInfoFirst = prdSpecInfo.get(0);
			prdSpecInfoList.add(prdSpecInfoFirst);
			prdSpecInfoEntity.modifyPrdSpecStList(prdSpecInfoList); // 상품사양상태목록수정 (첫번째 한건만 보내 전체 사양상태코드를 'D' Update)

			prdSpecInfoEntity.savePrdSpecInfoList(prdSpecInfo); // 상품사양값목록저장

			// 아이템코드 업데이트 (2011/03/30 OSM)
			ItemReg pItemReg = new ItemReg();
			pItemReg.setPrdCd(prdSpecInfoFirst.getPrdCd());	/* 상품코드 */
			pItemReg.setSessionObject(prdSpecInfoFirst);    /* 세션정보 */

			modifyPrdMstItem(pItemReg);
		}
	}

	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_CVS_Ret_Consign_Delivery
	 */
//	public void Check_CVS_Ret_Consign_Delivery() {
//
//	}
	// SOURCE_CLEANSING : END
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_CVS_Ret_Consign_Delivery
	 */
//	public PrdmMain Check_CVS_Ret_Consign_Delivery(PrdmMain prdmMain, PrdOrdPsblQtyDinsert prdOrdPsblQtyD,
//			SupDtlInfo supDtlInfo, PrdprcHinsert prdprcHinsert, SafeCertPrd safeCertPrd, PrdStockDinsert prdAttrPrdm,
//			PrdPrdDinsert prdPrdD, PrdAttrPrdMinsert prdAttrPrdMinsert) {
//		// function Check_CVS_Ret_Consign_Delivery()
//		// {
//		/*
//		 * var sAC_CVSRetFlag = this.GetFieldValue("Account CVS Ret Flag"); //협력사.편의점반품 var sProd_CVSRetFlag = this.GetFieldValue("CVS Ret Flag");
//		 * //편의점택배반품여부 var sPriceList = this.GetFieldValue("Price List"); // 판매가격 var sCarrier = this.GetFieldValue("Carrier"); // 배송수거방법코드
//		 *
//		 * var sAC_ConsingDnFlag = this.GetFieldValue("Account Consign Dn Flag"); //협력사.직송위탁배송여부 var sAC_ConsingReFlag =
//		 * this.GetFieldValue("Account Consign Re Flag"); //협력사.직송위탁수거여부
//		 *
//		 * var sRetReceiptCenterAddressCode = this.GetFieldValue("Ret Receipt Center Address Code"); //상품반송지주소코드 var sShipCenterAddressCode =
//		 * this.GetFieldValue("Ship Center Address Code"); //상품출고지주소코드
//		 */
//		String sAC_CVSRetFlag = supDtlInfo.getCvsRtpYn(); // 협력사.편의점반품
//		String sProd_CVSRetFlag = prdmMain.getCvsDlvsRtpYn(); // 편의점택배반품여부
//		BigDecimal sPriceList = prdprcHinsert.getSalePrc(); // 판매가격
//		String sCarrier = prdmMain.getDlvPickMthodCd(); // 배송수거방법코드
//
//		String sAC_ConsingDnFlag = supDtlInfo.getDirdlvEntrstDlvYn(); // 협력사.직송위탁배송여부
//		String sAC_ConsingReFlag = supDtlInfo.getDirdlvEntrstPickYn(); // 협력사.직송위탁수거여부
//
////		String sRetReceiptCenterAddressCode = prdmMain.getPrdRetpAddrCd(); // 상품반송지주소코드
////		String sShipCenterAddressCode = prdmMain.getPrdRelspAddrCd(); // 상품출고지주소코드
//
//		// 거래처의 편의점 반품이 'Y'가 아닌 경우 상품 편의점 반품 로직 check
//		if (!sAC_CVSRetFlag.equals("Y")) {
//			if (sProd_CVSRetFlag.equals("Y")) {
//				// TheApplication().RaiseErrorText("거래처의 편의점 반품이 불가능으로 되어 있어 편의점 반품 가능 상품으로 등록할 수 없습니다.");
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.328"));
//				return prdmMain;
//			}
//		}
//		// 상품 판매가가 50만원이상이면 편의점 반품이 안된다.
//		BigDecimal fifty = new BigDecimal("500000");
//
//		if (sProd_CVSRetFlag.equals("Y") && sPriceList.compareTo(fifty) == 1) {
//			// TheApplication().RaiseErrorText("상품판매가가 오십만원(500,000원) 이상이면 편의점 반품 가능 상품으로 등록할 수 없습니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.329"));
//			return prdmMain;
//		}
//
//		// 편의점반품 가능 하면서 배송수거형태가 아래에 같으면 편의점 반품이 안된다. 2009.10.08 SNY 반영
//		// 입고택배(의류)-택배 1100, 입고택배(의류)-직반 1400, 직송(설치)-업체 3100, 원재료/나석/매장상품 5000
//		if (sProd_CVSRetFlag.equals("Y") && (sCarrier.equals("1100")) || (sCarrier.equals("1400"))
//				|| (sCarrier.equals("3100")) || sCarrier.equals("5000")) {
//			// TheApplication().RaiseErrorText("편의점 반품 가능 상품으로 등록할 수 없는 배송수거방법 입니다.");
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.330"));
//			return prdmMain;
//		}
//
//		// 편의점반품 가능 하면서 업체수거 상품이거나 거래처의 직송위탁수거여부가 'Y'인 상품인 경우 상품반송지가 필수 입력되어야 한다.
//		if ((sProd_CVSRetFlag.equals("Y") && (sCarrier.equals("2200") || sCarrier.equals("2500")
//				|| sCarrier.equals("2800") || sCarrier.equals("3100") || sCarrier.equals("3200") || sCarrier
//				.equals("3400")))
//				|| sAC_ConsingReFlag.equals("Y")) {
//			if (prdmMain.getPrdRetpAddrCd().equals("")) // 상품반송지주소코드
//			{
//				PrdQryCond pPrdQryCond1 = new PrdQryCond();
//				pPrdQryCond1.setSupCd(prdmMain.getSupCd());
//				EntityDataSet<DSMultiData> prddevcBaseLst = prdEntity.setdevcBaseLst(pPrdQryCond1);// 상품기본반송지
//				if (prddevcBaseLst.size() > 0) {
//					prdmMain.setPrdRetpAddrCd(prddevcBaseLst.getValues().getString("cmmCd"));
//				} else {
//					prdmMain.setRetCd("-1");
//					prdmMain.setRetMsg(Message.getMessage("prd.msg.331"));
//					return prdmMain;
//				}
//				/* 지정택배수거업체의 기본주소를 조회한다. - 쿼리추가 */
//				/*
//				 * var oBusObj = TheApplication().GetBusObject("Account"); var oBusComp = oBusObj.GetBusComp("LGHS Supp Center"); var sVendorId =
//				 * GetFieldValue("Vendor Id"); var isRecord; with ( oBusComp ) { SetViewMode(AllView); ClearToQuery(); // 2009.09.04 SNY 지정택배수거업체의 기본
//				 * 주소를 조회한다. (Primary를 Base Ret Center Flag로 수정함) SetSearchExpr('[Account ID] = "' + sVendorId + '" and [Base Ret Center Flag] =
//				 * "Y"');// 거래처코드, 기본반송지여부 ExecuteQuery(ForwardBackward); isRecord = FirstRecord(); if (isRecord) {
//				 * this.SetFieldValue("Ret Receipt Center Address Code", GetFieldValue("Addr Type")); } else {
//				 * TheApplication().RaiseErrorText("상품의 업체정보에 상품반송지 정보가 없습니다."); return(CancelOperation); prdmMain.setRetCd("-1");
//				 * prdmMain.setRetMsg("도서상품이 아닌 경우 도서 분류를 선택할 수 없습니다."); return prdmMain; } oBusComp = null; oBusObj = null; }
//				 */
//			}
//			// 요기까지
//		}
//
//		// 거래처의 직송위탁 배송여부가 'Y'인 경우 상품출고지가 필수로 있어야 한다.
//		if (sAC_ConsingDnFlag.equals("Y")) {
//			if (prdmMain.getPrdRelspAddrCd().equals("")) // 상품출고지주소코드
//			{
//				PrdQryCond pPrdQryCond1 = new PrdQryCond();
//				pPrdQryCond1.setSupCd(prdmMain.getSupCd());
//				EntityDataSet<DSMultiData> prddevcBaseLst = prdEntity.setdevcBaseLst(pPrdQryCond1);// 상품기본반송지
//				if (prddevcBaseLst.size() > 0) {
//					prdmMain.setPrdRetpAddrCd(prddevcBaseLst.getValues().getString("cmmCd"));
//				} else {
//					prdmMain.setRetCd("-1");
//					prdmMain.setRetMsg(Message.getMessage("prd.msg.331"));
//					return prdmMain;
//				}
//				/* 지정택배수거업체의 기본주소를 조회한다. - 쿼리추가 */
//				/*
//				 * var oBusObj = TheApplication().GetBusObject("Account"); var oBusComp = oBusObj.GetBusComp("LGHS Supp Center"); var sVendorId =
//				 * GetFieldValue("Vendor Id"); var isRecord; with ( oBusComp ) { SetViewMode(AllView); ClearToQuery(); // 2009.09.04 SNY 지정택배수거업체의 기본
//				 * 주소를 조회한다. (Primary를 Base Ret Center Flag로 수정함) SetSearchExpr('[Account ID] = "' + sVendorId + '" and [Base Ship Center Flag] =
//				 * "Y"'); ExecuteQuery(ForwardBackward); isRecord = FirstRecord(); if (isRecord) { this.SetFieldValue("Ship Center Address Code",
//				 * GetFieldValue("Addr Type")); //상품출고지주소코드 } else { TheApplication().RaiseErrorText("상품의 업체정보에 상품출고지 정보가 없습니다.");
//				 * return(CancelOperation); prdmMain.setRetCd("-1"); prdmMain.setRetMsg("도서상품이 아닌 경우 도서 분류를 선택할 수 없습니다."); return prdmMain; } oBusComp
//				 * = null; oBusObj = null; }
//				 */
//			}
//			// 요기까지
//		}
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
//		return prdmMain;
//
//	}
	// SOURCE_CLEANSING : END
			
			
			
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_Fixed_Delivery
	 */
//	public void Check_Fixed_Delivery() {
//
//	}
	// SOURCE_CLEANSING : END
	
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_Fixed_Delivery
	 */
//	public PrdmMain Check_Fixed_Delivery(PrdmMain prdmMain, PrdOrdPsblQtyDinsert prdOrdPsblQtyD, SupDtlInfo supDtlInfo,
//			PrdprcHinsert prdprcHinsert, SafeCertPrd safeCertPrd, PrdStockDinsert prdAttrPrdm, PrdPrdDinsert prdPrdD,
//			PrdAttrPrdMinsert prdAttrPrdMinsert) {
//
//		// function Check_Fixed_Delivery()
//		// {
//		/*
//		 * var sCarrier = this.GetFieldValue("Carrier"); //배송수거방법코드 var sBundlePackagingType = this.GetFieldValue("Bundle Packaging Type"); //합포장코드
//		 * var sFixedDlvyRetFlag = this.GetFieldValue("Fixed Delivery Ret Flag"); //지정택배시행여부 var sRetReceiptCenterAddressCode =
//		 * this.GetFieldValue("Ret Receipt Center Address Code"); //상품반송지주소코드 var sSuppFixedDeliveryType =
//		 * this.GetFieldValue("Supp Fixed Delivery Type"); //협력사.지정택배수거유형코드
//		 */
//		String sCarrier = prdmMain.getDlvPickMthodCd(); // 배송수거방법코드
//		String sBundlePackagingType = prdmMain.getOboxCd(); // 합포장코드
//		String sFixedDlvyRetFlag = prdmMain.getApntDlvsImplmYn(); // 지정택배시행여부
//		String sRetReceiptCenterAddressCode = prdmMain.getPrdRetpAddrCd(); // 상품반송지주소코드
//		String sSuppFixedDeliveryType = supDtlInfo.getApntDlvsPickTypCd(); // 협력사.지정택배수거유형코드
//
//		if (sFixedDlvyRetFlag.equals("Y")) {
//
//			if (!sSuppFixedDeliveryType.equals("A") && !sSuppFixedDeliveryType.equals("B")) {
//				// TheApplication().RaiseErrorText("지정택배수거 가능 업체가 아닙니다.");
//				// return(CancelOperation);
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.352"));
//				return prdmMain;
//			}
//
//			if (!sCarrier.equals("2200") && !sCarrier.equals("2500") && !sCarrier.equals("2800")
//					&& !sCarrier.equals("3100") && !sCarrier.equals("3200") && !sCarrier.equals("3400")) {
//				// TheApplication().RaiseErrorText("지정택배수거는 수거형태가 [업체] 만 가능합니다.");
//				// return(CancelOperation);
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.244"));
//				return prdmMain;
//			}
//
//			if (sBundlePackagingType.equals("1") || sBundlePackagingType.equals("2")) {
//				// TheApplication().RaiseErrorText("지정택배수거는 화장품/도서 합포장상품은 불가합니다.");
//				// return(CancelOperation);
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.245"));
//				return prdmMain;
//			}
//
//			if (sRetReceiptCenterAddressCode.equals("")) // 상품반송지주소코드
//			{
//				/*
//				 * TheApplication().RaiseErrorText("지정택배수거 시행상품은 반드시 상품반송지를 입력하세요"); return(CancelOperation);
//				 */
//				PrdQryCond pPrdQryCond1 = new PrdQryCond();
//				pPrdQryCond1.setSupCd(prdmMain.getSupCd());
//				EntityDataSet<DSMultiData> prddevcBaseLst = prdEntity.setdevcBaseLst(pPrdQryCond1);// 상품기본반송지
//				if (prddevcBaseLst.size() > 0) {
//					prdmMain.setPrdRetpAddrCd(prddevcBaseLst.getValues().getString("cmmCd"));
//				} else {
//					prdmMain.setRetCd("-1");
//					prdmMain.setRetMsg(Message.getMessage("prd.msg.282"));
//					return prdmMain;
//				}
//
//				/* 지정택배수거업체의 기본주소를 조회한다. - 쿼리추가 */
//				/*
//				 * var oBusObj = TheApplication().GetBusObject("Account"); var oBusComp = oBusObj.GetBusComp("LGHS Supp Center"); var sVendorId =
//				 * GetFieldValue("Vendor Id"); var isRecord; with ( oBusComp ) { SetViewMode(AllView); ClearToQuery(); // SetSearchExpr('[Account ID]
//				 * = "' + sVendorId + '" and [Primary] = "Y"'); 2009.09.25 SNY 주석처리 SetSearchExpr('[Account ID] = "' + sVendorId + '" and [Base Ret
//				 * Center Flag] = "Y"'); // 2009.09.25 SNY 추가 ExecuteQuery(ForwardBackward); isRecord = FirstRecord(); if (isRecord) {
//				 * this.SetFieldValue("Ret Receipt Center Address Code", GetFieldValue("Addr Type")); } else {
//				 * TheApplication().RaiseErrorText("지정택배수거시행 상품의 업체정보에 대표주소정보가 없습니다."); return(CancelOperation); } oBusComp = null; oBusObj = null; }
//				 */
//				// 요기까지
//			}
//		}
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END

	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_GMS_Prod
	 */
//	public PrdmMain Check_GMS_Prod(PrdmMain prdmMain, PrdOrdPsblQtyDinsert prdOrdPsblQtyD, SupDtlInfo supDtlInfo,
//			PrdprcHinsert prdprcHinsert, SafeCertPrd safeCertPrd, PrdStockDinsert prdAttrPrdm, PrdPrdDinsert prdPrdD,
//			PrdAttrPrdMinsert prdAttrPrdMinsert) {
//		// function Check_GMS_Prod(aGmsProdType)
//		// {
//		/* 2007.07.24 jewelLee (수입대행프로세스구축) C20070620_81547 */
//
//		/*
//		 * var sProdType = this.GetFieldValue("Prod Type"); //상품유형코드 var sCarrier = this.GetFieldValue("Carrier"); //배송수거방법코드 var sPurchType =
//		 * this.GetFieldValue("Purchase Type"); //매입유형코드 var sRepProdFlg = this.GetFieldValue("Representative Prod Flg"); //대표상품여부 var sFreeGiftType =
//		 * this.GetFieldValue("Free Gift Type"); //사은품유형코드 var sMDId = this.GetFieldValue("MD Id"); //운영MDID var sTaxType =
//		 * this.GetFieldValue("Tax Type"); //세금유형코드 var sInsaleLimitFlag = this.GetFieldValue("Insale Limit Flag"); //제거 var sGmsProdType =
//		 * this.GetFieldValue("GMS Prod Type"); //상품구분코드 var sMsg = ""; // var sVendorId = this.GetFieldValue("Vendor Id"); //협력사코드 var sMiMType ; var
//		 * oBusObj = TheApplication().GetBusObject("Account"); var oBusComp = oBusObj.GetBusComp("Account");
//		 */
//		String sProdType = prdmMain.getPrdTypCd(); // 상품유형코드
//		String sCarrier = prdmMain.getDlvPickMthodCd(); // 배송수거방법코드
//		String sPurchType = prdmMain.getPrchTypCd(); // 매입유형코드
//		String sRepProdFlg = prdmMain.getRepPrdYn(); // 대표상품여부
//		String sFreeGiftType = prdmMain.getGftTypCd(); // 사은품유형코드
////		String sMDId = prdmMain.getOperMdId(); // 운영MDID
//		String sTaxType = prdmMain.getTaxTypCd(); // 세금유형코드
//		String sGmsProdType = prdmMain.getPrdGbnCd(); // 상품구분코드
////		BigDecimal sVendorId = prdmMain.getSupCd(); // 협력사코드
////		String oBusObj = "";
////		String oBusComp = "";
//		String sMsg = ""; //
//		String sMiMType = "";
//		// String sInsaleLimitFlag = this.GetFieldValue("Insale Limit Flag"); //제거
//
//		/*
//		 * with ( oBusComp ) { SetViewMode(AllView); ActivateField("MIM Type"); //협력사.몰인몰유형코드 ClearToQuery(); SetSearchExpr('[Id] =
//		 * "' + sVendorId + '"'); ExecuteQuery(ForwardBackward); var isRecord; isRecord = FirstRecord(); if (isRecord) { sMiMType =
//		 * GetFieldValue("MIM Type"); } }
//		 */
//		int ok = 0;
//		if (!sMiMType.equals("06")) {
//			//sMsg = "해외쇼핑 대표상품은 거래처가 [해외쇼핑 업체]만 가능합니다.";
//			sMsg = Message.getMessage("prd.msg.353");
//
//		} else if (!sProdType.equals("G")) {
//			//sMsg = "해외쇼핑 대표상품은 상품형태가 [일반]만 가능합니다.";
//			sMsg = Message.getMessage("prd.msg.354");
//		} else if (!sCarrier.equals("3200")) {
//			//sMsg = "해외쇼핑 대표상품은 배송형태가 [직송(택배)-업체수거]만 가능합니다.";
//			sMsg =  Message.getMessage("prd.msg.355");
//		} else if (!sPurchType.equals("03")) {
//			//sMsg = "해외쇼핑 대표상품은 매입형태가 [수수료매입]만 가능합니다.";
//			sMsg =  Message.getMessage("prd.msg.356");
//
//		} else if (sRepProdFlg.equals("Y")) {
//			//sMsg = "해외쇼핑 대표상품은 대표상품여부가 [Y]만 가능합니다.";
//			sMsg =  Message.getMessage("prd.msg.357");
//
//		} else if (!sFreeGiftType.equals("00")) {
//			//sMsg = "해외쇼핑 대표상품은 판매구분이 [판매상품]만 가능합니다.";
//			sMsg = Message.getMessage("prd.msg.358");
//
//		} else if (!sTaxType.equals("02") && !sGmsProdType.equals("C")) {
//			//sMsg = "쇼핑 대표상품은 세금구분이 [과세]만 가능합니다.";
//			sMsg = Message.getMessage("prd.msg.359");
//
//		} else if (!sTaxType.equals("01") && sGmsProdType.equals("C")) {
//			//sMsg = "해외쇼핑 반품비 상품은 세금구분이 [면세]만 가능합니다.";
//			sMsg = Message.getMessage("prd.msg.360");
//		} else {
//			ok++;
//		}
//		// else if ( sInsaleLimitFlag != "Y" )
//		// sMsg = "해외쇼핑 대표상품은 사내판매 불가합니다.";
//		if (ok > 0) {
//			prdmMain.setRetCd("0");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
//
//		} else {
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(sMsg);
//		}
//
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
			
			
			
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_GMS_Prod
	 */
//	public String Check_GMS_Prod(String chkval) {
//		return "";
//	}
	// SOURCE_CLEANSING : END
	
	

	public String Check_MD_ID(String arg_MD_ID)
	// function Check_MD_ID(String arg_MD_ID)
	{
		if (arg_MD_ID.equals("E")) // session.chanlCd = 'P' or 'GSEC'
		{
			return "N";
		} else {
			return "Y";
		}
	}

	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_Owner
	 */
//	public String Check_Owner(PrdmMain prdmMain)
	// function Check_Owner()
//	{
//		/*
//		 * var gEmp_Role = TheApplication().GetSharedGlobal("gEmp_Role"); //session.직책 var iReturn = ContinueOperation; var sStatus =
//		 * this.GetFieldValue("Approval Status") //상품결재상태코드
//		 */
//		String gEmp_Role = prdmMain.getSessionUserGbnCd(); // session.직책
//		String iReturn = "";
//		String sStatus = prdmMain.getPrdAprvStCd(); // 상품결재상태코드
//
//		if (sStatus.equals("30") || gEmp_Role.equals("30")) {
//			// UndoRecord(); //rollback
//			// TheApplication().RaiseErrorText("방송중에만 판매가능 필드는 MD팀장결재 이전 또는 편성팀에만 권한이 있습니다.");
//			// iReturn = CancelOperation;
//			iReturn = Message.getMessage("prd.msg.361");
//		}
//
//		return (iReturn);
//	}
	// SOURCE_CLEANSING : END
	
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_Prod_Closed
	 */
//	public String Check_Prod_Closed(PrdmMain prdmMain, SupDtlInfo supDtlInfo)
	// function Check_Prod_Closed(FieldValue)
//	{
//		/*
//		 * var iReturn = ""; var dChangedDate = new Date(FieldValue); var sChangedDate = Clib.rsprintf("%04d-%02d-%02d %02d:%02d:%02d",
//		 * dChangedDate.getFullYear(), dChangedDate.getMonth()+1, dChangedDate.getDate(), dChangedDate.getHours(), dChangedDate.getMinutes(),
//		 * dChangedDate.getSeconds()); //sysdate var sVendorEndDate = this.GetFormattedFieldValue("Vendor End Date"); //협력사.거래종료일자
//		 */
//		Date date = SysUtil.getCurrTime();
//		String sysdate = DateUtils.format(date, "yyyyMMddHHmmss");
//		String iReturn = "";
////		String dChangedDate = "";
//		String sChangedDate = sysdate;
//		String sVendorEndDate = supDtlInfo.getTxnEndDt(); // 협력사.거래종료일자
//
//		if ((!sVendorEndDate.equals("")) && Integer.parseInt(sVendorEndDate) < Integer.parseInt(sChangedDate)) {
//			/*
//			 * var dtVendorEndDate = new Date(this.GetFieldValue("Vendor End Date")); //협력사.거래종료일자 var TimeBuf; Clib.strftime(TimeBuf,"%Y-%m-%d %X"
//			 * ,Clib.localtime(Clib.time()+120));
//			 */
//
//			// TheApplication().RaiseErrorText( sVendorEndDate +
//			// "로 거래종료된 거래처입니다. \n상품종료일자는 거래종료일자 이전이어야 합니다.\n\n 판매종료일자는 변경 되지않습니다.\n\n거래종료일자는 SAP에서 변경가능합니다.");
//
//			iReturn = sVendorEndDate
//					+ "로 거래종료된 거래처입니다. \n상품종료일자는 거래종료일자 이전이어야 합니다.\n\n 판매종료일자는 변경 되지않습니다.\n\n거래종료일자는 SAP에서 변경가능합니다.";
//			// }
//			// }
//		}
//		return (iReturn);
//
//	}
	// SOURCE_CLEANSING : END
	
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_Spec
	 */
//	public String Check_Spec(PrdmMain prdmMain, SupDtlInfo supDtlInfo)
	// function Check_Spec(FieldValue)
//	{
//
//		// LGHS Key Feature for eShop LGHS Product Note Admin
//		/*
//		 * var sPartNum = GetFieldValue("Part #"); //상품코드 var sQA = GetFieldValue("Show In Internet"); //제거 var oBusObj =
//		 * TheApplication().GetBusObject("Admin Product Definition"); var oBusComp = oBusObj.GetBusComp("Key Feature"); //요기까지 var sMsg = "Y"; var
//		 * iMsg = "Y"; var rMsg = "Y";
//		 */
////		BigDecimal sPartNum = prdmMain.getPrdCd(); // 상품코드
//		String sMsg = "Y";
//		String iMsg = "Y";
//		String rMsg = "Y";
//		if (prdmMain.getPrdAprvStCd().equals("30"))// sQA.equals("Y") && //상품결재상태코드
//		{
//
//			/*
//			 * with ( oBusComp ) { SetViewMode(AllView); ClearToQuery(); SetSearchExpr('[Part #] = "' + sPartNum + '"'); //상품코드를 조회조건으로 일반기술서 조회
//			 * ExecuteQuery(ForwardBackward); var isRecord; isRecord = FirstRecord(); if (isRecord) //조회건이 있으면 {
//			 *
//			 * sMsg = "Y"; //리턴 } else { sMsg = "N"; //리턴 }
//			 *
//			 * }
//			 */
//			sMsg = "Y"; // 리턴
//		} else if (prdmMain.getPrdAprvStCd().equals("25")) // 상품결재상태코드 = '25'
//		{
//			/*
//			 * with ( oBusComp ) { SetViewMode(AllView); ClearToQuery(); SetSearchExpr('[Part #] = "' + sPartNum + '"'); //상품코드를 조회조건으로 일반기술서 조회
//			 * ExecuteQuery(ForwardBackward); var isRecord; isRecord = FirstRecord(); if (isRecord) //조회건이 있으면 {
//			 *
//			 * sMsg = "Y"; //리턴 } else { sMsg = "N"; //리턴 }
//			 *
//			 * }
//			 */
//			sMsg = "Y"; // 리턴
//		}
//		/* 상품html기술서테이블 지정 */
//		/*
//		 * var iBusObj = TheApplication().GetBusObject("LGHS Product Note Admin"); var iBusComp = iBusObj.GetBusComp("LGHS Key Feature for eShop"
//		 */
//		// 요기까지
//		if (prdmMain.getPrdAprvStCd().equals("30"))// sQA.equals("Y" && //상품결재상태코드
//		{
//			/*
//			 * with ( iBusComp ) { SetViewMode(AllView); ClearToQuery(); SetSearchExpr('[Name] = "' + sPartNum + '"'); //상품코드를 조회조건으로 html기술서 조회
//			 * ExecuteQuery(ForwardBackward); var isRecord; isRecord = FirstRecord(); if (isRecord) //조회건이 있으면 {
//			 *
//			 * iMsg = "Y"; //리턴 } else { iMsg = "N"; //리턴 }
//			 *
//			 * }
//			 */
//			iMsg = "Y"; // 리턴
//		} else if (prdmMain.getPrdAprvStCd().equals("25")) // 상품결재상태코 = '25'
//		{
//			/*
//			 * with ( iBusComp ) { SetViewMode(AllView); ClearToQuery(); SetSearchExpr('[Name] = "' + sPartNum + '"'); //를 조회조건으로 html기술서 조회
//			 * ExecuteQuery(ForwardBackward); var isRecord; isRecord = FirstRecord(); if (isRecord) //조회건이 있으면 {
//			 *
//			 * iMsg = "Y"; //리턴 } else { iMsg = "N"; //리턴 }
//			 *
//			 * }
//			 */
//			iMsg = "Y"; // 리턴
//		}
//		if (sMsg.equals("N") && iMsg.equals("N")) // 리턴값확인
//		{
//			rMsg = "N";
//		} else {
//			// 리턴
//			rMsg = "Y";
//		}
//		return rMsg;
//	}
	// SOURCE_CLEANSING : END
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_SWFlag
	 */
//	public String Check_SWFlag(PrdmMain prdmMain, SupDtlInfo supDtlInfo, String aSoftwareFlg)
////	 function Check_SWFlag (aSoftwareFlg) //무형상품유형코드
//	{
//		/*
//		 * var sProdType = this.GetFieldValue("Prod Type"); //상품유형코드 var sCarrier = this.GetFieldValue("Carrier"); //배송수거방법코드 var sPurchType =
//		 * this.GetFieldValue("Purchase Type"); //매입유형코드 var sRepProdFlg = this.GetFieldValue("Representative Prod Flg") //대표상품여부 var sFreeGiftType =
//		 * this.GetFieldValue("Free Gift Type"); //사은품유형코드 var sMDId = this.GetFieldValue("MD Id"); //운영MDID var sMsg = ""; var sMsgPrefix = "";
//		 */
//		String sProdType = prdmMain.getPrdTypCd(); // 상품유형코드
//		String sCarrier = prdmMain.getDlvPickMthodCd(); // 배송수거방법코드
//		String sPurchType = prdmMain.getPrchTypCd(); // 매입유형코드
//		String sRepProdFlg = prdmMain.getRepPrdYn(); // 대표상품여부
//		String sFreeGiftType = prdmMain.getGftTypCd(); // 사은품유형코드
//		String sMDId = prdmMain.getOperMdId(); // 운영MDID
//		String sMsg = "";
//		String sMsgPrefix = "";
//
//		if (aSoftwareFlg.equals("S"))
//			sMsgPrefix = "e스토어대표상품";
//		else
//			sMsgPrefix = "MP대표상품"; // 2005.1-3월버젼
//
//		if (aSoftwareFlg.equals("S")) {
////			String sVendorId = prdmMain.getSupCd().toString(); // 협력사코드
////			String sMiMType;
//			/* 협력사테이블 지정 */
//			/*
//			 * var oBusObj = TheApplication().GetBusObject("Account"); var oBusComp = oBusObj.GetBusComp("Account");
//			 */
//			// 요기까지
//			/*
//			 * with ( oBusComp ) { SetViewMode(AllView); ActivateField("MIM Type"); //협력사.몰인몰유형코드 ClearToQuery(); SetSearchExpr('[Id] =
//			 * "' + sVendorId + '"'); //협력사코드로 ? ExecuteQuery(ForwardBackward); var isRecord; isRecord = FirstRecord(); if (isRecord) //조회건이 있으면 {
//			 * sMiMType = GetFieldValue("MIM Type"); //몰인몰유형코드를 가져온다 } } oBusComp = null; oBusObj = null;
//			 *
//			 * if ( sMiMType != "02" ) { sMsg = sMsgPrefix + "은 거래처가 [e스토어대표업체]만 가능합니다."; }
//			 */
//			if (!supDtlInfo.getMimTypCd().equals("02")) {
//				sMsg = sMsgPrefix + "은 거래처가 [e스토어대표업체]만 가능합니다.";
//			}
//
//		}
//
//		if (!sProdType.equals("G")) // 상품유형코드 <> 'G' OR 상품유형코드 <> 'P
//			sMsg = sMsgPrefix + "은 상품형태가 [일반]만 가능합니다.";
//
//		else if (!sCarrier.equals("3200"))
//			sMsg = sMsgPrefix + "은 배송형태가 [직송(택배)-업체수거]만 가능합니다.";
//
//		else if (!sPurchType.equals("03"))
//			sMsg = sMsgPrefix + "은 매입형태가 [수수료매입]만 가능합니다.";
//
//		else if (!sRepProdFlg.equals("Y"))
//			sMsg = sMsgPrefix + "은 대표상품여부가 [Y]만 가능합니다.";
//
//		else if (!sFreeGiftType.equals("00"))
//			sMsg = sMsgPrefix + "은 판매구분이 [판매상품]만 가능합니다.";
//
//		else if (!sMDId.substring(0, 1).equals("E"))
//			sMsg = sMsgPrefix + "은 담당MD가 [e스토어담당MD(E####)]만 가능합니다.";
//
//		return (sMsg);
//	}
	// SOURCE_CLEANSING : END
	
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_SWFlag
	 */
//	public String Check_SWFlag(String chkval) {
//		return "";
//	}
	// SOURCE_CLEANSING : END
	
	
	

	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Check_Vendor_for_ShipFee
	 */
//	public String Check_Vendor_for_ShipFee(PrdmMain prdmMain, SupDtlInfo supDtlInfo, String aSoftwareFlg)
//	// function Check_Vendor_for_ShipFee()
//	{
//
////		String sVendorId = prdmMain.getSupCd().toString(); // 협력사코드
//		/* 협력사테이블 지정 */
//		/*
//		 * var oBusObj = TheApplication().GetBusObject("Account"); var oBusComp = oBusObj.GetBusComp("Account");
//		 */
//		// 요기까지
//		String sMsg = "";
//		/*
//		 * with ( oBusComp ) { SetViewMode(AllView); ClearToQuery(); SetSearchExpr('[Id] = "' + sVendorId + '"'); //협력사코드로 조회한다.
//		 * ExecuteQuery(ForwardBackward); var isRecord; isRecord = FirstRecord(); //조회건이 있으면 if (isRecord) { var sShipFeeFlag =
//		 * GetFieldValue("Ship Fee Flag"); //IIF([Ship Fee Cut Out Amount] IS NULL or [Ship Fee Part #] IS NULL,'N','Y') --> IF(유료배송기준금액 IS NULL OR
//		 * 유료배송비코드 IS NULL) THEN 유료배송여부 = 'N' ELSE 'Y' if (sShipFeeFlag.equals("Y" ) sMsg = ""; else sMsg = "유료배송서비스 업체가 아닙니다"; } else { sMsg =
//		 * "해당 거래처가 존재하지 않습니다."; } return ( sMsg ) ; }
//		 */
//		if (supDtlInfo != null) {
//			if (supDtlInfo.getChrDlvcCd().equals("Y")) {
//				sMsg = "";
//			} else {
//				sMsg = "유료배송서비스 업체가 아닙니다";
//			}
//		} else {
//			sMsg = "해당 거래처가 존재하지 않습니다.";
//		}
//		return (sMsg);
//	}
	// SOURCE_CLEANSING : END
	
	
	
	/**
	 * <pre>
	 *
	 * desc : 상품등록 시 필수입력 값을 체크한다.
	 *
	 * </pre>
	 *
	 * @author Sojunggu
	 * @date 2011-02-28 10:49:08
	 * @param SupDtlInfo
	 *            supDtlInfo, PrdmMain prdmMain, PrdPrdDinsert prdPrdD, SafeCertPrd psafeCertPrd, List<PrdSpecVal> prdSpecInfo, List<PrdUdaDtl>
	 *            prdUdaDtl, List<PrdNumvalDinfo> prdNumvalDinfo, List<PrdChanlDinsert> prdChanlDinsert
	 * @return PrdmMain
	 */
	public PrdmMain checkPrdmPK(SupDtlInfo supDtlInfo, PrdmMain prdmMain, PrdprcHinsert prdprcHinsert, PrdPrdDinsert prdPrdD,
			SafeCertPrd psafeCertPrd, List<PrdSpecVal> prdSpecInfo, List<PrdUdaDtl> prdUdaDtl,
			List<PrdNumvalDinfo> prdNumvalDinfo, List<PrdChanlInfo> prdChanlDinsert,
			List<PrdNmChgHinsert> prdNmChg) {
		/*
		 * 필수값부터 체크 Method 별도 구현할지는 대표채널 업무 결정되면..(by Sojung)
		 */
		//SMTCLogger.writePrd("필수 입력 값 Check1");
		if (prdmMain.getOperMdId() == null || "".equals(prdmMain.getOperMdId())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "운영MD" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check3");
		if (prdmMain.getPrdClsCd() == null || "".equals(prdmMain.getPrdClsCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "상품분류" }));
			return prdmMain;
		}

		//SMTCLogger.writePrd("필수 입력 값 Check4");
		if (prdmMain.getPrdNm() == null || "".equals(prdmMain.getPrdNm())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "상품명" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check5");
		if (prdmMain.getSupCd() == null || "".equals(prdmMain.getSupCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "협력사코드" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check6");
		if (prdmMain.getBrandCd() == null || "".equals(prdmMain.getBrandCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "브랜드" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check7");
		if (prdmMain.getOrgpNm() == null || "".equals(prdmMain.getOrgpNm())){
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "원산지명)" }));
			return prdmMain;
		}
		/* 원산지코드 체크 추가 [패널]해외배송 2014.08.01 윤승욱(sitjjang)
		 * 해외배송여부가 Y 이면 원산지국가코드는 필수
		 */
		if("Y".equals(prdmMain.getForgnDlvPsblYn()) 
				&& (prdmMain.getOrgpCd() == null || "".equals(prdmMain.getOrgpCd()))){
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "해외배송 가능 상품일경우 원산지국가코드)" }));
			return prdmMain;
		}
		
		//SMTCLogger.writePrd("필수 입력 값 Check8");
		if (prdmMain.getMnfcCoNm() == null || "".equals(prdmMain.getMnfcCoNm())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "제조사" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check9");
		if (prdmMain.getPrchTypCd() == null || "".equals(prdmMain.getPrchTypCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "거래형태" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check10");
		if (prdmMain.getGftTypCd() == null || "".equals(prdmMain.getGftTypCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "사은품유형" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check11");
		if (prdmMain.getTaxTypCd() == null || "".equals(prdmMain.getTaxTypCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "부가세 구분" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check12");
		if (prdmMain.getDlvPickMthodCd() == null || "".equals(prdmMain.getDlvPickMthodCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "배송수거방법" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check13");
		if (prdmMain.getPrdTypCd() == null || "".equals(prdmMain.getPrdTypCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "상품타입" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check14");
		if (prdmMain.getOrdPrdTypCd() == null || "".equals(prdmMain.getOrdPrdTypCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "주문상품유형" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check15");
		if (prdmMain.getRfnTypCd() == null || "".equals(prdmMain.getRfnTypCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "환불유형" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check16");
		if ("".equals(StringUtils.NVL(prdPrdD.getPrdBaseCmposCntnt()))
				&& "".equals(StringUtils.NVL(prdPrdD.getPrdAddCmposCntnt()))
				&& "".equals(StringUtils.NVL(prdPrdD.getPrdGftCmposCntnt()))
				&& "".equals(StringUtils.NVL(prdPrdD.getPrdEtcCmposCntnt()))) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "상품구성" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check17");
		if (prdmMain.getSaleEndDtm() == null || "".equals(prdmMain.getSaleEndDtm())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "판매종료일시" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check18");
		// 20110303 위드넷(GSEC)에서 들어온 것은 체크하지 않는다.
		if ((prdSpecInfo.isEmpty() || prdSpecInfo.size() == 0)
				&& !PrdConstants.PRD_SESSIONCHANLCD.equals(StringUtils.NVL(prdmMain.getSessionChanlCd()))) {
			// prdmMain.setRetCd("-1");
			// prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[]{"상품사양정보"}));
			// return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check19");
		// 대표채널이 CA인 경우에 Check
		if ("GC".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {
			if (prdmMain.getMnfcCoGbnCd() == null || "".equals(prdmMain.getMnfcCoGbnCd())) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "제조사구분" }));
				return prdmMain;
			}
		}
		//SMTCLogger.writePrd("필수 입력 값 Check20");
		// EC표준출고일수 필수체크 [PD-2015-007]
		String sRepPrdYn = prdmMain.getRepPrdYn();
		String sFrmlesPrdTypCd = prdmMain.getFrmlesPrdTypCd();
//		String sGftTypCd = prdmMain.getGftTypCd();
//		String sPrdClsCd = prdmMain.getPrdClsCd();
		if (!prdChanlDinsert.isEmpty()) {
			for (int i = 0; i < prdChanlDinsert.size(); i++) {
				if ("P".equals(prdChanlDinsert.get(i).getChanlCd()) || "B".equals(prdChanlDinsert.get(i).getChanlCd()) || "S".equals(prdChanlDinsert.get(i).getChanlCd())) {
					/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 _489 Start */
					PrdClsUda param = new PrdClsUda();
					param.setUdaNo(new BigDecimal( "172" ));
					param.setPrdClsCd(prdmMain.getPrdClsCd());		
					
					if (prdChanlDinsert.get(i).getStdRelsDdcnt() == null 
							   //&& !"B67010705".equals(prdmMain.getPrdClsCd()) 
							   //&& !"B31".equals(prdmMain.getPrdClsCd().substring(0, 3))
							   && !PrdClsUdaUtils.isPrdClsUdaFlag(param)
							   && !"Y".equals(sRepPrdYn)
							   && "N".equals(sFrmlesPrdTypCd)
							   && "00,01,02".indexOf(prdmMain.getGftTypCd()) >= 0 							      
					    ) {
					/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 End */
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "EC표준출고일수" }));
						return prdmMain;
					}
				}
			}
		} else {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "EC표준출고일수" }));
			return prdmMain;
		}
		
		// 대표채널이 EC인 경우에 Check
		if ("GE".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {

//			String sGftTypCd = prdmMain.getGftTypCd();
//			if (!prdChanlDinsert.isEmpty()) {
//				for (int i = 0; i < prdChanlDinsert.size(); i++) {
//					if (prdChanlDinsert.get(i).getStdRelsDdcnt() == null) {
//						prdmMain.setRetCd("-1");
//						prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "EC표준출고일수" }));
//						return prdmMain;
//					}
//				}
//			} else {
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "EC표준출고일수" }));
//				return prdmMain;
//			}
			// 인터넷상품명
            if(!prdNmChg.isEmpty()){
                for(int i=0; i < prdNmChg.size(); i++){
                    if(prdNmChg.get(i).getExposPrdNm()==null || "".equals(prdNmChg.get(i).getExposPrdNm())){
                        prdmMain.setRetCd("-1");
                        prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "인터넷상품명" }));
                        return prdmMain;
                    }
                }
            } else {
                prdmMain.setRetCd("-1");
                prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "인터넷상품명" }));
                return prdmMain;
            }
		}

		//SMTCLogger.writePrd("필수 입력 값 Check21");
		/*
		 * 조건에 따른 Validation Check
		 */
	
		// 반품/교환비 유료이면 교환비용 필수입력
		if ("Y".equals(StringUtils.NVL(prdmMain.getExchRtpChrYn())) && "".equals(StringUtils.NVL(prdmMain.getExchOnewyRndtrpCd()))){
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.219", new String[] { "교환비용" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check22");
		// 무형상품유형이 "문자쿠폰(상품교환권)" 인 경우 메시지유형 필수 입력 ==>
		// if("R".equals(StringUtils.NVL(prdmMain.getFrmlesPrdTypCd()))){
		// 메시지유형 값 못찾음.
		// }

		// 상품타입이 당사상품권은 상품권액면가 필수입력
		if ("Y".equals(StringUtils.NVL(prdmMain.getGshsGftcertYn()))
				&& (prdmMain.getGftcertFacePrc() == null || "0".equals(prdmMain.getGftcertFacePrc()))) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.220", new String[] { "상품권상품", "상품권액면가" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check23");
		// 상품분류의 정보조회
		PrdClsQryCond prdClsQryCond = new PrdClsQryCond();
		prdClsQryCond.setPrdClsCd(prdmMain.getPrdClsCd());
		prdClsQryCond.setClsLvlNo(new BigDecimal(4));
		EntityDataSet<DSMultiData> prdClsDS = prdClsBaseEntity.getPrdClsList(prdClsQryCond);

		String safeCertTgtYn = "";
		String unitMandtYn = "";

		if (prdClsDS.size() > 0) {
			safeCertTgtYn = prdClsDS.getValues().get(0).getString("safeCertTgtYn");
			unitMandtYn = prdClsDS.getValues().get(0).getString("weihtMandYn");
		}

		// 해외상품은 제외 (2011/06/27 OSM)
		if (!"10".equals(prdmMain.getPrdGbnCd()) && prdmMain.getFrTmSeq() == null) {
			if ("Y".equals(StringUtils.NVL(safeCertTgtYn)) && !"20".equals(prdmMain.getBundlPrdGbnCd())) {
				// 상품분류의 안전인증대상이면 안전인증구분정보 필수입력
				if ("".equals(StringUtils.NVL(psafeCertPrd.getSafeCertGbnCd()))) {
					prdmMain.setRetCd("-1");
					// 안전인증 대상상품이므로 인증구분을 입력하셔야 합니다.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.256"));
					return prdmMain;
				} else if (!"0".equals(StringUtils.NVL(psafeCertPrd.getSafeCertGbnCd()))) {
					// 상품분류의 안전인증대상이면 인증기관 필수입력
					if ("".equals(StringUtils.NVL(psafeCertPrd.getSafeCertOrgCd()))) {
						prdmMain.setRetCd("-1");
						// 안전인증 대상상품이므로 인증기관을 입력하셔야 합니다.
						prdmMain.setRetMsg(Message.getMessage("prd.msg.257"));
						return prdmMain;
					}
					// 상품분류의 안전인증대상이면 인증모델명 필수입력
					if ("".equals(StringUtils.NVL(psafeCertPrd.getSafeCertModelNm()))) {
						prdmMain.setRetCd("-1");
						// 안전인증 대상상품이므로 인증모델명을 입력하셔야 합니다.
						prdmMain.setRetMsg(Message.getMessage("prd.msg.258"));
						return prdmMain;
					}
					//	[SR02160531123]어린이제품 안전 특별법 시행에 따른 어린이 도서 안전기준 인증값 설정의 건 - 인증번호 필수 해제
					// 상품분류의 안전인증대상이면 인증번호 필수입력
					/*
					if ("".equals(StringUtils.NVL(psafeCertPrd.getSafeCertNo()))) {
						prdmMain.setRetCd("-1");
						// 안전인증 대상상품이므로 인증번호를 입력하셔야 합니다.
						prdmMain.setRetMsg(Message.getMessage("prd.msg.259"));
						return prdmMain;
					}*/
					// 상품분류의 안전인증대상이면 인증일 필수입력
					if ("".equals(StringUtils.NVL(psafeCertPrd.getSafeCertDt()))) {
						prdmMain.setRetCd("-1");
						// 안전인증 대상상품이므로 인증일을 입력하셔야 합니다.
						prdmMain.setRetMsg(Message.getMessage("prd.msg.285"));
						return prdmMain;
					}
				}
			}
		}
		//SMTCLogger.writePrd("필수 입력 값 Check25");
		/*
		 * # 직송, 업체수거 ( 2200,2500,2800,3100, 3200, 3400 ) & 지정택배수거상품은 상품반송지 필수입력 편의점택배수거는 상품반송지 필수입력 편의점택배수거, 지정택배수거, 직송위탁수거는 상품반송지 필수입력 $ 편의점반품 가능
		 * 하면서 업체수거 상품이거나 거래처의 직송위탁수거여부가 'Y'인 상품인 경우 상품반송지가 필수 입력 ( 2200, 2500, 2800, 3100, 3200, 3400) $ 협력사.상품반송지 조회(s) $ 기본상품반송지가 없으면 오류
		 */
		// 해외상품은 제외 (2011/06/04 OSM)
		if (!"10".equals(prdmMain.getPrdGbnCd()) && prdmMain.getFrTmSeq() == null) {
			
			/* HANGBOT-16987_GSITMBO-9804 지정택배수거 종료
			// 직송이면서 지정택배 수거상품은 상품반송지 필수 입력
			if ("3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
					// || ("2200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) || "2500".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) ||
					// "2800".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())))
					&& "Y".equals(StringUtils.NVL(prdmMain.getApntDlvsImplmYn()))
					&& "".equals(StringUtils.NVL(prdmMain.getPrdRetpAddrCd()))) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.220", new String[] { "지정택배수거상품", "상품반송지" }));
				return prdmMain;
			}*/
			//SMTCLogger.writePrd("필수 입력 값 Check25");
			// 편의점 반품가능하면서 업체수거 or 직송위탁수거여부 Y이면 상품반송지 필수 입력
			if (("3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
					|| "2200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
					|| "2500".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) || "2800".equals(StringUtils
					.NVL(prdmMain.getDlvPickMthodCd())))
					&& "Y".equals(StringUtils.NVL(prdmMain.getCvsDlvsRtpYn()))
					&& "".equals(StringUtils.NVL(prdmMain.getPrdRetpAddrCd()))) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.220", new String[] { "편의점택배를 시행하면서 업체수거상품", "상품반송지" }));
				return prdmMain;
			}
			//SMTCLogger.writePrd("필수 입력 값 Check26");
			// 거래처가 지정택배수거나 직송위탁수거를 시행하면 상품반송지 필수입력 , 업체수거인 경우만
			if (("Y".equals(StringUtils.NVL(supDtlInfo.getDirdlvEntrstPickYn())) 
					//HANGBOT-16987_GSITMBO-9804 지정택배수거 종료
					//|| ("A".equals(StringUtils.NVL(supDtlInfo.getApntDlvsPickTypCd())) || "B".equals(StringUtils.NVL(supDtlInfo.getApntDlvsPickTypCd())))
					)
				 	&& ("2200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
				 			||"2500".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
				 			||"2800".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
				 			||"3100".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
				 			||"3200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
				 			||"3400".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())))
					&& "".equals(StringUtils.NVL(prdmMain.getPrdRetpAddrCd()))) {
				prdmMain.setRetCd("-1");
				// 거래처가 지정택배수거나 직송위탁수거를 시행하면,\n상품의 반송지 값이 존재해야 합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.268"));
				return prdmMain;
			}
			//SMTCLogger.writePrd("필수 입력 값 Check27");
			// 직송일 경우 상품출고지가 필수
			// 직송의 경우 상품반송지도 필수로 체크할 수 있도록 변경 진행함.
			if ("3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
					&& ("".equals(StringUtils.NVL(prdmMain.getPrdRelspAddrCd())) || "".equals(StringUtils.NVL(prdmMain.getPrdRetpAddrCd())))) {
				prdmMain.setRetCd("-1");
				// 직송일 경우 상품 출고지 값을 입력하셔야 합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.269"));
				return prdmMain;
			}

			// 직택배, 센터집하일 경우 상품출고지가 필수, sap 재구축 (2013/01/22 안승훈)
			if ("2".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())
					.substring(0, 1))
					&& "".equals(StringUtils.NVL(prdmMain.getPrdRelspAddrCd()))) {
				prdmMain.setRetCd("-1");
				// 직하/직택배일 경우 상품 출고지 값을 입력하셔야 합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.510"));
				return prdmMain;
			}

			// 수거형태가 직송반송지이면 상품반송지 필수입력
			if (("1300".equals(StringUtils.NVL(prdmMain
							.getDlvPickMthodCd()))
							|| "1400".equals(StringUtils.NVL(prdmMain
									.getDlvPickMthodCd()))
							|| "1500".equals(StringUtils.NVL(prdmMain
									.getDlvPickMthodCd()))
							|| "2100".equals(StringUtils.NVL(prdmMain
									.getDlvPickMthodCd()))
							|| "2400".equals(StringUtils.NVL(prdmMain
									.getDlvPickMthodCd())) || "2700"
								.equals(StringUtils.NVL(prdmMain
										.getDlvPickMthodCd())))
					&& "".equals(StringUtils.NVL(prdmMain.getPrdRetpAddrCd()))) {
				prdmMain.setRetCd("-1");
				// 수거형태가 직송반송지이면 상품의 반송지 값을 입력하셔야 합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.511"));
				return prdmMain;
			}


			/* 2012/01/17 직송인 경우만 협력사 직송위탁을 체크하므로 의미 없음 - 4721 line에서 체크중
			// 협력사의 직송위탁 배송여부가 'Y'인 경우 상품출고지가 필수
			if ("Y".equals(StringUtils.NVL(supDtlInfo.getDirdlvEntrstDlvYn()))
					&& "".equals(StringUtils.NVL(prdmMain.getPrdRelspAddrCd()))) {
				prdmMain.setRetCd("-1");
				// 거래처가 직송위탁배송을 시행하면,\n상품의 출고지 값이 존재해야 합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.270"));
				return prdmMain;
			}
			*/

		}

		// 유료배송인 경우 배송비 코드 필수입력
		if ("Y".equals(StringUtils.NVL(prdmMain.getChrDlvYn()))
				&& "N".equals(StringUtils.NVL(prdmMain.getChrDlvAddYn()))
				&& (prdmMain.getChrDlvcCd() == null || "".equals(prdmMain.getChrDlvcCd()))) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.220", new String[] { "유료배송", "배송비코드" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check28");
		// 입고택배는 입고유형이 필수 입력값 , 그 외에는 입고유형 값이 있으면 안됨
		if ("1000".equals(prdmMain.getDlvPickMthodCd()) || "1100".equals(prdmMain.getDlvPickMthodCd())
				|| "1200".equals(prdmMain.getDlvPickMthodCd()) || "1300".equals(prdmMain.getDlvPickMthodCd())
				|| "1400".equals(prdmMain.getDlvPickMthodCd()) || "1500".equals(prdmMain.getDlvPickMthodCd())) {
			if ("".equals(StringUtils.NVL(prdmMain.getIstTypCd()))) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.219", new String[] { "입고유형" }));
				return prdmMain;
			}
		} else {
			if (!"".equals(StringUtils.NVL(prdmMain.getIstTypCd()))) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.221", new String[] { "입고유형" }));
				return prdmMain;
			}
		}

		// 직택배,직송택배인인경우 택배사 필수입력
		if (prdmMain.getDlvPickMthodCd() != null
				&& ("2".equals(prdmMain.getDlvPickMthodCd().substring(0, 1))
						|| "3000".equals(prdmMain.getDlvPickMthodCd()) || "3200".equals(prdmMain.getDlvPickMthodCd()))
				&& (prdmMain.getDlvsCoCd() == null || "".equals(prdmMain.getDlvsCoCd()))) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.219", new String[] { "택배사" }));
			return prdmMain;
		}
		if (prdmMain.getDlvsCoCd() == null || "".equals(prdmMain.getDlvsCoCd())) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.219", new String[] { "택배사" }));
			return prdmMain;
		}
		//SMTCLogger.writePrd("필수 입력 값 Check29");
		// 판매종료처리시 사유코드 필수입력
		if ("Y".equals(StringUtils.NVL(prdmMain.getSaleEndYn()))) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.220", new String[] { "판매종료", "판매종료사유" }));
			return prdmMain;
		}

		// 하위협력사가 있는 경우 하위협력업체코드 필수 입력
		SubSupQryCond pSubSupQryCond = new SubSupQryCond();
		pSubSupQryCond.setShopGbn("1");
		pSubSupQryCond.setUpperSupCd(prdmMain.getSupCd().toString());
		EntityDataSet<DSMultiData> supSupList = subSupEntity.getSubSupList(pSubSupQryCond);
		logger.debug("prdmMain.getSubSupCd=>"+ prdmMain.getSubSupCd());
		
		if (supSupList.size() > 0 && "".equals(StringUtils.NVL(prdmMain.getSubSupCd()))) {
			// 하위거래처코드를 반드시 입력해주시기 바랍니다.
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.260"));
			return prdmMain;
		}

		// 설치 배송상품은 설치비 필수 입력  (2011.07.07  설치비 필수 체크안함)
//		if ("Y".equals(StringUtils.NVL(prdmMain.getInstlDlvPrdYn())) && prdprcHinsert != null
//				&& (prdprcHinsert.getInstlCost() == null || "".equals(prdprcHinsert.getInstlCost()))) {
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg(Message.getMessage("prd.msg.219", new String[] { "설치비" }));
//			return prdmMain;
//		}
		//SMTCLogger.writePrd("필수 입력 값 Check30");
		// 대표채널이 CA인 경우에 Check
		if ("GC".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {
			// CA상품의 "수입업체" 가 아닌경우 제조사사업자번호 필수입력
			if (!"3".equals(StringUtils.NVL(prdmMain.getMnfcCoGbnCd()))
					&& "".equals(StringUtils.NVL(prdmMain.getMnfcCoBzNo()))) {
				prdmMain.setRetCd("-1");
				// 제조사 사업자등록번호는 필수 입니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.261"));
				return prdmMain;
			}

			// 기본구성값입력시 기본구성수량 > 0
			if (!"".equals(StringUtils.NVL(prdPrdD.getPrdBaseCmposCntnt())) && prdPrdD.getOrgprdPkgCnt() == null) {
				prdmMain.setRetCd("-1");
				// 기본구성 수량을 0이상 입력하세요.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.262"));
				return prdmMain;
			}

			// 추가구성값입력시 추가구성수량 > 0
			if (!"".equals(StringUtils.NVL(prdPrdD.getPrdAddCmposCntnt())) && prdPrdD.getAddCmposPkgCnt() == null) {
				prdmMain.setRetCd("-1");
				// 추가구성 수량을 0이상 입력하세요.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.263"));
				return prdmMain;
			}
			//SMTCLogger.writePrd("필수 입력 값 Check31");
			// 사은품구성값입력시 사은품구성수량 > 0
			if (!"".equals(StringUtils.NVL(prdPrdD.getPrdGftCmposCntnt())) && prdPrdD.getGftPkgCnt() == null) {
				prdmMain.setRetCd("-1");
				// 사은품구성 수량을 0이상 입력하세요.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.264"));
				return prdmMain;
			}

			// 자동주문가능한 상품 Check
			if ("Y".equals(StringUtils.NVL(prdmMain.getAutoOrdPsblYn()))) {
				// 자동주문가능한 상품은 자동주문분류 필수 입력
				if ("".equals(StringUtils.NVL(prdmMain.getAutoOrdClsCd()))) {
					prdmMain.setRetCd("-1");
					// 자동주문상품 분류를 입력하십시오.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.265"));
					return prdmMain;
				}

				// 자동주문가능한 상품은 자동주문상품명 필수 입력
				if ("".equals(StringUtils.NVL(prdmMain.getAutoOrdPrdNm()))) {
					prdmMain.setRetCd("-1");
					// 자동주문상품명을 선택하십시오.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.266"));
					return prdmMain;
				}
			}
		}

		// 대표채널이 EC인 경우에 Check
		if ("GE".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {
			// 자동주문가능한 상품 Check
			if ("Y".equals(StringUtils.NVL(prdmMain.getAutoOrdPsblYn()))) {
				// 자동주문가능한 상품은 자동주문분류 필수 입력
				if ("".equals(StringUtils.NVL(prdmMain.getAutoOrdClsCd()))) {
					prdmMain.setRetCd("-1");
					// 자동주문상품 분류를 입력하십시오.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.265"));
					return prdmMain;
				}

				// 자동주문가능한 상품은 자동주문상품명 필수 입력
				if ("".equals(StringUtils.NVL(prdmMain.getAutoOrdPrdNm()))) {
					prdmMain.setRetCd("-1");
					// 자동주문상품명을 선택하십시오.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.266"));
					return prdmMain;
				}
			}

			// 직송인경우( 3*** ) 묶음배송여부 필수 입력
			if ("3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
					&& (prdmMain.getBundlDlvCd() == null || "".equals(prdmMain.getBundlDlvCd()))) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "묶음배송여부" }));
				return prdmMain;
			}

			// 반품/교환비 유료이면 반품배송비코드,반품비용 필수입력
			if ("Y".equals(StringUtils.NVL(prdmMain.getExchRtpChrYn()))) {
				if (prdmMain.getRtpDlvcCd() == null || "".equals(prdmMain.getRtpDlvcCd())) {
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "반품(교환)비용코드" }));
					return prdmMain;
				}
				/*
				// 반품비용 검색
				PrdQryCond pPrdQryCond = new PrdQryCond();
				pPrdQryCond.setSupCd(prdmMain.getSupCd());
				pPrdQryCond.setDlvcCd(prdmMain.getRtpDlvcCd());
				logger.debug("pPrdQryCond=>"+ pPrdQryCond);
				EntityDataSet<DSMultiData> delyAmtList = prdEntity.setdelyAmt(pPrdQryCond);

				BigDecimal dlvcAmt = new BigDecimal("0");
				BigDecimal rtpCost = new BigDecimal("0");
				BigDecimal exchCost = new BigDecimal("0");
				BigDecimal stdAmt = new BigDecimal("1000");
				logger.debug("delyAmtList=>"+ delyAmtList);
				for (int i = 0; i < delyAmtList.size(); i++) {
					dlvcAmt = delyAmtList.getValues().get(i).getBigDecimal("dlvcAmt");
					rtpCost =  delyAmtList.getValues().get(i).getBigDecimal("rtpCost");
					exchCost =  delyAmtList.getValues().get(i).getBigDecimal("exchCost");

					if (rtpCost.compareTo(stdAmt) < 0) {
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg("반품비용은 1000원이상 입력해야 합니다." );
						return prdmMain;
					}

					if (exchCost.compareTo(stdAmt) < 0) {
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg("교환비용은 1000원이상 입력해야 합니다." );
						return prdmMain;
					}
				}*/

			}

			// 합포장은 반품코드 필수 입력
			if ("Y".equals(StringUtils.NVL(prdmMain.getOboxCd())) && "Y".equals(prdmMain.getChrDlvcCd())) {
				if (prdmMain.getRtpDlvcCd() == null || "".equals(prdmMain.getRtpDlvcCd())) {
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "반품(교환)비용코드" }));
					return prdmMain;
				}
			}

			// GS단독상품인 경우 GS단독노출일자(from,to) 일자 필수입력
			if (!prdUdaDtl.isEmpty() && prdUdaDtl.size() > 0) {
				for (int i = 0; i < prdUdaDtl.size(); i++) {
					logger.debug("prdUdaDtl getChk ********** " + StringUtils.NVL(prdUdaDtl.get(i).getChk()) + " **********");
					if(("Y".equals(StringUtils.NVL(prdUdaDtl.get(i).getChk()))) &&
					   ("1045".equals(StringUtils.NVL(prdUdaDtl.get(i).getUdaNo().toString())) ||
		                "1046".equals(StringUtils.NVL(prdUdaDtl.get(i).getUdaNo().toString())) ||
		                "1047".equals(StringUtils.NVL(prdUdaDtl.get(i).getUdaNo().toString()))) &&
		                   ("".equals(StringUtils.NVL(prdUdaDtl.get(i).getValidStrDtm())) ||
		                    "".equals(StringUtils.NVL(prdUdaDtl.get(i).getValidEndDtm()))) ){
						prdmMain.setRetCd("-1");
						// GS단독 노출일자를 확인하세요.
						prdmMain.setRetMsg(Message.getMessage("prd.msg.267"));
						return prdmMain;
					}
				}
			}

			// 상품정보단위 관리 상품분류의 상품은 "단위" 필수입력
			if ("Y".equals(unitMandtYn)) {
				if (!prdNumvalDinfo.isEmpty()) {
					int check = 0;
					for (int i = 0; i < prdNumvalDinfo.size(); i++) {
						if (prdNumvalDinfo.get(i).getUnitVal1() != null || prdNumvalDinfo.get(i).getUnitVal2() != null) {
							check++;
						}
					}
					if (check == 0) {
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg(Message.getMessage("prd.msg.284"));
						return prdmMain;
					}
				}
			}
		}
		
		//SR02180510025 세일즈원과 위드넷 자동주문상품명 길이 일치 처리 
		// 자동주문가능한 상품 Check
		if ("Y".equals(StringUtils.NVL(prdmMain.getAutoOrdPsblYn()))) {
			String ProdName = prdmMain.getAutoOrdPrdNm(); // 자동주문상품명                                                                                                                       
			
			int lengthChk = 0;
			try {
			    if (ProdName.getBytes("UTF-8").length > 51) {
			        lengthChk++;
			    }
			} catch (Exception e) {
				//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
			    SMTCLogger.errorPrd(e.getMessage(), e);
			} finally {
			    if ( lengthChk > 0 ) {
			        prdmMain.setRetCd("-1");                                                                                                                                             
					prdmMain.setRetMsg("자동주문상품명은 51byte 이내(한글17자)로 등록가능합니다.");                                                                                                                 
					return prdmMain; 
			    }
			}
			
		}
		//SR02180510025 세일즈원과 위드넷 자동주문상품명 길이 일치 처리 
		
		
		//SMTCLogger.writePrd("필수 입력 값 Check32");
		/*[패널]해외배송 2014.07.31 윤승욱(sitjjang)
		 * 해외배송무게값 필수입력 */
		if ("Y".equals(StringUtils.NVL(prdmMain.getForgnDlvPsblYn()))
				&& prdmMain.getForgnDlvWeihtVal() == null) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.220", new String[] { "해외배송가능상품", "해외배송무게값" })); // {0}은(는) {1}을(를) 반드시 입력해야 합니다.
			return prdmMain;
		}
		
		//SMTCLogger.writePrd("필수 입력 값 Check31");
		////////////////////////////////////////////////////////////////
		//[현대백화점제휴-패널] 현대백화점 제휴여부 조회 및 백화점관련 필수값 체크 sagekim	2014.08.20 
		SupMstQryCond pSupMstQryCond = new SupMstQryCond();
		pSupMstQryCond.setUdaNo(Constant.getString("hris.uda.no"));
		//[SR02160315161][2016.05.12][김영현]:롯데백화점 RIS 연동을 위한 세일즈원 상품 정보 변경요청
		//일반상품, 수수료 매입일 경우 백화점 마진 정보 확인
		if("00".equals(StringUtils.NVL(prdmMain.getGftTypCd())) && "03".equals(StringUtils.NVL(prdmMain.getPrchTypCd()))){
			pSupMstQryCond.setLlcUdaNo(Constant.getString("llc.uda.no"));
		}
		pSupMstQryCond.setSupCd(prdmMain.getSupCd());
		EntityDataSet<DSData> dpatInfoData = vendrEntity.getDpatInfo(pSupMstQryCond);		
		
		if (dpatInfoData.getValues() != null) {
			if (prdprcHinsert.getDpatOpVal() == null || "".equals(prdprcHinsert.getDpatOpVal())) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "백화점 OP코드" }));
				return prdmMain;
			}
			if (prdprcHinsert.getDpatPrdVal() == null || "".equals(prdprcHinsert.getDpatPrdVal())) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "백화점 상품코드" }));
				return prdmMain;
			}
			if (prdprcHinsert.getGshsFeeRt() == null || "".equals(prdprcHinsert.getGshsFeeRt())) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "홈쇼핑 수수료율" }));
				return prdmMain;
			}
			if (prdprcHinsert.getDpatSaleTypVal() == null || "".equals(prdprcHinsert.getDpatSaleTypVal())) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "백화점매출유형값" }));
				return prdmMain;
			}
			if (prdprcHinsert.getDpatMargnSeq() == null || "".equals(prdprcHinsert.getDpatMargnSeq())) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "백화점마진순번" }));
				return prdmMain;
			}
		}
		////////////////////////////////////////////////////////////////
		
		prdmMain.setRetCd("0");
		prdmMain.setRetMsg("체크로직 통과");
		return prdmMain;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품등록 시  엽관입력 값을 체크한다.
	 *
	 * </pre>
	 *
	 * @author Sojunggu
	 * @date 2011-02-28 10:49:08
	 * @param SupDtlInfo
	 *            supDtlInfo, PrdmMain prdmMain, PrdprcHinsert prdprcHinsert, PrdPrdDinsert prdPrdD, SafeCertPrd psafeCertPrd, List<PrdSpecVal>
	 *            prdSpecInfo, List<PrdUdaDtl> prdUdaDtl
	 * @return PrdmMain
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public PrdmMain checkPrdmRelativeValue(SupDtlInfo supDtlInfo, PrdmMain prdmMain, PrdprcHinsert prdprcHinsert,
			PrdPrdDinsert prdPrdD, SafeCertPrd psafeCertPrd, List<PrdSpecVal> prdSpecInfo, List<PrdUdaDtl> prdUdaDtl, List prdNmChg) {
		/**
		 * 연관필드 Check 및 필수 입력사항 Default Setting *
		 **/
		// prchTypCd 조건부매입은(04) 는 해당협력사가 조건부매입 가능일 경우에만 가능
		if ("04".equals(StringUtils.NVL(prdmMain.getPrchTypCd()))
				&& !"Y".equals(StringUtils.NVL(supDtlInfo.getCondtlPrchPmsnYn()))) {
			prdmMain.setRetCd("-1");
			// 조건부매입 가능거래처가 아닙니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.223", new String[] { "조건부매입 가능거래처" }));
			return prdmMain;
		}
		logger.debug("checkPrdmRelativeValue1");
		////SMTCLogger.writePrd("checkPrdmRelativeValue1");
		// 사은품 유형이 사은품-당사제공 ,경품-당사제공(고객부담),경품-당사제공(당사부담) 은 완전매입/조건부매입만 가능
		if (prdmMain.getGftTypCd() != null
				&& ("01".equals(prdmMain.getGftTypCd()) || "03".equals(prdmMain.getGftTypCd()) || "04".equals(prdmMain
						.getGftTypCd()))
				&& (!"01".equals(StringUtils.NVL(prdmMain.getPrchTypCd())) && !"04".equals(StringUtils.NVL(prdmMain
						.getPrchTypCd())))) {
			prdmMain.setRetCd("-1");
			// 매입형태는 완전/조건부매입이어야 합니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.224", new String[] { "매입형태", "완전/조건부매입" }));
			return prdmMain;
		}
		logger.debug("checkPrdmRelativeValue2");
		//SMTCLogger.writePrd("checkPrdmRelativeValue2");

		/*
		 상품타입 당사상품권은 배송수거방법이 "택배귀금속-택배수거(1200)" 만 가능
		if ("Y".equals(StringUtils.NVL(prdmMain.getGshsGftcertYn()))
				&& !"1200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))) {
			prdmMain.setRetCd("-1");
			// 당사상품권의 배송수거는 택배귀금속-택배수거입니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.225"));
			return prdmMain;
		}
		*/

		//[패널] 모바일상품권 2013.12.17 김영준(kyjhouse)  GS상품권무형상품코드
		
		//상품타입 당사상품권
		if ("Y".equals(StringUtils.NVL(prdmMain.getGshsGftcertYn()))){
			
			//GS상품권PP이고 입고택배가 아니면
			if(    "N".equals(prdmMain.getGsPrdFrmlesPrdTypCd())
			   && !"1200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
			){
				prdmMain.setRetCd("-1");
				// 당사상품권의 배송수거는 택배귀금속-택배수거입니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.225"));
				return prdmMain;
			//GS상품권(email) 이거나 GS상품권(모바일) 이고 직송이 아니면

			}else if(     ("E".equals(prdmMain.getGsPrdFrmlesPrdTypCd()) || "D".equals(prdmMain.getGsPrdFrmlesPrdTypCd()))
					  &&  !"3200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))

			){
				prdmMain.setRetCd("-1");
				// 당사상품권의 배송수거는 직송(택배)-업체수거입니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.520"));
				return prdmMain;
			}
		}

		logger.debug("checkPrdmRelativeValue3");
		//SMTCLogger.writePrd("checkPrdmRelativeValue3");
		// 직반출 선택시 ( 1300, 1400, 1500, 2100, 2400,2700) => 협력사정보가 직반출가능업체만 가능
		/*
		if (prdmMain.getDlvPickMthodCd() != null
				&& ("1300".equals(prdmMain.getDlvPickMthodCd()) || "1400".equals(prdmMain.getDlvPickMthodCd())
						|| "1500".equals(prdmMain.getDlvPickMthodCd()) || "2100".equals(prdmMain.getDlvPickMthodCd())
						|| "2400".equals(prdmMain.getDlvPickMthodCd()) || "2700".equals(prdmMain.getDlvPickMthodCd()))
				&& !"Y".equals(StringUtils.NVL(supDtlInfo.getDirTakoutYn()))) {
			prdmMain.setRetCd("-1");
			// 직반출 불가능 거래처입니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.226"));
			return prdmMain;

		}
		*/
		
		
		//[HANGBOT-9096 GSITMBO-4581] 2021.04.21 이태호  위수탁 반품 수거형태 직반출 Default 변경관련
		//완전매입이은 택배수거 불가능 거래처라도 택배수거 해도 됨.
		/* 개발은 했지만 오픈 시 혼선을 막기 위해 서버단에서 체크하는 로직 제거, 등록화면에서만 체크하게 함. 수정화면에서는 체크하지 않음. 아래 소스는 공통이라 같이 주석처리함.
		if (!"01".equals(StringUtils.NVL(prdmMain.getPrchTypCd()))
				&& prdmMain.getDlvPickMthodCd() != null
				&& ("1000".equals(prdmMain.getDlvPickMthodCd()) || "1100".equals(prdmMain.getDlvPickMthodCd())
						|| "1200".equals(prdmMain.getDlvPickMthodCd()) || "2000".equals(prdmMain.getDlvPickMthodCd())
						|| "2300".equals(prdmMain.getDlvPickMthodCd()) || "2600".equals(prdmMain.getDlvPickMthodCd()))
				&& !"Y".equals(StringUtils.NVL(supDtlInfo.getDlvsPickPsblYn()))) {
			prdmMain.setRetCd("-1");
			// 택배수거 불가능 거래처입니다.
			prdmMain.setRetMsg("택배수거 불가능 거래처입니다.");
			return prdmMain;

		}
		*/
		
		logger.debug("checkPrdmRelativeValue4");
		//SMTCLogger.writePrd("checkPrdmRelativeValue4");
		// 매입유형이 완전매입이면 직반출 배송수거방법(1300, 1400, 1500 2100 2400 2700 ) 불가처리
		if ("01".equals(StringUtils.NVL(prdmMain.getPrchTypCd()))
				&& ("1300".equals(prdmMain.getDlvPickMthodCd()) || "1400".equals(prdmMain.getDlvPickMthodCd())
						|| "1500".equals(prdmMain.getDlvPickMthodCd()) || "2100".equals(prdmMain.getDlvPickMthodCd())
						|| "2400".equals(prdmMain.getDlvPickMthodCd()) || "2700".equals(prdmMain.getDlvPickMthodCd()))) {
			prdmMain.setRetCd("-1");
			// 완전매입은 직반출을 선택할 수 없습니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.227"));
			return prdmMain;
		}

		// 완전매입 상품은 집하택배로 수정되지 않도록 변경 (sap 재구축 추가 : 2013/06/21 안승훈 )
		if ("01".equals(StringUtils.NVL(prdmMain.getPrchTypCd()))
				&& ("2300".equals(prdmMain.getDlvPickMthodCd()) || "2400".equals(prdmMain.getDlvPickMthodCd())
						|| "2500".equals(prdmMain.getDlvPickMthodCd()) || "2600".equals(prdmMain.getDlvPickMthodCd())
						|| "2700".equals(prdmMain.getDlvPickMthodCd()) || "2800".equals(prdmMain.getDlvPickMthodCd()))) {
			prdmMain.setRetCd("-1");
			// 완전매입 상품은 집하택배를 선택할 수 없습니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.519"));
			return prdmMain;
		}


		logger.debug("checkPrdmRelativeValue5");
		//SMTCLogger.writePrd("checkPrdmRelativeValue5");
		// 완전매입, 조건부매입인 경우 직택배 불가
		if (("01".equals(StringUtils.NVL(prdmMain.getPrchTypCd())) || "04".equals(StringUtils.NVL(prdmMain.getPrchTypCd())))
				&& (  ("2000".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())))
					||("2100".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())))
					||("2200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))))) {

			// 직택배-택배, 완전매입, 협력사의 3PL 설정이면 허용한다. (sap 재구축 추가 : 2013/01/23 안승훈 )
			if (!("01".equals(StringUtils.NVL(prdmMain.getPrchTypCd())) && "2000".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) && "Y".equals(StringUtils.NVL(supDtlInfo.getThplUseYn())) )) {
				prdmMain.setRetCd("-1");
				// 매입형태가 완전매입/조건부매입인 경우 배송형태 직택배는 사용불가능 합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.228"));
				return prdmMain;
			}
		}
		logger.debug("checkPrdmRelativeValue6");
		//SMTCLogger.writePrd("checkPrdmRelativeValue6");
		// 보험상품은 보험사명 필수 입력, 보험상품이 아닐경우 보험사명 없어야함
		if ("I".equals(StringUtils.NVL(prdmMain.getFrmlesPrdTypCd()))
				&& (prdmMain.getInsuCoCd() == null || "".equals(prdmMain.getInsuCoCd()))) {
			prdmMain.setRetCd("-1");
			// 보험상품은 반드시 보험상품 보험사명을 입력해야합니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.229"));
			return prdmMain;
		} else {
			if (!"I".equals(StringUtils.NVL(prdmMain.getFrmlesPrdTypCd()))){
				prdmMain.setInsuCoCd("");
			}
		}
		logger.debug("checkPrdmRelativeValue7");
		//SMTCLogger.writePrd("checkPrdmRelativeValue7");
		// 업체부담경품은 수수료분매입, 경품면세만 가능
		if (("07".equals(StringUtils.NVL(prdmMain.getGftTypCd())) || "08".equals(StringUtils
				.NVL(prdmMain.getGftTypCd())))) {
			if (!"03".equals(StringUtils.NVL(prdmMain.getPrchTypCd()))) {
				prdmMain.setRetCd("-1");
				// 업체부담 경품은 수수료분매입 매입형태만 가능합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.230"));
				return prdmMain;
			}
			//[SR02160729022][2016.08.31][김영현]:경품 공동부담 개발요청 - 업체부담 경품 과세 제한 로직 제거
			/*
			if (!"05".equals(StringUtils.NVL(prdmMain.getTaxTypCd()))) {
				prdmMain.setRetCd("-1");
				// 업체부담 경품은 경품면세 세금구분만 가능합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.232"));
				return prdmMain;
			}*/
		}
		logger.debug("checkPrdmRelativeValue8");
		//SMTCLogger.writePrd("checkPrdmRelativeValue8");
		// 사은품 유형이 경품일 때(03,04,07,08) 세금유형은 경품과세(04), 경품면세(05)로
		if (prdmMain.getGftTypCd() != null
				&& ("03".equals(prdmMain.getGftTypCd()) || "04".equals(prdmMain.getGftTypCd())
						|| "07".equals(prdmMain.getGftTypCd()) || "08".equals(prdmMain.getGftTypCd()))
				&& !("04".equals(StringUtils.NVL(prdmMain.getTaxTypCd())) || "05".equals(StringUtils.NVL(prdmMain
						.getTaxTypCd())))) {
			prdmMain.setRetCd("-1");
			// 세금구분과 사은품경품구분을 확인하십시오.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.231"));
			return prdmMain;
		}
		// 사은품유형이 사은품이고 속성상품인 경우 에러
		if ("S".equals(StringUtils.NVL(prdmMain.getPrdTypCd()))
				&& ("01".equals(prdmMain.getGftTypCd())||"02".equals(prdmMain.getGftTypCd()))) {
			prdmMain.setRetCd("-1");
			// 판매상품이 아닌 경우 속성상품으로 선택할수 없습니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.376"));
			return prdmMain;
		}
		logger.debug("checkPrdmRelativeValue9");
		//SMTCLogger.writePrd("checkPrdmRelativeValue9");
		// 1200 입고택배(귀금)-택배, 1500 입고택배(귀금)-직반 은 센터추가포장 필수 설정(강제설정 bpr 2013.04.15
		if (("1200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) || "1500".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())))
				&& !"Y".equals(StringUtils.NVL(prdmMain.getCentAddPkgYn()))) {
			prdmMain.setCentAddPkgYn("Y");
			//prdmMain.setRetCd("-1");
			// 귀금속은 센터추가포장을 해야합니다.
			//prdmMain.setRetMsg(Message.getMessage("prd.msg.233"));
			//return prdmMain;
		}
		logger.debug("checkPrdmRelativeValue10");
		//SMTCLogger.writePrd("checkPrdmRelativeValue10");
		// 유료배송은 직송만 가능, 자동주문 불가능
		if ("Y".equals(StringUtils.NVL(prdmMain.getChrDlvYn()))) {
			// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
			// 입고택배, 직택배도 유료배송 가능하도록 수정
			// --> 수정시작
			/*
			if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))) {
				prdmMain.setRetCd("-1");
				// 유료배송 항목은 직송배송 상품만 선택할 수 있습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.234"));
				return prdmMain;
			}
			*/
			// --> 수정종료
			if ("Y".equals(StringUtils.NVL(prdmMain.getAutoOrdPsblYn()))) {
				prdmMain.setRetCd("-1");
				// 유료배송 상품은 자동주문여부 항목을 선택할 수 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.235"));
				return prdmMain;
			}
		}
		// 유료교환/반품비는 직송만 가능
		if ("Y".equals(StringUtils.NVL(prdmMain.getExchRtpChrYn()))) {
			if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))) {
				// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
				// 입고택배, 직택배도 유료배송 가능하도록 수정
				// --> 수정시작
				/*
				prdmMain.setRetCd("-1");
				// 유료배송 항목은 직송배송 상품만 선택할 수 있습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.234"));
				return prdmMain;
				*/
				// --> 수정종료
			}
		}

		//[SR02161024027]배송 형태별 배송조건 변경 요청 건(조건에 따라 다름)
		//직송(설치)의 경우에만 '조건에 따라 다름 추가비용 발생' 항목 선택 가능
		if(!"31".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 2))
			&& "Y".equals(StringUtils.NVL(prdmMain.getChrDlvAddYn()))){
			prdmMain.setRetCd("-1");
			//배송형태가 직송(설치)의 경우에만 '조건에 따라 다름' 항목을 선택할 수 있습니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.546"));
			return prdmMain;
		}
		
		// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
		// 수정시작
		// 배송형태에 따른 묶음배송 유효성 체크
		if ("1".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
				&& "A02".equals(StringUtils.NVL(prdmMain.getBundlDlvCd()))) {
			prdmMain.setRetCd("-1");
			// 입고택배는 묶음배송 불가능[A02] 을 선택할수 없습니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.523"));
			return prdmMain;
		}
		
		if ("2".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
				&& !"".equals(StringUtils.NVL(prdmMain.getBundlDlvCd()))
				&& !"A03".equals(StringUtils.NVL(prdmMain.getBundlDlvCd()))) {
			prdmMain.setRetCd("-1");
			// 직택배는 묶음배송 불가능[배송비상품수만큼결제] 만 선택할수 있습니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.524"));
			return prdmMain;
		}
		// 수정종료
		
		logger.debug("checkPrdmRelativeValue11");
		//SMTCLogger.writePrd("checkPrdmRelativeValue11");
		// 상품타입이 당사상품권, 무형상품유형 문자쿠폰은 자동주문불가
		if ("Y".equals(StringUtils.NVL(prdmMain.getAutoOrdPsblYn()))) {
			if ("Y".equals(StringUtils.NVL(prdmMain.getGshsGftcertYn()))) {
				prdmMain.setRetCd("-1");
				// 당사상품권은 카드결재가 불가능하므로\n자동주문에 노출할 수 없습니다.\n\n자동주문여부를 확인하십시오
				prdmMain.setRetMsg(Message.getMessage("prd.msg.236"));
				return prdmMain;
			}
			if ("R".equals(StringUtils.NVL(prdmMain.getFrmlesPrdTypCd()))) {
				prdmMain.setRetCd("-1");
				// 문자쿠폰은 자동주문이 불가능합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.237"));
				return prdmMain;
			}
		}
		logger.debug("checkPrdmRelativeValue12");
		//SMTCLogger.writePrd("checkPrdmRelativeValue12");
		// 상품타입이 일반상품은 주문상품유형이 "일반/속성별주문" 만 가능
		if (("G".equals(StringUtils.NVL(prdmMain.getPrdTypCd())) || "P".equals(StringUtils.NVL(prdmMain.getPrdTypCd())))
				&& !"02".equals(StringUtils.NVL(prdmMain.getOrdPrdTypCd()))) {
			prdmMain.setRetCd("-1");
			// 상품타입이 일반상품이므로 주문형태는 속성별주문(02)만 가능합니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.238"));
			return prdmMain;
		}
		logger.debug("checkPrdmRelativeValue13");
		//SMTCLogger.writePrd("checkPrdmRelativeValue13");
		// 속성대표상품은 스타일상품만 가능
		if ("01".equals(StringUtils.NVL(prdmMain.getOrdPrdTypCd()))
				&& !"S".equals(StringUtils.NVL(prdmMain.getPrdTypCd()))) {
			prdmMain.setRetCd("-1");
			// 속성대표상품별주문은 스타일상품만 가능합니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.239"));
			return prdmMain;
		}
		logger.debug("checkPrdmRelativeValue14");
		//SMTCLogger.writePrd("checkPrdmRelativeValue14");
		// 50만원 이상은 편의점반품불가
		BigDecimal limitPrice = new BigDecimal("500000");
		if(prdprcHinsert.getSalePrc() != null) {
			if ("Y".equals(StringUtils.NVL(prdmMain.getCvsDlvsRtpYn()))
					&& limitPrice.compareTo(prdprcHinsert.getSalePrc()) <= 0) {
				prdmMain.setRetCd("-1");
				// 상품판매가가 오십만원(500,000원) 이상이면\n편의점 반품 가능 상품으로 등록 할 수 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.240"));
				return prdmMain;
			}
		}
		// MC/PC 물류 센터 상품은 편의점 반품을 선택할 수 없다. 2014.03.06 SR02140303125
		if("Y".equals(StringUtils.NVL(prdmMain.getCvsDlvsRtpYn())) &&
				"DC02".equals(StringUtils.NVL(prdmMain.getDtctCd())) )  {
			prdmMain.setRetCd("-1");
			//MC/PC센터전용상품은 편의점반품을 선택할 수 없습니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.389"));
			return prdmMain;
		}
		
		logger.debug("checkPrdmRelativeValue15");
		//SMTCLogger.writePrd("checkPrdmRelativeValue15");
		/*
		 * 환불유형 Check Start # 직송 ( 상품분류의 환불유형에 따른 설정가능값) 20 -> 20(즉시) : '상품확인후 환불||즉시환불' 30 -> 10(상품확인/직), 20(즉시) : '상품확인후 환불||즉시환불' 40 -> 10(상품확인/직),
		 * 20(즉시), 40(AS센터y) : '상품확인후 환불||즉시환불||업체반품절차따름' 50 -> 50(불가) : '기타' # 직택배,택배 ( 상품분류의 환불유형에 따른 설정가능값) 20 -> 20(즉시): '상품확인후 환불||상담원우선상담' 30 ->
		 * 30(상품확인/택), 20(즉시):'상품확인후 환불||상담원우선상담' 40 -> 30(상품확인/택), 20(즉시), 40(AS센터):'상품확인후 환불||상담원우선상담||업체반품절차에따라다름' 50 -> 50(불가) : '기타' 입력한 환불유형방법이
		 * 해당분류, 배송수거형태에 속한값이 아닙니다.'+ls_rtn_info+"만 가능합니다.
		 */
		PrdClsQryCond prdClsCond = new PrdClsQryCond();
		prdClsCond.setPrdClsCd(prdmMain.getPrdClsCd());
		prdClsCond.setClsLvlNo(new BigDecimal(4));
		EntityDataSet<DSMultiData> prdClsListInfo = prdClsBaseEntity.getPrdClsList(prdClsCond);

		String clsRfnTyp = prdClsListInfo.getValues().get(0).getString("rfnTypCd"); // 상품분류의 환불유형
		String prdRfnTyp = StringUtils.NVL(prdmMain.getRfnTypCd()); // 상품기본의 환불유형
		logger.debug("checkPrdmRelativeValue15==clsRfnTyp=>"+ clsRfnTyp);
		logger.debug("checkPrdmRelativeValue15==prdRfnTyp=>"+ prdRfnTyp);
		//OAHU그룹추가 BPR 2013.03.05
		if( !prdmMain.getFrmlesPrdTypCd().equals("N") ||
			 StringUtils.NVL(prdmMain.getRepPrdYn()).equals("Y") ||
			 StringUtils.NVL(prdmMain.getInstlDlvPrdYn()).equals("Y") ){
			if (prdmMain.getFrmlesPrdTypCd().equals("R") && ( "GE".equals(prdmMain.getRegChanlGrpCd()) ||
															  "GS".equals(prdmMain.getRegChanlGrpCd())) ) {
				if (!("10".equals(prdRfnTyp) || "50".equals(prdRfnTyp))) {
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||기타" }));
					return prdmMain;
				}
			} else {
				if (!("50".equals(prdRfnTyp)) && !"3100".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))) {
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "기타" }));
					return prdmMain;
				}
			}
		} else if ("3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))) {
			//[SR02160614099][2016.06.17][김영현]:해외직구 환불유형 선택 목록 추가 요청 
			//직송인 경우 해외 상품은 상품확인후환불(직송), 업체AS센터문의만 사용 가능
			if ("20".equals(clsRfnTyp) ) {
				if("Y".equals(StringUtils.NVL(prdmMain.getForgnDirprhYn()))){
					if (!"10".equals(prdRfnTyp) && !"40".equals(prdRfnTyp)) {
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||업체반품절차따름" }));
						return prdmMain;
					}				
				}else{
					if("GC".equals(prdmMain.getRegChanlGrpCd()) ){
						if( !"20".equals(prdRfnTyp) ) {
							prdmMain.setRetCd("-1");
							prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||즉시환불[방송상품]" }));
							return prdmMain;
						}
					} else {
						if( !"10".equals(prdRfnTyp) && !"20".equals(prdRfnTyp) ) {
							prdmMain.setRetCd("-1");
							prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||즉시환불" }));
							return prdmMain;
						}
					}
				}
			}
			if ("30".equals(clsRfnTyp)){
				if("Y".equals(StringUtils.NVL(prdmMain.getForgnDirprhYn()))){
					if (!"10".equals(prdRfnTyp) && !"40".equals(prdRfnTyp)) {
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||업체반품절차따름" }));
						return prdmMain;
					}				
				}else{
					if(!("10".equals(prdRfnTyp) || "20".equals(prdRfnTyp))) {
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||즉시환불" }));
						return prdmMain;
					}
				}
			}
			if ("40".equals(clsRfnTyp) && !("10".equals(prdRfnTyp) || "20".equals(prdRfnTyp) || "40".equals(prdRfnTyp) || ("50".equals(prdRfnTyp) && "3100".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))) )) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||즉시환불||업체반품절차따름" }));
				return prdmMain;
			}
			if ("50".equals(clsRfnTyp) && !"50".equals(prdRfnTyp)) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "기타" }));
				return prdmMain;
			}

		} else if ("1".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1)) || "2".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))) {
			logger.debug("checkPrdmRelativeValue16");
			//SMTCLogger.writePrd("checkPrdmRelativeValue16");
			if ("20".equals(clsRfnTyp) ) {

				//&& !("30".equals(prdRfnTyp) || "20".equals(prdRfnTyp))
				if("GC".equals(prdmMain.getRegChanlGrpCd()) ){
					if( !"20".equals(prdRfnTyp) ) {
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "즉시환불[방송상품]" }));
						return prdmMain;
					}
				} else {
					if( !"20".equals(prdRfnTyp) && !"30".equals(prdRfnTyp) ) {
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||상담원우선상담" }));
						return prdmMain;
					}
				}
			}
			if ("30".equals(clsRfnTyp) && !("30".equals(prdRfnTyp) || "20".equals(prdRfnTyp))) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||상담원우선상담" }));
				return prdmMain;
			}
			if ("40".equals(clsRfnTyp) && !("30".equals(prdRfnTyp) || "20".equals(prdRfnTyp) || "40".equals(prdRfnTyp))) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "상품확인후 환불||상담원우선상담||업체반품절차에따라다름" }));
				return prdmMain;
			}
			if ("50".equals(clsRfnTyp) && !"50".equals(prdRfnTyp)) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.281", new String[] { "기타" }));
				return prdmMain;
			}
		}
		//직송상품만 합포장가능 2012.05.10
		if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
				&& !"".equals(StringUtils.NVL(prdmMain.getOboxCd()))) {
			prdmMain.setRetCd("-1");
			// 합포장상품은 지정택배수거를 입력 할 수 없습니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.385"));
			return prdmMain;
		}

		if ("Y".equals(StringUtils.NVL(prdmMain.getExchRtpChrYn()))) {
			// 반품비용 검색
			PrdQryCond pPrdQryCond = new PrdQryCond();
			pPrdQryCond.setSupCd(prdmMain.getSupCd());
			pPrdQryCond.setDlvcCd(prdmMain.getRtpDlvcCd());
			pPrdQryCond.setRtpOnewyRndtrpCd(prdmMain.getRtpOnewyRndtrpCd()) ;
			pPrdQryCond.setExchOnewyRndtrpCd(prdmMain.getExchOnewyRndtrpCd());
			logger.debug("pPrdQryCond=>"+ pPrdQryCond);
			EntityDataSet<DSMultiData> delyAmtList = prdEntity.setdelyAmt(pPrdQryCond);

//			BigDecimal dlvcAmt = new BigDecimal("0");
			BigDecimal rtpCost = new BigDecimal("0");
			BigDecimal exchCost = new BigDecimal("0");
			BigDecimal stdAmt = new BigDecimal("1000");
			BigDecimal stdZero = new BigDecimal("0");
			logger.debug("delyAmtList=>"+ delyAmtList);
			for (int i = 0; i < delyAmtList.size(); i++) {
//				dlvcAmt = delyAmtList.getValues().get(i).getBigDecimal("dlvcAmt");
				rtpCost =  delyAmtList.getValues().get(i).getBigDecimal("rtpCost");
				exchCost =  delyAmtList.getValues().get(i).getBigDecimal("exchCost");

				if (rtpCost.compareTo(stdAmt) < 0 && rtpCost.compareTo(stdZero) > 0 ) {
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg("반품비용은 1000원 이상 입력해야 합니다." );
					return prdmMain;
				}

				if (exchCost.compareTo(stdAmt) < 0 && exchCost.compareTo(stdZero) > 0 ) {
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg("교환비용은 1000원 이상 입력해야 합니다." );
					return prdmMain;
				}
			}
		}
		logger.debug("checkPrdmRelativeValue17");
		//SMTCLogger.writePrd("checkPrdmRelativeValue17");
		/* HANGBOT-16987_GSITMBO-9804 지정택배수거 종료
		// 지정택배수거일때 체크로직 Start
		if ("Y".equals(StringUtils.NVL(prdmMain.getApntDlvsImplmYn()))) {

			// 배송형태가 직송(설치) 또는 택배사가 '업체(설치)'배송인 경우 지정택배수거 불가
			//if ("3100".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
			//		|| "ZY".equals(StringUtils.NVL(prdmMain.getDlvsCoCd()))) {
			//	prdmMain.setRetCd("-1");
				// 배송형태가 직송(설치) 또는 택배사가 업체(설치)배송인 경우는 지정택배수거를 입력 할 수 없습니다.
			//	prdmMain.setRetMsg(Message.getMessage("prd.msg.273"));
			//	return prdmMain;
			//}
			logger.debug("checkPrdmRelativeValue18");
			// 직송( 3***)의 합포장은 지정택배수거 불가
			if ("3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
					&& !"".equals(StringUtils.NVL(prdmMain.getOboxCd()))) {
				prdmMain.setRetCd("-1");
				// 합포장상품은 지정택배수거를 입력 할 수 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.242"));
				return prdmMain;
			}
			logger.debug("checkPrdmRelativeValue19");
			//SMTCLogger.writePrd("checkPrdmRelativeValue19");
			// 협력사.지정택배수거형태가 A, B가 아니면 지정택배 불가
			
			if (!"A".equals(StringUtils.NVL(supDtlInfo.getApntDlvsPickTypCd()))
					&& !"B".equals(StringUtils.NVL(supDtlInfo.getApntDlvsPickTypCd()))) {
				prdmMain.setRetCd("-1");
				// 지정택배수거 가능 업체가 아닙니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.243"));
				return prdmMain;
			}
			
			logger.debug("checkPrdmRelativeValue20");
			//SMTCLogger.writePrd("checkPrdmRelativeValue20");
			// 수거방법이 업체수거가 아니멸 지정택배수거 불가 ( 2200,2500,2800, 3100, 3200, 3400 )
			if (prdmMain.getDlvPickMthodCd() != null
					&& (!"2200".equals(prdmMain.getDlvPickMthodCd()) && !"2500".equals(prdmMain.getDlvPickMthodCd())
							&& !"2800".equals(prdmMain.getDlvPickMthodCd())
							&& !"3100".equals(prdmMain.getDlvPickMthodCd())
							&& !"3200".equals(prdmMain.getDlvPickMthodCd()) && !"3400".equals(prdmMain
							.getDlvPickMthodCd()))) {
				prdmMain.setRetCd("-1");
				// 지정택배수거는 수거형태가 [업체] 만 가능합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.244"));
				return prdmMain;
			}
			logger.debug("checkPrdmRelativeValue21");
			//SMTCLogger.writePrd("checkPrdmRelativeValue21");
			// 합포장유형이 화장품합포장, 도서합포장은 지정택배수거 불가
			if ("1".equals(StringUtils.NVL(prdmMain.getOboxCd())) || "2".equals(StringUtils.NVL(prdmMain.getOboxCd()))) {
				prdmMain.setRetCd("-1");
				// 지정택배수거는 화장품/도서 합포장상품은 불가합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.245"));
				return prdmMain;
			}
		} else {
			/*로직제거 20110711
			// 협력사.지정택배수거형태가 A, B가 이고 복사상품인 경우 지정택배 수정불가 , 업체수거만
			if (!"N".equals(StringUtils.NVL(supDtlInfo.getApntDlvsPickTypCd()))
					&& "Y".equals(StringUtils.NVL(prdmMain.getCopyPrdYn()))
					&& ("2200".equals(prdmMain.getDlvPickMthodCd())
							|| "2500".equals(prdmMain.getDlvPickMthodCd())
							|| "2800".equals(prdmMain.getDlvPickMthodCd())
							|| "3100".equals(prdmMain.getDlvPickMthodCd())
							|| "3200".equals(prdmMain.getDlvPickMthodCd())
							|| "3400".equals(prdmMain.getDlvPickMthodCd()))) {
				prdmMain.setRetCd("-1");
				// 지정택배업체의 복사상품인 경우 지정택배수거여부는 반드시 'Y'이어야 합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.382"));
				return prdmMain;
			}
			//  
		} */

		logger.debug("checkPrdmRelativeValue23");
		//SMTCLogger.writePrd("checkPrdmRelativeValue23");
		// 배송수거방법(1*, 2*) 상품은 택배사가 대한통운, 한진 ,동부 만 가능 동부추가 2013.05.08
		if ("1".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
				|| "2".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))) {
			if (prdmMain.getDlvsCoCd() != null
					&& !("HJ".equals(prdmMain.getDlvsCoCd()) || "HF".equals(prdmMain.getDlvsCoCd())
							|| "DH".equals(prdmMain.getDlvsCoCd()) || "DH1".equals(prdmMain.getDlvsCoCd())
							|| "FA".equals(prdmMain.getDlvsCoCd()) )) {

				prdmMain.setRetCd("-1");
				// 현 배송수거 방법은 택배사가 한진, 현대, 대한통운입니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.247"));
				return prdmMain;

			}
		}
		logger.debug("checkPrdmRelativeValue24");
		//SMTCLogger.writePrd("checkPrdmRelativeValue24");
		// 배송수거방법(3000,3200) 상품은 택배사가 현대, 대한통운, 한진 만 가능 동부 만 가능 동부추가 2013.05.08
		if ("3000".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))) {
				//|| "3200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))) {
			if (prdmMain.getDlvsCoCd() != null
					&& !("HJ".equals(prdmMain.getDlvsCoCd()) || "HF".equals(prdmMain.getDlvsCoCd())
							|| "DH".equals(prdmMain.getDlvsCoCd()) || "DH1".equals(prdmMain.getDlvsCoCd())
							|| "FA".equals(prdmMain.getDlvsCoCd()) )) {

				prdmMain.setRetCd("-1");
				// 현 배송수거 방법은 택배사가 한진, 동부, 대한통운입니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.247"));
				return prdmMain;

			}
		}
		logger.debug("checkPrdmRelativeValue25");
		//SMTCLogger.writePrd("checkPrdmRelativeValue25");
		// 배송수거방법이 직송(우편)-업체수거(3400) 이면 우체국등기(ER) 만 가능
		if ("3400".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
				&& !"ER".equals(StringUtils.NVL(prdmMain.getDlvsCoCd()))) {
			prdmMain.setRetCd("-1");
			// 현 배송수거 방법은 우체국등기만 가능합니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.248"));
			return prdmMain;
		}
		logger.debug("checkPrdmRelativeValue26");
		//SMTCLogger.writePrd("checkPrdmRelativeValue26");
		String currDate = DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"); // 오늘날짜
		String txnEndDt = supDtlInfo.getTxnEndDt(); // 협력사 거래종료일자
		String contYn   = supDtlInfo.getContYn();
		logger.debug("contYn:::::"+contYn);
		if( "N".equals(StringUtils.NVL(contYn)) ){
			prdmMain.setRetCd("-1");

			//prd.msg.383 - 기본거래 계약서 계약 완료 후 상품등록 가능합니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.383"));
			return prdmMain;
		}

		if (!"".equals(StringUtils.NVL(txnEndDt))) {
			// 협력사의 거래종료일자가 과거인경우 상품등록불가
			if (txnEndDt.compareTo(currDate) < 0) {
				prdmMain.setRetCd("-1");
				// 거래처가 '+string(ldt_supp_end_date)+'일짜로 거래종료되어 상품을 등록할 수 없습니다
				prdmMain.setRetMsg(Message.getMessage("prd.msg.271", new String[] { txnEndDt.substring(0, 8) }));
				return prdmMain;
			}
			logger.debug("checkPrdmRelativeValue27");
			//SMTCLogger.writePrd("checkPrdmRelativeValue27");
			// 판매종료일시는 거래처 종료일시보다 작게 입력
			if (!"".equals(StringUtils.NVL(prdmMain.getSaleEndDtm())) && !"29991231235959".equals(StringUtils.NVL(prdmMain.getSaleEndDtm()))) {
				if (prdmMain.getSaleEndDtm().compareTo(txnEndDt) > 0) {
					prdmMain.setRetCd("-1");
					// 판매종료일시는 거래처 종료일시보다 작게 입력해야 합니다.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.272", new String[] { txnEndDt.substring(0, 8) }));
					return prdmMain;
				}
			}
		}
		logger.debug("checkPrdmRelativeValue28");
		//SMTCLogger.writePrd("checkPrdmRelativeValue28");
		// 편의점 반품 시 Check Logic
		if ("Y".equals(StringUtils.NVL(prdmMain.getCvsDlvsRtpYn()))) {
			// 반품비무료 인경우 직택배-업체수거, 센터집하-업체수거, 거점집하-업체수거 는 편의점택배수거 불가
			//2013-10-04 (SR02130819032) 반품비무료 인경우 직택배-업체수거는 편의점택배수거가능하도록 수정처리
			/* [SR02160630101][2016.0629][김영현]:편의점반품 설정 로직 추가
			if ("N".equals(StringUtils.NVL(prdmMain.getExchRtpChrYn()))
					&& ("2500".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) || "2800"
							.equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())))) {
				prdmMain.setRetCd("-1");
				// 무료반품인 경우는 편의점 택배를 선택 할 수 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.249"));
				return prdmMain;
			} */
			logger.debug("checkPrdmRelativeValue29");
			//SMTCLogger.writePrd("checkPrdmRelativeValue29");
			// 직송( 3***) && 합포장상품은 편의점택배수거 불가
			if ("3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))
					&& !"".equals(StringUtils.NVL(prdmMain.getOboxCd()))) {
				prdmMain.setRetCd("-1");
				// 합포장상품은 편의점택배를 입력 할 수 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.250"));
				return prdmMain;
			}

			// 편의점택배수거가능한 협력사의 상품만 편의점택배수거 가능
			if (!"Y".equals(StringUtils.NVL(supDtlInfo.getCvsRtpYn()))) {
				prdmMain.setRetCd("-1");
				// 업체가 편의점택배 대상일 경우 상품선택이 가능합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.251"));
				return prdmMain;
			}
			logger.debug("checkPrdmRelativeValue30");
			//SMTCLogger.writePrd("checkPrdmRelativeValue30");
			// 편의점택배수거는 입고택배(의류)-택배 1100, 입고택배(의류)-직반 1400, 직송설치,원재료는 제외 ( 3100, 5000, 택배사:'ZY')
			if (prdmMain.getDlvPickMthodCd() != null
					&& ("1100".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
							|| "1400".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))
							|| "3100".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) || "3500"
							.equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())))) {
				prdmMain.setRetCd("-1");
				// 배송수거형태가 입고택배(의류), 직송(설치), 원재료/나석은 편의점 택배를 선택 할 수 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.252"));
				return prdmMain;
			}
		}
		logger.debug("checkPrdmRelativeValue31");
		//SMTCLogger.writePrd("checkPrdmRelativeValue31");
		// 사은품유형이 판매상품이 아니면 세금계산서발행이 불가
		if (!"00".equals(StringUtils.NVL(prdmMain.getGftTypCd())) && "Y".equals(StringUtils.NVL(prdmMain.getTaxInvIssueYn()))) {
			prdmMain.setRetCd("-1");
			// 사은품유형이 판매상품이 아니면 세금계산서발행이 불가합니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.283"));
			return prdmMain;
		}
		logger.debug("checkPrdmRelativeValue32");
		//SMTCLogger.writePrd("checkPrdmRelativeValue32");
		// 대표채널이 CA인 경우에 Check
		if ("GC".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {
			// 방송중상품 필드를 수정한경우 수정가능 체크
			// - MD팀장합격 이전까지는 수정 가능
			// - MD팀장이후에는 편성팀만 수정 가능
			// 방송중판매 필드는 MD팀장 결재 이전 또는 편성팀에만 권한이 있습니다.
			// CA인 경우 상품구분이 일반/방송상품이 아닌경우 안됨
			if ("10".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))
					|| "20".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))
					|| "Y".equals(StringUtils.NVL(prdmMain.getRepPrdYn()))) {
				prdmMain.setRetCd("-1");
				// 플레인/몰인몰 상품등은 TV그룹으로 등록할수 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.379"));
				return prdmMain;
			}
			logger.debug("checkPrdmRelativeValue322");
			//SMTCLogger.writePrd("checkPrdmRelativeValue322");
		}
		logger.debug("checkPrdmRelativeValue33");
		//SMTCLogger.writePrd("checkPrdmRelativeValue33");
		// 대표채널이 EC인 경우에 Check
		if ("GE".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {
			// 협력사에 따른 배송부가 정보 조회
			PrdQryCond pPrdQryCond1 = new PrdQryCond();
			pPrdQryCond1.setSupCd(prdmMain.getSupCd());
			EntityDataSet<DSMultiData> delyAmtInfo = prdEntity.setdelyAmt(pPrdQryCond1);
			logger.debug("checkPrdmRelativeValue34");
			//SMTCLogger.writePrd("checkPrdmRelativeValue34");
			// 합포장일 경우 Check
			if (!"".equals(StringUtils.NVL(prdmMain.getOboxCd()))) {
				pPrdQryCond1.setSupCd(new BigDecimal ("989081"));
				EntityDataSet<DSMultiData> oboxDelyAmtInfo = prdEntity.setdelyAmt(pPrdQryCond1);
				// 합포장은 교환이용코드 : 왕복만 가능
				if (!"2".equals(StringUtils.NVL(prdmMain.getExchOnewyRndtrpCd())) && "Y".equals(prdmMain.getRtpDlvcCd())) {
					prdmMain.setRetCd("-1");
					// EC합포장은 왕복만 가능합니다.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.253"));
					return prdmMain;
				}
				logger.debug("checkPrdmRelativeValue35");
				//SMTCLogger.writePrd("checkPrdmRelativeValue35");
				// 합포장은 반품이용코드 : 편도만 가능
				if (!"1".equals(StringUtils.NVL(prdmMain.getRtpOnewyRndtrpCd())) && "Y".equals(prdmMain.getRtpDlvcCd())) {
					prdmMain.setRetCd("-1");
					// EC합포장은 편도만 가능합니다.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.254"));
					return prdmMain;
				}
				logger.debug("checkPrdmRelativeValue36");
				//SMTCLogger.writePrd("checkPrdmRelativeValue36");
				/* 합포장 상품은 유료만 가능 --> 무료도 가능 20110501 김주영
				if ("N".equals(prdmMain.getChrDlvYn())) {
					prdmMain.setRetCd("-1");
					// EC합포장은 유료만 가능합니다.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.255"));
					return prdmMain;
				}
				*/
				logger.debug("checkPrdmRelativeValue37");
				//SMTCLogger.writePrd("checkPrdmRelativeValue37");
				// 합포장인 경우 합포장배송비인지 체크
				String prdOboxCd = "";
				for (int i = 0; i < oboxDelyAmtInfo.size(); i++) {
					if (!"".equals(StringUtils.NVL(oboxDelyAmtInfo.getValues().get(i).getString("prdOboxCd")))) {
						prdOboxCd = "Y";
					}
				}
				if (!"Y".equals(prdOboxCd) && "Y".equals(prdmMain.getChrDlvcCd())) {
					prdmMain.setRetCd("-1");
					// 합포장배송비 상품이 아닙니다.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.278"));
					return prdmMain;
				}
				logger.debug("checkPrdmRelativeValue38");
				//SMTCLogger.writePrd("checkPrdmRelativeValue38");
			} else {
				logger.debug(prdmMain.getChrDlvcCd()+ "====> chrDlvcCd");
				if (prdmMain.getChrDlvcCd() != null &&  prdmMain.getChrDlvcCd().compareTo(BigDecimal.ZERO) != 0){
					String supDlvcYn = "";
					
					// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
					// 직송이 아닐경우 당사배송비를 조회 하기 위해 배송비구분코드에 'GS', 협력사코드에 '989081' 를 세팅한다.
					if ("3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){
						for (int i = 0; i < delyAmtInfo.size(); i++) {
							if (prdmMain.getChrDlvcCd().toString().equals(delyAmtInfo.getValues().get(i).getString("dlvcCd"))) {
								supDlvcYn = "Y";
							}
						}
					} else {
						// 협력사에 따른 배송부가 정보 조회
						PrdQryCond prdQryCondTemp = new PrdQryCond();
						prdQryCondTemp.setSupCd(new BigDecimal("989081"));
						prdQryCondTemp.setDlvcCd(prdmMain.getChrDlvcCd()) ;
						EntityDataSet<DSMultiData> delyAmtInfoList = prdEntity.setdelyAmt(prdQryCondTemp);
						if(delyAmtInfoList != null && delyAmtInfoList.size() > 0){
							if (prdmMain.getChrDlvcCd().toString().equals(delyAmtInfoList.getValues().get(0).getString("dlvcCd"))) {
								supDlvcYn = "Y";
							}
						}
					}
					
					
					if (!"Y".equals(supDlvcYn)) {
						prdmMain.setRetCd("-1");
						// 해당업체의 유료배송코드가 아닙니다.
						prdmMain.setRetMsg(Message.getMessage("prd.msg.277"));
						return prdmMain;
					}
				}
			}
			logger.debug("checkPrdmRelativeValue39");
			logger.debug("checkPrdmRelativeValue39----->" + prdmMain.getExchRtpChrYn());
			logger.debug("checkPrdmRelativeValue39----->" + prdmMain.getRtpDlvcCd());
			// 반품배송비코드입력했으면 반품/교환비 유료 설정
			if (prdmMain.getRtpDlvcCd() != null && !"".equals(StringUtils.NVL(prdmMain.getRtpDlvcCd().toString())  )
					&&  prdmMain.getRtpDlvcCd().compareTo(BigDecimal.ZERO) != 0
					&& !"Y".equals(StringUtils.NVL(prdmMain.getExchRtpChrYn()))) {
				prdmMain.setRetCd("-1");
				// 반품교환비용이 있을경우 반품교환여부가 체크되어있어야 합니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.279"));
				return prdmMain;
			}

			// 상품정보의 본사정품여부 설정은 협력사의정보가 본사정품이증 대상인 경우에만 가능
			if ("Y".equals(StringUtils.NVL(prdmMain.getGnuinYn()))) {
				BrandSupGnuin brandSupGnuinQryCond = new BrandSupGnuin();
				brandSupGnuinQryCond.setBrandCd(prdmMain.getBrandCd());
				brandSupGnuinQryCond.setSupCd(prdmMain.getSupCd());

				EntityDataSet<DSData> brandSupGnuinInfo = brandEntity.getBrandSupGnuinInfo(brandSupGnuinQryCond);
				if (brandSupGnuinInfo != null
						&& !"Y".equals(StringUtils.NVL(brandSupGnuinInfo.getValues().getString("gnuinYn")))) {
					prdmMain.setRetCd("-1");
					// 본사정품 비대상 브랜드&업체 입니다. 상품단위의 본사정품여부를 확인하세요.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.276"));
					return prdmMain;
				}
			}
			logger.debug("checkPrdmRelativeValue40");
			// 무형상품유형이 "문자쿠폰(상품교환권)" 인 경우 문자쿠폰선택불가
			if ("R".equals(StringUtils.NVL(prdmMain.getFrmlesPrdTypCd()))) {
				PrdClsQryCond pPrdClsQryCond = new PrdClsQryCond();
				pPrdClsQryCond.setPrdClsCd(prdmMain.getPrdClsCd());

				EntityDataSet<DSMultiData> prdClsList = prdClsBaseEntity.getPrdClsList(pPrdClsQryCond);
				for (int i = 0; i < prdClsList.size(); i++) {
					if (!"Y".equals(prdClsList.getValues().get(i).getString("smsPrdRegPsblYn"))) {
						prdmMain.setRetCd("-1");
						// 선택한 상품분류는 문자쿠폰 선택불가 입니다.
						prdmMain.setRetMsg(Message.getMessage("prd.msg.280"));
						return prdmMain;
					}
				}
				
				//[SR02140912055][2014.10.06][김지혜] : [문자쿠폰 상품 속성(옵션상품) 생성 제한 로직 추가]
				if("Y".equals(prdmMain.getMobilEcApiAttrExcptYn()) && "S".equals(prdmMain.getPrdTypCd()) ) {
					prdmMain.setRetCd("-1");
					// 해당 협력사는 문자쿠폰 상품의 경우 속성(옵션)상품을 생성하실수 없습니다.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.526"));
					return prdmMain;
				}
			}
			logger.debug("checkPrdmRelativeValue41");
			// 완전매입상품(01),조건부매입상품 (04): EC배송일 안내 안함
			if (("01".equals(StringUtils.NVL(prdmMain.getPrchTypCd())) || "04".equals(StringUtils.NVL(prdmMain
					.getPrchTypCd())))
					&& "Y".equals(StringUtils.NVL(prdmMain.getDlvDtGuideCd()))) {
				prdmMain.setRetCd("-1");
				// 조건부매입상품은 EC배송일 안내상품으로 등록할 수 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.274"));
				return prdmMain;
			}
			logger.debug("checkPrdmRelativeValue42");
			// 예약판매상품은 주문제작상품 설정 불가
			if ("Y".equals(StringUtils.NVL(prdmMain.getRsrvSalePrdYn()))
					&& "Y".equals(StringUtils.NVL(prdmMain.getOrdMnfcYn()))) {
				prdmMain.setRetCd("-1");
				// 예약판매상품은 주문제작상품과 동시에 체크 할 수 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.275"));
				return prdmMain;
			}

		}
		//스타일 직접입력 상품은 직송(택배)-업체수거, 직송(설치)-업체수거, 직송(우편)-업체수거 형태만 가능합니다
		if ("Y".equals(StringUtils.NVL(prdmMain.getStyleDirEntYn())) || "E".equals(StringUtils.NVL(prdmMain.getStyleDirEntYn()))){
			if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1)) ||
				"3000".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())))	{
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.289"));
				return prdmMain;
			}
		}

		logger.debug("checkPrdmRelativeValue43");
		/*[패널]해외배송 2014.07.31 윤승욱(sitjjang)
		 * 해외배송무게값 필수입력 */
		if ("Y".equals(StringUtils.NVL(prdmMain.getForgnDlvPsblYn()))
				&& prdmMain.getForgnDlvWeihtVal() == null) {
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.220", new String[] { "해외배송가능상품", "해외배송무게값" })); // {0}은(는) {1}을(를) 반드시 입력해야 합니다.
			return prdmMain;
		}
		
		/*[패널]해외배송 2014.07.31 윤승욱(sitjjang)
		 * 해외배송가능 상품은 배송형태 직송(설치) 선택 불가*/
		if ("Y".equals(StringUtils.NVL(prdmMain.getForgnDlvPsblYn()))
				&& "3100".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd(), "")) ){
			prdmMain.setRetCd("-1");
			prdmMain.setRetMsg(Message.getMessage("prd.msg.525")); // 해외배송가능 상품은 배송형태가 직송(설치) 일수 없습니다.
			return prdmMain;
		}
		
		// 해당 분류의 금지어가 상품명에 포함되었는지 체크한다. 20111018 mckim
		ProhibitWordCond prohibitWordCond = new ProhibitWordCond();

		if (null != prdmMain.getPrdCd()) {
			prohibitWordCond.setPrdCd(prdmMain.getPrdCd());
		}

		if (null == prdmMain.getPrdCd()){
			prohibitWordCond.setMode("1");
		} else {
			prohibitWordCond.setMode("2");
		}

		prohibitWordCond.setPrdClsCd(prdmMain.getPrdClsCd());
		prohibitWordCond.setPrdNm(prdmMain.getPrdNm());

		if (prdNmChg != null && 0 < prdNmChg.size()){
			Object obj = prdNmChg.get(0);

			if(obj instanceof PrdNmChgHinsert) {

				PrdNmChgHinsert prdNmChgobj = (PrdNmChgHinsert) obj;

				if (null != prdNmChgobj.getExposPrdNm()) {
					prohibitWordCond.setExposPrdNm(prdNmChgobj.getExposPrdNm());
				}
				if (null != prdNmChgobj.getExposPmoNm()) {
					prohibitWordCond.setExposPmoNm(prdNmChgobj.getExposPmoNm());
				}
				if (null != prdNmChgobj.getExposPrSntncNm()) {
					prohibitWordCond.setExposPrSntncNm(prdNmChgobj.getExposPrSntncNm());
				}
			} else if(obj instanceof ExposPrdNm){
				ExposPrdNm prdNmChgobj = (ExposPrdNm) obj;

				if (null != prdNmChgobj.getExposPrdNm()) {
					prohibitWordCond.setExposPrdNm(prdNmChgobj.getExposPrdNm());
				}
				if (null != prdNmChgobj.getExposPmoNm()) {
					prohibitWordCond.setExposPmoNm(prdNmChgobj.getExposPmoNm());
				}
				if (null != prdNmChgobj.getExposPrSntncNm()) {
					prohibitWordCond.setExposPrSntncNm(prdNmChgobj.getExposPrSntncNm());
				}
			}
		} else {

			//prohibitWordCond.setMode("3");

			prohibitWordCond.setExposPrdNm("");
			prohibitWordCond.setExposPmoNm("");
			prohibitWordCond.setExposPrSntncNm("");
		}

		Map countMap = null;

		if ( new BigDecimal("1036018").compareTo(prdmMain.getSupCd()) != 0 ) { // 프레쉬몰 협력사는 금지어 체크에서 제외한다.
			
			countMap = prdClsBaseEntity.getProhibitWordPrd(prohibitWordCond);
		}
		
		if (countMap != null) {
			int count = ((BigDecimal)countMap.get("count")).intValue();
			if (count > 0) {
				prdmMain.setRetCd("-1");
				if (null == prdmMain.getPrdCd()) {
					prdmMain.setRetMsg(Message.getMessage("prd.msg.384", new String[] { " 금지어: " + countMap.get("prhbWrdCntnt")}));
				} else {
					prdmMain.setRetMsg(Message.getMessage("prd.msg.384", new String[] { "상품코드: " + prdmMain.getPrdCd() + " 금지어: " + countMap.get("prhbWrdCntnt")}));
				}
				return prdmMain;
			}
		}

		// 지정일 배송 체크 [SR02180727185][2018.10.01][이용문]: 지정일(설치포함) 프로세스 개선을 위한 상품등록 화면 수정
		if( prdPrdD != null ){
			checkPrdPrdDApntDtDlv(prdmMain, prdPrdD);
			if( "-1".equals(prdmMain.getRetCd()) ){
				return prdmMain;
			}
		}
		
		// [새벽배송2차] 새벽배송비 체크 [S]
		if ( "Y".equals(StringUtils.NVL(prdPrdD.getDawnChrDlvYn())) ){
			
			String preDlvPickMthodCd = StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1);
			
			// 지정일배송상품여부 & 지정일배송유형-새벽배송(지정일없음) 인 경우만 새벽배송 세팅 가능
			if ( !("Y".equals(StringUtils.NVL(prdPrdD.getApntDtDlvYn())) || "1".equals(StringUtils.NVL(prdPrdD.getApntDtDlvYn())))
				|| !"X".equals(StringUtils.NVL(prdPrdD.getApntDtDlvTyp())) ){
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.560"));	// 새벽배송비는 지정일배송상품이고 지정일배송유형이 새벽배송(지정일없음)인 경우만 선택할 수 있습니다.
				return prdmMain;
			}
			
			// 1. 상품유형 - 일반상품 (사은품유형코드(PRD027) '00'인 판매상품)만 대상
			if ( !"00".equals(StringUtils.NVL(prdmMain.getGftTypCd())) ){
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.561"));	// 새벽배송비는 상품유형이 일반상품인 경우만 선택할 수 있습니다.
				return prdmMain;
			}
			
			// 2. 배송형태 - 입고택배, 직택배
			if ( !"1".equals(preDlvPickMthodCd) && !"2".equals(preDlvPickMthodCd) ){
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("prd.msg.558"));	// 새벽배송비는 배송형태가 입고택배, 직택배인 경우만 선택할 수 있습니다.
				return prdmMain;
			}
			
			// 3. 배송비 금액 체크
			if ( "N".equals(prdPrdD.getDawnDlvcLimitYn()) ){
				if ( "0".equals(StringUtils.NVL(prdPrdD.getDawnChrDlvcAmt(),"0")) ){
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg(Message.getMessage("prd.msg.559"));	// 새벽배송비는 0원 이상 입력해야 합니다.
					return prdmMain;
				}
			}
		}
		// [새벽배송2차] 새벽배송비 체크 [E]
		
		// [새벽배송2차] 새벽교환/반품배송비 체크 [S]
		if ( "Y".equals(StringUtils.NVL(prdPrdD.getDawnExchRtpChrYn())) ){
			
			// 1. 새벽교환/반품배송비코드 체크
			if ( StringUtils.isEmpty(prdPrdD.getDawnRtpDlvcCd()) || "0".equals(StringUtils.NVL(prdPrdD.getDawnRtpDlvcCd())) ){
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] { "새벽반품(교환)비용코드" }));
				return prdmMain;
			}
			
			// 2. 새벽교환/반품배송비 금액 체크
			PrdQryCond pPrdQryCond = new PrdQryCond();
			pPrdQryCond.setSupCd(new BigDecimal("989081"));							// 당사 협력사코드 989081
			pPrdQryCond.setDlvcCd(new BigDecimal(prdPrdD.getDawnRtpDlvcCd()));		// 새벽교환/반품배송비 코드
			pPrdQryCond.setRtpOnewyRndtrpCd(prdPrdD.getDawnRtpOnewyRndtrpCd()) ;
			pPrdQryCond.setExchOnewyRndtrpCd(prdPrdD.getDawnExchOnewyRndtrpCd());
			EntityDataSet<DSMultiData> delyAmtList = prdEntity.setdelyAmt(pPrdQryCond);

			BigDecimal rtpCost = new BigDecimal("0");
			BigDecimal exchCost = new BigDecimal("0");
			BigDecimal stdAmt = new BigDecimal("1000");
			BigDecimal stdZero = new BigDecimal("0");

			for (int i = 0; i < delyAmtList.size(); i++) {
				
				rtpCost =  delyAmtList.getValues().get(i).getBigDecimal("rtpCost");
				exchCost =  delyAmtList.getValues().get(i).getBigDecimal("exchCost");

				if (rtpCost.compareTo(stdAmt) < 0 && rtpCost.compareTo(stdZero) > 0 ) {
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg(Message.getMessage("prd.msg.562"));	// 새벽반품비용은 1000원 이상 입력해야 합니다.
					return prdmMain;
				}

				if (exchCost.compareTo(stdAmt) < 0 && exchCost.compareTo(stdZero) > 0 ) {
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg(Message.getMessage("prd.msg.563"));	// 새벽교환비용은 1000원 이상 입력해야 합니다.
					return prdmMain;
				}
			}
		}
		
		// [새벽배송2차] 새벽교환/반품배송비 체크 [E]

		
		prdmMain.setRetCd("0");
		prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
		return prdmMain;
	}

	@Override
	public PrdmMain checkPrdchrDlvInsp(PrdmMain prdmMain, List<PrdChanlInfo> prdChanlDinsert, List<PrdChanlInfo> prdChanlDupdate){
		// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
		/*
		 * 입고택배, 직택배도 유료배송 가능하도록 수정 (채널코드 'C', 'U', 'D', 'H' 제외) 
		 *  -> checkPrdmRelativeValue 에서 유료배송비 부분을 checkPrdchrDlvInsp 이동..
		 *     채널코드 도 체크해야 하기 때문에 기존함수 사용이 어려워 분리. 
		 */
		
		String prdChanCdCYn = "N" ;
		for (PrdChanlInfo prdChanlInfo : prdChanlDinsert) {
			//if("C".equals(StringUtils.NVL(prdChanlInfo.getChanlCd())) && "Y".equals(StringUtils.NVL(prdChanlInfo.getSalePsblYn()))){
			if("Y".equals(StringUtils.NVL(prdChanlInfo.getSalePsblYn())) 
					&& ("C".equals(StringUtils.NVL(prdChanlInfo.getChanlCd()))
						|| "U".equals(StringUtils.NVL(prdChanlInfo.getChanlCd()))
						|| "D".equals(StringUtils.NVL(prdChanlInfo.getChanlCd()))
						//|| "H".equals(StringUtils.NVL(prdChanlInfo.getChanlCd())) /* SR02150818070 */ 
						)
						){
				
				prdChanCdCYn = "Y" ;
				break;
			}
		}

		if(!"Y".equals(prdChanCdCYn)){
			for (PrdChanlInfo prdChanlInfo : prdChanlDupdate) {
				//if("C".equals(StringUtils.NVL(prdChanlInfo.getChanlCd())) && "Y".equals(StringUtils.NVL(prdChanlInfo.getSalePsblYn()))){
				if("Y".equals(StringUtils.NVL(prdChanlInfo.getSalePsblYn())) 
						&& ("C".equals(StringUtils.NVL(prdChanlInfo.getChanlCd()))
							|| "U".equals(StringUtils.NVL(prdChanlInfo.getChanlCd()))
							|| "D".equals(StringUtils.NVL(prdChanlInfo.getChanlCd()))
							//|| "H".equals(StringUtils.NVL(prdChanlInfo.getChanlCd()))  /* SR02150818070 */  
								)
							){
					prdChanCdCYn = "Y" ;
					break;
				}
			}
		}

		//GRIT-90644 2023.11.16 이태호 TV상품 유료반품 시행관련 시스템변경 요청. 로직 제거. 
		// (유료교환/반품비는 직송만 가능) 에서 입고택배, 직택배도 유료배송 가능하도록 수정 (채널코드 'C', 'U', 'D', 'H' 제외) 
		/*
		if("Y".equals(prdChanCdCYn) && !"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){
			if("Y".equals(StringUtils.NVL(prdmMain.getChrDlvYn())) || "Y".equals(StringUtils.NVL(prdmMain.getExchRtpChrYn()))){
				prdmMain.setRetCd("-1");
				// 유료배송불가 채널이 포함되어 있습니다.\n(입고택배,직택배)불가채널 [CATV, 위성TV, DM, T커머스]
				prdmMain.setRetMsg(Message.getMessage("prd.msg.522"));
				return prdmMain;
			}
		}
		*/
		
		
		//[SR02161024027]배송 형태별 배송조건 변경 요청 건(조건에 따라 다름)
		//배송비 항목 입력 여부 체크
		if(StringUtils.isEmpty(prdmMain.getChrDlvYn()) || StringUtils.isEmpty(prdmMain.getChrDlvAddYn())){
			prdmMain.setRetCd("-1");
			//{0} 값은 필수 입력값입니다.
			prdmMain.setRetMsg(Message.getMessage("cmm.msg.004", new String[] {"배송비"}));
			return prdmMain;
		}
		
		//직송(설치)의 경우에만 '조건에 따라 다름 추가비용 발생' 항목 선택 가능
		if(!"31".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 2))
			&& "Y".equals(StringUtils.NVL(prdmMain.getChrDlvAddYn()))){
			prdmMain.setRetCd("-1");
			//배송형태가 직송(설치)의 경우에만 '조건에 따라 다름' 항목을 선택할 수 있습니다.
			prdmMain.setRetMsg(Message.getMessage("prd.msg.546"));
			return prdmMain;
		}
		
		return prdmMain;
	}
	
	// 상품 물류확장 정보의 validation check , sap 재구축 (2013/01/18 안승훈)
	@Override
	public  PrdmMain checkPrdDtrdRelativeValue(PrdmMain prdmMain, PrdDtrD prdDtrD) {

		// 입고택배일 경우 유효기관, 양품화 구분을 체크한다.
		/* 화면단의 체크로 대체한다. 해외상품등의 다릉 경로로 들어왔을 경우는 체크하지 않기 때문에
		if (StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1).equals("1")) {
			// 상품분류의 유효기관 관리여부가 'Y' 이면 유효기관 필수 입력
			if (StringUtils.NVL(prdDtrD.getValidTermMngYn()).equals("Y")) {
				if (StringUtils.NVL(prdDtrD.getPrdValidTermCd()).equals("") ) {
					prdmMain.setRetCd("-1");
					// 입고택배이고 상품분류의 유효기간 관리가 설정된 상품은 유효기간을 지정해야 합니다.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.506"));
					return prdmMain;
				}
			}

			// 완전매입 상품은 양품화 구분을 체크한다.
			if (StringUtils.NVL(prdmMain.getPrchTypCd()).equals("01")) {
				if (StringUtils.NVL(prdDtrD.getFprdTypCd()).equals("")) {
					prdmMain.setRetCd("-1");
					// 입고택배이고 완전매입 상품은 양품화구분을 지정해야 합니다.
					prdmMain.setRetMsg(Message.getMessage("prd.msg.508"));
					return prdmMain;
				}
			}
		}
		*/
		
		
		//SR02170413078]  2017-04-14 직송관리대행 상품에대한 요청 데이터 추가 validation 체크  gowinix Start
		if ("3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){ //직송이면
			DlvsCoCdCond dlvsCoCdCond = new DlvsCoCdCond();
			dlvsCoCdCond.setSupCd(String.valueOf(prdmMain.getSupCd()));
			dlvsCoCdCond.setDlvsCoCd(prdmMain.getDlvsCoCd());
			dlvsCoCdCond.setPrdRelspAddrCd(prdmMain.getPrdRelspAddrCd());
			dlvsCoCdCond.setPrdRetpAddrCd(prdmMain.getPrdRetpAddrCd());

			EntityDataSet<DSData> dirMngYn = prdEntity.getSupAddrDirdlvMngAgncyYn(dlvsCoCdCond);
			
			if("Y".equals(prdDtrD.getDirdlvMngAgncyYn()) || "1".equals(prdDtrD.getDirdlvMngAgncyYn())){
				if("1".equals(prdDtrD.getDirdlvMngAgncyYn())) prdDtrD.setDirdlvMngAgncyYn("Y");
				int ckd = Integer.parseInt(dirMngYn.getValues().getString("rtnVal"));
				if(ckd == 0){
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg("직송관리대행 협력사가 아닙니다.");
					return prdmMain;
				}
				
				/* HANGBOT-16987_GSITMBO-9804 지정택배수거 종료
				if("Y".equals(prdmMain.getApntDlvsImplmYn())){
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg("지정택배수거와 직송관리대행을 동시에 사용하실 수 없습니다.");
					return prdmMain;				
				}*/
				
				if(prdmMain.getBundlDlvCd() == null || "".equals(prdmMain.getBundlDlvCd())){
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg("직송관리대행 상품의 묶음배송여부는 필수입니다.");
					return prdmMain;					
				}
				
				if("A01".equals(prdmMain.getBundlDlvCd())){
					if(prdDtrD.getBundlDlvPsblQty() == null || "".equals(prdDtrD.getBundlDlvPsblQty())){
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg("직송관리대행 상품의 묶음배송가능수량은 필수입니다.");
						return prdmMain;						
					}
					
					if(Integer.parseInt(prdDtrD.getBundlDlvPsblQty()) < 2){
						prdmMain.setRetCd("-1");
						prdmMain.setRetMsg("묶음배송가능수량은 2개이상 입력 가능합니다.");
						return prdmMain;						

					}
				}else{
					prdmMain.setBundlDlvPsblQty("");
				}
			}
		}
		//SR02170413078]  2017-04-14 직송관리대행 상품에대한 요청 데이터 추가 validation 체크  gowinix End		
		
		prdmMain.setRetCd("0");
		prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
		return prdmMain;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 상품 추가 정보 유효성 체크 
	 * 1) 지정일 배송 체크.
	 * [SR02180727185][2018.10.01][이용문]: 지정일(설치포함) 프로세스 개선을 위한 상품등록 화면 수정
	 * </pre>
	 *
	 * @author ymlee
	 * @date 2018. 10. 1.
	 * @param prdmMain
	 * @param prdPrdD
	 */
	@Override
	public void checkPrdPrdDApntDtDlv(PrdmMain prdmMain, PrdPrdDinsert prdPrdD) {

		// 지정일 배송 사용이라면 체크.
		if( "Y".equals(StringUtils.NVL(prdPrdD.getApntDtDlvYn())) || "1".equals(StringUtils.NVL(prdPrdD.getApntDtDlvYn())) ){
			
			String apntDtDlvTyp = StringUtils.NVL(prdPrdD.getApntDtDlvTyp()); //지정일자 유형코드
			//지정일배송 미선택시.
			if( StringUtils.isEmpty(apntDtDlvTyp) ){
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg("지정일 배송유형을 입력하셔야 합니다.");
				return;
			}
			
			//일반 상품 인데 지정일 배송유형이 날짜/시간지정이라면 오류.
			if( "P".equals(prdmMain.getPrdTypCd())
					&& ("D".equals(apntDtDlvTyp) || "T".equals(apntDtDlvTyp)) ){
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg("지정일 배송유형이 올바르지 않습니다.(일반속성)");
				return;
			}
			
			String dlvCd = StringUtils.NVL(prdmMain.getDlvPickMthodCd());
			if( dlvCd.length() >= 2 ){
				dlvCd = dlvCd.substring(0, 2);
			}
			//직송(택배) 인 경우 날짜/시간/새벽배송 선택가능.
			if( ( "D".equals(apntDtDlvTyp) || "T".equals(apntDtDlvTyp) || "E".equals(apntDtDlvTyp) ) 
					&& !"32".equals(dlvCd) ){
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg("선택하신 지정일배송유형 값은 배송형태가 직송(택배)일때만 선택 가능합니다.");
				return;
			}

			// 지정일 유형이 "주문 후 안내"인경우 체크
			if( "F".equals(apntDtDlvTyp) ){
				if( !"31".equals(dlvCd) ){
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg("선택하신 지정일배송유형 값은 배송형태가 직송(설치)일때만 선택 가능합니다.");
					return;					
				}

				//주문 후 안내"일 경우 주문 후 설치예정일 안내(해피콜)여부를 진행으로만 저장되는지 확인. 
				//(상품등록시 meta정보를 사용하고 있어서 체크하기 복잡하여 화면에서 체크로직 처리)
//				if( !"Y".equals(prdmMain.getHcYn()) ){
//					prdmMain.setRetCd("-1");
//					prdmMain.setRetMsg("선택하신 지정일배송유형 값은 주문 후 설치예정일 안내 여부는 필수 선택사항입니다.");
//					return;
//				}
			}
			
			// 지정일 유형이 협력사 연동(api)인 경우
			if( "S".equals(apntDtDlvTyp)  ) {
				if( !"31".equals(dlvCd) ){
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg("선택하신 지정일배송유형 값은 배송형태가 직송(설치)일때만 선택 가능합니다.");
					return;
				}
				
				//API 추가배송비 제한 협력사 여부 b2bi -> 협력사 UDA64로 변경
				SupUdaDtl supUdaDtl = new SupUdaDtl();
				supUdaDtl.setUdaNo(new BigDecimal(64));
				supUdaDtl.setSupCd(prdmMain.getSupCd());
				supUdaDtl.setUseYn("Y");
				DSData supUdaDtlInfo = supEntity.getSupUdaDtlInfo(supUdaDtl);
				if( supUdaDtlInfo == null || StringUtils.isEmpty(supUdaDtlInfo.getString("supCd")) ){
					prdmMain.setRetCd("-1");
					prdmMain.setRetMsg("지정일배송유형이 협력사연동(API) 값은 지정협력사인(UDA:64) 경우에만 선택 가능합니다.");
					return;
				}
			}
			
			//지정일자 배송여부 체크 [HANGBOT-30716] 2022.01.27 이용문 : 프레시몰 새벽센터 폐점으로 인한 시스템 수정요청
			CmmCdQryCond pCmmCd = new CmmCdQryCond();
			pCmmCd.setUseYn("Y");
			pCmmCd.setCmmGrpCd("PRD209");
			pCmmCd.setCmmCd(StringUtil.nvl(prdPrdD.getApntDtDlvTyp()));
			EntityDataSet<DSData> rCmmCd = cmmCdEntity.getCmmCd(pCmmCd);
			if (rCmmCd == null || rCmmCd.size() == 0) {
				prdmMain.setRetCd("-1");
				prdmMain.setRetMsg("지정일자배송유형 항목이 올바르지 않습니다.["+prdPrdD.getApntDtDlvTyp()+"] ");
				return;
			}
		}
		
		prdmMain.setRetCd("0");
		prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
	}
	
	
	/* [SR02170913526][2017.09.14][최미영]:전통주기본프로세스 
	 *  체크로직 추가
	 */
	@Override
	public PrdmMain checkTraditionalLiquorPrd(PrdmMain prdmMain) {
	    /*
	    logger.debug("여기 들어옴 :  **********************checkPrdTraditionalDrink **********************");	    
	    logger.debug("prdmMain.getRegChanlGrpCd() GE: "+ prdmMain.getRegChanlGrpCd());
	    logger.debug("prdmMain.getGftTypCd() 00: "+ prdmMain.getGftTypCd());
	    logger.debug("prdmMain.getAutoOrdPsblYn() : "+ prdmMain.getAutoOrdPsblYn());
	    logger.debug("prdmMain.getPrchTypCd() 03: "+ prdmMain.getPrchTypCd());
	    logger.debug("prdmMain.getTaxTypCd() 02: "+ prdmMain.getTaxTypCd());
	    logger.debug("prdmMain.getDlvPickMthodCd() 3200: "+ prdmMain.getDlvPickMthodCd());
	    logger.debug("prdmMain.getCpnApplyTypCd() 09: "+ prdmMain.getCpnApplyTypCd());
	    logger.debug("prdmMain.getSessionUserId() : "+ prdmMain.getSessionUserId());
	    logger.debug("prdmMain.getForgnDlvPsblYn() : "+ prdmMain.getForgnDlvPsblYn());
	    logger.debug("prdmMain.getCvsDlvsRtpYn() : "+ prdmMain.getCvsDlvsRtpYn());	    
	    logger.debug("prdmMain.getImmAccmDcLimitYn() : "+ prdmMain.getImmAccmDcLimitYn());
	    logger.debug("prdmMain.getAliaSpclsalLimitYn() : "+ prdmMain.getAliaSpclsalLimitYn());
	    */  
		
		/* 딜더미상품인 경우 체크안하게  */ 
		if("88".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))){
		    prdmMain.setRetCd("0");
		    prdmMain.setRetMsg("딜더미상품인 체크로직 통과");
		    return prdmMain;
		}
	    
	    if(!"GE".equals(prdmMain.getRegChanlGrpCd())){
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 판매채널이 인터넷/모바일만 가능합니다.");
	        return prdmMain;
	    }     
	    if(!"00".equals(prdmMain.getGftTypCd())){
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 별도구성/사은품, 경품으로 등록할 수 없습니다.");
	        return prdmMain;
	    }     
	    if("Y".equals(prdmMain.getAutoOrdPsblYn())){
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 ARS자동주문이 불가합니다.");
	        return prdmMain;
	    }   
	    if(!"03".equals(prdmMain.getPrchTypCd())){
	        if(prdmMain.getPrchTypCd() != null){ /* 위드넷 수정시 널로 들어옴(무시)*/
	            prdmMain.setRetCd("-1");
	            prdmMain.setRetMsg("전통주 분류는 거래형태 수수료매입만 가능합니다.");
	            return prdmMain;
	        }
	    }     
	    if(!"02".equals(prdmMain.getTaxTypCd())){
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 부가세구분 과세만 가능합니다.");
	        return prdmMain;
	    }     
	    if(!"3200".equals(prdmMain.getDlvPickMthodCd())){
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 배송형태 직송(택배)-업체수거만 가능합니다.");
	        return prdmMain;
	    }   
	    // API 추가 [HANGBOT-8465_GSITMBO-4059] 2020.12.07 이용문 : GS리테일_와인25플러스 상품/주문 API 연동 진행건
	    if(!"09".equals(prdmMain.getCpnApplyTypCd()) 
	    		&& !("WITH".equals(prdmMain.getSessionUserId()) || "VENDR".equals(prdmMain.getSessionUserId())) ){ // API 추가 [HANGBOT-8465_GSITMBO-4059]
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 쿠폰적용여부 쿠폰 미적용만 가능합니다.");
	        return prdmMain;
	    }        
	    if("Y".equals(prdmMain.getCvsDlvsRtpYn())){
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 편의점배송/반품이 불가합니다.");
	        return prdmMain;
	    }
	    if(!"Y".equals(prdmMain.getImmAccmDcLimitYn()) 
	    		&& !("WITH".equals(prdmMain.getSessionUserId()) || "VENDR".equals(prdmMain.getSessionUserId())) ){ // API 추가 [HANGBOT-8465_GSITMBO-4059]
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 적립금 즉시할인제한만 가능합니다.");
	        return prdmMain;
	    }
	    if(!"Y".equals(prdmMain.getAliaSpclsalLimitYn()) 
	    		&& !("WITH".equals(prdmMain.getSessionUserId()) || "VENDR".equals(prdmMain.getSessionUserId())) ){ // API 추가 [HANGBOT-8465_GSITMBO-4059]
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 제휴특판제한만 가능합니다.");
	        return prdmMain;
	    }	    
	    if(!"Y".equals(prdmMain.getGsnpntNoGivYn()) 
	    		&& !("WITH".equals(prdmMain.getSessionUserId()) || "VENDR".equals(prdmMain.getSessionUserId())) ){ // API 추가 [HANGBOT-8465_GSITMBO-4059]
	        prdmMain.setRetCd("-1");
	        prdmMain.setRetMsg("전통주 분류는 GS&포인트적립제한만 가능합니다.");
	        return prdmMain;
	    }
	    ////////////////////////////////////////////////////////////////
	    
	    prdmMain.setRetCd("0");
	    prdmMain.setRetMsg("체크로직 통과");
	    return prdmMain;
	}

	// EAI_Trans_Check ( FieldName );//인터페이스 여부 확인;
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : EAI_Trans_Check
	 */
//	public int EAI_Trans_Check(String FieldName) {
//		return 0;
//	}
	// SOURCE_CLEANSING : END
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : EC_Prod
	 */
//	public void EC_Prod() {
//
//	}
	// SOURCE_CLEANSING : END

	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : EC_Prod
	 */
//	public PrdmMain EC_Prod(PrdmMain prdmMain, SupDtlInfo supDtlInfo) {
//		if (!prdmMain.getRegChanlGrpCd().equals("E")) // session.chanlCd = 'P' or 'GSEC'
//		{
//			String sReserve = prdmMain.getRsrvSalePrdYn(); // 예약판매상품여부
//			String sOrder = prdmMain.getOrdMnfcYn(); // 주문제작여부
//			String sECDelivery = ""; // ec채널 표준출고일수
//
//			if (sReserve.equals("Y")) {
//				// this.SetFieldValue("EC Delivery ATP Day", ""); //채널이 EC인 경우 표준출고일수
//				BigDecimal a = new BigDecimal("0");
//				prdmMain.setStdRelsDdcnt(a);
//
//				if (sOrder.equals("Y")) {
//					// TheApplication().RaiseErrorText("예약판매상품은 주문제작상품과 동시에 체크 할 수 없습니다.");
//					prdmMain.setRetCd("-1");
//					prdmMain.setRetMsg(Message.getMessage("prd.msg.362"));
//					return prdmMain;
//				}
//			}
//
//			if (sOrder.equals("Y")) {
//				if (sECDelivery.equals("")) {
//					// TheApplication().RaiseErrorText("주문제작상품은 EC표준 출고일이 들어가야 합니다.");
//					prdmMain.setRetCd("-1");
//					prdmMain.setRetMsg(Message.getMessage("prd.msg.363"));
//					return prdmMain;
//				}
//
//				if (sReserve.equals("Y")) {
//					// TheApplication().RaiseErrorText("주문제작상품은 예약판매상품과 동시에 체크 할 수 없습니다.");
//					prdmMain.setRetCd("-1");
//					prdmMain.setRetMsg(Message.getMessage("prd.msg.364"));
//					return prdmMain;
//				}
//			}
//		}
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg(Message.getMessage("prd.msg.324"));
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : FS_Undo_Shortage
	 */
//	public PrdmMain FS_Undo_Shortage(PrdmMain prdmMain, SupDtlInfo supDtlInfo)
//	// function FS_Undo_Shortage() //
//	{
//		// 결품해제
//
//		/*
//		 * var sPartNum = this.GetFieldValue("Part #"); //상품코드 var sEndDate = "2999-12-31 23:59:59" ; var CurrTimeBuff;
//		 * Clib.strftime(CurrTimeBuff,"%Y/%m/%d %X" ,Clib.localtime(Clib.time())); //sysdate
//		 *
//		 * var sLoginName = TheApplication().LoginName(); //userid
//		 *
//		 * var oECShortageBC; var oBO;
//		 */
////		BigDecimal sPartNum = prdmMain.getPrdCd();
//		prdmMain.setSaleEndRsnCd("");
//		prdmMain.setEcAprvStCd("");
//		Date date = SysUtil.getCurrTime();
//		String sysdate = DateUtils.format(date, "yyyyMMddHHmmss");
//		prdmMain.setSaleEndDtm(sysdate);
//		// SetFormattedFieldValue("End Date", sEndDate ); //판매종료일시
//		// WriteRecord(); //저장
//
//		/*
//		 * 품절주문 테이블 지정 (ORD_SHTITM_D) oBO = TheApplication().GetBusObject("Admin Product Definition"); oECShortageBC =
//		 * oBO.GetBusComp("LGHS EC Shortage Order List")
//		 */// 요기까지
//		/*
//		 * var iCount = 0; with (oECShortageBC) { ClearToQuery(); SetSearchSpec("Part Num", sPartNum) ; //상품코드, 상태코드 = 10 인것으로 조회
//		 * SetSearchSpec("Status", "10") ; ExecuteQuery(); var isRecord = oECShortageBC.FirstRecord(); while(isRecord) //조회건이 있으면 {
//		 * SetFieldValue("Status","20"); // 상태코드 = 20 SetFormattedFieldValue("Undo Date",CurrTimeBuff); // 결품해제일자 = sysdate
//		 * SetFieldValue("Undo User",sLoginName); // 결품해자자ID = session.userId WriteRecord(); ++iCount ; isRecord = NextRecord(); } }
//		 *
//		 * var sMessage = "EC,DM 결품해제 처리가 완료되었고 \n\n[미처리 주문 : " + ToString(iCount) + " 건] 이 원복되었습니다." ; TheApplication().RaiseErrorText(sMessage);
//		 *
//		 * return(CancelOperation); }
//		 */
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg("0");
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
	
	
	/**
	 * <pre>
	 *
	 * desc : 속성상품 조회 팝업
	 *
	 * </pre>
	 *
	 * @author
	 * @date 2010-12-13 10:24:37
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getAttrPrdQryPopupList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		String key = "inGetAttrPrdQryPopupList";
		PrdQryCond pPrdQryCond = dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);

		// 속성상품목록팝업조회
		returnMap.put("outGetAttrPrdQryPopupList", prdEntity.getAttrPrdQryPopupList(pPrdQryCond));

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 멀티코드/인입코드를 조회조건으로, 주문상품내역을 조회한다.
	 *     1. 멀티코드를 조건으로 상품코드 및 채널을 조회한다.
	 *     2. 조회된 채널에 대하여, 아래 로직에 따라, 인입코드에 따른 채널을 재등록한다.
	 *     # 채널강제변경
	 *     IF (인입코드 IN ( "05", "06" )) {
	 *         IF (채널코드 = "C")  {
	 *             채널코드 = "U" 채널상세코드 = "UA"
	 *         }
	 *     }ELSE IF (인입코드 = "13" ) {
	 *         IF ( 채널코드 = "C" OR 채널코드 = "P" ) {
	 *             채널코드 = "H" 채널상세코드 = "HB"
	 *         }
	 *     }ELSE IF ( 인입코드 IN ("14","15","16")) {
	 *         IF (채널코드 = "C" OR 채널코드 = "P")  {
	 *             채널코드 = "B"  IF (인입코드 = "14")  {
	 *             채널상세코드 = "bA"
	 *             } ELSE IF (인입코드 ="15")  {
	 *                 채널상세코드 = "BA"
	 *             } ELSE  {
	 *                 채널상세코드 = "BB"
	 *             }
	 *         }
	 *     }ELSE IF (인입코드="18" ) {
	 *         채널코드 = "C" 채널상세코드 = "CC"
	 *     }ELSE IF (인입코드="19" ) {
	 *         채널코드 = "C" 채널상세코드 = "CD"
	 *     }ELSE IF (인입코드="20" ){
	 *         채널코드 = "C" 채널상세코드 = "CE"
	 *     }
	 *
	 *     if ( 채널코드 != "" ){
	 *         채널코드 = 채널코드 채널상세코드 = 채널상세코드
	 *     }
	 *
	 *     3. 정해진 채널의 판매가능여부를 조회한다,
	 *     IF(판매가능여부 = 'N') THEN
	 *     판매가능채널을 조회한다.
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-12-07 01:16:07
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public EntityDataSet<DSData> getChanlCd(PrdQryCond pPrdQryCond) throws DevEntException {
		PrdChanlQryCond pPrdChanlQryCond = new PrdChanlQryCond();

		// 멀티코드채널조회
		EntityDataSet<DSData> multiCdChanl = multiCdEntity.getMultiCdChanl(pPrdQryCond);
		pPrdChanlQryCond.setPrdCd(multiCdChanl.getValues().getBigDecimal("prdCd"));
		String aniCd = pPrdQryCond.getAniCd();
		String chanlCd = multiCdChanl.getValues().getString("chanlCd");
		String chanlDtlCd = multiCdChanl.getValues().getString("chanlDtlCd");

		//logger.info("#####MC제휴로그 chanlCd :" + chanlCd);
		//logger.info("#####MC제휴로그 chanlDtlCd :" + chanlDtlCd);
		
		if ( "D".equals(chanlCd) ) {
			pPrdChanlQryCond.setChanlCd(chanlCd);
			pPrdChanlQryCond.setChanlDtlCd(chanlDtlCd);

		} else {
			if ("05,06".indexOf(aniCd) > -1) {
				if (chanlCd.equals("C")) {
					pPrdChanlQryCond.setChanlDtlCd("UA");
					pPrdChanlQryCond.setChanlCd("U");
				}
			} else if (aniCd.equals("13")) {
				if ("C,P".indexOf(chanlCd) > -1) {
					pPrdChanlQryCond.setChanlDtlCd("HB");
					pPrdChanlQryCond.setChanlCd("H");
				}
			/** SR02140924125 2014-10-20 이명기 watch on, 삼성 스마트 TV, 하루 하나 자동주문,상담원 call 프로세스 추가건 **/
			/** 공통코드 ORD574로 처리[S] **
			} else if ("14,15,16,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36".indexOf(aniCd) > -1) {
			**/
			} else if ("14,15,16".indexOf(aniCd) > -1) {
				if ("C,P".indexOf(chanlCd) > -1) {
					pPrdChanlQryCond.setChanlCd("B");
					if (aniCd.equals("14")) {
						pPrdChanlQryCond.setChanlDtlCd("bA");
					} else if (aniCd.equals("15")) {
						pPrdChanlQryCond.setChanlDtlCd("BA");
						/** 2013-07-17 김태엽 수정 - SR02130717045 - MC채널에 대해서 M커머스_SK플레닛_Tshopping 지능망번호에 대한 상세채널 세팅  */
						/** 2013-10-28 김태엽 수정 - SR02130814003 - MC채널 코드 신규생성 작업 요청 */
						/** 2014-01-13 김태엽 수정 - SR02131112125 - 신규 상담/자동주문 11월 오픈에 따른 매체작업요청 */
						/** 2014-04-10 이명기 수정 - SR02140402020 - MC 주문 채널을 위한 전화번호 생성에 따른 개발 요청 */
						/** 2014-08-14 김태엽 수정 - SR02140806108 - 올레TV모바일 주문번호 연결 작업 */
						/** SR02140924125 2014-10-20 이명기 watch on, 삼성 스마트 TV, 하루 하나 자동주문,상담원 call 프로세스 추가건 **/
					} else {
						pPrdChanlQryCond.setChanlDtlCd("BB");
					}
				}
			} else if (aniCd.equals("18")) {
				pPrdChanlQryCond.setChanlDtlCd("CC");
				pPrdChanlQryCond.setChanlCd("C");
			} else if (aniCd.equals("19")) {
				pPrdChanlQryCond.setChanlDtlCd("CD");
				pPrdChanlQryCond.setChanlCd("C");
			} else if (aniCd.equals("20")) {
				pPrdChanlQryCond.setChanlDtlCd("CE");
				pPrdChanlQryCond.setChanlCd("C");

				
			//} else if ("51,52,56,57,58,59,60,61,62,63,68,69,103,104,105,106".indexOf(aniCd) > -1) {
				//20161219 dhyoo(유동훈) 	SR02161212038   [데이터홈쇼핑] CJHV 플랫폼 확장으로 인한 ARS, 상담원 전화번호 연결 요청의 건
				/** [2022-04-18][강민서 수정] - [HANGBOT-34126_GSITMBO-19449] - 걸려오는 ARS 카드 즉시 할인 이슈 대응 */
			} else if (aniCd.equals("51") || aniCd.equals("52") || aniCd.equals("56") || aniCd.equals("57")
					|| aniCd.equals("58") || aniCd.equals("59") || aniCd.equals("60") || aniCd.equals("61")
					|| aniCd.equals("62") || aniCd.equals("63")|| aniCd.equals("68") || aniCd.equals("69")
					|| aniCd.equals("103") || aniCd.equals("104")|| aniCd.equals("105") || aniCd.equals("106")
					|| aniCd.equals("107") || aniCd.equals("108")
					|| aniCd.equals("139") || aniCd.equals("140") || aniCd.equals("141") || aniCd.equals("142") || aniCd.equals("134")
					|| aniCd.equals("135") || aniCd.equals("136") || aniCd.equals("137") || aniCd.equals("138")
			) {
				pPrdChanlQryCond.setChanlCd("H");
			
				if (aniCd.equals("51")) {
					pPrdChanlQryCond.setChanlDtlCd("LGU"); // KT_데이터홈쇼핑_상담원
				} else if (aniCd.equals("52")) {
					pPrdChanlQryCond.setChanlDtlCd("pL"); // KT_데이터홈쇼핑_ARS
				} else if (aniCd.equals("56")) {
					pPrdChanlQryCond.setChanlDtlCd("LIA"); // CM_데이터홈쇼핑_상담원
				} else if (aniCd.equals("57")) {
					pPrdChanlQryCond.setChanlDtlCd("LIN"); // CM_데이터홈쇼핑_ARS
				} else if (aniCd.equals("58")) {
					pPrdChanlQryCond.setChanlDtlCd("LIB"); // LGU_데이터홈쇼핑_상담원
				} else if (aniCd.equals("59")) {
					pPrdChanlQryCond.setChanlDtlCd("LID"); // LGU_데이터홈쇼핑_ARS
				} else if (aniCd.equals("60")) {
					pPrdChanlQryCond.setChanlDtlCd("LIG"); // SKB_데이터홈쇼핑_상담원
				} else if (aniCd.equals("61")) {
					pPrdChanlQryCond.setChanlDtlCd("LIE"); // SKB_데이터홈쇼핑_ARS
				} else if (aniCd.equals("62")) {
					pPrdChanlQryCond.setChanlDtlCd("LHz"); // SKY_데이터홈쇼핑_상담원
				} else if (aniCd.equals("63")) {
					pPrdChanlQryCond.setChanlDtlCd("LHw"); // SKY_데이터홈쇼핑_ARS
				} else if (aniCd.equals("68")) {//SR02151012098 데이터 홈쇼핑 스트리밍 ARS/상담원 번호 매체 변경 요청건 2015-10-19 유동훈K
					pPrdChanlQryCond.setChanlDtlCd("LGj"); // 데이터 홈쇼핑_스트리밍_상담원 
				} else if (aniCd.equals("69")) {//SR02151012098 데이터 홈쇼핑 스트리밍 ARS/상담원 번호 매체 변경 요청건 2015-10-19 유동훈K
					pPrdChanlQryCond.setChanlDtlCd("LGi"); // 데이터 홈쇼핑_스트리밍_ARS
				
				/** 2016-10-18 김태엽 추가 - SR02161007008 - [데이터홈쇼핑] HCN, Tbroad 플랫폼 확장으로 인한 ARS, 상담원 전화번호 연결 요청의 건 */
				} else if (aniCd.equals("103")) {
					pPrdChanlQryCond.setChanlDtlCd("LJt"); // HCN_데이터홈쇼핑_ARS
				} else if (aniCd.equals("104")) {
					pPrdChanlQryCond.setChanlDtlCd("Lju"); // HCN_데이터홈쇼핑_상담원					
				} else if (aniCd.equals("105")) {
					pPrdChanlQryCond.setChanlDtlCd("LJx"); // Tbroad_데이터홈쇼핑_ARS
				} else if (aniCd.equals("106")) {
					//[SR02161123009] GS My Shop Tbroad 소매체 코드 변경 요청의 건
					pPrdChanlQryCond.setChanlDtlCd("LJy"); // Tbroad_데이터홈쇼핑_상담원
					//20161219 dhyoo(유동훈) 	SR02161212038   [데이터홈쇼핑] CJHV 플랫폼 확장으로 인한 ARS, 상담원 전화번호 연결 요청의 건	
				} else if (aniCd.equals("107")) {
					pPrdChanlQryCond.setChanlDtlCd("LKN"); // CJHV_데이터홈쇼핑_ARS
				} else if (aniCd.equals("108")) {
					// CJHV_데이터홈쇼핑_상담원
					pPrdChanlQryCond.setChanlDtlCd("LKO"); //  CJHV_데이터홈쇼핑_상담원

				/** [2021-09-30][김다혜 추가] - [HANGBOT-21957_GSITMBO-12147] [데이터홈쇼핑] 걸려오는 ARS 채널 상세코드 추가 적용의 건 */
				/** [2022-04-18][강민서 수정] - [HANGBOT-34126_GSITMBO-19449] - 걸려오는 ARS 카드 즉시 할인 이슈 대응 */
				} else if (aniCd.equals("139")) {
					pPrdChanlQryCond.setChanlDtlCd("LjV"); // KT_데이터홈쇼핑_ARSCall
				} else if (aniCd.equals("140")) {
					pPrdChanlQryCond.setChanlDtlCd("LjW"); // SKY_데이터홈쇼핑_ARSCall
				} else if (aniCd.equals("141")) {
					pPrdChanlQryCond.setChanlDtlCd("Ljk"); // LGU_데이터홈쇼핑_ARSCall
				} else if (aniCd.equals("142")) {
					pPrdChanlQryCond.setChanlDtlCd("LjX"); // DLive_데이터홈쇼핑_ARSCall
				} else if (aniCd.equals("134")) {
					pPrdChanlQryCond.setChanlDtlCd("Ljb"); // SKB_데이터홈쇼핑_ARSCall
				} else if (aniCd.equals("135")) {
					pPrdChanlQryCond.setChanlDtlCd("Ljc"); // HCN_데이터홈쇼핑_ARSCall
				} else if (aniCd.equals("136")) {
					pPrdChanlQryCond.setChanlDtlCd("Lje"); // Tbroad_데이터홈쇼핑_ARSCall
				} else if (aniCd.equals("137")) {
					pPrdChanlQryCond.setChanlDtlCd("Ljg"); // LGHV_데이터홈쇼핑_ARSCall
				} else if (aniCd.equals("138")) {
					pPrdChanlQryCond.setChanlDtlCd("Lji"); // KCTV_데이터홈쇼핑_ARSCall
				}
			} else {
				/** SR02140924125 2014-10-20 이명기 watch on, 삼성 스마트 TV, 하루 하나 자동주문,상담원 call 프로세스 추가건 **/
				/** 공통코드 ORD574로 처리[S] **/
				
				boolean codeSetYn = false;
				if ("C,P".indexOf(chanlCd) > -1) {
					if(!"".equals(StringUtils.NVL(aniCd,"").trim())) {
						CmmCdQryCond cmmCdQryCond = new CmmCdQryCond();
						cmmCdQryCond.setCmmGrpCd("ORD574");  //MC제휴서비스 ORD574
						cmmCdQryCond.setCmmCd(aniCd); //cmmCd
						cmmCdQryCond.setUseYn("Y"); //사용여부 Y
						
						//logger.info("#####MC제휴로그 인입 aniCd :" + aniCd);						
						EntityDataSet<DSMultiData> cmmCdList = cmmCdEntity.getCdList(cmmCdQryCond);						
						//logger.info("#####MC제휴로그 인입 cmmCdList.getValues().size() :" + cmmCdList.getValues().size());
						
						if (cmmCdList.getValues().size() > 0) {
							pPrdChanlQryCond.setChanlCd("B");
							pPrdChanlQryCond.setChanlDtlCd(StringUtils.NVL(cmmCdList.getValues().get(0).getString("cdVal"),"").trim());						
							//logger.info("#####MC제휴로그 인입 pPrdChanlQryCond1 :" + pPrdChanlQryCond.getChanlCd());
							//logger.info("#####MC제휴로그 인입 pPrdChanlQryCond2 :" + pPrdChanlQryCond.getChanlDtlCd());							
						} else {
							pPrdChanlQryCond.setChanlCd(chanlCd);
							pPrdChanlQryCond.setChanlDtlCd(chanlDtlCd);
						}
						codeSetYn = true;
					}									
				}
				
				if (!codeSetYn) 	{
					pPrdChanlQryCond.setChanlCd(chanlCd);
					pPrdChanlQryCond.setChanlDtlCd(chanlDtlCd);
				}
				
				/** 2015-06-09 김태엽 추가 - SR02150518062 - 데이타 홈쇼핑 주문 및 편성화면 구성 요청 */
				/**
				boolean codeSetYn2 = false;
				if ("C,P,H".indexOf(chanlCd) > -1) {
					if(!"".equals(StringUtils.NVL(aniCd,"").trim())) {
						CmmCdQryCond cmmCdQryCond2 = new CmmCdQryCond();
						cmmCdQryCond2.setCmmGrpCd("ORD596");  // GSMYSHOP서비스 ORD596
						cmmCdQryCond2.setUseYn("Y");
						cmmCdQryCond2.setCdRefVal(aniCd); //CD_REF_VAL : aniCd, CD_SUBTYP_VAL : mediaCd, CD_VAL : chanlDtlCd
						EntityDataSet<DSMultiData> cmmCdList2 = cmmCdEntity.getCdList(cmmCdQryCond2);				
						
						if ( cmmCdList2 != null && cmmCdList2.getValues() != null && cmmCdList2.getValues().size() > 0 ) {
							pPrdChanlQryCond.setChanlCd("H");
							pPrdChanlQryCond.setChanlDtlCd(StringUtils.NVL(cmmCdList2.getValues().get(0).getString("cdVal"),"").trim());		
						}	else {
							pPrdChanlQryCond.setChanlCd(chanlCd);
							pPrdChanlQryCond.setChanlDtlCd(chanlDtlCd);
						}
						codeSetYn2 = true;
					}
				}
				
				if (!codeSetYn2) {
					pPrdChanlQryCond.setChanlCd(chanlCd);
					pPrdChanlQryCond.setChanlDtlCd(chanlDtlCd);
				}
				*/
			}
		}


		/*
		 * if(pPrdQryCond.getChanlCd() != null) { chanlDtlCd = "UA"; chanlCd = pPrdQryCond.getChanlCd(); }
		 */

		// 채널판매가능여부조회
		EntityDataSet<DSData> chanlSalePsblYn = prdChanlEntity.getChanlSalePsblYn(pPrdChanlQryCond);
		EntityDataSet<DSData> SalePsblChanl = new EntityDataSet<DSData>();
		DSData psblChanl = new DSData();
		SalePsblChanl.setValues(new DSData());
		SalePsblChanl.setFieldInfoSet(new FieldInfoSet());
		SalePsblChanl.getFieldInfoSet().add("prdCd", String.class.getName(), 255);
		SalePsblChanl.getFieldInfoSet().add("chanlCd", String.class.getName(), 255);
		SalePsblChanl.getFieldInfoSet().add("chanlDtlCd", String.class.getName(), 255);
		SalePsblChanl.getFieldInfoSet().add("salePsblYn", String.class.getName(), 255);

		if (chanlSalePsblYn.getValues() != null) {
			if (chanlSalePsblYn.getValues().getString("salePsblYn").equals("N")) {
				// 판매가능채널조회
				SalePsblChanl = prdChanlEntity.getSalePsblChanl(pPrdChanlQryCond);
				if (SalePsblChanl.getValues() != null) {
					psblChanl.put("prdCd", pPrdChanlQryCond.getPrdCd());
					psblChanl.put("chanlCd", pPrdChanlQryCond.getChanlCd());
					psblChanl.put("chanlDtlCd", pPrdChanlQryCond.getChanlDtlCd());
					psblChanl.put("salePsblYn", "C");

					SalePsblChanl.setValues(psblChanl);
				} else {
					logger.debug("3-1-2 getCodeChanlSelYn=");
					// pPrdQryCond.setChanlCd(pPrdChanlQryCond.getChanlCd());
				}
			} else {
				psblChanl.put("prdCd", pPrdChanlQryCond.getPrdCd());
				psblChanl.put("chanlCd", pPrdChanlQryCond.getChanlCd());
				psblChanl.put("chanlDtlCd", pPrdChanlQryCond.getChanlDtlCd());
				psblChanl.put("salePsblYn", "Y");

				SalePsblChanl.setValues(psblChanl);
			}
		} else {
			psblChanl.put("prdCd", pPrdChanlQryCond.getPrdCd());
			psblChanl.put("chanlCd", pPrdChanlQryCond.getChanlCd());
			psblChanl.put("chanlDtlCd", pPrdChanlQryCond.getChanlDtlCd());
			psblChanl.put("salePsblYn", "N");

			SalePsblChanl.setValues(psblChanl);
		}

		return SalePsblChanl;
	}

	/**
	 *
	 * <pre>
	 *
	 *
	 *
	 * desc : 상품코드별 공헌이익을 조회한다
	 *  조회조건의 널체크가 null이면 alert(PRD052)메시지를 보여주고 이벤트를 종료한다.
	 *  조회조건의 값을 리턴할 공헌이익Msg에 지정한다.
	 *  if (상품등록여부가'Y'이면) then{
	 *  	상품코드가 없으면 이벤트를종료한다.
	 *  	(공헌이익조회를 위한 상품,상품가격정보를 조회한다.
	 *  	 > 조회실패시 alert(PRD053)메시지와 공헌이익MSG를 리턴하고 이벤트를 종료한다.
	 *  	   공헌이익조회조건.상품코드와 공헌이익조회조건.상품명을 공헌이익Msg에 지정한다.
	 *  	)
	 *  	(공헌이익조회를 위한 프로모션정보를 조회한다.
	 *  	 > 공헌이익무이자조회 실패시
	 *   	   공헌이익조회조건.무이자개월수가 널이면, 공헌이익Msg에 지정하고 아니면 공헌이익.무이자개월수를 0으로 지정한다.
	 *   	 > 공헌이익MD할인조회 실패시
	 *         공헌이익조회조건.율액코드가 널이면, 공헌이익Msg에 지정하고 아니면 공헌이익.율액코드를 A로 지정한다.
	 *   	   공헌이익조회조건.당사적용액이 널이면, 공헌이익Msg에 지정하고 아니면 공헌이익.적용액을 0으로 지정한다.
	 *  	 > 공헌이익사은품조회 실패시
	 *   	   공헌이익사은품목록을 null로 지정한다.
	 *  	 > 공헌이익사은품조회수행 후,해당 상품코드의 사은품이 여러개일 때
	 *  	   공헌이익사은품목록의 개별 사은품에 대한 원가를 loop돌면서 가지고 와서, 모두 합한 값을 공헌이익Msg.사은품원가에 넣는다.
	 *  }
	 *  else{ //상품등록여부가 Y가 아닌경우
	 *  	공헌이익조회조건.무이자할부개월수를 공헌이익Msg에 지정한다.
	 *  	공헌이익.율액코드를 A로 지정한다.
	 *  	공헌이익.적용액을 0으로 지정한다.
	 *  	공헌이익.임직원할인제한여부를 N으로 지정한다.
	 *  	공헌이익.그룹사할인제한여부를 N으로 지정한다.
	 *  	공헌이익.기본적립금제한여부를 N으로 지정한다.
	 *  	공헌이익.선택적립금적용여부를 N으로 지정한다.
	 *  	공헌이익.카드사용제한여부를 N으로 지정한다.
	 *  }
	 *  공헌이익.무이자할부개월수를 조회결과로 지정한다.
	 *  쿠폰적용유형코드를 조회결과로 지정한다.
	 *  공헌이익공통조회를 수행하여 공헌이익Msg에 지정한다. -> 실패시 alert(PRD054)메시지를 보여주고, 현재까지 정해진 공헌이익Msg를 리턴하고 이벤트를 종료한다.
	 *  /* [공헌이익] (이 주석을 소스에 포함한다.)
	 *  1.결제예상가(고객) 1_1.상품판매가-
	 *  1_2.업체지급액 -
	 *  1_3.가격할인 당사부담-
	 *  1_4.임직원할인
	 *  1_4_1.임직원(내부)공통할인율 임직원제한여부,
	 *  1_4_2.그룹사(외부)공통할인율 상품그룹사제한여부-
	 *  1_5.쿠폰할인 상품쿠폰제한타입
	 *  1-5-1.전용쿠폰 전용쿠폰정보의 최대쿠폰할인액 (Only 다운쿠폰),
	 *  1-5-2.Thanks Thanks쿠폰할인율 * Thanks쿠폰적용률,
	 *  1-5-3.제한쿠폰 제한쿠폰할인율 * 제한쿠폰적용률
	 *  - 3.기타제비용-
	 *  3_1.공통적립금 기본적립금(3%) * 공통다운비율 + 공통적립금부여율-
	 *  3_2.무이자할부수수료 무이자개월수별 공통적용비율(상품무이자프로모션)-
	 *  3_3.카드가맹점수수료 소분류적용비율-
	 *  3_4.포장비 소분류적용비율-
	 *  3_5.판매촉진비 공통적용비율-
	 *  3_6.배송비 공통적용비율(직송 제외)-
	 *  3_7.제휴수수료 소분류적용비율-
	 *  3_8.고객센터비 소분류적용비율-
	 *  3_9.기타변동비 소분류적용비율
	 * @ 소분류쿠폰적용비율은 취급액 대비 비율(SAP-최근월)@ 소분류별 비율 적용 항목의 경우, 신규 분류에 대해서는 상위 중분류 &gt; 대분류 비율 순으로 적용@ 기타변동비 = 고객입금수수료, 송금수수료, 디자인용역수수료, 견본비, 카탈로그제작비, CS비,교환광고료(현재미집행), 보석감별수수료(EC제외), 주문전화료(EC제외)
	 */
	/* 1-1 상품판매가
	 * 공헌이익.판매가격에 공헌이익조회조건.판매가격으로 지정한다.
	 *  -> 판매가격이 0인 경우, alert(PRD055)메시지를 보여주고 현재까지의 공헌이익Msg를 리턴한 후 이벤트를 종료한다.(변경됨)
	 *  -> 판매가격이 0인 경우, 공헌이익.공헌이익율, 공헌이익.공헌이익금액, 공헌이익.판매가격을 0으로 수정한 후
	 *                          현재까지의 공헌이익Msg를 리턴하고 이벤트를 종료한다.
	 *
	/* 1-2 협력사지급액
	 * if(매입유형코드가 03이면) then {
	 *  	if(협력사지급율액코드가 A이면 공헌이익.협력사지급액에 공헌이익조회조건.협력사지급율액을 지정한다.)
	 *  	if(협력사지급율액코드가 R이면 공헌이익.협력사지급액에
	 * 			round(공헌이익조회조건.판매가격 * 공헌이익조회조건.협력사지급율액 / 100, 0) 을 지정한다.)
	 * } else {
	 * 		공헌이익.협력사지급액에 공헌이익조회조건.매입가격을 지정한다.
	 * }
	 *
 	/* 1-3 가격할인
	 * 공헌이익조회조건.율액코드를 공헌이익.율액코드에 지정한다.
	 * if(율액코드가 R이면) then {
	 *  	공헌이익.적용액에 round(공헌이익조회조건.판매가격 *공헌이익조회조건.당사적용율액 / 100, 0)을 지정한다.)
	 * } else {
	 *  	공헌이익.적용액에 공헌이익조회조건.당사적용율액을 지정한다.
 	 * }
 	 *
 	 * 결재예상금액을 (판매가격 - 공헌이익.적용액) 으로 계산한다.
 	 * 공헌이익.적용율은 공헌이익조회조건.당사적용율액으로 지정한다.
 	 *
 	/* 1-4 임직원내부할인
 	 * 임시결재예상금액 = 결재예상금액 (그룹사할인액계산용)
 	 *
  	 * if(Mid(대분류코드,1,3)가 A39 또는 A41 또는 A43 또는 B11 또는 B31 이거나
  	 *    대표상품여부가 Y이거나,
  	 *    사은품유형코드가 '00'이 아니거나,
  	 *    카드사용제한여부가 Y이면) {
  	 * 		임직원할인제한여부를 Y로 지정한다.
  	 * } else {
  	 * 		임직원 할인제한 여부를 N으로 지정한다.
  	 * }
  	 *
  	 * 공헌이익.임직원할인제한여부에 임직원할인제한여부를 지정한다.
  	 * 최소임직원할인율 = 0.05, 최대임직원할인율 = 0.25
  	 *
  	 * if(임직원할인제한여부가 N이면) {
  	 * 		if(세금유형코드가 '02' 이고 매입유형코드가 '03'이면) {
  	 *  		임시임직원할인율 = (결재예상금액 - ( ( 공헌이익.협력사지급액 * 1.21 ) +사은품원가 ) ) / 결재예상금액
  	 *   	} else {
	 * 			임시임직원할인율 = (결재예상금액 - ( ( 공헌이익.협력사지급액 * 1.1 ) + 사은품원가 ) ) / 결재예상금액
	 *  	}
	 *
	 *   	if(임시임직원할인율 >= 최소임직원할인율) {
	 *    		임직원할인율 = Min(임시임직원할인율, 최대임직원할인율)
	 *     		공헌이익.임직원할인금액 = round(공헌이익.협력사지급액 * 임직원할인율 * (공헌이익공통.당사임직원비율/100), 0)
	 *      } else { 공헌이익.임직원할인금액 = 0 }
	 *
	 * 		결재예상금액 = 결재예상금액 - 공헌이익.임직원할인금액
	 * } else { 임직원할인율 = 0, 공헌이익.임직원할인금액 = 0 }
	 *
	 * if(세금유형코드가 '02' 이고 매입유형코드가 '03'이면) {
	 * 		임시그룹사할인액 = 결재예상금액 - ( (공헌이익.협력사지급액 * 1.1 ) * 1.2)
	 * } else { 임시그룹사할인액 =결재예상금액 - (공헌이익.협력사지급액 * 1.2) }
	 *
	 * 공헌이익.임직원할인율 = 임직원할인율
	 *
	/* 2. 쿠폰할인
	 * if(상품등록여부가 Y이면) {
	 *  	if(쿠폰적용유형코드가 '09'가 아니면) {
	 *  		if(쿠폰번호가 null이면) { 공헌이익쿠폰 대상조회를 수행한다. }
	 *   		else                    { 공헌이익쿠폰 조회를 수행한다. }
	 *
	 *    		if(조회실패시) {
	 *				공헌이익.유효종료일시 = sysdate
	 * 				공헌이익.쿠폰대상유형코드 = 'YC'
	 *  			공헌이익.당사할인율액 = 0
	 *   			공헌이익.쿠폰유형코드 = '1'
	 *          } else {
     *				공헌이익.유효종료일시 = 공헌이익쿠폰대상.유효종료일시
	 * 				공헌이익.쿠폰대상유형코드 = 공헌이익쿠폰대상.쿠폰대상유형코드
	 *  			공헌이익.당사할인율액 = 공헌이익쿠폰대상.당사할인율액
	 *   			공헌이익.쿠폰유형코드 = 공헌이익쿠폰대상.쿠폰유형코드
	 *          }
	 *
	 *          if(쿠폰적용유형코드가 '03'이면) {
	 *              if(sysdate < 공헌이익.유효종료일시 이고 공헌이익.당사할인율액 > 0 이고 쿠폰대상유형코드가 'YC'이면) {
	 *                  if(쿠폰유형코드가 '1'이면) {
	 *                      쿠폰할인액 = round(결재예상금액 *공헌이익.당사할인율액 * 0.01, 0)
	 *                  } else { 쿠폰할인액 = 공헌이익.당사할인율액 }
	 *              } else { 쿠폰할인액 = 0 }
	 *          } else {
	 *              쿠폰할인액2 = round(결재예상금액 * (공헌이익공통.쿠폰적용비율 * 0.01) * (공헌이익공통.쿠폰할인비율 * 0.01), 0)
	 *
	 *              if(sysdate < 공헌이익.유효종료일시 이고 공헌이익.당사할인율액 > 0 이고 쿠폰대상유형코드가 'YC'이면) {
	 *                  if(쿠폰유형코드가 '1'이면)
	 *                      쿠폰할인액1 = round(결재예상금액 *공헌이익.당사할인율액 * 0.01, 0)
	 *                  else
	 *                      쿠폰할인액1 = 공헌이익.당사할인율액
	 *
	 *                  if(쿠폰할인액1 >= 쿠폰할인액2이면) 쿠폰할인액 = 쿠폰할인액1
	 *                  else                               쿠폰할인액 = 쿠폰할인액2
	 *              } else { 쿠폰할인액 = 0 }
	 *          }
	 *      } else { 쿠폰할인액 = 0 }
	 *      ??????  else { 쿠폰할인액 = round(결재예상금액 * (공헌이익공통.쿠폰적용비율 * 0.01) * (공헌이익공통.쿠폰할인비율* 0.01), 0) } ---> 이건 뭥미? OSM
	 * } else{ 쿠폰할인액 = 0 }
	 *
	 * 공헌이익.최적쿠폰할인금액 = 쿠폰할인액
	 * if(쿠폰최대할인액 > 0 and 쿠폰최대할인액 < 쿠폰할인액) {
	 *      쿠폰할인액 = 쿠폰최대할인액
	 *      공헌이익.최적쿠폰할인금액 = 쿠폰최대할인액
	 * }
	 *
	 * 결제예정금액 = 결제예정금액 - 쿠폰할인액
	 *
	 * if(sysdate < 공헌이익.유효종료일시 이고 공헌이익.당사할인율액 > 0 이고 쿠폰대상유형코드가 'YC'이면) {
	 *      그룹사할인제한여부 = Y
	 * } else {
	 *      if(임시그룹사할인액 >= 0) 그룹사할인제한여부 = N else 그룹사할인제한여부 = Y
	 * }
	 *
	 * 공헌이익.그룹사할인금액 = round(임시결재예상금액* (그룹사임직원비율 * (그룹사임직원할인율 / 100) / 100), 0)
	 * 공헌이익.그룹사할인제한여부 = 그룹사할인제한여부
	 *
	 * if(그룹사할인제한여부가 N이면) { 결재예상금액 =결재예상금액 - 공헌이익.그룹사할인금액}

	 * 공헌이익.결재예상금액 = 결재예상금액
	 * 공헌이익.매출이익 = 결재예상금액 - 공헌이익.협력사지급액
	 *
    /* 3. 기타제비용
	 * 공헌이익.기본적립금제한여부 = 공헌이익조회조건.기본적립금제한여부
	 * 공헌이익.선택적립금적용여부 = 공헌이익조회조건.선택적립금적용여부
	 *
	 * if(기본적립금적용여부가 Y이면) {
	 *      if(선택적립금적용여부가 N이면) 적립율 = 0 else 적립율 = 선택적립율
	 * } else { 적립율 =3 }
	 *
	 * 공헌이익.적립율 = 적립율
	 * 적립액 = round(결재예상금액 * (((적립율 *적립금다운비율) / 100) / 100), 0) + round(결재예상금액 * (공통적립금부여율 / 100), 0)
	 * 공헌이익.적립액 = 적립액
	 * 무이자할인금액 = round(결재예상금액 * (((무이자할부수수료율 * 무이자다운비율) / 100) / 100), 0)
	 * 공헌이익.무이자할인금액 = 무이자할인금액
	 * 공헌이익.카드사용제한여부 = 카드사용제한여부
	 *
	 * if(카드사용제한여부가 N이면){
	 *      카드수수료할인액 = round(결재예상금액 * (카드수수료율 / 100), 0)
	 *      공헌이익.카드수수료할인액 = 카드수수료할인액
	 * }
	 *
	 * 포장할인액 = round(결재예상금액 * (포장비율 / 100), 0)
	 * 공헌이익.포장할인액 = 포장할인액
	 * 공통판촉비할인액 = round(결재예상금액 * (공통판촉비비율 / 100), 0)
	 * 공헌이익.공통판촉비할인액 = 공통판촉비할인액
	 *
	 * if(mid(배송수거방법코드,1,1) != '3') {
	 *      배송비할인액 = round(출고배송비금액 * (1 + (반품배송비금액 / 100) / (1 - 반품율 / 100)) +
	 *                           반품배송비금액 * ( (반품율 / 100) / (1 - 반품율 / 100))
	 *                          , 0)
	 *      공헌이익.배송비할인액 = 배송비할인액
	 * }
	 *
	 * 제휴수수료할인액 = round(결재예상금액 * (제휴수수료율 / 100), 0)
	 * 공헌이익.제휴수수료할인액 = 제휴수수료할인액
	 * 고객센터할인액 = round(결재예상금액 * (고객센터비율 / 100), 0)
	 * 공헌이익.고객센터할인액 = 고객센터할인액
	 * 기타변경할인액 = round(결재예상금액 * (기타변경비율/ 100), 0)
	 * 공헌이익.기타변경할인액 = 기타변경할인액
	 * 공헌이익액 = 결재예상금액 - 협력사지급액 - (공헌이익.적립액 + 공헌이익.무이자할인금액 +
	 *                                             공헌이익.카드수수료할인액 + 공헌이익.포장할인액 +
	 *                                             공헌이익.공통판촉비할인액 + 공헌이익.배송비할인액 +
	 *                                             공헌이익.제휴수수료할인액 + 공헌이익.고객센터할인액 +
	 *                                             공헌이익.기타변경할인액)
	 * 공헌이익.공헌이익액 = 공헌이익액
	 * 공헌이익율 = round(공헌이익액 / 결재예상금액 * 100, 2)
	 * 공헌이익.공헌이익율 = 공헌이익율
	 *
	 * 공헌이익Msg를 리턴한다.
	 *
	 * </pre>
	 *
	 * @author lgcns-213eab4ac
	 *
	 * @date 2010-10-11 09:54:32
	 *
	 * @param RSPDataSet dataSet
	 *
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public CntrbProfit getCntrbProfit(CntrbProfitQryCond pCntrbProfitQryCond) throws DevEntException {
		CntrbProfit cntrbProfit = new CntrbProfit();

		String nullChk = StringUtils.NVL(pCntrbProfitQryCond.getNullChk()); // 널체크

		if (nullChk.equals("")) {
			throw new DevEntException(Message.getMessage("prd.error.msg.052")); // 공헌이익공통조회의 setNull()이 호출되지 않았습니다. \n 공헌이익조회조건을 Null로 초기화되어야 합니다.
		}
		BigDecimal salePrc = new BigDecimal("0");
		String prdRegYn = StringUtils.NVL(pCntrbProfitQryCond.getPrdRegYn()); // 상품등록여부
		EntityDataSet<DSData> getCntrbProfitPrdData = null;

		if ("Y".equals(prdRegYn)) {
			if (pCntrbProfitQryCond.getPrdCd() == null) {
				throw new DevEntException(Message.getMessage("cmm.msg.004", new String[] { "상품코드" })); // {0} 값은 필수 입력값입니다.
			}

			// 공헌이익조회를 위한 상품마스터,상품가격히스토리의 기본내용을 조회
			getCntrbProfitPrdData = prdEntity.getCntrbProfitPrd(pCntrbProfitQryCond);
			//logger.debug("getCntrbProfitPrdData check =================================================");
			//logger.debug(getCntrbProfitPrdData);

			if (getCntrbProfitPrdData.getValues() == null) {
				throw new DevEntException(Message.getMessage("prd.error.msg.053")); // 상품정보를 읽어오지 못했습니다
			}

			cntrbProfit.setPrdCd(pCntrbProfitQryCond.getPrdCd()); // 상품코드
			cntrbProfit.setPrdNm(getCntrbProfitPrdData.getValues().getString("prdNm")); // 상품명
			cntrbProfit.setCpnMaxDcAmt(getCntrbProfitPrdData.getValues().getBigDecimal("cpnMaxDcAmt")); // 쿠폰최대할인금액

			pCntrbProfitQryCond.setFmlyDcLimitYn(getCntrbProfitPrdData.getValues().getString("fmlyDcLimitYn")); // 그룹사할인제한여부
			pCntrbProfitQryCond.setEmpDcLimitYn(getCntrbProfitPrdData.getValues().getString("empDcLimitYn")); // 임직원할인제한여부
			pCntrbProfitQryCond.setCardUseLimitYn(getCntrbProfitPrdData.getValues().getString("cardUseLimitYn")); // 카드사용제한여부
			pCntrbProfitQryCond.setBaseAccmLimitYn(getCntrbProfitPrdData.getValues().getString("baseAccmLimitYn")); // 기본적립금제한여부
			pCntrbProfitQryCond.setSalePrc(new BigDecimal(getCntrbProfitPrdData.getValues().getString("salePrc"))); // 판매가격
			pCntrbProfitQryCond.setPrchTypCd(getCntrbProfitPrdData.getValues().getString("prchTypCd")); // 매입유형코드
			pCntrbProfitQryCond.setSupGivRtamtCd(getCntrbProfitPrdData.getValues().getString("supGivRtamtCd")); // 협력사지급율액코드
			pCntrbProfitQryCond.setSupGivRtamt(new BigDecimal(getCntrbProfitPrdData.getValues()
					.getString("supGivRtamt"))); // 협력사지급율액
			pCntrbProfitQryCond.setPrchPrc(new BigDecimal(getCntrbProfitPrdData.getValues().getString("prchPrc"))); // 매입가격
			pCntrbProfitQryCond.setDlvPickMthodCd(getCntrbProfitPrdData.getValues().getString("dlvPickMthodCd")); // 배송수거방법코드
			pCntrbProfitQryCond.setLrgClsCd(getCntrbProfitPrdData.getValues().getString("lrgClsCd")); // 대분류코드
			pCntrbProfitQryCond.setMidClsCd(getCntrbProfitPrdData.getValues().getString("midClsCd")); // 중분류코드
			pCntrbProfitQryCond.setSmlClsCd(getCntrbProfitPrdData.getValues().getString("smlClsCd")); // 소분류코드
			pCntrbProfitQryCond.setSelAccmApplyYn(getCntrbProfitPrdData.getValues().getString("selAccmApplyYn")); // 선택적립금적용여부
			pCntrbProfitQryCond.setSelAccRt(new BigDecimal(getCntrbProfitPrdData.getValues().getString("selAccRt"))); // 선택적적립율
			pCntrbProfitQryCond.setCpnMaxDcAmt(new BigDecimal(getCntrbProfitPrdData.getValues()
					.getString("cpnMaxDcAmt"))); // 쿠폰최대할인금액
			pCntrbProfitQryCond.setRepPrdYn(getCntrbProfitPrdData.getValues().getString("repPrdYn")); // 대표상품여부
			pCntrbProfitQryCond.setGftTypCd(getCntrbProfitPrdData.getValues().getString("gftTypCd")); // 사은품유형코드
			pCntrbProfitQryCond.setTaxTypCd(getCntrbProfitPrdData.getValues().getString("taxTypCd")); // 세금유형코드
			/* 1-1 상품판매가 ----------------------------------------------------*/
			salePrc = (pCntrbProfitQryCond.getSalePrc() == null)
			             		? new BigDecimal(0) : pCntrbProfitQryCond.getSalePrc().setScale(0, BigDecimal.ROUND_HALF_UP); // 판매가격
	        if (salePrc.compareTo(new BigDecimal("0")) == 0){
				// throw new DevEntException(Message.getMessage("prd.error.msg.055")); //상품판매가가 0입니다
				cntrbProfit.setCntrbProfitRt(new BigDecimal("0"));
				cntrbProfit.setCntrbProfitAmt(new BigDecimal("0"));
				cntrbProfit.setSalePrc(new BigDecimal("0"));
				return cntrbProfit;
			}
			// 쿠폰적용유형코드
			if (getCntrbProfitPrdData.getValues().getString("cpnApplyTypCd") != null) {
				cntrbProfit.setCpnApplyTypCd(getCntrbProfitPrdData.getValues().getString("cpnApplyTypCd"));
			}

			// 공헌이익무이자조회
			EntityDataSet<DSData> getCntrbProfitNoIntData = prdPmoEntity.getCntrbProfitNoInt(pCntrbProfitQryCond);

			if (getCntrbProfitNoIntData.getValues() == null) {
				cntrbProfit.setNoIntMmCnt(new BigDecimal(0)); // 무이자개월수
			} else {
				cntrbProfit.setNoIntMmCnt(new BigDecimal(getCntrbProfitNoIntData.getValues().getString("noIntMmCnt")).setScale(0, BigDecimal.ROUND_HALF_UP)); // 무이자개월수
			}
			logger.debug("무이자개월수(NoIntMmCnt) 1. ==>[" + cntrbProfit.getNoIntMmCnt() + "]");

			// 공헌이익MD할인조회 -> 아래쪽으로 위치 옮김. (2011/03/10 OSM)

			// 공헌이익사은품조회
			EntityDataSet<DSMultiData> getCntrbProfitGftList = prdPmoEntity.getCntrbProfitGft(pCntrbProfitQryCond);

			if (getCntrbProfitGftList.size() > 0) {
				BigDecimal prchPrc = new BigDecimal(0);
				int getCntrbProfitGftListSize = getCntrbProfitGftList.size();
				for (int i = 0; i < getCntrbProfitGftListSize; i++) {
					prchPrc.add((getCntrbProfitGftList.getValues().getBigDecimal(i, "prchPrc") == null)
					        ? new BigDecimal(0) : getCntrbProfitGftList.getValues().getBigDecimal(i, "prchPrc").setScale(0, BigDecimal.ROUND_HALF_UP)); // 매입가격
				}

				cntrbProfit.setGftCprc(prchPrc); // 사은품원가
				logger.debug("사은품원가(prchPrc)==>[" + prchPrc + "]");
			}
		} else {

			/* 1-1 상품판매가 ----------------------------------------------------*/
			salePrc = (pCntrbProfitQryCond.getSalePrc() == null)
			             		? new BigDecimal(0) : pCntrbProfitQryCond.getSalePrc().setScale(0, BigDecimal.ROUND_HALF_UP); // 판매가격
	        if (salePrc.compareTo(new BigDecimal("0")) == 0){
				// throw new DevEntException(Message.getMessage("prd.error.msg.055")); //상품판매가가 0입니다
				cntrbProfit.setCntrbProfitRt(new BigDecimal("0"));
				cntrbProfit.setCntrbProfitAmt(new BigDecimal("0"));
				cntrbProfit.setSalePrc(new BigDecimal("0"));
				return cntrbProfit;
			}

			// 무이자할부개월수
			if (pCntrbProfitQryCond.getNoIntMmCnt() != null) {
				cntrbProfit.setNoIntMmCnt(pCntrbProfitQryCond.getNoIntMmCnt());
			} else {
				cntrbProfit.setNoIntMmCnt(new BigDecimal(0));
			}
			logger.debug("무이자개월수(NoIntMmCnt) 2. ==>[" + cntrbProfit.getNoIntMmCnt() + "]");

			cntrbProfit.setRtAmtCd("A"); // 율액코드
			cntrbProfit.setApplyAmt(new BigDecimal(0)); // 적용액
			cntrbProfit.setBaseAccmLimitYn("N"); // 기본적립금제한여부
			cntrbProfit.setSelectAccmApplyYn("N"); // 선택적립금적용여부
			cntrbProfit.setCardUseLimitYn("N"); // 카드사용제한여부
		}

		cntrbProfit.setSalePrc(salePrc);
		logger.debug("상품판매가(salePrc) ==>[" + salePrc + "]");

		/* 1-2 협력사지급액 --------------------------------------------------*/
		String prchTypCd = (pCntrbProfitQryCond.getPrchTypCd() == null)
		                 ? "" : StringUtils.NVL(pCntrbProfitQryCond.getPrchTypCd()); // 매입유형코드

		if ("03".equals(prchTypCd)) {
			String supGivRtAmtCd = (pCntrbProfitQryCond.getSupGivRtamtCd() == null)
			                     ? "" : StringUtils.NVL(pCntrbProfitQryCond.getSupGivRtamtCd()); // 협력사지급율액코드

			if ("01".equals(supGivRtAmtCd)) {
				cntrbProfit.setSupGivAmt(pCntrbProfitQryCond.getSupGivRtamt().setScale(0, BigDecimal.ROUND_HALF_UP)); // 협력사지급액
			}

			if ("02".equals(supGivRtAmtCd)) {
				BigDecimal supGivRtAmt = (pCntrbProfitQryCond.getSupGivRtamt() == null)
				                 		? new BigDecimal(0) : pCntrbProfitQryCond.getSupGivRtamt(); // 협력사지급율액
				//supGivRtAmt = Math.round(salePrc * supGivRtAmt / 100);
				supGivRtAmt = salePrc.multiply(supGivRtAmt, MathContext.DECIMAL64).
								divide(new BigDecimal(100), 16, BigDecimal.ROUND_HALF_UP).
								setScale(0, BigDecimal.ROUND_HALF_UP);
				cntrbProfit.setSupGivAmt(supGivRtAmt); // 협력사지급액
			}
		} else {
			if (pCntrbProfitQryCond.getPrchPrc() == null)
				cntrbProfit.setSupGivAmt(new BigDecimal(0));
			else
				cntrbProfit.setSupGivAmt(pCntrbProfitQryCond.getPrchPrc());
		}
		logger.debug("협력사지급액(SupGivAmt) ==>[" + cntrbProfit.getSupGivAmt() + "]");

		/* 1-3 가격할인 ------------------------------------------------------*/
		// 공헌이익MD할인조회
		// -> 공헌이익무이자조회와 공헌이익사은품조회 사이에 있던 로직을 여기로 옮김.(2011.03.10 OSM)
		// -> 적용액 계산로직을 통합함.(2011.03.10 OSM)
		// -> 신규상품은 상품코드가 없으므로 0으로 고정처리 (2011/03/30 OSM)
		if (pCntrbProfitQryCond.getPrdCd() == null) {
			cntrbProfit.setRtAmtCd("A"); // 율액코드
			cntrbProfit.setApplyAmt(new BigDecimal("0")); // 적용액
			cntrbProfit.setApplyRt(new BigDecimal("0")); // 적용율
		} else {
			EntityDataSet<DSData> getCntrbProfitMdDcData = prdPmoEntity.getCntrbProfitMdDc(pCntrbProfitQryCond);

			BigDecimal applyRt = new BigDecimal(0);
			if (getCntrbProfitMdDcData.getValues() != null) {
				if  (getCntrbProfitMdDcData.getValues().getString("applyRt") != null)
					applyRt = new BigDecimal(getCntrbProfitMdDcData.getValues().getString("applyRt")); // 적용율

				if (getCntrbProfitMdDcData.getValues() == null) {
					cntrbProfit.setRtAmtCd("A"); // 율액코드
					cntrbProfit.setApplyAmt(new BigDecimal("0")); // 적용액
					cntrbProfit.setApplyRt(new BigDecimal("0")); // 적용율
				} else {
					if ("R".equals(getCntrbProfitMdDcData.getValues().getString("rtamtCd"))) {
						//long applyAmt = Math.round(salePrc * applyRt / 100);
						BigDecimal applyAmt = salePrc.multiply(applyRt, MathContext.DECIMAL64).
												divide(new BigDecimal(100), 16, BigDecimal.ROUND_HALF_UP).
												setScale(0, BigDecimal.ROUND_HALF_UP);
						cntrbProfit.setApplyAmt(applyAmt); // 적용액
					} else {
						if (getCntrbProfitMdDcData.getValues().getString("applyAmt") == null)
							cntrbProfit.setApplyAmt(new BigDecimal(0));
						else
							cntrbProfit.setApplyAmt(new BigDecimal(getCntrbProfitMdDcData.getValues().getString("applyAmt")));
					}

					// 율액코드
					if (getCntrbProfitMdDcData.getValues().getString("rtamtCd") == null)
						cntrbProfit.setRtAmtCd("A");
					else
						cntrbProfit.setRtAmtCd(getCntrbProfitMdDcData.getValues().getString("rtamtCd"));

					cntrbProfit.setApplyRt(applyRt); // 적용율
				}
			} else {
				cntrbProfit.setRtAmtCd("A"); // 율액코드
				cntrbProfit.setApplyAmt(new BigDecimal("0")); // 적용액
				cntrbProfit.setApplyRt(new BigDecimal("0")); // 적용율
			}
		}
		EntityDataSet<DSData> getProfit = cntrbProfitMngEntity.getCntrbProfitRc(pCntrbProfitQryCond);

		int size = 0;
		try {
			size = getProfit.size();
		} catch(NullPointerException e) {
			//e.printStackTrace();
		}

		if ( size > 0 ) {
			logger.debug("판매가 ===============================================");
			logger.debug("상품판매가(salePrc) ==>[" + cntrbProfit.getSalePrc() + "]");
			cntrbProfit.setSalePrc(getProfit.getValues().getBigDecimal("salePrc"));
			logger.debug("공헌이익MD할인===============================================");
			logger.debug("율액코드(rtAmtCd) ==>" + cntrbProfit.getRtAmtCd() + "]");
			logger.debug("적용액(applyAmt) ==>" + cntrbProfit.getApplyAmt() + "]");
			logger.debug("적용율(applyRt) ==>" + cntrbProfit.getApplyRt() + "]");
			cntrbProfit.setEmpDcLimitYn(getProfit.getValues().getString("empDcLimitYn")); // 임직원할인제한여부
			cntrbProfit.setEmpDcAmt(getProfit.getValues().getBigDecimal("empDcAmt")); //임직원할인액
			cntrbProfit.setEmpDcRt(getProfit.getValues().getString("empDcRt")); //임직원할인율
			cntrbProfit.setOptmlCpnDcAmt(getProfit.getValues().getBigDecimal("optmlCpnDcAmt")); // 최적쿠폰할인금액
			cntrbProfit.setFmlyDcAmt(getProfit.getValues().getBigDecimal("fmlyDcAmt")); //그룹사할인금액
			cntrbProfit.setGshsDcRtamt(getProfit.getValues().getBigDecimal("gshsDcRtamt"));
			logger.debug("그룹사임직원할인  ================================================");
			logger.debug("그룹사임직원할인금액(fmlyDcAmt) ==>[" + cntrbProfit.getFmlyDcAmt() + "]");
			BigDecimal payExpctPrc = new BigDecimal(0);
			payExpctPrc = getProfit.getValues().getBigDecimal("payExpctPrc");
			cntrbProfit.setPayExpctPrc(payExpctPrc);// 결재예상금액

			logger.debug("결재예상금액  ================================================");
			logger.debug("결재예상금액(payExpctPrc) ==>[" + payExpctPrc + "]");

			// 매출이익 = 결재예상금액 - 공헌이익.협력사지급액
			if (cntrbProfit.getSupGivAmt() == null) {
				//cntrbProfit.setSaleProfit(new BigDecimal(payExpctPrc - 0));
				cntrbProfit.setSaleProfit(payExpctPrc);
			} else {
				//cntrbProfit.setSaleProfit(new BigDecimal(payExpctPrc - NumberUtils.getLong(cntrbProfit.getSupGivAmt())));
				cntrbProfit.setSaleProfit(payExpctPrc.subtract(cntrbProfit.getSupGivAmt()).setScale(0, BigDecimal.ROUND_HALF_UP));
			}
			logger.debug("매출이익  ================================================");
			logger.debug("매출이익(saleProfit) ==>[" + cntrbProfit.getSaleProfit() + "]");

			/* 3. 기타제비용 -----------------------------------------------------*/
			// 기본적립금적용여부
			String baseAccmLimitYn = "N";
			if (pCntrbProfitQryCond.getBaseAccmLimitYn() != null)
				baseAccmLimitYn = StringUtils.NVL(pCntrbProfitQryCond.getBaseAccmLimitYn());
			cntrbProfit.setBaseAccmLimitYn(baseAccmLimitYn);

			// 선택적립금적용여부
			String selectAccmApplyYn = "N";
			if (pCntrbProfitQryCond.getSelAccmApplyYn() != null)
				selectAccmApplyYn = StringUtils.NVL(pCntrbProfitQryCond.getSelAccmApplyYn());
			cntrbProfit.setSelectAccmApplyYn(selectAccmApplyYn);

			BigDecimal accRt = new BigDecimal(0); // 적립율
			if ("Y".equals(baseAccmLimitYn)) {
				if ("N".equals(selectAccmApplyYn)) {
					//accRt = 0; // 적립율
				} else {
					accRt = (pCntrbProfitQryCond.getSelAccRt() == null)
					      ? new BigDecimal(0) : pCntrbProfitQryCond.getSelAccRt();
				}
			} else {
				accRt = new BigDecimal(3); // 적립율
			}

			cntrbProfit.setAccmDownRt( getProfit.getValues().getBigDecimal("accmDownRt")); // 적립금다운비율
			cntrbProfit.setAccRt(accRt); // 적립율
			cntrbProfit.setCmmAccmGivRt(getProfit.getValues().getBigDecimal("cmmAccmGivRt")); // 공통적립금부여율
			cntrbProfit.setNoIntDownRt(getProfit.getValues().getBigDecimal("noIntDownRt")); // 무이자다운비율
			cntrbProfit.setMemAccmCost(getProfit.getValues().getBigDecimal("memAccmCost")); // 적립금비용

			//long accAmt = Math.round(payExpctPrc * (((accRt * accmDownRt) / 100) / 100))
			//		    + Math.round(payExpctPrc * (cmmAccmGivRt / 100)); // 적립액
			cntrbProfit.setAccAmt(getProfit.getValues().getBigDecimal("accAmt")); // 적립액
			cntrbProfit.setNoIntDcAmt(getProfit.getValues().getBigDecimal("noIntDcAmt")); // 무이자할인금액
			cntrbProfit.setCardUseLimitYn(getProfit.getValues().getString("cardUseLimitYn")); // 카드사용제한여부
			cntrbProfit.setCardFeeDcAmt(getProfit.getValues().getBigDecimal("cardFeeDcAmt")); // 카드사용제한여부

			cntrbProfit.setPkgDcAmt(getProfit.getValues().getBigDecimal("pkgDcAmt")); // 포장할인액

			cntrbProfit.setCmmSaleproCostDcAmt(getProfit.getValues().getBigDecimal("cmmSaleproCostDcAmt")); // 공통판촉비할인액
			cntrbProfit.setDlvcDcAmt(getProfit.getValues().getBigDecimal("dlvcDcAmt")); // 배송비할인액
			cntrbProfit.setAliaFeeDcAmt(getProfit.getValues().getBigDecimal("aliaFeeDcAmt")); // 제휴수수료할인액
			cntrbProfit.setCustCentDcAmt(getProfit.getValues().getBigDecimal("custCentDcAmt")); // 고객센터할인액
			cntrbProfit.setEtcChgDcAmt(getProfit.getValues().getBigDecimal("etcChgDcAmt")); // 기타변경할인액
			cntrbProfit.setCntrbProfitAmt(getProfit.getValues().getBigDecimal("cntrbProfitAmt")); // 공헌이익액
			cntrbProfit.setCntrbProfitRt(getProfit.getValues().getBigDecimal("cntrbProfitRt")); // 공헌이익율
			cntrbProfit.setNoIntDownRt(getProfit.getValues().getBigDecimal( "noIntDownRt")); // 무이자다운비율
			cntrbProfit.setM2NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m2NoIntInsmFeeRt")); // 2개월무이자할부수수료율
			cntrbProfit.setM3NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m3NoIntInsmFeeRt")); // 3개월무이자할부수수료율
			cntrbProfit.setM4NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m4NoIntInsmFeeRt")); // 4개월무이자할부수수료율
			cntrbProfit.setM5NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m5NoIntInsmFeeRt")); // 5개월무이자할부수수료율
			logger.debug("getCntrbProfit=> 2-1");
			cntrbProfit.setM6NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m6NoIntInsmFeeRt")); // 6개월무이자할부수수료율
			cntrbProfit.setM7NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m7NoIntInsmFeeRt")); // 7개월무이자할부수수료율
			cntrbProfit.setM8NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m8NoIntInsmFeeRt")); // 8개월무이자할부수수료율
			cntrbProfit.setM9NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m9NoIntInsmFeeRt")); // 9개월무이자할부수수료율
			cntrbProfit.setM10NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m10NoIntInsmFeeRt")); // 10개월무이자할부수수료율
			cntrbProfit.setM11NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m11NoIntInsmFeeRt")); // 11개월무이자할부수수료율
			cntrbProfit.setM12NoIntInsmFeeRt(getProfit.getValues().getBigDecimal( "m12NoIntInsmFeeRt")); // 12개월무이자할부수수료율
			cntrbProfit.setAccmDownRt(getProfit.getValues().getBigDecimal( "accmDownRt")); // 적립금다운비율
			cntrbProfit.setCmmAccmGivRt(getProfit.getValues().getBigDecimal( "cmmAccmGivRt")); // 공통적립금부여율
			cntrbProfit.setGftCprc(getProfit.getValues().getBigDecimal( "gftCprc"));//원가성사은품
			cntrbProfit.setNoIntMmCnt(getProfit.getValues().getBigDecimal( "noIntMmCnt"));
		}
		return cntrbProfit;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품코드별 공헌이익을 조회한다
	 * 	조회조건의 널체크가 null이면 alert(PRD052)메시지를 보여주고 이벤트를 종료한다.
	 * 	조회조건의 값을 리턴할 공헌이익Msg에 지정한다.
	 * 	if (상품등록여부가'Y'이면) then{
	 * 	상품코드가 없으면 이벤트를종료한다.
	 * 	(공헌이익조회를 위한 상품,상품가격정보를 조회한다. -&gt; 조회실패시 alert(PRD053)메시지와 공헌이익MSG를 리턴하고 이벤트를 종료한다.
	 * 	공헌이익조회조건.상품코드와 공헌이익조회조건.상품명을 공헌이익Msg에 지정한다.
	 * 	)
	 * 	(공헌이익조회를 위한 프로모션정보를 조회한다.
	 * 	-&gt; 공헌이익무이자조회 실패시
	 * 	공헌이익조회조건.무이자개월수가 널이면, 공헌이익Msg에 지정하고 아니면 공헌이익.무이자개월수를 0으로 지정한다.
	 * 	-&gt; 공헌이익MD할인조회 실패시
	 * 	공헌이익조회조건.율액코드가 널이면, 공헌이익Msg에 지정하고 아니면 공헌이익.율액코드를 A로 지정한다.
	 * 	공헌이익조회조건.당사적용액이 널이면, 공헌이익Msg에 지정하고 아니면 공헌이익.적용액을 0으로 지정한다.
	 * 	-&gt; 공헌이익사은품조회 실패시
	 * 	공헌이익사은품목록을 null로 지정한다.
	 * 	공헌이익사은품조회수행 후,해당 상품코드의 사은품이 여러개일 때
	 * 	공헌이익사은품목록의 개별 사은품에 대한 원가를 loop돌면서 가지고 와서, 모두 합한 값을 공헌이익Msg.사은품원가에 넣는다.
	 * 	}
	 * 	else{ //상품등록여부가 Y가 아닌경우
	 * 	공헌이익조회조건.무이자할부개월수를 공헌이익Msg에 지정한다.
	 * 	공헌이익.율액코드를 A로 지정한다
	 * 	공헌이익.적용액을 0으로 지정한다.
	 * 	공헌이익.임직원할인제한여부를 N으로 지정한다.
	 * 	공헌이익.그룹사할인제한여부를 N으로 지정한다.
	 * 	공헌이익.기본적립금제한여부를 N으로 지정한다.
	 * 	공헌이익.선택적립금적용여부를 N으로 지정한다.
	 * 	공헌이익.카드사용제한여부를 N으로 지정한다.
	 * 	}
	 * 	공헌이익.무이자할부개월수를 조회결과로 지정한다.
	 * 	쿠폰적용유형코드를 조회결과로 지정한다.
	 * 	공헌이익공통조회를 수행하여 공헌이익Msg에 지정한다. -&gt; 실패시 alert(PRD054)메시지를 보여주고, 현재까지 정해진 공헌이익Msg를 리턴하고 이벤트를 종료한다.
	 * 	/* [공헌이익] (이 주석을 소스에 포함한다.)
	 * 	1.결제예상가(고객) 1_1.상품판매가- 1_2.업체지급액 - 1_3.가격할인 당사부담- 1_4.임직원할인 1_4_1.임직원(내부)공통할인율 임직원제한여부,1_4_2.그룹사(외부)공통할인율 상품그룹사제한여부- 1_5.쿠폰할인 상품쿠폰제한타입 1-5-1.전용쿠폰 전용쿠폰정보의 최대쿠폰할인액 (Only 다운쿠폰),1-5-2.Thanks Thanks쿠폰할인율 * Thanks쿠폰적용률,1-5-3.제한쿠폰 제한쿠폰할인율 * 제한쿠폰적용률
	 * 	- 3.기타제비용- 3_1.공통적립금 기본적립금(3%) * 공통다운비율 + 공통적립금부여율- 3_2.무이자할부수수료 무이자개월수별 공통적용비율(상품무이자프로모션)- 3_3.카드가맹점수수료 소분류적용비율- 3_4.포장비 소분류적용비율- 3_5.판매촉진비 공통적용비율- 3_6.배송비 공통적용비율(직송 제외)- 3_7.제휴수수료 소분류적용비율- 3_8.고객센터비 소분류적용비율- 3_9.기타변동비 소분류적용비율
	 * @ 소분류쿠폰적용비율은 취급액 대비 비율(SAP-최근월)@ 소분류별 비율 적용 항목의 경우, 신규 분류에 대해서는 상위 중분류 &gt; 대분류 비율 순으로 적용@ 기타변동비 = 고객입금수수료, 송금수수료, 디자인용역수수료, 견본비, 카탈로그제작비, CS비,교환광고료(현재미집행), 보석감별수수료(EC제외), 주문전화료(EC제외)
	 */
	@Override
	/* 1-1상품판매가 */
	// 공헌이익.판매가격에 공헌이익조회조건.판매가격으로 지정한다. -&gt; 판매가격이 0인 경우, alert(PRD055)메시지를 보여주고 현재까지 공헌이익Msg를 리턴하고 현재까지 공헌이익Msg를 리턴한하고, 이벤트를 종료한다.
	/* 1-2협력사지급액 */
	/*
	 * if(매입유형코드가 03이면) then{ if(협력사지급율액코드가 A이면 공헌이익.협력사지급액에 공헌이익조회조건.협력사지급율액을 지정한다.) if(협력사지급율액코드가 R이면 공헌이익.협력사지급액에 round(공헌이익조회조건.판매가격 *
	 * 공헌이익조회조건.협력사지급율액/ 100, 0) 을 지정한다.) } else{ 공헌이익.협력사지급액에 공헌이익조회조건.매입가격을 지정한다. } /*1-3 가격할인
	 */
	/*
	 * 공헌이익조회조건.율액코드를 공헌이익.율액코드에 지정한다. if(율액코드가 R이면 공헌이익.적용액에 round(공헌이익조회조건.판매가격 *공헌이익조회조건.당사적용율액 / 100, 0)을 지정한다. else(공헌이익.적용액에 공헌이익조회조건.당사적용율액을
	 * 지정한다.) 결재예상금액을 (판매가격 - 공헌이익.적용액) 으로 계산한다. 공헌이익.적용율은 공헌이익조회조건.당사적용율액으로 지정한다. /*1-4-1임직원내부할인
	 */
	/* 임시결재예상금액 = 결재예상금액 /*그룹사할인액계산용 */
	/*
	 * if(Mid(대분류코드,1,3)가 A39또는 A41 또는 A43 이거나 대표상품여부가 Y이거나, 사은품유형코드가 '00'이 아니거나 카드사용제한여부가 Y이면) 임직원할인제한여부를 Y로 지정한다 else 임직원 할인제한 여부를 N으로 지정한다.
	 * 공헌이익.임직원할인제한여부에 임직원할인제한여부를 지정한다. 최소임직원할인율 = 0.05 최대임직원할인율 = 0.25 if(임직원할인제한여부가 N이면){ if(세금유형코드가 '02' 이고 매입유형코드가 '03'이면){ 임시임직원할인율 = (결재예상금액 - (
	 * ( 공헌이익.협력사지급액 * 1.21 ) +사은품원가 ) )/결재예상금액 } else{ 임시임직원할인율 = (결재예상금액 - ( ( 공헌이익.협력사지급액 * 1.1 ) +사은품원가 ) )/결재예상금액 } if(임시임직원할인율 &gt;= 최소임직원할인율) {
	 * 임직원할인율 = Min(임시임직원할인율, 최대임직원할인율) 공헌이익.임직원할인금액 = round(공헌이익.협력사지급액* 임직원할인율*(공헌이익공통.당사임직원비율/100), 0) else{ 공헌이익.임직원할인금액 = 0 } 결재예상금액 = 결재예상금액 -
	 * 공헌이익.임직원할인금액 } else{ 임직원할인율 = 0 공헌이익.임직원할인금액 = 0 } if(세금유형코드가 '02' 이고 매입유형코드가 '03'이면){ 임시그룹사할인액 =결재예상금액 - ( (공헌이익.협력사지급액 * 1.1 ) * 1.2) } else{
	 * 임시그룹사할인액 =결재예상금액 - (공헌이익.협력사지급액 * 1.2) } 공헌이익.임직원할인율 = 임직원할인율 /*2. 쿠폰할인
	 */
	/*
	 * if(상품등록여부가 Y이면){ if(쿠폰적용유형코드가 '09'가 아니면){ if(쿠폰번호가 null이면){ 공헌이익쿠폰대상조회를 수행한다 } else{ 공헌이익쿠폰조회를 수행한다. } 조회실패시{ 공헌이익.유효종료일시 = sysdate
	 * 공헌이익.쿠폰대상유형코드 = 'YC' 공헌이익.당사할인율액 = 0 공헌이익.쿠폰유형코드 = '1' } if(쿠폰적용유형코드가 '03'이면){ if(sysdate &lt; 공헌이익.유효종료일시 이고 공헌이익.당사할인율액&gt;0 이고 쿠폰대상유형코드가
	 * 'YC'이면){ if(쿠폰유형코드가 '1'이면)쿠폰할인액 = round(결재예상금액 *공헌이익.당사할인율액 * 0.01, 0) else쿠폰할인액 = 공헌이익.당사할인율액 else{ 쿠폰할인액 = 0 } } else{ 쿠폰할인액2 = =
	 * round(결재예상금액 * (공헌이익공통.쿠폰적용비율 * 0.01) * (공헌이익공통.쿠폰할인비율 * 0.01), 0) if(sysdate &lt; 공헌이익.유효종료일시 이고 공헌이익.당사할인율액&gt;0 이고 쿠폰대상유형코드가 'YC'이면){
	 * if(쿠폰유형코드가 '1'이면)쿠폰할인액1 = round(결재예상금액 *공헌이익.당사할인율액 * 0.01, 0) else쿠폰할인액1 = 공헌이익.당사할인율액 if(쿠폰할인액1 &gt;= 쿠폰할인액2이면) 쿠폰할인액 = 쿠폰할인액1 else 쿠폰할인액 =
	 * 쿠폰할인액2 } else 쿠폰할인액 = 쿠폰할인액1 } } else if(쿠폰적용유형코드가 '09'가 이면){ 쿠폰할인액 = 0 } else{ 쿠폰할인액 = round(결재예상금액 * (공헌이익공통.쿠폰적용비율 * 0.01) * (공헌이익공통.쿠폰할인비율*
	 * 0.01), 0) } } else{ 쿠폰할인액 = 0 쿠폰최대할인금액 = 0 } 공헌이익.최적쿠폰할인금액 = 쿠폰할인액 if(쿠폰최대할인액&gt; 0 and쿠폰최대할인액 &lt; 쿠폰할인액){ 쿠폰할인액 = 쿠폰최대할인액 공헌이익.최적쿠폰할인금액 =
	 * 쿠폰최대할인액 } 결재대상금액 = 결재대상금액 - 쿠폰할인액 if(sysdate &lt; 공헌이익.유효종료일시 이고 공헌이익.당사할인율액&gt;0 이고 쿠폰대상유형코드가 'YC'이면){ 그룹사할인제한여부 = Y } else{ if(임시그룹사할인액
	 * &gt;=0) 그룹사할인제한여부 = N else 그룹사할인제한여부 = Y } 공헌이익.그룹사할인금액 = round(임시결재예상금액* (그룹사임직원비율 * (그룹사임직원할인율/ 100) / 100), 0) 공헌이익.그룹사할인제한여부 = 그룹사할인제한여부
	 * if(그룹사할인제한여부가 N이면){ 결재예상금액 =결재예상금액 - 공헌이익.그룹사할인금액} } 공헌이익.결재예상금액 = 결재예상금액 공헌이익.매출이익 = 결재예상금액 - 공헌이익.협력사지급액 /*3기타제비용
	 */
	/*
	 * 공헌이익.기본적립금제한여부 = 공헌이익조회조건.기본적립금제한여부 공헌이익.선택적립금적용여부 = 공헌이익조회조건.선택적립금적용여부 if(기본적립금적용여부가 Y이면){ if(선택적립금적용여부가 N이면) 적립율 = 0 else 적립율 = 선택적립율 } else{
	 * 적립율 =3 } 공헌이익.적립율 = 적립율 적립액 = round(결재예상금액 * (((적립율 *적립금다운비율) / 100) / 100), 0) + round(결재예상금액 * (공통적립금부여율 / 100), 0) 공헌이익.적립액 = 적립액 무이자할인금액=
	 * round(결재예상금액 * (((무이자할부수수료율 * ldc_muija_무이자다운비율_rate) / 100) / 100), 0) 공헌이익.무이자할인금액= 무이자할인금액 공헌이익.카드사용제한여부 = 카드사용제한여부 if(카드사용제한여부가 N이면){
	 * 카드수수료할인액 = round(결재예상금액 * (카드수수료율 / 100), 0) 공헌이익.카드수수료할인액 = 카드수수료할인액 } 포장할인액 = round(결재예상금액 * (포장비율 / 100), 0) 공헌이익.포장할인액 = 포장할인액 공통판촉비할인액
	 * round(결재예상금액 * (공통판촉비비율 / 100), 0) 공헌이익.공통판촉비할인액 = 공통판촉비할인액 if(mid(배송수거방법코드,1,1) &lt;&gt; '3'){ 배송비할인액 = round(출고배송비금액* (1 + (반품배송비금액 / 100) /
	 * (1 - 반품율 / 100)) + 반품배송비금액 * ( (반품율 / 100) / (1 - 반품율 / 100)), 0) 공헌이익.배송비할인액 = 배송비할인액 } 제휴수수료할인액 round(결재예상금액 * (제휴수수료율/ 100), 0)
	 * 공헌이익.제휴수수료할인액 = 제휴수수료할인액 고객센터할인액 round(결재예상금액 * (고객센터비율 / 100), 0) 공헌이익.고객센터할인액 = 고객센터할인액 기타변경할인액 round(결재예상금액 * (기타변경비율/ 100), 0) 공헌이익.기타변경할인액
	 * = 기타변경할인액 공헌이익액 = 결재예상금액 - 협력사지급액 - (공헌이익.적립액 +공헌이익.무이자할인금액 + 공헌이익.카드수수료할인액+ 공헌이익.포장할인액 + 공헌이익.공통판촉비할인액 + 공헌이익.배송비할인액 + 공헌이익.제휴수수료할인액 +
	 * 공헌이익.고객센터할인액 + 공헌이익.기타변경할인액) 공헌이익.공헌이익액= 공헌이익액 공헌이익율 = round(공헌이익액 /결재예상금액 * 100, 2) 공헌이익.공헌이익율 = 공헌이익율 공헌이익Msg를 리턴한다.
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 *
	 * @date 2011-01-25 07:37:22
	 *
	 * @param RSPDataSet dataSet
	 *
	 * @return Map<String, EntityDataSet>
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> getCntrbProfit(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		String key = "inCntrbProfitQryCond";
		CntrbProfitQryCond pCntrbProfitQryCond =  dataSet.getDataset4NormalObjFirst(key, CntrbProfitQryCond.class);
		DSData cntrProfitInfo = new DSData();

		if(pCntrbProfitQryCond.getPromotionGb().equals("price")){  // 가격프로모션 공헌이익조회
			CntrbProfit profitInfo = prdPrcEntity.getProfitInfo(pCntrbProfitQryCond);
			cntrProfitInfo.put("cntrbProfitAmt", profitInfo.getCntrbProfitAmt());
			cntrProfitInfo.put("cntrbProfitRt", profitInfo.getCntrbProfitRt());

			returnMap.put("outProfitInfo", EDSFactory.create(CntrbProfit.class, cntrProfitInfo));
		}else if(pCntrbProfitQryCond.getPromotionGb().equals("prom1")){  // 무이자, 가격할인 프로모션 공헌이익조회
			CntrbProfit profitInfo = prdPrcEntity.getProfitInfoProm1(pCntrbProfitQryCond);
			cntrProfitInfo.put("cntrbProfitAmt", profitInfo.getCntrbProfitAmt());
			cntrProfitInfo.put("cntrbProfitRt", profitInfo.getCntrbProfitRt());

			returnMap.put("outProfitInfo", EDSFactory.create(CntrbProfit.class, cntrProfitInfo));

			logger.debug("무이자.가격할인--->"+cntrProfitInfo);
		}else{    // 사은품프로모션 공헌이익조회
			CntrbProfit profitInfo = prdPrcEntity.getProfitInfoProm2(pCntrbProfitQryCond);
			cntrProfitInfo.put("cntrbProfitAmt", profitInfo.getCntrbProfitAmt());
			cntrProfitInfo.put("cntrbProfitRt", profitInfo.getCntrbProfitRt());
			logger.debug("사은품------------->"+cntrProfitInfo);
			returnMap.put("outProfitInfo", EDSFactory.create(CntrbProfit.class, cntrProfitInfo));
		}

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품 공헌이익을 조회한다
	 *
	 * </pre>
	 *
	 * @author kskang
	 *
	 * @date 2011-01-25 07:37:22
	 *
	 * @param RSPDataSet dataSet
	 *
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> getPrdCntrbProfit(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		String key = "inCntrbProfitQryCond";
		CntrbProfitQryCond pCntrbProfitQryCond =  dataSet.getDataset4NormalObjFirst(key, CntrbProfitQryCond.class);
		DSData cntrProfitInfo = new DSData();

		CntrbProfit profitInfo = prdPrcEntity.getPrdCntrbProfit(pCntrbProfitQryCond);
		cntrProfitInfo.put("cntrbProfitAmt", profitInfo.getCntrbProfitAmt());
		cntrProfitInfo.put("cntrbProfitRt", profitInfo.getCntrbProfitRt());

		returnMap.put("outProfitInfo", EDSFactory.create(CntrbProfit.class, cntrProfitInfo));

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 해외사이트 목록을 조회한다.
	 *   .유효시작일시가 없는 경우 '1900-01-01 00:00:00'을 지정한다
	 *   .유효종료일시가 없는 경우 '2999-12-31 23:59:59'을 지정한다
	 *
	 * </pre>
	 *
	 * @author ParkDaeBum
	 * @date 2010-10-14 11:48:26
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getForgnSiteList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		String key = "inGetForgnSiteList";
		ForgnSiteQryCond pForgnSiteQryCond = dataSet.getDataset4NormalObjFirst(key, ForgnSiteQryCond.class);

		returnMap.put("outGetForgnSiteList", forgnPrdEntity.getForgnSiteList(pForgnSiteQryCond));

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 아이템코드팝업화면에서 조회한다
	 *
	 * </pre>
	 *
	 * @author DingMingHe
	 * @date 2010-11-15 08:54:32
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getItemCdListPopup(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		String key = "inGetItemCdListPopup";
		ItemCdQryCond pItemCdQryCond = dataSet.getDataset4NormalObjFirst(key, ItemCdQryCond.class);
		if ((pItemCdQryCond.getItemCd() != null && !"".equals(pItemCdQryCond.getItemCd().toString()))
				&& (pItemCdQryCond.getItemNm() != null && !"".equals(pItemCdQryCond.getItemNm()))) {
			pItemCdQryCond.setItemNm("");
		}
		// 아이템코드목록팝업조회
		returnMap.put("outGetItemCdListPopup", itemCdEntity.getItemCdListPopup(pItemCdQryCond));
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 멀티코드/인입코드를 조회조건으로, 주문상품내역을 조회한다.
	 * 	1. 멀티코드를 조건으로 상품코드 및 채널을 조회한다.
	 * 	2. 조회된 채널에 대하여, 아래 로직에 따라, 인입코드에 따른 채널을 재등록한다.
	 * 	# 채널강제변경
	 * 	IF (인입코드 IN ( "05", "06" )) { IF (채널코드 = "C")  { 채널코드 = "U" 채널상세코드 = "UA" }}ELSE IF (인입코드 = "13" ) { IF ( 채널코드 = "C" OR 채널코드 = "P" )  { 채널코드 = "H" 채널상세코드 = "HB"  }} ELSE IF ( 인입코드 IN ("14","15","16")) THEN{ IF (채널코드 = "C" OR 채널코드 = "P")  { 채널코드 = "B"  IF (인입코드 = "14")  { 채널상세코드 = "bA" } ELSE IF (인입코드 ="15")  { 채널상세코드 = "BA" } ELSE  { 채널상세코드 = "BB" } }}ELSE IF (인입코드="18" ) { 채널코드 = "C" 채널상세코드 = "CC" }ELSE IF (인입코드="19" ) { 채널코드 = "C" 채널상세코드 = "CD" }ELSE IF (인입코드="20" ){ 채널코드 = "C" 채널상세코드 = "CE" }
	 * 	if ( 채널코드 != "" ){ 채널코드 = 채널코드 채널상세코드 = 채널상세코드}
	 * 	3. 정해진 채널의 판매가능여부를 조회한다,
	 * 	IF(판매가능여부 = 'N') THEN
	 * 	 판매가능채널을 조회한다.
	 * 	4. 주문상품을 조회한다.
	 *
	 * </pre>
	 *
	 * @author JEON
	 * @date 2010-11-17 12:36:06
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	/*
	 * public Map<String, EntityDataSet> getOrdPrd(RSPDataSet dataSet) throws DevEntException { Map<String, EntityDataSet> returnMap = new
	 * HashMap<String, EntityDataSet>();
	 *
	 * String key = "inGetOrdPrd"; List<PrdQryCond> pPrdQryCondList = dataSet.getDataset4UpdateObj(key, PrdQryCond.class);
	 *
	 * returnMap = getOrdPrd(pPrdQryCondList);
	 *
	 * return returnMap; }
	 */
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getOrdPrd(List<PrdQryCond> pPrdQryCondList) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		// QryPrd ordPrd = new QryPrd();

		// for (PrdQryCond PrdQryCond : pPrdQryCondList) {
		// 멀티코드/인입코드를 조회조건으로, 주문상품내역을 조회한다.
		// 1. 멀티코드를 조건으로 상품코드 및 채널을 조회한다.
		// 2. 조회된 채널에 대하여, 아래 로직에 따라, 인입코드에 따른 채널을 재등록한다.
		// # 채널강제변경
		// IF (인입코드 IN ( "05", "06" )) { IF (채널코드 = "C") { 채널코드 = "U" 채널상세코드 = "UA" }}ELSE IF (인입코드 = "13" ) { IF ( 채널코드 = "C" OR 채널코드 = "P" ) { 채널코드
		// = "H" 채널상세코드 = "HB" }} ELSE IF ( 인입코드 IN ("14","15","16")) THEN{ IF (채널코드 = "C" OR 채널코드 = "P") { 채널코드 = "B" IF (인입코드 = "14") { 채널상세코드 =
		// "bA" } ELSE IF (인입코드 ="15") { 채널상세코드 = "BA" } ELSE { 채널상세코드 = "BB" } }}ELSE IF (인입코드="18" ) { 채널코드 = "C" 채널상세코드 = "CC" }ELSE IF (인입코드="19"
		// ) { 채널코드 = "C" 채널상세코드 = "CD" }ELSE IF (인입코드="20" ){ 채널코드 = "C" 채널상세코드 = "CE" }
		// if ( 채널코드 != "" ){ 채널코드 = 채널코드 채널상세코드 = 채널상세코드}
		// 3. 정해진 채널의 판매가능여부를 조회한다,
		// IF(판매가능여부 = 'N') THEN
		// 판매가능채널을 조회한다.
		// 4. 주문상품을 조회한다.
		// 5. 주문속성상품목록을 조회한다.
		// }

		return returnMap;
	}

	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getOrdPrd(Map<String, Object> inputMap) throws DevEntException {
		logger.debug("**** getOrdPrd Start *****");
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		PrdQryCond pPrdQryCond = (PrdQryCond) inputMap.get("inPrdQryCond"); // dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);
		PrdChanlQryCond pPrdChanlQryCond = (PrdChanlQryCond) inputMap.get("inPrdChanlQryCond"); // dataSet.getDataset4NormalObjFirst(key,
																								// PrdChanlQryCond.class);
//		PrdChanlQryCond pPrdChanlQryCond1 = (PrdChanlQryCond) inputMap.get("inPrdChanlQryCond1"); // dataSet.getDataset4NormalObjFirst(key,
																									// PrdChanlQryCond.class);
//		PrdQryCond pPrdQryCond2 = (PrdQryCond) inputMap.get("inPrdQryCond2"); // dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);
//		PrdQryCond pPrdQryCond1 = (PrdQryCond) inputMap.get("inPrdQryCond1"); // dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);

		if(StringUtils.NVL(pPrdQryCond.getChanlCd(), "").equals("")){ // 멀티코드가 input으로 없을 경우 11.05.09

			// 멀티코드채널조회
			logger.debug("**** getCodeChanl *****s");
			EntityDataSet<DSData> getCodeChanl = multiCdEntity.getMultiCdChanl(pPrdQryCond);
			logger.debug("**** getCodeChanl *****1");
			logger.debug("**** getCodeChanl *****2=" + getCodeChanl.getValues());

			EntityDataSet<DSMultiData> returnNullds = prdEntity.setNull();
			logger.debug("**** returnNullds *****2=" + returnNullds);
			returnMap.put("outSalePsblYn", returnNullds);
			if (getCodeChanl.getValues() == null) {
				logger.debug("**** getCodeChanl *****31");
				returnMap.put("outGetOrdPrdDtl", returnNullds);
				return returnMap;
			}

			// 플레인상품 관련 리턴 추가 110527
			if("10".equals(getCodeChanl.getValues().getString("prdGbnCd"))
					|| "20".equals(getCodeChanl.getValues().getString("prdGbnCd"))){

				getCodeChanl.getValues().put("prdGbnCd", getCodeChanl.getValues().getString("prdGbnCd"));
				returnMap.put("outGetOrdPrd", getCodeChanl );
				return returnMap;
			}

			logger.debug("**** getCodeChanl *****32");

			logger.debug("1-1. getCodeChanl" + getCodeChanl);

			/** 2012-04-30 김태엽 수정 - SR02120418148 - 멀티코드가 DM인 경우에는 IPTV 셋팅 로직 타지 않도록 수정 */
			String strChanlCd = StringUtils.NVL(getCodeChanl.getValues().getString("chanlCd"));
			String strChanlDtlCd = StringUtils.NVL(getCodeChanl.getValues().getString("chanlDtlCd"));

			pPrdChanlQryCond.setChanlCd(strChanlCd);
			pPrdChanlQryCond.setChanlDtlCd(strChanlDtlCd);
			pPrdChanlQryCond.setPrdCd(getCodeChanl.getValues().getBigDecimal(("prdCd")));
			pPrdQryCond.setChanlCd(getCodeChanl.getValues().getString("chanlCd"));
			pPrdQryCond.setBefChanlCd(getCodeChanl.getValues().getString("chanlCd"));
			pPrdQryCond.setChanlChgYn("N");

			logger.debug("pPrdQryCond.getDnisNo() : " + pPrdQryCond.getDnCd());
			String chanlGbnCd = StringUtils.NVL(pPrdQryCond.getDnCd());

			logger.debug("1. chanlGbnCd" + chanlGbnCd);
			
			//logger.info("#####MC제휴로그 chanlGbnCd :" + chanlGbnCd);
						
			/** 2012-04-30 김태엽 수정 - SR02120418148 - 멀티코드가 DM인 경우에는 IPTV 셋팅 로직 타지 않도록 수정 */
			if ( "D".equals(strChanlCd) ) {
				pPrdChanlQryCond.setChanlCd(strChanlCd);
				pPrdChanlQryCond.setChanlDtlCd(strChanlDtlCd);

			} else {

				// IF (인입코드 IN ( "05", "06" )) {
				if (chanlGbnCd.equals("05") || chanlGbnCd.equals("06")) {
					// IF (채널코드 = "C") {
					if (getCodeChanl.getValues().getString("chanlCd").equals("C")) {
						// 채널코드 = "U" 채널상세코드 = "UA"
						pPrdChanlQryCond.setChanlCd("U");
						pPrdChanlQryCond.setChanlDtlCd("UA");
					}
					// }ELSE IF (인입코드 = "13" ) {
				} else if (chanlGbnCd.equals("13")) {
					/*
					 * IF ( 채널코드 = "C" OR 채널코드 = "P" ) { 채널코드 = "H" 채널상세코드 = "HB"
					 */
					if (getCodeChanl.getValues().getString("chanlCd").equals("C")
							|| getCodeChanl.getValues().getString("chanlCd").equals("P")) {
						pPrdChanlQryCond.setChanlCd("H");
						pPrdChanlQryCond.setChanlDtlCd("HB");
					}
					// } ELSE IF ( 인입코드 IN ("14","15","16")) THEN{
				} else if (chanlGbnCd.equals("14") || chanlGbnCd.equals("15") || chanlGbnCd.equals("16")
						/** SR02140924125 2014-10-20 이명기 watch on, 삼성 스마트 TV, 하루 하나 자동주문,상담원 call 프로세스 추가건 **/
						/** 공통코드 ORD574로 처리[S] **
						|| chanlGbnCd.equals("21") || chanlGbnCd.equals("22") || chanlGbnCd.equals("23") || chanlGbnCd.equals("24")
						|| chanlGbnCd.equals("25") || chanlGbnCd.equals("26") || chanlGbnCd.equals("27") || chanlGbnCd.equals("28")
						|| chanlGbnCd.equals("29") || chanlGbnCd.equals("30") || chanlGbnCd.equals("31") || chanlGbnCd.equals("32")
						|| chanlGbnCd.equals("33") || chanlGbnCd.equals("34") || chanlGbnCd.equals("35") || chanlGbnCd.equals("36")
						공통코드 ORD574로 처리[E]**/
						) {
					// IF (채널코드 = "C" OR 채널코드 = "P") {
					if (getCodeChanl.getValues().getString("chanlCd").equals("C")
							|| getCodeChanl.getValues().getString("chanlCd").equals("P")) {
						// 채널코드 = "B"
						pPrdChanlQryCond.setChanlCd("B");

						/*
						 * IF (인입코드 = "14"){ 채널상세코드 = "bA" } ELSE IF (인입코드 ="15") { 채널상세코드 = "BA" } ELSE { 채널상세코드 = "BB" }
						 */
						if (chanlGbnCd.equals("14")) {
							pPrdChanlQryCond.setChanlDtlCd("bA");
						} else if (chanlGbnCd.equals("15")) {
							pPrdChanlQryCond.setChanlDtlCd("BA");
							/** 2013-07-23 김태엽 수정 - SR02130717045 - MC채널에 대해서 M커머스_SK플레닛_Tshopping 지능망번호에 대한 상세채널 세팅  */
							/** 2013-10-28 김태엽 수정 - SR02130814003 - MC채널 코드 신규생성 작업 요청 */
							/** 2014-01-13 김태엽 수정 - SR02131112125 - 신규 상담/자동주문 11월 오픈에 따른 매체작업요청 */
							/** 2014-04-10 이명기 수정 - SR02140402020 - MC 주문 채널을 위한 전화번호 생성에 따른 개발 요청 */
							/** 2014-08-14 김태엽 수정 - SR02140806108 - 올레TV모바일 주문번호 연결 작업 */							
							/** 21 - Bf - MC제휴_Tshopping_상담
							 * 22 - BF - MC제휴_Tshopping_ARS (05)
							 * 23 - BG - MC제휴_HDTV_상담
							 * 24 - BJ - MC제휴_HDTV_ARS(08)
							 * 25 - BP - MC제휴_스마트TV_상담
							 * 26 - BAQ - MC제휴_스마트TV_ARS(09)
							 * 27 - bQ - MC제휴_Tving_상담
							 * 28 - B4 - MC제휴_Tving_ARS(06)
							 * 29 - Bc - MC제휴_에브리온_상담
							 * 30 - Be - MC제휴_에브리온_ARS(07)
							 * 31 - LCO - MC제휴_DMB_상담
							 * 32 - bb - MC제휴_DMB_ARS (10)
							 * 33 - Og - MC제휴_홈쇼핑모아 상담원 주문
							 * 34 - Oh - MC제휴_홈쇼핑모아 ARS (11)
							 * 35 - BAR - MC제휴_KT올레TV모바일_상담
							 * 36 - XC - MC제휴_KT올레TV모바일_ARS (12)
							 **/
						/** SR02140924125 2014-10-20 이명기 watch on, 삼성 스마트 TV, 하루 하나 자동주문,상담원 call 프로세스 추가건 **/
						/** 공통코드 ORD574로 처리[S] **
						} else if (chanlGbnCd.equals("21")) {
							pPrdChanlQryCond.setChanlDtlCd("Bf"); // MC제휴_Tshopping_상담
						} else if (chanlGbnCd.equals("22")) {
							pPrdChanlQryCond.setChanlDtlCd("BF"); // MC제휴_Tshopping_ARS (05)
						} else if (chanlGbnCd.equals("23")) {
							pPrdChanlQryCond.setChanlDtlCd("BG"); // MC제휴_HDTV_상담
						} else if (chanlGbnCd.equals("24")) {
							pPrdChanlQryCond.setChanlDtlCd("BJ"); // MC제휴_HDTV_ARS (08)
						} else if (chanlGbnCd.equals("25")) {
							pPrdChanlQryCond.setChanlDtlCd("BP"); // MC제휴_스마트TV_상담
						} else if (chanlGbnCd.equals("26")) {
							pPrdChanlQryCond.setChanlDtlCd("BAQ"); // MC제휴_스마트TV_ARS (09)
						} else if (chanlGbnCd.equals("27")) {
							pPrdChanlQryCond.setChanlDtlCd("bQ"); // MC제휴_Tving_상담
						} else if (chanlGbnCd.equals("28")) {
							pPrdChanlQryCond.setChanlDtlCd("B4"); // MC제휴_Tving_ARS (06)
						} else if (chanlGbnCd.equals("29")) {
							pPrdChanlQryCond.setChanlDtlCd("Bc"); // MC제휴_에브리온_상담
						} else if (chanlGbnCd.equals("30")) {
							pPrdChanlQryCond.setChanlDtlCd("Be"); // MC제휴_에브리온_ARS (07)
						} else if (chanlGbnCd.equals("31")) {
							pPrdChanlQryCond.setChanlDtlCd("LCO"); // MC제휴_DMB_상담
						} else if (chanlGbnCd.equals("32")) {
							pPrdChanlQryCond.setChanlDtlCd("bb"); // MC제휴_DMB_ARS (10)
						} else if (chanlGbnCd.equals("33")) {
							pPrdChanlQryCond.setChanlDtlCd("Og"); // MC제휴_홈쇼핑모아 상담
						} else if (chanlGbnCd.equals("34")) {
							pPrdChanlQryCond.setChanlDtlCd("Oh"); // MC제휴_홈쇼핑모아 ARS (11)
						} else if (chanlGbnCd.equals("35")) {
							pPrdChanlQryCond.setChanlDtlCd("BAR"); // MC제휴_KT올레TV모바일_상담								
						} else if (chanlGbnCd.equals("36")) {
							pPrdChanlQryCond.setChanlDtlCd("XC"); // MC제휴_KT올레TV모바일_ARS (12)
						공통코드 ORD574로 처리[E] **/
						} else {
							pPrdChanlQryCond.setChanlDtlCd("BB");
						}						
					}
					// }ELSE IF (인입코드="18" ) {
				} else if (chanlGbnCd.equals("18")) {
					// 채널코드 = "C" 채널상세코드 = "CC"
					pPrdChanlQryCond.setChanlCd("C");
					pPrdChanlQryCond.setChanlDtlCd("CC");
					// }ELSE IF (인입코드="19" ) {
				} else if (chanlGbnCd.equals("19")) {
					// 채널코드 = "C" 채널상세코드 = "CD"
					pPrdChanlQryCond.setChanlCd("C");
					pPrdChanlQryCond.setChanlDtlCd("CD");
					// }ELSE IF (인입코드="20" ){
				} else if (chanlGbnCd.equals("20")) {
					// 채널코드 = "C" 채널상세코드 = "CE"
					pPrdChanlQryCond.setChanlCd("C");
					pPrdChanlQryCond.setChanlDtlCd("CE");
				
				//SR02150402003 데이터 홈쇼핑 관련 운영인력 지원 (주문/결제) 2015-04-15 이명기C
				/** 2015-06-09 김태엽 추가 - SR02150518062 - 데이타 홈쇼핑 주문 및 편성화면 구성 요청 */
				/** 20151030_유동훈(dhyoo)_SR02151026016  데이터 홈쇼핑 C&M 서비스 런칭을 위한 ARS/상담원 번호 신규 생성건 */
				} else if (chanlGbnCd.equals("51") || chanlGbnCd.equals("52") || chanlGbnCd.equals("56") || chanlGbnCd.equals("57")
						|| chanlGbnCd.equals("58") || chanlGbnCd.equals("59") || chanlGbnCd.equals("60") || chanlGbnCd.equals("61")
						|| chanlGbnCd.equals("62") || chanlGbnCd.equals("63")|| chanlGbnCd.equals("68") || chanlGbnCd.equals("69")
						|| chanlGbnCd.equals("103") || chanlGbnCd.equals("104")|| chanlGbnCd.equals("105") || chanlGbnCd.equals("106")
						|| chanlGbnCd.equals("107") || chanlGbnCd.equals("108")) {
					if (getCodeChanl.getValues().getString("chanlCd").equals("C")
							|| getCodeChanl.getValues().getString("chanlCd").equals("P")
							|| getCodeChanl.getValues().getString("chanlCd").equals("H")) {
						pPrdChanlQryCond.setChanlCd("H");
						
						if (chanlGbnCd.equals("51")) {
							pPrdChanlQryCond.setChanlDtlCd("LGU"); // KT_데이터홈쇼핑_상담원
						} else if (chanlGbnCd.equals("52")) {
							pPrdChanlQryCond.setChanlDtlCd("pL"); // KT_데이터홈쇼핑_ARS
						} else if (chanlGbnCd.equals("56")) {
								pPrdChanlQryCond.setChanlDtlCd("LIA"); // CM_데이터홈쇼핑_상담원
						} else if (chanlGbnCd.equals("57")) {
								pPrdChanlQryCond.setChanlDtlCd("LIN"); // CM_데이터홈쇼핑_ARS
						} else if (chanlGbnCd.equals("58")) {
							pPrdChanlQryCond.setChanlDtlCd("LIB"); // LGU_데이터홈쇼핑_상담원
						} else if (chanlGbnCd.equals("59")) {
							pPrdChanlQryCond.setChanlDtlCd("LID"); // LGU_데이터홈쇼핑_ARS
						} else if (chanlGbnCd.equals("60")) {
							pPrdChanlQryCond.setChanlDtlCd("LIG"); // SKB_데이터홈쇼핑_상담원
						} else if (chanlGbnCd.equals("61")) {
							pPrdChanlQryCond.setChanlDtlCd("LIE"); // SKB_데이터홈쇼핑_ARS
						} else if (chanlGbnCd.equals("62")) {
							pPrdChanlQryCond.setChanlDtlCd("LHz"); // SKY_데이터홈쇼핑_상담원
						} else if (chanlGbnCd.equals("63")) {
							pPrdChanlQryCond.setChanlDtlCd("LHw"); // SKY_데이터홈쇼핑_ARS
						} else if (chanlGbnCd.equals("68")) {//SR02151012098 데이터 홈쇼핑 스트리밍 ARS/상담원 번호 매체 변경 요청건 2015-10-19 유동훈K
							pPrdChanlQryCond.setChanlDtlCd("LGj"); // 데이터 홈쇼핑_스트리밍_상담원
						} else if (chanlGbnCd.equals("69")) {//SR02151012098 데이터 홈쇼핑 스트리밍 ARS/상담원 번호 매체 변경 요청건 2015-10-19 유동훈K
							pPrdChanlQryCond.setChanlDtlCd("LGi"); // 데이터 홈쇼핑_스트리밍_ARS

							/** 2016-10-18 김태엽 추가 - SR02161007008 - [데이터홈쇼핑] HCN, Tbroad 플랫폼 확장으로 인한 ARS, 상담원 전화번호 연결 요청의 건 */
						} else if (chanlGbnCd.equals("103")) {
							pPrdChanlQryCond.setChanlDtlCd("LJt"); // HCN_데이터홈쇼핑_ARS
						} else if (chanlGbnCd.equals("104")) {
							pPrdChanlQryCond.setChanlDtlCd("Lju"); // HCN_데이터홈쇼핑_상담원					
						} else if (chanlGbnCd.equals("105")) {
							pPrdChanlQryCond.setChanlDtlCd("LJx"); // Tbroad_데이터홈쇼핑_ARS
						} else if (chanlGbnCd.equals("106")) {
							//[SR02161123009] GS My Shop Tbroad 소매체 코드 변경 요청의 건
							pPrdChanlQryCond.setChanlDtlCd("LJy"); // Tbroad_데이터홈쇼핑_상담원
							//20161219 dhyoo(유동훈) 	SR02161212038   [데이터홈쇼핑] CJHV 플랫폼 확장으로 인한 ARS, 상담원 전화번호 연결 요청의 건
						} else if (chanlGbnCd.equals("107")) {
							pPrdChanlQryCond.setChanlDtlCd("LKN"); // CJHV_데이터홈쇼핑_ARS
						} else if (chanlGbnCd.equals("108")) {
							// CJHV_데이터홈쇼핑_상담원
							pPrdChanlQryCond.setChanlDtlCd("LKO"); //  CJHV_데이터홈쇼핑_상담원
						}					
							
					}

			    /** SR02140924125 2014-10-20 이명기 watch on, 삼성 스마트 TV, 하루 하나 자동주문,상담원 call 프로세스 추가건 **/
				/** 공통코드 ORD574로 처리[S] **/
				} else {
					
					//logger.info("#####MC제휴로그 인입 :getordprd");
					
					// IF (채널코드 = "C" OR 채널코드 = "P") {
					if (getCodeChanl.getValues().getString("chanlCd").equals("C")
							|| getCodeChanl.getValues().getString("chanlCd").equals("P")) {						
						
						if(!"".equals(StringUtils.NVL(chanlGbnCd,"").trim())) {
							CmmCdQryCond cmmCdQryCond = new CmmCdQryCond();
							cmmCdQryCond.setCmmGrpCd("ORD574");  //MC제휴서비스 ORD574
							cmmCdQryCond.setUseYn("Y"); //사용여부 Y
							cmmCdQryCond.setCmmCd(chanlGbnCd); //cmmCd
							EntityDataSet<DSMultiData> cmmCdList = cmmCdEntity.getCdList(cmmCdQryCond);
														
							//logger.info("#####MC제휴로그 인입 :chanlGbnCd" + chanlGbnCd);							
							//logger.info("#####MC제휴로그 인입 cmmCdList.getValues().size() :" + cmmCdList.getValues().size());
							
							if (cmmCdList.getValues().size() > 0) {
								pPrdChanlQryCond.setChanlCd("B");
								pPrdChanlQryCond.setChanlDtlCd(StringUtils.NVL(cmmCdList.getValues().get(0).getString("cdVal"),"").trim());															
							}
						}			
					}
					
					/**
					if (getCodeChanl.getValues().getString("chanlCd").equals("C")
							|| getCodeChanl.getValues().getString("chanlCd").equals("P")
							|| getCodeChanl.getValues().getString("chanlCd").equals("H")) {						
						
						if(!"".equals(StringUtils.NVL(chanlGbnCd,"").trim())) {
							CmmCdQryCond cmmCdQryCond2 = new CmmCdQryCond();
							cmmCdQryCond2.setCmmGrpCd("ORD596");  // GSMYSHOP서비스 ORD596
							cmmCdQryCond2.setUseYn("Y");
							cmmCdQryCond2.setCdRefVal(chanlGbnCd); //CD_REF_VAL : aniCd, CD_SUBTYP_VAL : mediaCd, CD_VAL : chanlDtlCd
							EntityDataSet<DSMultiData> cmmCdList2 = cmmCdEntity.getCdList(cmmCdQryCond2);				
							
							if ( cmmCdList2 != null && cmmCdList2.getValues() != null && cmmCdList2.getValues().size() > 0 ) {
								pPrdChanlQryCond.setChanlCd("H");
								pPrdChanlQryCond.setChanlDtlCd(StringUtils.NVL(cmmCdList2.getValues().get(0).getString("cdVal"),"").trim());																
							}
						}			
					}					
					*/
				}
			}
			//logger.debug("2015-06-24 mcarey33 - chanlGbnCd : " + chanlGbnCd);
			
			
			/*
			 * if ( 채널코드 != "" ){ 채널코드 = 채널코드 채널상세코드 = 채널상세코드 }
			 */

			logger.debug("2. pPrdChanlQryCond=" + pPrdChanlQryCond);

			pPrdQryCond.setChanlCd(pPrdChanlQryCond.getChanlCd());
			pPrdQryCond.setChanlDtlCd(pPrdChanlQryCond.getChanlDtlCd());


			// 채널판매가능여부조회
			// returnMap.put("outGetOrdPrd", prdChanlEntity.getChanlSalePsblYn(pPrdChanlQryCond));
			EntityDataSet<DSData> getCodeChanlSelYn = prdChanlEntity.getChanlSalePsblYn(pPrdChanlQryCond);
			logger.debug("3. getCodeChanlSelYn.getValues()=" + getCodeChanlSelYn.getValues());
			if (getCodeChanlSelYn.getValues() == null) {
				returnMap.put("outGetOrdPrdDtl", returnNullds);
				return returnMap;
			}
			logger.debug("2-1. pPrdQryCond" + pPrdQryCond);
			logger.debug("3. getCodeChanlSelYn=" + getCodeChanlSelYn);
			returnMap.put("outSalePsblYn", getCodeChanlSelYn);
			// if(판매가능여부 = 'N') {
			if (getCodeChanlSelYn.getValues() != null) {
				if (getCodeChanlSelYn.getValues().getString("salePsblYn").equals("N")) {
					logger.debug("3-1 getCodeChanlSelYn=" + getCodeChanlSelYn.getValues().getString("salePsblYn"));
					// 판매가능채널조회
					// returnMap.put("outGetOrdPrd", prdChanlEntity.getSalePsblChanl(pPrdChanlQryCond));
					EntityDataSet<DSData> getSelChanl = prdChanlEntity.getSalePsblChanl(pPrdChanlQryCond);
					if (getSelChanl.getValues() != null) {
						logger.debug("3-1-1 getCodeChanlSelYn=");
						pPrdQryCond.setChanlCd(getSelChanl.getValues().getString("chanlCd").toString());
						/** 2013-12-23 김태엽 추가 - SR02131112138 - CATV 판매채널이 없을경우, 대/소매체 세팅 로직 수정 */
						pPrdQryCond.setChanlDtlCd(getSelChanl.getValues().getString("chanlCd").toString() + "A");
						pPrdQryCond.setChanlChgYn("Y");
						returnMap.put("outSalePsblYn", getSelChanl);
					} else {
						logger.debug("3-1-2 getCodeChanlSelYn=");
						pPrdQryCond.setChanlCd(pPrdChanlQryCond.getChanlCd());
						/** 2013-12-23 김태엽 추가 - SR02131112138 - CATV 판매채널이 없을경우, 대/소매체 세팅 로직 수정 */
						pPrdQryCond.setChanlDtlCd(pPrdChanlQryCond.getChanlCd() + "A");
					}
					// pPrdQryCond.setChanlCd(pPrdChanlQryCond.getChanlCd());
				} else {
					logger.debug("3-2 getCodeChanlSelYn=");

				}
			} else {
				logger.debug("3-3 getCodeChanlSelYn=");
				// pPrdQryCond.setChanlCd(pPrdChanlQryCond.getChanlCd());
			}

		} else {
			logger.debug("1-2. getCodeChanl" + pPrdQryCond.getChanlCd());

			//pPrdChanlQryCond.setChanlCd(pPrdQryCond.getChanlCd());
			//pPrdChanlQryCond.setChanlDtlCd(pPrdQryCond.getChanlDtlCd());
			//pPrdChanlQryCond.setPrdCd(pPrdQryCond.getPrdCd());

			EntityDataSet<DSData> getSelChanl = new EntityDataSet<DSData>();
			DSData chanlSalePsblYn = new DSData();
			chanlSalePsblYn.put("salePsblYn", "Y");
			getSelChanl.setValues(chanlSalePsblYn);
			logger.debug("주문가능여부 확인 :: " + getSelChanl);
			returnMap.put("outSalePsblYn", getSelChanl);
		}

		logger.debug("4. pPrdQryCond" + pPrdQryCond);
		// 주문상품조회
		EntityDataSet<DSData> getOrdPrd = prdEntity.getOrdPrd(pPrdQryCond);
		pPrdQryCond.setPrdCd(getOrdPrd.getValues().getBigDecimal("prdCd"));

		// 조회상품이 일반상품인 경우, 판매종료 보지 않는다 110531
		if(StringUtils.NVL(getOrdPrd.getValues().getString("prdTypCd")).equals("G")){
			pPrdQryCond.setQryGbn("");
		}

		if (getOrdPrd != null && getOrdPrd.getValues() != null) {
			getOrdPrd.getValues().put("chanlCd", pPrdQryCond.getChanlCd());
			getOrdPrd.getValues().put("chanlDtlCd", pPrdQryCond.getChanlDtlCd());

			logger.debug("5. getOrdPrd=>" + getOrdPrd);
			returnMap.put("outGetOrdPrd", getOrdPrd);

			// 주문속성상품목록조회
			returnMap.put("outGetOrdPrdDtl", attrPrdEntity.getOrdAttrPrdList(pPrdQryCond));
		}

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 주문가능수량.재고수량.공급계획수량을 차감/환원하기 위하여 주문수량임시테이블에 등록한다.(공통코드 :PRD092참조)(Y : 주문가능, N : 주문불가, O : 수량부족, G : 사은품부족, S : 재고부족, B : 공급계획수량부족)
	 * -주문수량이 0보다 작은 경우,
	 *  IF 수량구분코드가 'BRD'이면 THEN
	 *  주문상품공급계획정보조회를 수행하여, 공급계획관리번호를 받아온다.
	 *  (공급계획관리번호가 없는 경우, 이벤트를 종료한다.)
	 *  임시주문가능수량목록등록을 수행하고 이벤트를 종료한다.
	 * - 수량구분코드가 'ORD'인 경우 (주문가능수량 차감/환원요청)
	 *  주문가능수량조회를 수행한다.
	 *  IF (주문가능여부가 'Y' &amp; 조회후 결과값의 주문가능수량이 요청한 주문수량보다 크다)
	 *  임시주문가능수량목록을 등록한다.
	 * 사은품수량&lt; 주문수량 이면 주문가능여부를 'G'로 세팅한다.
	 *  ELSE
	 *  주문가능여부가 'Y'이면 주문가능여부를 'O'로 세팅하고 이벤트를 종료한다.
	 * - 수량구분코드가 'STK'인 경우 (재고수량 차감/환원요청)
	 * 주문가능수량조회를 수행한다.
	 *  IF (주문가능여부가 'Y' &amp; 조회후 결과값의 재고수량이 요청한 주문수량보다 크다)
	 * 임시주문가능수량목록을 등록한다.
	 * 재고수량&lt; 주문수량 이면 주문가능여부를 'S'로 세팅한다.
	 * - 수량구분코드가 'BRD'인 경우 (공급계획수량 차감/환원요청)
	 * 주문상품공급계획정보조회를 수행한다.(조회시 0번 조건을 따른다)
	 *  IF (주문가능여부가 'Y' &amp; 조회후 결과값의 주문가능수량이 요청한 주문수량보다 크다)
	 *  임시주문가능수량목록을 등록한다.
	 * 공급계획수량&lt; 주문수량 이면 주문가능여부를 'B'로 세팅한다.
	 *
	 * </pre>
	 *
	 * @author JEON
	 * @date 2010-11-26 03:42:37
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	/*
	 * public Map<String, EntityDataSet> addOrdQtyTmpList(RSPDataSet dataSet) throws DevEntException { Map<String, EntityDataSet> returnMap = new
	 * HashMap<String, EntityDataSet>();
	 *
	 * String key = "inAddOrdQtyTmpList";
	 *
	 * List<OrdPsblQty> pOrdPsblQty = dataSet.getDataset4NormalObj(key, OrdPsblQty.class);
	 *
	 * returnMap = addOrdQtyTmpList(pOrdPsblQty);
	 *
	 * return returnMap; }
	 */
	/**
	 * <pre>
	 *
	 * desc : 멀티코드/인입코드를 조회조건으로, 주문상품내역을 조회한다.
	 *  1. 멀티코드를 조건으로 상품코드 및 채널을 조회한다.
	 *  2. 조회된 채널에 대하여, 아래 로직에 따라, 인입코드에 따른 채널을 재등록한다.
	 *  # 채널강제변경
	 *  IF (인입코드 IN ( "05", "06" )) {
	 *      IF (채널코드 = "C")  {
	 *          채널코드 = "U" 채널상세코드 = "UA"
	 *      }
	 *  }ELSE IF (인입코드 = "13" ) {
	 *      IF ( 채널코드 = "C" OR 채널코드 = "P" )  {
	 *           채널코드 = "H" 채널상세코드 = "HB"
	 *       }
	 *  } ELSE IF ( 인입코드 IN ("14","15","16")) THEN{
	 *      IF (채널코드 = "C" OR 채널코드 = "P")  {
	 *          채널코드 = "B"
	 *          IF (인입코드 = "14"){
	 *              채널상세코드 = "bA"
	 *          } ELSE IF (인입코드 ="15")  {
	 *              채널상세코드 = "BA"
	 *          }ELSE  {
	 *              채널상세코드 = "BB"
	 *          }
	 *      }
	 *  }ELSE IF (인입코드="18" ) {
	 *      채널코드 = "C" 채널상세코드 = "CC"
	 *  }ELSE IF (인입코드="19" ) {
	 *      채널코드 = "C" 채널상세코드 = "CD"
	 *  }ELSE IF (인입코드="20" ){
	 *      채널코드 = "C" 채널상세코드 = "CE"
	 *  }
	 *
	 *  if ( 채널코드 != "" ){
	 *      채널코드 = 채널코드 채널상세코드 = 채널상세코드
	 *  }
	 *
	 *  3. 정해진 채널의 판매가능여부를 조회한다,
	 *  IF(판매가능여부 = 'N') THEN
	 *       판매가능채널을 조회한다.
	 *  4. 주문상품을 조회한다.
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-11-23 12:48:44
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getOrdPrd(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		logger.debug("########getOrdPrd########");
		String key = "inGetOrdPrd";
		PrdQryCond pPrdQryCond = dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);
		PrdChanlQryCond pPrdChanlQryCond = dataSet.getDataset4NormalObjFirst(key, PrdChanlQryCond.class);
		PrdChanlQryCond pPrdChanlQryCond1 = dataSet.getDataset4NormalObjFirst(key, PrdChanlQryCond.class);
		PrdQryCond pPrdQryCond2 = dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);
		PrdQryCond pPrdQryCond1 = dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);

		Map<String, Object> inputMap = new HashMap<String, Object>();

		logger.debug("pPrdQryCond=" + pPrdQryCond);
		logger.debug("pPrdChanlQryCond=" + pPrdChanlQryCond);
		logger.debug("pPrdChanlQryCond1=" + pPrdChanlQryCond1);
		logger.debug("pPrdQryCond2=" + pPrdQryCond2);
		logger.debug("pPrdQryCond1=" + pPrdQryCond1);

		inputMap.put("inPrdQryCond", pPrdQryCond);
		inputMap.put("inPrdChanlQryCond", pPrdChanlQryCond);
		inputMap.put("inPrdChanlQryCond1", pPrdChanlQryCond1);
		inputMap.put("inPrdQryCond2", pPrdQryCond2);
		inputMap.put("inPrdQryCond1", pPrdQryCond1);

		returnMap = getOrdPrd(inputMap);

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 주문가능수량을 조회한다.
	 *     FOR(i=1, 요청건수)
	 *      1.주문가능수량을 조회한다.
	 *      IF ( (채널코드 = 'P' OR session.chanlCd = 'GSEC') AND 주문가능여부 = 'Y' ) THEN
	 *      IF (주문가능수량.상품구분코드 = '80') THEN
	 *      1.dAndShop상품정보조회를 수행한다.
	 *      2.조회 데이터 값 설정 후 d&amp;shop 통신 하여 재고수량/`가격 파악 주문가능 수량 설정
	 *      IF (조회결과 = True) THEN
	 *      IF (dAndShop상품.판매가격 = 조회결과.판매가격) THEN
	 *      주문가능수량.주문가능수량 = 조회결과.주문가능수량
	 *      ELSE
	 *      주문가능수량.주문가능수량 = 0
	 *      END IF
	 *      주문가능수량.주문가능수량 = 0
	 *      END IF
	 *      3. 주문가능수량.안전재고수량 = 5
	 *      END IF
	 *      IF (주문가능수량.안전재고수량 &gt; 0) THEN
	 *      IF ( 주문가능수량.주문가능수량 &gt;= 0 AND 주문가능수량.주문가능수량 &lt;= 주문가능수량.안전재고수량 ) THEN
	 *      (속성상품재고부족정보 : 재고부족일자 = TO_DATE(SYSDATE, 'YYYYMMDD')
	 *      속성상품코드 = 주문가능수량.속성상품코드
	 *      채널그룹코드 = 주문가능수량.채널코드
	 *     상품코드= 주문가능수량.상품코드
	 *      주문가능수량 = 주문가능수량.주문가능수량
	 *      안전재고수량 = 주문가능수량.안전재고수량
	 *      )
	 *      /// 호출 메소드  Start( 우선순위 3)
	 *      1.수량부족상품결품등록 수행한다.
	 *      /// 호출 메소드  End
	 *      IF (주문가능수량.주문가능수량 &gt; 주문가능수량.주문수량) THEN
	 *                    주문가능수량.주문가능여부 = 'A'
	 *      ELSE   주문가능수량.주문가능여부 = 'O'  END IF
	 *      주문가능수량Msg를 리턴하고 다음 요청건으로 넘어간다
	 *      END IF
	 *      ELSE
	 *      IF ( 주문가능수량.주문가능수량 &gt;= 0 AND 주문가능수량.주문가능수량 &lt;= 1) THEN
	 *      (속성상품재고부족정보 : 재고부족일자 = TO_DATE(SYSDATE, 'YYYYMMDD')
	 *      속성상품코드 = 주문가능수량.속성상품코드
	 *      채널그룹코드 = 주문가능수량.채널코드
	 *     상품코드= 주문가능수량.상품코드
	 *      주문가능수량 = 주문가능수량.주문가능수량
	 *      안전재고수량 = 주문가능수량.안전재고수량
	 *      )
	 *       /// 호출 메소드  Start( 우선순위 3)
	 *      1.수량부족상품결품등록 수행한다.
	 *       /// 호출 메소드  End
	 *
	 *      주문가능수량.주문가능여부 = 'O'
	 *      주문가능수량Msg를 리턴하고 다음 요청건으로 넘어간다
	 *      END IF
	 *      END IF
	 *      IF (방송상품여부 = 'Y') THEN
	 *      1.현재방송여부조회를 수행한다. (방송상품상품.상품코드 = 주문가능수량.상품코드)
	 *      IF (조회건이 없으면) THEN
	 *      주문가능수량.주문가능여부 = 'B'
	 *      주문가능수량Msg를 리턴하고 다음 요청건으로 넘어간다
	 *      END IF
	 *      END IF
	 *      IF (주문가능수량.주문수량 &gt; 주문가능수량.주문가능수량) THEN
	 *      주문가능수량.주문가능여부 = 'O'
	 *      주문가능수량Msg를 리턴하고 다음 요청건으로 넘어간다
	 *      END IF
	 *      주문가능수량Msg를 리턴하고 다음 요청건으로 넘어간다
	 *      END IF
	 *      IF (주문가능수량.주문가능여부가 = 'Y' AND 주문가능수량.주문가능수량 &gt; 주문가능수량.주문수량) THEN
	 *      IF (주문가능수량.사은품수량 &lt; 주문가능수량.주문수량) THEN
	 *           주문가능수량.주문가능여부 = 'G'   주문가능수량Msg를 리턴하고 다음 요청건으로 넘어간다  END IF
	 *  ELSE
	 *      IF (주문가능수량.주문가능여부 = 'Y') THEN
	 *                       주문가능수량.주문가능여부 = 'O'   주문가능수량Msg를 리턴하고 다음 요청건으로 넘어간다  END IF
	 *      END IF END LOOP
	 *
	 *   20110513 로직 변경
					           * if(안전재고 > 0 && 안전재고수량 > (주문가능수량 - 요청수량)  && 주문가능수량 > 0  ) {
							          1. 메일발송메소드 호출(GS_MAIL_STOCK SMS
							                배송형태가 직송이면 협력사에게
							                           직송이 아니면 MD에게
								  2. 주문가능
								   if (ordPsblQty1.getOrdPsblQty().intValue() > ordPsblQty.getOrdPsblQty().intValue()) {
															// 주문가능수량.주문가능여부 = A;
															returnData.setOrdPsblYn("A");
														} else {
															// 주문가능수량.주문가능여부 = O;
															returnData.setOrdPsblYn("O");
														}
							   }
							   if(주문가능수량 <= 0 )  {
							          1.수량부족처리 메소스 호출
							               1-1속성상품재고부족정보조회
							                   데이터가 존재하지 않으면  부족정보등록
							                   데이터가 존재하면
							                               - 속성상품재고부족정보상품조회
							                                    데이터가 존재하면  부족정보 등록


								  2. 주문불가능
							              returnData.setOrdPsblYn("O");
							   }
   	 *
   	 *
   	 *
   	 *-주문가능여부 코드정리
   	 *
   	 *	case 'O' : (주문수량 > 주문가능수량)  => alertMsg = "현재 준비된 상품 수량이 부족하여 주문이 불가능합니다. 이용에 불편을 드려서 대단히 죄송합니다."; break;
	 *	case 'G' : 사은품수량부족 			  => alertMsg = "현재 준비된 사은품의 수량이 부족하여 주문이 불가능합니다. 이용에 불편을 드려서 대단히 죄송합니다."; break;
	 *	case 'S' : 센터재고수량부족 		  => alertMsg = "현재 준비된 상품 수량이 부족하여 주문이 불가능합니다. 이용에 불편을 드려서 대단히 죄송합니다."; break;
	 *	case 'B' : 방송 공급계획수량부족	  => alertMsg = "선택하신 상품이 일시품절되어 주문이 불가능합니다."; break;// <= "공급계획수량부족"; break; 로 변경
	 *	case 'E' : 판매종료					  => alertMsg = "선택하신 상품은 판매종료되어 주문이 불가능합니다."; break;
	 *	case 'T' : 일시품절					  => alertMsg = "선택하신 상품이 일시품절되어 주문이 불가능합니다."; break;
	 *	case 'N' : 주문가능불가				  => alertMsg = "선택하신 상품은 현재 주문이 불가능합니다. 이용에 불편을 드려서 대단히 죄송합니다."; break;
   	 *  case 'A' : 안전재고
   	 *  case 'Y' : 주문가능
   	 *
   	 *
   	 * [PD_2018_005_GUCCI 입점] 2018-07-19 김철현 : 프리미엄브랜드인 경우 주문가능수량정보를 해당 회사의 API
   	 *                                              를 통하여 가져오도록 한다.
   	 *                                              --> 2018.09.03 채널 속도 이슈로 구찌API 연계부분 주석처리한다.
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2011-01-13 02:27:57
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public List<OrdPsblQtyRsltCond> getOrdPsblQty(List<OrdPsblQty> ordPsblQtyList) throws DevEntException {
		List<OrdPsblQtyRsltCond> resultList = new ArrayList<OrdPsblQtyRsltCond>();

		for (int inx = 0; inx < ordPsblQtyList.size(); inx++) {
			OrdPsblQtyRsltCond returnData = new OrdPsblQtyRsltCond();
			OrdPsblQty ordPsblQty = ordPsblQtyList.get(inx);
			// 조건 셋팅
			OrdPsblQtyQryCond pOrdPsblQtyQryCond = new OrdPsblQtyQryCond();
			pOrdPsblQtyQryCond.setAttrPrdCd(ordPsblQty.getAttrPrdCd()); // 속성상품코드
			pOrdPsblQtyQryCond.setPrdCd(ordPsblQty.getPrdCd()); // 상품코드

			// 주문쪽 테스트시 null 포인트가 나와서 처리
			if (ordPsblQty.getSessionChanlCd() != null) {
				if ("GSEC".equals(ordPsblQty.getSessionChanlCd())) {
					/* MC 채널 단독 판매 201201109-MCKIM */
					/* 20130307 KJH 추가 oahu전용 채널이 들어올 경우 P가 아닌 oahu전용채널 셋팅 */
					if ( "B".equals(ordPsblQty.getChanlCd()) || ordPsblQty.getOahuChanlCd() != null ){
						pOrdPsblQtyQryCond.setChanlCd(ordPsblQty.getChanlCd());

						//20130307 oahu전용채널 들어올 경우
						if( ordPsblQty.getOahuChanlCd() != null ) {
							pOrdPsblQtyQryCond.setChanlCd(ordPsblQty.getOahuChanlCd());
							ordPsblQty.setChanlCd(ordPsblQty.getOahuChanlCd());
						}
						//-end
					} else {
						pOrdPsblQtyQryCond.setChanlCd("P");
						ordPsblQty.setChanlCd("P");
					}
				} else {
					pOrdPsblQtyQryCond.setChanlCd(ordPsblQty.getChanlCd()); //
				}
			} else {
				pOrdPsblQtyQryCond.setChanlCd(ordPsblQty.getChanlCd()); //
			}

			pOrdPsblQtyQryCond.setSessionObject(ordPsblQty);
			/** 2020-06-25 김태엽 수정 - HANGBOT-439_GSITMBO-61 - AK몰 제휴입점 */
			pOrdPsblQtyQryCond.setOrdQty(ordPsblQty.getOrdPsblQty());

			// 주문가능수량조회 1. 주문가능수량을 조회한다.
			EntityDataSet<DSData> pGetOrdPsblQty = ordPsblQtyEntity.getOrdPsblQty(pOrdPsblQtyQryCond);
			if (pGetOrdPsblQty.getValues() == null) {
				// returnData.setOrdPsblYn("O");
				// 20110209 (EC)김석진대리 요청에 의해 추가
				// 20110218 (WAP)김재기 과장님요청에 의해 주문가능 수량 0으로 리턴
				returnData.setPrdCd(ordPsblQty.getPrdCd());
				returnData.setAttrPrdCd(ordPsblQty.getAttrPrdCd());
				returnData.setOrdPsblQty(new BigDecimal("0"));
				returnData.setOrdPsblYn("N");
				returnData.setRetCd("E");
				returnData.setRetMsg(Message.getMessage("prd.esb.msg.012"));
			} else {
				//
				OrdPsblQty ordPsblQty1 = DevBeanUtils.wrappedMapToBean(pGetOrdPsblQty.getValues(), OrdPsblQty.class);

				/* [PD_2018_005_Gucci 입점] 2018-07-19, 김철현 : 프리미엄브랜드인 경우 주문가능수량정보를 해당 회사의 API
                   를 통하여 가져오도록 한다.
	               setPrmmBrandOrdPsblQty 함수내에서 변경될수 있는 값은 
	               주문가능수량(OrdPsblQty) 과 주문가능여부(OrdPsblYn) 이다. 
	               --> 2018.09.03 채널 속도 이슈로 구찌API 연계부분 주석처리한다. */
//				if (OrdConstant.PRMM_BRAND_GUCCI_SUP_CD.equals(ObjectUtils.toString(ordPsblQty1.getSupCd()))) {
//					prdPrmmBrandMngProcess.setPrmmBrandOrdPsblQty(ordPsblQty1);
//				}
				/* [PD_2018_005_Gucci 입점] END */				
				
				// [20110118] 조건 우선 주석 처리함. 김주영대리와 협의후 처리
				/*
				 * if(("P".equals(ordPsblQty.getChanlCd()) || "GSEC".equals(ordPsblQty.getSessionChanlCd())) &&
				 * "Y".equals(ordPsblQty1.getOrdPsblYn())) {
				 */
				//logger.debug("test1");
				returnData.setOrdPsblYn(ordPsblQty1.getOrdPsblYn());
				returnData.setPrdCd(ordPsblQty.getPrdCd());
				returnData.setAttrPrdCd(ordPsblQty.getAttrPrdCd());
				returnData.setOrdPsblQty(ordPsblQty1.getOrdPsblQty());
				returnData.setRetCd("S");
				returnData.setRetMsg("");
			//	if (("P".equals(StringUtils.NVL(ordPsblQty.getChanlCd())) || "GSEC".equals(StringUtils.NVL(ordPsblQty
			//			.getSessionChanlCd())))
			//			&& "Y".equals(StringUtils.NVL(ordPsblQty1.getOrdPsblYn()))) {
			if("Y".equals(StringUtils.NVL(ordPsblQty1.getOrdPsblYn()))) {
					if ("80".equals(ordPsblQty1.getPrdGbnCd())) { // 상품구분코드가 무엇인지 확인

						EntityDataSet<DSData> pGetDAndShopPrdInfo = prdEntity.getDAndShopPrdInfo(pOrdPsblQtyQryCond);

						// 조회데이터값 설정후 디앤샵 통신하여 재고수량/가격 파악 주문가능 수량 설정;
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("prodid", pGetDAndShopPrdInfo.getValues().getString("supPrdCd"));
						paramMap.put("opt_name", pGetDAndShopPrdInfo.getValues().getString("attrTypNm1"));
						paramMap.put("opt_value", pGetDAndShopPrdInfo.getValues().getString("attrVal1"));
						// 전송 정보 지정
						HttpInfoSet http = new HttpInfoSet();
						// url 정보
						String url = Constant.getString("dnshop.prd.price");
						http.setUrl(url);
						// timeout 정보, 지정 안하면 기본은 30초
						http.setTimeout(30000);
						// 한글 char set
						// http.setCharacterSet("euc-kr");
						// 파라미터 set
						http.setParamMap(paramMap);
						String content = httpClientService.sendHttpMsg(http);

						//logger.debug("content====>" + content);
						if (!"".equals(content)) {
							String[] prdInfo = StringUtils.split(content, "^");// 통신결과를 "^"구분자로 자른다.
							if (prdInfo.length < 2) {

								ordPsblQty1.setOrdPsblQty(new BigDecimal(0));// 자른 내용이 3개가 아닌경우 주문가능수량 0으로 설정
								continue;
							} else {
								long price = StringUtils.getLong(prdInfo[0]);// 첫번째 데이터는 가격
								String state = prdInfo[1];// 2번째 데이터는 통신결과
								long qty = StringUtils.getLong(prdInfo[2]);// 3번째 데이터는 주문가능수량

								if (!"정상".equals(state)) { // 상태가 정상이 아니면 주문가능수량 0으로 설정
									// return 0;
									// 주문가능수량 = 0
									// intOrdPsblQty = 0;
									ordPsblQty1.setOrdPsblQty(new BigDecimal(0));
								} else if (price != pGetDAndShopPrdInfo.getValues().getBigDecimal("salePrice")
										.intValue()) { // 디앤샵 판매가가 같지 않으면 주문가능수량 0으로 설정
									// return 0;
									// intOrdPsblQty = 0;
									ordPsblQty1.setOrdPsblQty(new BigDecimal(0));
								} else if (price == pGetDAndShopPrdInfo.getValues().getBigDecimal("salePrice")
										.intValue()) {
									// 주문가능수량. 주문가능수량 = 조회결과.주문가능수량; ORD_PSBL_QTY
									// intOrdPsblQty = price;
									ordPsblQty1.setOrdPsblQty(new BigDecimal(qty));
								}
								// 주문가능수량.안전재고수량 = 5; 재고수량
								// intSafeStockQty = 5;
								ordPsblQty1.setSafeStockQty(new BigDecimal("5"));
							}
						} else {

							// 결과값이 빈값으로 올경우 return 0;
							ordPsblQty1.setOrdPsblQty(new BigDecimal(0));
						}
					} // 상품구분 코드가 80일 경우
					if (ordPsblQty1.getSafeStockQty() == null) {
						ordPsblQty1.setSafeStockQty(new BigDecimal("0"));
					}
					//logger.debug("test3");

					if(ordPsblQty1.getSafeStockQty().intValue() > 0  &&
							 ordPsblQty1.getSafeStockQty().intValue() >= (ordPsblQty1.getOrdPsblQty().intValue() - ordPsblQty.getOrdPsblQty().intValue())
							                   && ordPsblQty1.getOrdPsblQty().intValue() > 0 ) {
				        //메일발송메소드 호출
						ordPsblQty.setDlvPickMthodCd(ordPsblQty1.getDlvPickMthodCd());
						
						//[SR02170711940][2017.08.17][백동현]:재고부족알람배치 건에 대한 수정 개발 건
						//[SR02170818129][2017.08.21][백동현]:재고부족알람배치 건에 대한 수정 개발 건 - (추가개발)
						//입력 수량 확인 로직 추가 - 주문수량 5건 이하일 경우에만 입력함
						//0시-8시까지는 입력하지 않음
						try{
							String vCurrTime = DateUtils.format(SysUtil.getCurrTime(), "HH");
							//제외 시간 적용 : 0시~8시 사이는 제외처리
							if(Integer.parseInt(vCurrTime, 10) >= 8  && ordPsblQty.getOrdPsblQty().intValue() <= 5){
								this.sendMailGsStock(ordPsblQty);
							}
						}catch(Exception e) {
							//do nothing
						}
						
						if (ordPsblQty1.getOrdPsblQty().intValue() >= ordPsblQty.getOrdPsblQty().intValue()) {
							// 주문가능수량.주문가능여부 = A;
							returnData.setOrdPsblYn("A");
						} else {
							// 주문가능수량.주문가능여부 = O;
							returnData.setOrdPsblYn("O");
						}
						// 주문가능수량 MSG를 리턴하고
						returnData.setOrdPsblQty(new BigDecimal(ordPsblQty1.getOrdPsblQty().intValue()));
						returnData.setRetCd("S");
						returnData.setRetMsg("");
						resultList.add(returnData);
						continue;
					}
					if(ordPsblQty1.getOrdPsblQty().intValue() <= 0 ) {
						AttrPrdStockShtInfo attrPrdStockShtInfo = new AttrPrdStockShtInfo();
						attrPrdStockShtInfo.setStockShtDt(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMdd")); // 재고부족일자 = sysdate, 'YYYYMMDD'
						attrPrdStockShtInfo.setAttrPrdCd(ordPsblQty.getAttrPrdCd());
						attrPrdStockShtInfo.setChanlGrpCd(ordPsblQty.getChanlCd()); // 채널코드
						attrPrdStockShtInfo.setPrdCd(ordPsblQty.getPrdCd()); // 확인사항
						attrPrdStockShtInfo.setOrdPsblQty(ordPsblQty1.getOrdPsblQty());
						attrPrdStockShtInfo.setSafeStockQty(ordPsblQty1.getSafeStockQty()); // 안전재고수량
						/* [PD_2018_005_Gucci 입점] 2018.08.27 김철현 : 업체코드, 속성상품대표코드 추가 
						 * -->2018.09.03 채널 속도 이슈로 구찌API 연계부분 주석처리한다. */
//						attrPrdStockShtInfo.setSupCd(ordPsblQty1.getSupCd());				//업체코드
//						attrPrdStockShtInfo.setAttrPrdRepCd(ordPsblQty1.getAttrPrdRepCd());	//속성상품대표코드
						/* [PD_2018_005_Gucci 입점] End */	
						attrPrdStockShtInfo.setSessionObject(ordPsblQty);
						// 수량부족상품결품등록 우선순위 3
						this.addQtyShtPrdShtprd(attrPrdStockShtInfo);
						returnData.setOrdPsblYn("O");
						// 주문가능수량 MSG를 리턴하고
						returnData.setOrdPsblQty(new BigDecimal(ordPsblQty1.getOrdPsblQty().intValue()));
						returnData.setRetCd("S");
						returnData.setRetMsg("");
						resultList.add(returnData);
						continue;
					}
					//logger.debug("test6");
					if ("Y".equals(StringUtils.NVL(ordPsblQty1.getBroadPrdYn()))) { // 방송시간외판매제한
						//logger.debug("test7");
						BroadFormPrd pBroadFormPrd = new BroadFormPrd();
						pBroadFormPrd.setPrdCd(ordPsblQty.getPrdCd());
						pBroadFormPrd.setBroadStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
						pBroadFormPrd.setBroadEndDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
						// 현재방송여부조회
						EntityDataSet<DSData> pGetCurrBroadYn = null; 

						/** 2015-06-24 김태엽 추가 - SR02150518062 - 데이타 홈쇼핑 주문 및 편성화면 구성 요청
						 * 1. 데이터홈쇼핑 생방송여부를 가져오는 쿼리를 기존 CATV 생방송여부를 가져오는 쿼리에 UNION ALL하여 처리 
						 **/
						//if ( "H".equals(ordPsblQty.getChanlCd()) ) { 
							//pGetCurrBroadYn = broadFormPrdEntity.getCurrBroadYnByDH(pBroadFormPrd);
						//} else {
							pGetCurrBroadYn = broadFormPrdEntity.getCurrBroadYn(pBroadFormPrd);
						//}
								
								
						if (pGetCurrBroadYn.getValues() == null) {
							// 주문가능수량.주문가능여부 = 'B';
							returnData.setOrdPsblYn("B");
							// 메시지 리턴 후 다음건으로
							returnData.setOrdPsblQty(ordPsblQty1.getOrdPsblQty());
							returnData.setRetCd("S");
							returnData.setRetMsg("");
							resultList.add(returnData);
							continue;
						}
					}
					//logger.debug("test8");
					//logger.debug(ordPsblQty.getOrdPsblQty().intValue());
					if (ordPsblQty.getOrdPsblQty().intValue() > ordPsblQty1.getOrdPsblQty().intValue()) {// 주문가능수량.주문수량 < 주문가능수량.주문가능수량 )
						// 주문가능수량.주문가능여부 = 'O';
						// 메시지 리턴
						returnData.setOrdPsblYn("O");
						// 메시지 리턴 후 다음건으로
						returnData.setOrdPsblQty(ordPsblQty1.getOrdPsblQty());
						returnData.setRetCd("S");
						returnData.setRetMsg("");
						resultList.add(returnData);
						continue;
					}
				// continue;
				}
				//logger.debug("test9");
				if (("Y".equals(StringUtils.NVL(ordPsblQty1.getOrdPsblYn())) || "A".equals(StringUtils.NVL(ordPsblQty1.getOrdPsblYn())))
						&& ordPsblQty1.getOrdPsblQty().intValue() >= ordPsblQty.getOrdPsblQty().intValue()) {
					// 사은품 수량이 null이 아닐경우만 체크 20110124
					if (ordPsblQty1.getGftRemanQty() != null) {
						if (ordPsblQty1.getGftRemanQty().intValue() < ordPsblQty.getOrdPsblQty().intValue()) {
							returnData.setOrdPsblYn("G");
							returnData.setOrdPsblQty(ordPsblQty1.getOrdPsblQty());
							returnData.setRetCd("S");
							returnData.setRetMsg("");
							resultList.add(returnData);
							continue;
						}
					}
				} else {
					// 위드넷 주문수량 > 조회된 주문 가능 수량
					returnData.setOrdPsblYn("O");
					returnData.setOrdPsblQty(ordPsblQty1.getOrdPsblQty());
					returnData.setRetCd("S");
					returnData.setRetMsg("");
					resultList.add(returnData);
				    continue;
				}
			}
			resultList.add(returnData);
			// continue;
		}// for end
		return resultList;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품채널목록을 조회한다.
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2011-01-01 07:01:44
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getPrdChanlList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

//		String key = "inGetPrdChanlList";
//		PrdQryCond pPrdQryCond = dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);

		// 상품채널목록조회
		// returnMap.put("outGetPrdChanlList", prdChanlEntity.getPrdChanlList(pPrdQryCond));

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 모든상품, 판매상품, 사은품 조회 팝업
	 *
	 * </pre>
	 *
	 * @author
	 * @date 2010-12-13 10:24:37
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getPrdGbnByQryPopupList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		String key = "inGetPrdGbnByQryPopupList";
		PrdQryCond pPrdQryCond = dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);

		// 상품목록팝업조회
		returnMap.put("outGetPrdGbnByQryPopupList", prdEntity.getPrdGbnByQryPopupList(pPrdQryCond));

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품조회 팝업화면에서 조회한다.
	 *   조회구분에 따라 멀티코드이면 멀티코드상품목록조회를 수행하고,
	 *   상품코드 또는 상품명이면 상품목록팝업조회를 수행한다.
	 *
	 * </pre>
	 *
	 * @author lgcns-213eab4ac
	 * @date 2010-09-20 08:18:55
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getPrdListPopup(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		String key = "inGetPrdListPopup";
		PrdQryCond pPrdQryCond = dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);

		if (pPrdQryCond.getQryGbn().equals("MULTICD")) { // 조회구분이 멀티코드인경우
			// 멀티코드상품목록조회tls
			returnMap.put("outGetPrdListPopup", prdEntity.getMultiCdPrdList(pPrdQryCond));
		} else if (pPrdQryCond.getQryGbn().equals("PRDCD") || pPrdQryCond.getQryGbn().equals("PRDNM")|| pPrdQryCond.getQryGbn().equals("EXPOSPRDNM") ) { // 조회구분이 멀티코드가 아닌경우
			// 상품목록팝업조회
			returnMap.put("outGetPrdListPopup", prdEntity.getPrdListPopup(pPrdQryCond));
		}

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품등록 서브 체크 메소드 구현
	 *
	 * </pre>
	 *
	 * @author 강대성
	 * @date 2010-12-10 09:39:34
	 * @param 화면에서
	 *            던진 데이타 셋을 dto 로 만든뒤 보낸 각기 dto 들.
	 * @return Map<String, EntityDataSet>
	 */
	/*
	 * PrdmMain prdmMain // 기본 및 배송 정보 List<PrdAttrPrdMinsert> pattrPrdM // 속성상품정보 PrdStockDinsert prdAttrPrdm // 상품재고정보 PrdprcHinsert prdprcHinsert
	 * // 상품가격이력 List<PrdChanlDinsert> prdChanlDinsert // 상품채널정보 PrdOrdPsblQtyDinsert prdOrdPsblQtyD // 상품주문가능수량정보 List<PrdChanlMappnDinsert>
	 * prdChanlMappnD // 채널매핑정보 PrdPrdDinsert prdPrdD // 상품확장정보 List<PrdNmChgHinsert> prdNmChg // 상품명변경이력 List<EcdSectPrdlstInfo> ecdSectPrdlstInfo //
	 * 카테고리 매장
	 */
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : getPrdSaveChk
	 */
//	public DSData getPrdSaveChk(PrdmMain prdmMain, List<PrdAttrPrdMinsert> pattrPrdM, PrdStockDinsert prdAttrPrdm,
//			PrdprcHinsert prdprcHinsert, List<PrdChanlDinsert> prdChanlDinsert, PrdOrdPsblQtyDinsert prdOrdPsblQtyD,
//			List<PrdChanlMappnDinsert> prdChanlMappnD, PrdPrdDinsert prdPrdD, List<PrdNmChgHinsert> prdNmChg,
//			List<EcdSectPrdlstInfo> ecdSectPrdlstInfo) throws DevEntException {
////		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
//
//		DSData returnDsData = new DSData();
//		// int tot_cnt = delok + insertprdAttr + upok;
//		int tot_cnt = 0;
//		String setmag = "";
//		// setmag = "삭제 : "+ delok +"건, 입력 : " + ok+"건, 수정 :"+upok+"건 성공";
//		if (tot_cnt == 0) {
//			returnDsData.put("retCd", "0");
//			returnDsData.put("retMsg", "입력건 없음");
//		} else if (tot_cnt > 0) {
//			returnDsData.put("retCd", "0");
//			returnDsData.put("retMsg", setmag);
//		} else {
//			returnDsData.put("retCd", tot_cnt);
//			returnDsData.put("retMsg", setmag);
//		}
//
//		return returnDsData;
//	}
	// SOURCE_CLEANSING : END
	
	
	/**
	 * <pre>
	 *
	 * desc : 매장구분이 결정된 이후는, 상위코드를 기준으로 매장코드를 조회한다.
	 *                              상위매장코드가 없는 경우까지 매장코드를 조회한다.
	 *                 DB상태가 'D'인 대상은 매장상품관리목록에 포함하지 않는다.
	 *
	 * </pre>
	 *
	 * @author 10101520
	 * @date 2010-9-29 上午09:38:12
	 * @param dataSet
	 * @return
	 * @throws DevEntException
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getShopCd(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		String key = "ds_ShopPrdMngQryCond";
		ShopPrdMngQryCond pShopPrdMngQryCond = dataSet.getDataset4NormalObjFirst(key, ShopPrdMngQryCond.class);
		returnMap.put("ds_ShopPrdMngList_0", ecShopEntity.getShopCd(pShopPrdMngQryCond));
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc :  최상위구분의 경우, 공통코드에서 매장구분조회를 수행한다.
	 *                   입력된 매장구분이 없으면 '%'를 매장구분에 지정하여 조회한다.
	 *                   개별 매장구분에 대하여 매장구분별로 포함된 매장코드를 조회한다.
	 *                   (매장코드 테이블에서 매장구분을 조건으로 조회하여 매장구분 하위에 포함해 놓는다.)
	 *                   DB상태가 "A"가 아니면 목록에 포함하지 않는다
	 *
	 * </pre>
	 *
	 * @author 10101520
	 * @date 2010-9-29 上午09:37:49
	 * @param dataSet
	 * @return
	 * @throws DevEntException
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, EntityDataSet> getShopGbn(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		String key = "ds_ShopPrdMngQryCond";
		ShopPrdMngQryCond pShopPrdMngQryCond = dataSet.getDataset4NormalObjFirst(key, ShopPrdMngQryCond.class);
		EDSManager mgr = new EDSManager();
		EntityDataSet dataset = ecShopEntity.getShopGbn(pShopPrdMngQryCond);
		if (dataset != null) {
			mgr.addEDS(dataset);
			DSMultiData datas = (DSMultiData) dataset.getValues();
			for (DSData data : datas) {
				ShopPrdMngQryCond conf = DevBeanUtils.wrappedMapToBean(data, ShopPrdMngQryCond.class);
				conf.setShopGbn(conf.getShopCd());
				conf.setUpperShopCd(conf.getShopCd());
				EntityDataSet datasetSub = ecShopEntity.getShopGbnByShopCd(conf);
				datasetSub.getFieldInfoSet().add("upperShopCd", String.class.getName(), 255);
				datasetSub.getFieldInfoSet().add("dbSt", String.class.getName(), 255);
				//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
				SMTCLogger.infoPrd("sub = " + datasetSub);
				DSMultiData datasub = (DSMultiData) datasetSub.getValues();
				DSMultiData retdatasub = new DSMultiData();
				if (datasub != null && !datasub.isEmpty()) {
					for (DSData dsub : datasub) {
						dsub.put("shopLvl", "2");
						dsub.put("upperShopCd", data.get("shopCd"));
						retdatasub.add(dsub);
					}
				}
				datasetSub.setValues(retdatasub);
				mgr.addEDS(datasetSub);

			}
			EntityDataSet<DSMultiData> result = mgr.getResultSet();
			//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
			SMTCLogger.infoPrd("result vals=" + result);
			returnMap.put("ds_ShopPrdMngList", result);
		}
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 협력사목록을 협력사조회조건에 따라 조회한다.
	 *
	 * </pre>
	 *
	 * @author DingMingHe
	 * @date 2010-09-29 02:06:13
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getSupList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		String key = "inGetSupList";
		SupQryCond pSupQryCond = dataSet.getDataset4NormalObjFirst(key, SupQryCond.class);
		// 협력사목록조회
		returnMap.put("outGetSupList", supEntity.getSupList(pSupQryCond));
		return returnMap;
	}
	
	/**
	 * <pre>
	 *
	 * desc : [SR02170223068][2017.03.09][김형진]:세일즈원-하위협력사쿠폰금액조회 화면 기능 추가 요청의 건 
	 * 
	 * </pre>
	 * 
	 * @author 김형진
	 * @date 2017-03-13
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getChkSupList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		String key = "inGetSupList";
		SupStringTypQryCond pSupStringTypQryCond = dataSet.getDataset4NormalObjFirst(key, SupStringTypQryCond.class);
		// 협력사목록조회
		returnMap.put("outGetSupList", supEntity.getChkSupList(pSupStringTypQryCond));
		return returnMap;
	}

	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Len_Check
	 */
//	public void Len_Check() {
//
//	}
	// SOURCE_CLEANSING : END
	
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Len_Check
	 */
//	public PrdmMain Len_Check(PrdmMain prdmMain, SupDtlInfo supDtlInfo) {
//		// 상품명 30Btye
//		String ProdName = prdmMain.getPrdNm(); // 상품명
//		int ProdLen = ProdName.length();
//		int j = 0;
//
//		for (j = 0; j < ProdLen; j++) // 상품명 길이 만큼 Check
//		{
//			String CheckChr = ProdName.substring(j, j + 1);
//
//			// 특수 문자가 들어있는지 Check
//			if ((CheckChr.equals("\'")) || (CheckChr.equals("\""))) {
//				// TheApplication().RaiseErrorText("특수문자 \', \", ! 는 사용하실 수 없습니다.");
//				// return(CancelOperation);
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg("특수문자 \', \", ! 는 사용하실 수 없습니다.");
//				return prdmMain;
//			}
//
//			// 상품명의 첫 Char는 ! 안됨.
//			if (j == 0 && CheckChr.equals("!")) {
//				// TheApplication().RaiseErrorText("상품명은 !로 시작할 수 없습니다.");
//				// return(CancelOperation);
//				prdmMain.setRetCd("-1");
//				prdmMain.setRetMsg("상품명은 !로 시작할 수 없습니다.");
//				return prdmMain;
//			}
//
//			/*
//			 * if(Clib.isascii(ProdName.substring(j,j+1))) //ascii값을 가지면 1byte로 카운트 { jByte = jByte + 1; } else //이외의 경우 한글로 취급하여 2byte로 카운트 { jByte =
//			 * jByte + 2; }
//			 */
//		}
//
//		// if(jByte > 30) // 상품명이 30byte인지를 Check
//		if (ProdLen > 30) {
//			// TheApplication().RaiseErrorText("상품명은 30 byte 이내 입니다.");
//			// return(CancelOperation);
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg("상품명은 30 byte 이내 입니다.");
//			return prdmMain;
//		}
//
//		// Brand Name 30
//
//		// var sBrandName = this.GetFieldValue("Brand Name"); //브랜드명
//		// var sBrandNameLen = sBrandName.length;
//		String sBrandName = prdmMain.getBrandNm(); // 브랜드명
//		int sBrandNameLen = sBrandName.length();
//		j = 0;
//
//		/*
//		 * for (j; j < sBrandNameLen ; j++) { if(Clib.isascii(sBrandName.substring(j,j+1))) { jByte = jByte + 1; } else //이외의 경우 한글로 취급하여 2byte로 카운트 {
//		 * jByte = jByte + 2; } }
//		 */
//
//		// if(jByte > 30)
//		if (sBrandNameLen > 30) {
//			// TheApplication().RaiseErrorText("브랜드명은 한글15자(영문.기호'공백30자)까지 입력 가능합니다.");
//			// return(CancelOperation);
//			prdmMain.setRetCd("-1");
//			prdmMain.setRetMsg("브랜드명은 한글15자(영문.기호'공백30자)까지 입력 가능합니다.");
//			return prdmMain;
//		}
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg("0");
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
	
	
	
	public int Limit_Check() {
		return 0;
	}
	
	
	

	/**
	 * <pre>
	 *
	 * desc : 주문가능수량을 수정한다.
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2011-01-09 09:11:08
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> modifyOrdPsblQty(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

//		String key = "inModifyOrdPsblQty";
//		OrdPsblQty pOrdPsblQty = dataSet.getDataset4UpdateObjFirst(key, OrdPsblQty.class);
//		OrdPsblQty pOrdPsblQty1 = dataSet.getDataset4UpdateObjFirst(key, OrdPsblQty.class);

		// 주문가능수량단순수정
		// ordPsblQtyEntity.modifyOrdPsblQtySimpl(pOrdPsblQty);

		// 주문가능수량로그등록
		// ordPsblQtyEntity.addOrdPsblQtyLog(pOrdPsblQty);
		DSData returnDsData = new DSData();
		returnDsData.put("prdCd", "0");
		returnDsData.put("retCd", "0");
		returnDsData.put("retMsg", "저장성공");
		returnMap.put("outSavePrdBase", EDSFactory.create(EsbCmm.class, returnDsData));
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품기본정보를 입력한다.(연계용)
	 *  [HANGBOT-9096 GSITMBO-4581] 2021.04.21 이태호  위수탁 반품 수거형태 직반출 Default 변경관련
	 *  
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-11-21 10:49:08
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	/*
	 *
	 * PrdBaseInfo modifyPrdBaseInfoList, // 상품메인 수정 정보 (단건) PrdPrdDinsert prdPrdD, // 상품 확장 수정 정보(단건) List<PrdChanlInfo> addPrdChanlInfoPrd, // 채널추가
	 * 정보 List<PrdChanlInfo> modifyPrdChanlInfoPrd, // 채널 수정 정보 List<PrdChanlInfo> removePrdChanlInfoPrd // 채널 삭제 정보 List<PrdNumvalDinfo>
	 * prdNumvalDinfo // 상품수치정보
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, EntityDataSet> modifyPrdBaseInfoList(PrdBaseInfo modifyPrdBaseInfoList, PrdPrdDinsert prdPrdD,
			List<PrdChanlInfo> addPrdChanlInfoPrd, List<PrdChanlInfo> modifyPrdChanlInfoPrd,
			List<PrdChanlInfo> removePrdChanlInfoPrd, List<PrdNumvalDinfo> prdNumvalDinfo) throws Exception {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		DSData returnDsData = new DSData();

		DSMultiData modifyYnList = prdEntity.getModifyYn(modifyPrdBaseInfoList);   // 세트상품 구성품여부 조회.

		if(modifyYnList.size() > 0){
			//throw new DevPrcException("세트상품구성품입니다. 유료배송여부, 카드사용제한, 무형상품유형코드, 당사상품권여부 확인 바랍니다.");
			returnDsData.put("prdCd", "");
			returnDsData.put("retCd", "S");
			returnDsData.put("retMsg", "세트상품구성품입니다. 유료배송여부, 카드사용제한, 무형상품유형코드, 당사상품권여부 확인 바랍니다.");
			returnMap.put("outSavePrdBase", EDSFactory.create(EsbCmm.class, returnDsData));
			return returnMap;
		}


		// 변경이력 및 인터페이스 필수항목 체크에 필요 변경전 상품정보 조회
		String changeYn = "";
		PrdColChgLogQryCond prdColChgLogQryCond = new PrdColChgLogQryCond();
		prdColChgLogQryCond.setPrdCd(modifyPrdBaseInfoList.getPrdCd());
		PrdColChgLog prdColChgLog = prdColChgLogEntity.getPrdPrdMInfo(prdColChgLogQryCond);

		PrdValidCond afterData = new PrdValidCond();
		PrdValidCond beforeData = new PrdValidCond();
		DevBeanUtils.wrappedObjToObj(beforeData, prdColChgLog);
		//20110613 확장정보추가
		DevBeanUtils.wrappedObjToObj(afterData, modifyPrdBaseInfoList);
		DevBeanUtils.wrappedObjToObj(afterData, prdPrdD);
		changeYn = PrdValidUtil.prdValidCheck("key", beforeData,  afterData);

		/* 20110706  연관체크 값 추가 */
		/*********************************************************************************************************/
		SupDtlInfo supDtlInfo = new SupDtlInfo();
		PrdmMain prdmMain = new PrdmMain();
		DevBeanUtils.wrappedObjToObj(prdmMain, modifyPrdBaseInfoList);
		EntityDataSet<DSMultiData> supDtlInfo1 = prdEntity.getPrdSupInfo(prdmMain);
		if (supDtlInfo1 == null) {
			returnDsData.put("prdCd", "");
			returnDsData.put("retCd", "-1");
			returnDsData.put("retMsg", Message.getMessage("cmm.msg.027", new String[] { "상품", "협력사 정보" })); // 해당 {0}의 {1}가(이) 존재하지 않습니다.
			returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
			return returnMap;
		}

		supDtlInfo.setSupCd(supDtlInfo1.getValues().getBigDecimal(0, "supCd").toString()); // 협력사 코드
		supDtlInfo.setContYn(supDtlInfo1.getValues().getString("contYn")); // 협력사 코드 계약여부
		supDtlInfo.setCondtlPrchPmsnYn(supDtlInfo1.getValues().getString("condtlPrchPmsnYn")); // 협력사.조건부매입허용여부
		supDtlInfo.setDirTakoutYn(supDtlInfo1.getValues().getString("dirTakoutYn")); // 직반출여부
		supDtlInfo.setCvsRtpYn(supDtlInfo1.getValues().getString("cvsRtpYn")); // 협력사.편의점반품여부
		supDtlInfo.setTxnEndDt(supDtlInfo1.getValues().getString("txnEndDtChk")); // 협력사.거래종료일자
		supDtlInfo.setPrdOboxCd(supDtlInfo1.getValues().getString("prdOboxCd")); // 협력사.상품합포장코드
		supDtlInfo.setDirdlvEntrstDlvYn(supDtlInfo1.getValues().getString("dirdlvEntrstDlvYn")); // 협력사.직송위탁배송여부
		supDtlInfo.setDirdlvEntrstPickYn(supDtlInfo1.getValues().getString("dirdlvEntrstPickYn")); // 협력사.직송위탁수거여부
		supDtlInfo.setApntDlvsPickTypCd(supDtlInfo1.getValues().getString("apntDlvsPickTypCd")); // 협력사.지정택배수거유형코드
		supDtlInfo.setThplUseYn(supDtlInfo1.getValues().getString("thplUseYn")); // 3PL사용여부 (sap 재구축 추가 : 2013/01/23 안승훈 )
		
//		supDtlInfo.setDlvsPickPsblYn(supDtlInfo1.getValues().getString("dlvsPickPsblYn"));  //택배수거가눙여부


		if (modifyPrdBaseInfoList.getOrdMnfcYn().equals("1") || modifyPrdBaseInfoList.getOrdMnfcYn().equals("Y")) {
			modifyPrdBaseInfoList.setOrdMnfcYn("Y");
		}else {
			modifyPrdBaseInfoList.setOrdMnfcYn("N");
		}

		///
		/*유료배송인경우 배송비 코드가 없고 유료배송비금액이 있는경우 기존에 배송비 코드가 없으면 배송비 코드 생성.[bpr 박현신]2013.01.30*/
		if( "Y".equals(prdmMain.getChrDlvYn()) &&
			prdmMain.getChrDlvcCd() == null &&
			prdmMain.getChrDlvcAmt() != null &&
			prdmMain.getChrDlvcAmt().compareTo(new BigDecimal("0")) > 0) {
			SupDlvcPrd supDlvcPrd = new SupDlvcPrd();

			supDlvcPrd.setDlvcAmt(prdmMain.getChrDlvcAmt());
			supDlvcPrd.setSupCd(prdmMain.getSupCd());
			supDlvcPrd.setDlvcLimitAmt(prdmMain.getShipLimitAmt());
			DSData dlvcCd = supDlvcEntity.getCntSupDlvcCd(supDlvcPrd);
			//배송비코드가 없으면 생성한다.
			if (dlvcCd.size ()  <= 0 ) {
				List<SupDlvcPrd> supDlvcPrdRegList = new ArrayList<SupDlvcPrd>();
				List<SupDlvcPrd> supDlvcPrdModList = new ArrayList<SupDlvcPrd>();

				supDlvcPrd.setSessionObject(prdmMain);
				supDlvcPrd.setMdId("60027");
				supDlvcPrd.setStdAmtYn(prdmMain.getShipLimitYn());
				supDlvcPrdRegList.add(supDlvcPrd);
				PrdEaiPrdReTnsRslt result = supDlvcMngProcess.saveSupDlvcPrdList2(supDlvcPrdRegList, supDlvcPrdModList);
				if ("S".equals(result.getResponseResult())) {
					prdmMain.setChrDlvcCd( new BigDecimal(result.getResponseMessage()));
				}
			} else {
				prdmMain.setChrDlvcCd(dlvcCd.getBigDecimal("dlvcCd"));
			}
		 }
		// 연관 입력 값 Check
		PrdprcHinsert  prdprcHinsert  = new PrdprcHinsert();
		SafeCertPrd    psafeCertPrd   = new SafeCertPrd();
		List<PrdSpecVal>    prdSpecInfo1    = new ArrayList<PrdSpecVal>();
		List<PrdUdaDtl>   prdUdaDtl1  = new ArrayList<PrdUdaDtl>();

// mckim
		/* [딜구조개선-패널] 2014-11-06, 김상철, 딜더미상품인 경우 체크안하게  */ 
		if(!"88".equals(StringUtils.NVL(modifyPrdBaseInfoList.getPrdGbnCd()))){
			prdmMain = this.checkPrdmRelativeValue(supDtlInfo, prdmMain, prdprcHinsert, prdPrdD, psafeCertPrd, prdSpecInfo1,
					prdUdaDtl1, new ArrayList());
			if (prdmMain.getRetCd().equals("-1")) {
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", prdmMain.getRetMsg());
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			logger.debug("checkPrdmRelativeValue 체크 완료");
		}
		/* [딜구조개선-패널] END */
		
		// 연관값 Default Setting
		Map valueMap = new HashMap();
		valueMap = this.setPrdmRelativeValue(supDtlInfo, prdmMain, prdprcHinsert, prdPrdD, psafeCertPrd, prdSpecInfo1,
				prdUdaDtl1);
		prdmMain = (PrdmMain) valueMap.get("prdmMain");
		DevBeanUtils.wrappedObjToObj(modifyPrdBaseInfoList , prdmMain);
		prdPrdD = (PrdPrdDinsert) valueMap.get("prdPrdD");
		logger.debug("setPrdmRelativeValue 체크 완료");

		/*********************************************************************************************************/

	    /*
	     * [S][SR02170913526][2017.09.14][최미영]:전통주기본프로세스
	     *  연간할인권, 제휴카드적립제한, 제휴특판제한, 자산사용, 제휴포인트 제한
	    */  
	    PrdClsUda prdClsUda = new PrdClsUda(); 
	    prdClsUda.setUdaNo(new BigDecimal( "230" ));
	    prdClsUda.setPrdClsCd(prdmMain.getPrdClsCd());  
	    //logger.debug("esb 수정로직  prdmMain.getPrdClsCd() :" + prdmMain.getPrdClsCd());
	    if(PrdClsUdaUtils.isPrdClsUdaFlag(prdClsUda)){	      
	      //전통주 체크로직 반영
	      prdmMain = checkTraditionalLiquorPrd(prdmMain);
	      if (prdmMain.getRetCd().equals("-1")) {
	        returnDsData.put("prdCd", "");
	        returnDsData.put("retCd", "-1");
	        returnDsData.put("retMsg", prdmMain.getRetMsg());
	        returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
	        return returnMap;
	      }
	    } 
	    //[E][SR02170913526][2017.09.14][최미영]:전통주기본프로세스
	    
		// 상품기본정보
		if (modifyPrdBaseInfoList != null) {

			if (prdColChgLog != null) {
				int returnValue = 0;

				// 상품기본정보 변경로그를 Entity로 이관 (2011/05/05 OSM)
				returnValue = prdColChgLogEntity.addPrdPrdMChgLog(modifyPrdBaseInfoList, prdColChgLog);
				if (returnValue < 0) {
					//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
					SMTCLogger.infoPrd("상품기본정보 로깅 실패!! ==> PrdMngProcess.setPrdUpdate");
				}
				
				//[SR02140822039][2014.11.20][김지혜] : [분류가 변경되었을 경우] 
				if(!prdColChgLog.getPrdClsCd().equals(prdmMain.getPrdClsCd())) {
					if( !"40".equals(prdColChgLog.getClsChkStCd()) && !"50".equals(prdColChgLog.getClsChkStCd()) ) {
						returnDsData.put("prdCd", "");
						returnDsData.put("retCd", "-1");
						returnDsData.put("retMsg", "분류변경은 분류검증상태가 불합격, 자동불합격일때만 가능합니다. 분류를 확인하세요."); 
						returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
						return returnMap;
					}
					
					Map valueMap1 = new HashMap();
					valueMap1 = this.setPrdClsChgSet(prdmMain, modifyPrdChanlInfoPrd);
					prdmMain = (PrdmMain) valueMap1.get("prdmMain");
					DevBeanUtils.wrappedObjToObj(modifyPrdBaseInfoList , prdmMain);
					modifyPrdChanlInfoPrd = (List<PrdChanlInfo>) valueMap1.get("prdChanlInfo");
				}
				//-end
			}
			// 상품변경
			prdEntity.modifyPrdBaseInfo(modifyPrdBaseInfoList);
		}

		
		//[S][PD_2016_009] 도서산간추가배송비 2016.08.19 ljb		
		if ("3000".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd())) || "3200".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))  ){
			//제주도 배송비 코드 생성
			if( "Y".equals(prdPrdD.getJejuDlvPsblYn()) && "Y".equals(prdPrdD.getJejuChrDlvYn()) &&  
				(prdPrdD.getJejuChrDlvcCd() == null || "".equals(prdPrdD.getJejuChrDlvcCd()) ) &&
				prdPrdD.getJejuChrDlvcAmt() != null && !prdPrdD.getJejuChrDlvcAmt().equals("")) {
				
				SupDlvcPrd supDlvcPrd = new SupDlvcPrd();	
				supDlvcPrd.setDlvcAmt(new BigDecimal(prdPrdD.getJejuChrDlvcAmt()));
				supDlvcPrd.setSupCd(prdmMain.getSupCd());
				supDlvcPrd.setDlvcLimitAmt(prdmMain.getShipLimitAmt());
				
				// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
				// 직송이 아닐경우 당사배송비를 조회 하기 위해 배송비구분코드에 'GS', 협력사코드에 '989081' 를 세팅한다.
				if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){
					supDlvcPrd.setDlvcGbnCd("GS") ; // 배송비구분코드 [ 당사 배송비 : 'GS'(협력사는 당사협력사코드 : 989081) ]
					supDlvcPrd.setSupCd(new BigDecimal("989081")) ; // 당사협력사코드
				}
				
				DSData dlvcCd = supDlvcEntity.getCntSupDlvcCd(supDlvcPrd);
				//배송비코드가 없으면 생성한다.
				if (dlvcCd.size()  <= 0 ) {
					List<SupDlvcPrd> supDlvcPrdRegList = new ArrayList<SupDlvcPrd>();
					List<SupDlvcPrd> supDlvcPrdModList = new ArrayList<SupDlvcPrd>();
	
					supDlvcPrd.setSessionObject(prdmMain);
					supDlvcPrd.setMdId("60027");
					supDlvcPrd.setStdAmtYn(prdmMain.getShipLimitYn());
					supDlvcPrdRegList.add(supDlvcPrd);
					PrdEaiPrdReTnsRslt result = supDlvcMngProcess.saveSupDlvcPrdList2(supDlvcPrdRegList, supDlvcPrdModList);
					if ("S".equals(result.getResponseResult())) {
						prdPrdD.setJejuChrDlvcCd(result.getResponseMessage());
					}	
	
				} else{
					prdPrdD.setJejuChrDlvcCd(dlvcCd.getBigDecimal("dlvcCd").toString());
				}
	
			 }
			
			//[SR02170516901][2017.05.24][백동현]:(내부개선과제) 사은품 등록시 상품 채널 확인 체크로직 외 2건
			//제주지방배송가능여부가 N으로 들어오는 경우 제주와 관련된 금액 필드는 모두 N 및 Null 처리한다.
			if("N".equals(prdPrdD.getJejuDlvPsblYn())){
				 prdPrdD.setJejuChrDlvYn("N") ;   				//제주도유료배송여부;
				 prdPrdD.setJejuChrDlvcCd("") ;   				//제주도유료배송비코드;
				 prdPrdD.setJejuChrDlvcAmt("") ;   			//제주도유료배송금액;
				 prdPrdD.setJejuExchRtpChrYn("N") ;   			//제주도교환반품유료여부;
				 prdPrdD.setJejuRtpDlvcCd("") ;   				//제주도반품배송비코드;
				 prdPrdD.setJejuRtpOnewyRndtrpCd("") ;   	//제주도반품편도왕복코드;
				 prdPrdD.setJejuExchOnewyRndtrpCd("") ;   	//제주도교환편도왕복코드;
			 }
			
			
			//도서지방 배송비 코드 생성
			if( "Y".equals(prdPrdD.getIlndDlvPsblYn()) &&  "Y".equals(prdPrdD.getIlndChrDlvYn()) &&  
				(prdPrdD.getIlndChrDlvcCd() == null || "".equals(prdPrdD.getIlndChrDlvcCd()) ) &&
				prdPrdD.getIlndChrDlvcAmt() != null && !prdPrdD.getIlndChrDlvcAmt().equals("")) {
				
				SupDlvcPrd supDlvcPrd = new SupDlvcPrd();	
				supDlvcPrd.setDlvcAmt(new BigDecimal(prdPrdD.getIlndChrDlvcAmt()));
				supDlvcPrd.setSupCd(prdmMain.getSupCd());
				supDlvcPrd.setDlvcLimitAmt(prdmMain.getShipLimitAmt());
				
				// [패널] 고객부담 배송비 확대 시스템 2014.05.12 sitjjang(윤승욱)
				// 직송이 아닐경우 당사배송비를 조회 하기 위해 배송비구분코드에 'GS', 협력사코드에 '989081' 를 세팅한다.
				if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){
					supDlvcPrd.setDlvcGbnCd("GS") ; // 배송비구분코드 [ 당사 배송비 : 'GS'(협력사는 당사협력사코드 : 989081) ]
					supDlvcPrd.setSupCd(new BigDecimal("989081")) ; // 당사협력사코드
				}
				
				DSData dlvcCd = supDlvcEntity.getCntSupDlvcCd(supDlvcPrd);
				//배송비코드가 없으면 생성한다.
				if (dlvcCd.size()  <= 0 ) {
					List<SupDlvcPrd> supDlvcPrdRegList = new ArrayList<SupDlvcPrd>();
					List<SupDlvcPrd> supDlvcPrdModList = new ArrayList<SupDlvcPrd>();
	
					supDlvcPrd.setSessionObject(prdmMain);
					supDlvcPrd.setMdId("60027");
					supDlvcPrd.setStdAmtYn(prdmMain.getShipLimitYn());
					supDlvcPrdRegList.add(supDlvcPrd);
					PrdEaiPrdReTnsRslt result = supDlvcMngProcess.saveSupDlvcPrdList2(supDlvcPrdRegList, supDlvcPrdModList);
					if ("S".equals(result.getResponseResult())) {
						prdPrdD.setIlndChrDlvcCd(result.getResponseMessage());
					}	
	
				} else{
					prdPrdD.setIlndChrDlvcCd(dlvcCd.getBigDecimal("dlvcCd").toString());
				}
	
			}
			
			//[SR02170516901][2017.05.24][백동현]:(내부개선과제) 사은품 등록시 상품 채널 확인 체크로직 외 2건
			//도서지방배송가능여부가 N으로 들어오는 경우 도서와 관련된 금액 필드는 모두 N 및 Null 처리한다.
			if("N".equals(prdPrdD.getIlndDlvPsblYn())){
				prdPrdD.setIlndChrDlvYn("N") ;  				// 도서지방유료배송여부;
				prdPrdD.setIlndChrDlvcCd("") ;  				// 도서지방유료배송비코드;
				prdPrdD.setIlndChrDlvcAmt("") ;  				// 도서지방유료배송금액;
				prdPrdD.setIlndExchRtpChrYn("N") ;  			// 도서지방교환반품유료여부;
				prdPrdD.setIlndRtpDlvcCd("") ;  				// 도서지방반품배송비코드;
				prdPrdD.setIlndRtpOnewyRndtrpCd("") ;   	//도서지방반품편도왕복코드;
				prdPrdD.setIlndExchOnewyRndtrpCd("") ;   	//도서지방교환편도왕복코드;
			}
			
			//배송불가지역 자동 데이터 등록 
			//[SR02161117016] [기간계/채널] 제주/도서불가 설정시 지역 확인 로직 변경 - 배송불가 자동 처리 로직 삭제
			//setPrdDlvNoadmtRegonList(prdPrdD);
			
		}
		//[E][PD_2016_009] 도서산간추가배송비 2016.08.19 ljb
		
		
		// [새벽배송2차] 새벽배송비 세팅 [S]
		//prdPrdD = setDawnDlvInfo(prdmMain, prdPrdD);
		// [새벽배송2차] 새벽배송비 세팅 [E]
		
		
		// 상품확장정보
		if (prdPrdD != null) {
			//20110614  추가
			String cmposCntntInfo = "";

			// 기본구성값이 없으면 기본구성수량=0
			if( prdPrdD.getPrdBaseCmposCntnt() != null ) {
				if ("".equals(StringUtils.NVL(prdPrdD.getPrdBaseCmposCntnt()).trim())) {
					prdPrdD.setOrgprdPkgCnt(new BigDecimal("0"));
				} else {
					cmposCntntInfo = "[기본]" + prdPrdD.getPrdBaseCmposCntnt();
				}
			} else {
				if( prdColChgLog.getPrdBaseCmposCntnt() != null ) {
					cmposCntntInfo = "[기본]" + prdColChgLog.getPrdBaseCmposCntnt();		//2014-03-03 null일 경우 기존의 기본구성값으로 셋팅 KJH
				}
			}
			
			// 추가구성값이 없으면 추가구성수량 = 0
			if( prdPrdD.getPrdAddCmposCntnt() != null ) {
				if ("".equals(StringUtils.NVL(prdPrdD.getPrdAddCmposCntnt()).trim())) {
					if( prdPrdD.getAddCmposPkgCnt() == null) {
						prdPrdD.setAddCmposPkgCnt(new BigDecimal("0"));
					}
				} else {
					cmposCntntInfo = cmposCntntInfo + "[추가]" + prdPrdD.getPrdAddCmposCntnt();
				}
			} else {
				if( prdColChgLog.getPrdAddCmposCntnt() != null ) {
					cmposCntntInfo = cmposCntntInfo + "[추가]" + prdColChgLog.getPrdAddCmposCntnt();
				}
			}
			

			// 사은품구성값이 없으면 사은품구성수량=0
			if( prdPrdD.getPrdGftCmposCntnt() != null ) {
				if ("".equals(StringUtils.NVL(prdPrdD.getPrdGftCmposCntnt()).trim())) {
					if( prdPrdD.getGftPkgCnt() == null) {
					prdPrdD.setGftPkgCnt(new BigDecimal("0"));
					}
				} else {
					cmposCntntInfo = cmposCntntInfo + "[사은품]" + prdPrdD.getPrdGftCmposCntnt();
				}
			} else {
				if( prdColChgLog.getPrdGftCmposCntnt() != null ) {
					cmposCntntInfo = cmposCntntInfo + "[사은품]" + prdColChgLog.getPrdGftCmposCntnt();
				}
			}
			
			if (!"".equals(StringUtils.NVL(prdPrdD.getPrdEtcCmposCntnt()).trim())) {
				cmposCntntInfo = cmposCntntInfo + "[기타]" + prdPrdD.getPrdEtcCmposCntnt();
			}
			cmposCntntInfo = cmposCntntInfo.replaceAll("\r", " ");
			cmposCntntInfo = cmposCntntInfo.replaceAll("\n", " ");
			prdPrdD.setPrdInfoCmposCntnt(cmposCntntInfo);
			
			//분류검증상태코드는 수동불합격일 경우 재요청만 가능함. 
			if( !("40".equals(prdColChgLog.getClsChkStCd()) && "10".equals(prdPrdD.getClsChkStCd())) ) {
				prdPrdD.setClsChkStCd(null);
			}
			
			
			// 변경이력 및 인터페이스 필수항목 체크에 필요 변경전 상품정보 조회
			PrdColChgLogQryCond prdDColChgLogQryCond = new PrdColChgLogQryCond();
			prdDColChgLogQryCond.setPrdCd(prdPrdD.getPrdCd());
			PrdColChgLog prdDColChgLog = prdColChgLogEntity.getPrdPrdMInfo(prdColChgLogQryCond);
			
			prdEntity.setUpPrdPrdD(prdPrdD);
			
			if (prdDColChgLog != null) {
				int returnValue = 0;

				returnValue = prdColChgLogEntity.addPrdPrdDChgLog(prdPrdD, prdDColChgLog);
				if (returnValue < 0) {
					//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
					SMTCLogger.infoPrd("상품상세정보 로깅 실패!! ==> PrdMngProcess.savePrdList");
				}
				logger.debug("prdDColChgLog After!! #########################################");
				logger.debug(prdDColChgLog);
			}
			

			
			//[PD-2015-007] EC표준출고일 변경
			if(prdPrdD.getEcStdRelsDdcnt() != null && !"".equals(prdPrdD.getEcStdRelsDdcnt())){
				BigDecimal ecStdRelsDdcnt = new BigDecimal(prdPrdD.getEcStdRelsDdcnt());
				
				PrdQryCond prdQryCond = new PrdQryCond();
				prdQryCond.setPrdCd(prdPrdD.getPrdCd());
				
				List<PrdChanlInfo> ecPrdChanlInfoPrdOrg = prdChanlEntity.getPrdChanlDForEc(prdQryCond);// 상품채널목록조회		
				List<PrdChanlInfo> ecPrdChanlInfoPrd = new ArrayList<PrdChanlInfo>();	
				List<PrdChanlInfo> emptyPrdChanlInfoPrd = new ArrayList<PrdChanlInfo>();
				
				if (ecPrdChanlInfoPrdOrg != null) {
					for (int i = 0; i < ecPrdChanlInfoPrdOrg.size(); i++) {						
						if(!ecStdRelsDdcnt.equals(ecPrdChanlInfoPrdOrg.get(i).getStdRelsDdcnt())){
							ecPrdChanlInfoPrdOrg.get(i).setStdRelsDdcnt(ecStdRelsDdcnt);
							ecPrdChanlInfoPrdOrg.get(i).setSessionUserId(prdPrdD.getSessionUserId());		
						    ecPrdChanlInfoPrd.add(ecPrdChanlInfoPrdOrg.get(i));	 
						}
					}
					if(ecPrdChanlInfoPrd != null) {
					   prdChanlEntity.savePrdChanlList(emptyPrdChanlInfoPrd, ecPrdChanlInfoPrd, emptyPrdChanlInfoPrd);
					}
				}
			}

			//[PD-2015-007] 당일출고 주문마감시간 변경
			if(prdPrdD.getThedayRelsOrdDedlnTime() != null && !"".equals(prdPrdD.getThedayRelsOrdDedlnTime())){
				PrdUdaDtl prdUdaDtl = new PrdUdaDtl();
				prdUdaDtl.setUdaGbnCd("10");
				prdUdaDtl.setUdaNo(new BigDecimal("27"));
				prdUdaDtl.setUdaVal(prdPrdD.getThedayRelsOrdDedlnTime());
				prdUdaDtl.setUseYn("1");
				prdUdaDtl.setChk("Y");
				prdUdaDtl.setValidEndDtm("29991231235959");
				prdUdaDtl.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
				prdUdaDtl.setSessionUserId(prdmMain.getSessionUserId());
				prdUdaDtl.setPrdCd(modifyPrdBaseInfoList.getPrdCd());
				int etcinchk = 0;// 상품UDA정보입력
				etcinchk = prdUdaEntity.setUpdatePrdUda(prdUdaDtl);
				if (etcinchk == 0) {
					etcinchk = prdUdaEntity.setInsertPrdUda(prdUdaDtl);
				}						
			}	
		}		

		// 채널정보
		if (addPrdChanlInfoPrd.size() > 0 || modifyPrdChanlInfoPrd.size() > 0 || removePrdChanlInfoPrd.size() > 0) {
			prdChanlEntity.savePrdChanlList(addPrdChanlInfoPrd, modifyPrdChanlInfoPrd,
					removePrdChanlInfoPrd);
		}

		// 상품수치정보
		if (prdNumvalDinfo != null) {
			for (int i = 0; i < prdNumvalDinfo.size(); i++) {
				if (StringUtils.NVL(prdNumvalDinfo.get(i).getGubun()).equals("Y")) {
					prdNumvalDinfo.get(i).setPrdCd(modifyPrdBaseInfoList.getPrdCd());
					int etcinchk = prdEntity.setUpdatePrdNumvalD(prdNumvalDinfo.get(i));
					if (etcinchk == 0) {
						etcinchk = prdEntity.setInsertPrdNumvalD(prdNumvalDinfo.get(i));
					}
				}
			}
		}
		
		/*[직송관리대행] 직송관리대행서비스구축ㅣ 2015.07 패널프로젝트 김효석(hs_style) Start*/
		/* 아래 checkPrdDtrdRelativeValue 메소드로 대체함 2018.08.27 이용문 
		if("Y".equals(modifyPrdBaseInfoList.getDirdlvMngAgncyYn()) || "1".equals(modifyPrdBaseInfoList.getDirdlvMngAgncyYn())){
			if("1".equals(modifyPrdBaseInfoList.getDirdlvMngAgncyYn())) modifyPrdBaseInfoList.setDirdlvMngAgncyYn("Y");
			
			//상품물류확장정보
			DlvsCoCdCond dlvsCoCdCond = new DlvsCoCdCond();
			dlvsCoCdCond.setSupCd(String.valueOf(modifyPrdBaseInfoList.getSupCd()));
			dlvsCoCdCond.setDlvsCoCd(modifyPrdBaseInfoList.getDlvsCoCd());
			dlvsCoCdCond.setPrdRelspAddrCd(modifyPrdBaseInfoList.getPrdRelspAddrCd());
			dlvsCoCdCond.setPrdRetpAddrCd(modifyPrdBaseInfoList.getPrdRetpAddrCd());
			
			EntityDataSet<DSData> dirMngYn = prdEntity.getSupAddrDirdlvMngAgncyYn(dlvsCoCdCond);
			int ckd = Integer.parseInt(dirMngYn.getValues().getString("rtnVal"));
			if(ckd == 0){
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", "직송관리대행 협력사가 아닙니다."); 
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			
			if("Y".equals(modifyPrdBaseInfoList.getApntDlvsImplmYn())){
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", "지정택배수거와 직송관리대행을 동시에 사용하실 수 없습니다.");
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			
			if(modifyPrdBaseInfoList.getBundlDlvCd() == null || "".equals(modifyPrdBaseInfoList.getBundlDlvCd())){
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", "직송관리대행 상품의 묶음배송여부는 필수입니다."); 
				returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
				return returnMap;
			}
			
			if("A01".equals(modifyPrdBaseInfoList.getBundlDlvCd())){
				if(modifyPrdBaseInfoList.getBundlDlvPsblQty() == null || "".equals(modifyPrdBaseInfoList.getBundlDlvPsblQty())){
					returnDsData.put("prdCd", "");
					returnDsData.put("retCd", "-1");
					returnDsData.put("retMsg", "직송관리대행 상품의 묶음배송가능수량은 필수입니다."); 
					returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
					return returnMap;
				}
				
				if(Integer.parseInt(modifyPrdBaseInfoList.getBundlDlvPsblQty()) < 2){
					returnDsData.put("prdCd", "");
					returnDsData.put("retCd", "-1");
					returnDsData.put("retMsg", "묶음배송가능수량은 2개이상 입력 가능합니다."); 
					returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
					return returnMap;
				}
			}else{
				modifyPrdBaseInfoList.setBundlDlvPsblQty("");
			}
		}
		*/
		
		PrdDtrD prdDtrD = new PrdDtrD();
		prdDtrD.setPrdCd(modifyPrdBaseInfoList.getPrdCd());
		prdDtrD.setDlvsCoCd(modifyPrdBaseInfoList.getDlvsCoCd());
		prdDtrD.setDlvPickMthodCd(modifyPrdBaseInfoList.getDlvPickMthodCd());
		prdDtrD.setDirdlvMngAgncyYn(modifyPrdBaseInfoList.getDirdlvMngAgncyYn());
		prdDtrD.setBundlDlvPsblQty(modifyPrdBaseInfoList.getBundlDlvPsblQty());
		prdDtrD.setSessionUserId(modifyPrdBaseInfoList.getSessionUserId());
		
		//상품 물류확장 정보의 validation check
		this.checkPrdDtrdRelativeValue(prdmMain, prdDtrD);
		if (prdmMain.getRetCd().equals("-1")) {
			returnDsData.put("prdCd", "");
			returnDsData.put("retCd", "-1");
			returnDsData.put("retMsg", prdmMain.getRetMsg());
			returnMap.put("outSavePrdBase", EDSFactory.create(PrdCmm.class, returnDsData));
			return returnMap;
		}
		
		prdPrdDtrEntity.modifyPrdPrdDtr(prdDtrD);
		/*[직송관리대행] 직송관리대행서비스구축ㅣ 2015.07 패널프로젝트 김효석(hs_style) End*/

		// 상품 동기화 상품코드 설정
		PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
		pPrdEaiPrdSyncInfo.setPrdCd(modifyPrdBaseInfoList.getPrdCd());
		// 상품 동기화
		if(//modifyPrdBaseInfoList.getPrdCd().toString().length() >= 9 || [HANGBOT-28953_GSITMBO-15549][2022.01.10][김진석]:[상품]SAP로 상품코드가 내려오지 않는 현상
				"20".equals(modifyPrdBaseInfoList.getBundlPrdGbnCd())
				|| "N".equals(changeYn)){
			pPrdEaiPrdSyncInfo.setSapSaleBlockYn("Y");
		}
		
		  wsPrdEaiPrdSyncProcess.prdEaiPrdSyncProcess(pPrdEaiPrdSyncInfo);


		// 상품채널정보 eai 호출
		if (addPrdChanlInfoPrd.size() > 0 || modifyPrdChanlInfoPrd.size() > 0 || removePrdChanlInfoPrd.size() > 0) {
			this.savePrdChanlList(addPrdChanlInfoPrd, modifyPrdChanlInfoPrd, removePrdChanlInfoPrd);
		}

		returnDsData.put("prdCd", "");
		returnDsData.put("retCd", "S");
		returnDsData.put("retMsg", Message.getMessage("prd.esb.msg.001"));
		returnMap.put("outSavePrdBase", EDSFactory.create(EsbCmm.class, returnDsData));

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품판매종료해제를 수정한다.
	 *
	 * </pre>
	 * 정보고시 항목이 없는경우 판매종료 해지 할 수 없도록 변경 2013.03.08 PHS
	 * @author lgcns-213eab4ac
	 * @date 2010-09-15 04:51:39
	 * @param List
	 *            <SaleEndClr> saleEndClrList
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> modifyPrdSaleEndClr(List<SaleEndClr> saleEndClrList) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		PrdQryCond prdQryCond = new PrdQryCond();
		DSData returnDsData = new DSData();
		ExposAprvCond pExposAprvCond = new ExposAprvCond();

		// 상품마스터 판매종료/해제
		for (int i = 0; i < saleEndClrList.size(); i++) {
			SaleEndClr saleEndClr = saleEndClrList.get(i);
			String pOrgSaleEndRsnCd = StringUtils.NVL(saleEndClr.getOrgSaleEndRsnCd());
			//판매종료 해지시 정보고시 건수 조회 건수가 없으면 해지 불가 처리
			pExposAprvCond.setPrdCd(saleEndClr.getPrdCd());
			if("29991231235959".equals(saleEndClr.getSaleEndDtm())) {
				DSData getGovPublsCnt = prdEntity.getGovPublsCnt(pExposAprvCond);

				if (getGovPublsCnt.getInt("cnt") <= 0)  {
					returnDsData.put("retCd", "0");
					returnDsData.put("prdCd",pExposAprvCond.getPrdCd());
					returnDsData.put("retMsg", Message.getMessage("prd.msg.387",new String[] { "판매종료해제"} ));
					returnMap.put("outGetModifyPrdSaleEndClr",EDSFactory.create(PrdCmm.class, returnDsData));
					return returnMap;
				}
				
				//SR02150327087 재고나판매여부 상품의 경우는 반출중인 상품이 존재하면 판매중으로 상태를 수정할 수 없다.
				AttrPrdQryCond pAttrPrdQryCond = new AttrPrdQryCond();
				pAttrPrdQryCond.setAttrPrdCd(saleEndClr.getPrdCd());
				
				//반출진행상태를 확인한다 (값이 조회되면 반출이 진행중)
				EntityDataSet<DSData> eds1 = prdEntity.getPrdTofList(pAttrPrdQryCond);
				PrdQryCond pPrdQryCond = new PrdQryCond();
				pPrdQryCond.setPrdCd( saleEndClr.getPrdCd());
				
				String szStockInSaleYn = "N";
				//재고내판매여부를 확인한다
				EntityDataSet<DSData> eds2 = prdEntity.getStockInSaleYn( pPrdQryCond);
				if(eds2.size() > 0){
					szStockInSaleYn = eds2.getValues().getString("stockInSaleYn");
				}
				//반출이 진행중이고 재고내판매상품일 경우는 오류 전송\
				if(szStockInSaleYn.equals("Y")){							
					if(eds1.size() > 0){
						returnDsData.put("retCd", "0");
						returnDsData.put("prdCd", saleEndClr.getPrdCd() );
						returnDsData.put("retMsg", "이 상품은 재고범위내 판매 상품으로, 현재 반출이 진행 중입니다. 반출이 완료 된 후 종료해제 가능합니다");
						returnMap.put("outGetModifyPrdSaleEndClr", EDSFactory.create(EsbCmm.class, returnDsData));
						return returnMap;
					}
				}
			}
			
			//[PD][2016.08.10][김지혜] : 판매종료/해지 프로세스 개편
			// 34번 코드 33번으로 통합 
			if (!pOrgSaleEndRsnCd.equals("") && pOrgSaleEndRsnCd.equals("33")){				//출고예정일 미입력
				//미출고주문조회 건수가 > 0 return
				prdQryCond.setPrdCd(saleEndClr.getPrdCd());
				prdQryCond.setAttrPrdCd(saleEndClr.getAttrPrdCd());
				DSData ordOverCnt = ecOrdOverEntity.getOrdOverCnt(prdQryCond);
//				DSData shtprdOrdCnt = prdEntity.getShtprdOrdCnt(prdQryCond);		//34번 코드 통합
				if ( ordOverCnt.getInt("cnt") > 0 || ordOverCnt.getInt("shtprdOrdCnt") > 0) {
					returnDsData.put("retCd", "0");
					returnDsData.put("prdCd",saleEndClr.getPrdCd());
					returnDsData.put("retMsg", Message.getMessage("prd.msg.373"));
					returnMap.put("outGetModifyPrdSaleEndClr",EDSFactory.create(PrdCmm.class, returnDsData));
					return returnMap;
				}
			}
			//사유가 34일경우  미출고(출하지시  주문이 있을경우 오류) -> 기존 데이터 잇을 경우때문에 남겨놓음.
			if (!pOrgSaleEndRsnCd.equals("") && pOrgSaleEndRsnCd.equals("34")){				//출고예정일 미준수
				//미출고주문조회 건수가 > 0 return
				prdQryCond.setPrdCd(saleEndClr.getPrdCd());
				prdQryCond.setAttrPrdCd(saleEndClr.getAttrPrdCd());
				DSData ordOverCnt = prdEntity.getShtprdOrdCnt(prdQryCond);
				logger.debug("ordOverCnt=============>"+ordOverCnt);

				if ( ordOverCnt.getInt("cnt") > 0) {
					returnDsData.put("retCd", "0");
					returnDsData.put("retMsg", Message.getMessage("prd.msg.373"));
					returnDsData.put("prdCd",saleEndClr.getPrdCd());
					returnMap.put("outGetModifyPrdSaleEndClr",EDSFactory.create(PrdCmm.class, returnDsData));
					return returnMap;
				}

			}
			//-end
			
			//if (!StringUtils.NVL(saleEndClr.getSaleEndRsnCd()).equals("")) {
				PrdColChgLogQryCond prdColChgLogQryCond = new PrdColChgLogQryCond();
				prdColChgLogQryCond.setPrdCd(saleEndClrList.get(i).getPrdCd());

				PrdColChgLog prdColChgLog = prdColChgLogEntity.getPrdPrdMInfo(prdColChgLogQryCond);

				String orgSaleEndRsnCd = StringUtils.NVL(prdColChgLog.getSaleEndRsnCd());
				String curSaleEndRsnCd = StringUtils.NVL(saleEndClrList.get(i).getSaleEndRsnCd());

				if (!orgSaleEndRsnCd.equals(curSaleEndRsnCd)) {
					saleEndClrList.get(i).setOrgSaleEndRsnCd(orgSaleEndRsnCd);
				}

				// 1) 판매종료로그등록
				prdEndLogEntity.addPrdSaleEndLog(saleEndClrList.get(i));

				// 2) 판매종료관련로그등록 - 2011/08/30 KJH
				if( "G".equals(saleEndClrList.get(i).getPrdTypCd()) ) {

					prdColChgLog.setSessionUserId(saleEndClrList.get(i).getSessionUserId());
					prdColChgLog.setSessionUserIp(saleEndClrList.get(i).getSessionUserIp());

					/* 로깅내용 초기화 */
					prdColChgLog.setChgColNm(null);
					prdColChgLog.setChgBefVal(null);
					prdColChgLog.setChgAftVal(null);

					//판매종료일시
					if(saleEndClrList.get(i).getSaleEndDtm() != null) {
						if (!prdColChgLog.getSaleEndDtm().equals(saleEndClrList.get(i).getSaleEndDtm())) {
							prdColChgLog.setChgColNm("SALE_END_DTM");
							prdColChgLog.setChgAftVal(saleEndClrList.get(i).getSaleEndDtm());

							prdColChgLogEntity.addColChgLog(prdColChgLog);
						}
					}

					//판매종료사유코드
					if (!orgSaleEndRsnCd.equals(curSaleEndRsnCd)) {
						logger.debug("SALE_END_RSN_CD Changed!!");
						prdColChgLog.setChgColNm("SALE_END_RSN_CD");
						prdColChgLog.setChgBefVal(prdColChgLog.getSaleEndRsnCd());
						prdColChgLog.setChgAftVal(saleEndClrList.get(i).getSaleEndRsnCd());

						prdColChgLogEntity.addColChgLog(prdColChgLog);

					}

					//판매종료사유내용
					String orgSaleEndRsnCntnt = StringUtils.NVL(prdColChgLog.getSaleEndRsnCntnt());
					String curSaleEndRsnCntnt = StringUtils.NVL(saleEndClrList.get(i).getSaleEndRsnCntnt());

					if (!orgSaleEndRsnCntnt.equals(curSaleEndRsnCntnt)) {
						logger.debug("SALE_END_RSN_CNTNT Changed!!");
						prdColChgLog.setChgColNm("SALE_END_RSN_CNTNT");
						prdColChgLog.setChgBefVal(prdColChgLog.getSaleEndRsnCntnt());
						prdColChgLog.setChgAftVal(saleEndClrList.get(i).getSaleEndRsnCntnt());

						prdColChgLogEntity.addColChgLog(prdColChgLog);

					}

				}
				//2) -end

				if (StringUtils.NVL(saleEndClr.getSaleEndRsnCd()).equals("32")) { // 2011.03.08 고객서비스불만 사유로 판매종료시 로그생성 (삭제X)
					attrPrdEndInfoEntity.saveAttrPrdSaleEndLog(saleEndClrList.get(i));
				}
			//}
		}
		// 상품에 대한 판매종료는 선행한다.
		returnMap.put("outGetModifyPrdSaleEndClr", prdEntity.modifyPrdSaleEndClr(saleEndClrList));

		//판매종료/해제 eai호출
		for (int i = 0; i < saleEndClrList.size(); i++) {
			SaleEndClr saleEndClr = saleEndClrList.get(i);
			PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
			pPrdEaiPrdSyncInfo.setPrdCd(saleEndClr.getPrdCd());
			if(!"29991231235959".equals(saleEndClr.getSaleEndDtm()) //|| (saleEndClr.getPrdCd().toString().length() >= 9) [HANGBOT-28953_GSITMBO-15549][2022.01.10][김진석]:[상품]SAP로 상품코드가 내려오지 않는 현상 
			){
			    pPrdEaiPrdSyncInfo.setSapSaleBlockYn("Y");
			}else{
				pPrdEaiPrdSyncInfo.setSapSaleBlockYn("N");
			}
			
			PRD_EAI_Res pRD_EAI_Res = new PRD_EAI_Res();
			// 상품 동기화
			pRD_EAI_Res = wsPrdEaiPrdSyncProcess.prdEaiPrdSyncProcess(pPrdEaiPrdSyncInfo);
			logger.debug("wsPrdEaiPrdSyncProcess val=>" + pRD_EAI_Res.getResponseResult());
		}

		if (saleEndClrList.size() > 0) {
			for (int i = 0; i < saleEndClrList.size(); i++) {
				// 속성상품도 같이 판매종료할경우.
				if (StringUtils.NVL(saleEndClrList.get(i).getAttrInclYn()).equals("Y")) {
					// 속성 상품 판매종료 하기 위해 필요한 자료 조회 하여 넘기는 구문 들어가야 함..
					prdQryCond.setPrdCd(saleEndClrList.get(i).getPrdCd());
					EntityDataSet<DSMultiData> attrprdlist = attrPrdEntity.getAttrList(prdQryCond);
					List<SaleEndClr> piAttrPrdSaleEndClr = new ArrayList<SaleEndClr>();

					for (int j = 0; j < attrprdlist.size(); j++) {
						SaleEndClr s = new SaleEndClr();
						s.setAttrPrdCd(attrprdlist.getValues().get(j).getBigDecimal("attrPrdCd"));
						s.setPrdTypCd(attrprdlist.getValues().get(j).getString("prdTypCd"));
						s.setPrdCd(attrprdlist.getValues().get(j).getBigDecimal("prdCd"));
						s.setSaleEndDtm(saleEndClrList.get(i).getSaleEndDtm());
						s.setSaleEndRsnCd(saleEndClrList.get(i).getSaleEndRsnCd());
						s.setSaleEndRsnCntnt(saleEndClrList.get(i).getSaleEndRsnCntnt());
						s.setSessionUserId(saleEndClrList.get(i).getSessionUserId());
						piAttrPrdSaleEndClr.add(s);
					}
					Map<String, EntityDataSet> resultMap1 = attrPrdMngCmmProcess.modifyAttrPrdSaleEndClr(piAttrPrdSaleEndClr);
					returnMap.put("outGetModifySaleEndClr2", resultMap1.get("outGetModifyPrdSaleEndClr"));
				}
			}
		}

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품판매종료해제를 수정한다.
	 * 	if(상품코드가 없고 and 거래종료일이 없는 경우)
	 * 	{판매종료일을 2999-12-31 23:59:59 로 지정한다.}
	 * 	else
	 * 	{판매종료일을 거래종료일로 지정한다.}
	 * 	if(상품결재상태코드가 '30'인 경우)
	 * 	{
	 * 		if(주문가능수량이 0 이거나, null인 경우)
	 * 		{
	 * 	 		if(판매종료사유코드가 null 이면)
	 * 	 			{alert(PRD049)메시지를 보여주고 이벤트를 종료한다.}
	 * 	 	else
	 * 	 		{판매종료유형코드를 '3'으로 지정한다.}
	 * 	 	}
	 * 		if(판매종료일시가 있는 경우)
	 * 		{
	 * 	 		if(판매종료일자가 오늘일자와 같은 경우)
	 * 	 		{
	 * 	 			판매종료사유가 없으면alert(PRD049)메시지를 보여주고 이벤트를 종료한다.
	 * 	 		아니면, 판매종료유형코드를 '4'으로 지정한다.
	 * 	 		}
	 * 	 	}
	 * 	}
	 * 	else
	 * 	{
	 * 		if (판매종료사유가 있는 경우)
	 * 			{ alert(PRD050)메시지를 보여주고 이벤트를 종료한다.}
	 * 	}
	 *
	 * </pre>
	 *
	 * @author lgcns-213eab4ac
	 * @date 2010-09-16 06:41:20
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> modifyPrdSaleEndClr(RSPDataSet dataSet) throws DevEntException {
		logger.debug("modifyPrdSaleEndClr(RSPDataSet dataSet)=1");
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		String key = "inModifyPrdSaleEndClr";
		SaleEndClr pSaleEndClr = dataSet.getDataset4UpdateObjFirst(key, SaleEndClr.class);
		SaleEndClr pSaleEndClrN = dataSet.getDataset4NormalObjFirst("inModifySaleEndClr", SaleEndClr.class);

		logger.debug("pSaleEndClrN=" + pSaleEndClrN);
		// 상품판매종료해제수정
		prdEntity.modifyPrdSaleEndClr(pSaleEndClr);

		// 상품판매종료로그등록
		prdEndLogEntity.addPrdSaleEndLog(pSaleEndClr);

		if (pSaleEndClrN != null) {
			prdEntity.modifyPrdSaleEndClr(pSaleEndClrN);
			// 상품판매종료로그등록
			prdEndLogEntity.addPrdSaleEndLog(pSaleEndClrN);
		}
		return returnMap;
	}

	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> modifyPrdSaleEndClr(SaleEndClr pSaleEndClr) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		logger.debug("modifyPrdSaleEndClr(SaleEndClr pSaleEndClr=1)");
		logger.debug("modifyPrdSaleEndClr(SaleEndClr pSaleEndClr=>)" + pSaleEndClr);

		// 상품판매종료해제수정
		prdEntity.modifyPrdSaleEndClr(pSaleEndClr);

		prdEndLogEntity.addPrdSaleEndLog(pSaleEndClr);

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품결품목록을 수정한다.
	 *      IF (작업구분 = 'Reg') THEN
	 *       판매종료해제.판매종료사유코드 = '30'
	 *       판매종료해제.판매종료일시 = SYSDATE
	 *       상품기본정보.상품코드 = 판매종료해제.상품코드
	 *       상품기본정보.결품등록일시 = 판매종료해제.결품등록일시
	 *      ELSE IF (작업구분 = 'Undo) THEN
	 *       판매종료해제.판매종료사유코드 = ''
	 *       판매종료해제.판매종료일시 = '2999-12-31 23:59:59'
	 *       상품기본정보.상품코드 = 판매종료해제.상품코드
	 *       상품기본정보.결품등록일시 =' '
	 *      END IF
	 *      1.상품판매종료해제를 수정한다.
	 *      2. 상품기본정보를 수정한다.
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2011-01-19 12:24:31
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> modifyPrdShtprd(SaleEndClr saleEndClr) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		// 상품판매종료해제수정
		List<SaleEndClr> saleEndClrList    = new ArrayList<SaleEndClr>();
		List<PrdBaseInfo> pPrdBaseInfoList = new ArrayList<PrdBaseInfo>();

		PrdBaseInfo pPrdBaseInfo = new PrdBaseInfo();
		// session setting
		pPrdBaseInfo.setSessionObject(saleEndClr);

		String today = DateUtils.getToday("yyyyMMddHHmmss");

		if (StringUtils.NVL(saleEndClr.getWorkGbn()).equals("Undo")) {
			// 작업구분 = Undo일 경우
			saleEndClr.setSaleEndRsnCd(""); // 판매종료해제.판매종료사유코드 = ''
			saleEndClr.setSaleEndDtm("29991231235959"); // 판매종료해제.판매종료일 = '2999-12-31 23:59:59'

			pPrdBaseInfo.setPrdCd(saleEndClr.getPrdCd()); // 상품기본정보.상품코드
			pPrdBaseInfo.setShtprdRegDtm(""); // 상품기본정보.결품등록일시

		} else {
			// 작업구분 = Reg이거나 null일 경우
			saleEndClr.setSaleEndRsnCd("1"); // 판매종료해제.판매종료사유코드 = '30' , 위드넷_상품개편에 따른 판종사유 변경 30 -> 1
			saleEndClr.setSaleEndDtm(today); // 판매종료해제.판매종료일 = SYSDATE

			// 결품등록일시가 비어있을 경우 sysdate로 셋팅해준다.
			if (StringUtils.NVL(saleEndClr.getShtprdRegDtm()).equals("")) {
				saleEndClr.setShtprdRegDtm(today);
			}

			pPrdBaseInfo.setPrdCd(saleEndClr.getPrdCd()); // 상품기본정보.상품코드
			pPrdBaseInfo.setShtprdRegDtm(saleEndClr.getShtprdRegDtm()); // 상품기본정보.결품등록일시

			//Reg인 경우 품절등록
			OutstkTgt outstkTgt = new OutstkTgt();
		    if (StringUtils.NVL(saleEndClr.getWorkGbn()).equals("Reg")){

		    	outstkTgt.setOrdItemId(saleEndClr.getOrdItemId()); // 주문아이템Id - ordItemId
				outstkTgt.setStCd("999"); // 상태코드 - stCd
				// session setting
				outstkTgt.setSessionObject(saleEndClr);

		    	this.addPrdOutstk(outstkTgt);
		    }
		}

		saleEndClrList.add(saleEndClr);

		this.modifyPrdSaleEndClr(saleEndClrList);


		// 상품기본정보수정
		pPrdBaseInfoList.add(pPrdBaseInfo);

		DSMultiData modifyYnList = prdEntity.getModifyYn(pPrdBaseInfo);   // 세트상품 구성여부 조회.

		if(modifyYnList.size() > 0){
			throw new DevPrcException(pPrdBaseInfo.getPrdCd()+"는 세트상품구성품입니다. 유료배송여부, 카드사용제한, 무형상품유형코드, 당사상품권여부 확인 바랍니다.");
		}

		prdEntity.modifyPrdBaseInfo(pPrdBaseInfoList);

		return returnMap;
	}

	/**
	 * <pre>
	 * 탭1 판매종료 수정
	 * desc : 판매종료해제를 내역을 수정하고, 변경로그에 등록한다.
	 * if (판매종료일시가 비어 있으면) then
	 * {alert(PRD048)메시지를 보여주고 이벤트를 종료한다.}
	 * 상품유형코드에 따라서 상품판매종료해제수정 또는 속성상품판매종료해제수정을 수행한다.
	 * 판매종료인경우 판매종료해제구분에 '1'을 종료해제인경우 판매종료해제구분에 '2'를 지정한다.
	 *
	 * </pre>
	 *
	 * @author lgcns-213eab4ac
	 * @date 2010-09-16 06:41:21
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> modifySaleEndClrtoMip(List<SaleEndClr> saleEndClrList) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		List<SaleEndClr> piPrdSaleEndClr = new ArrayList<SaleEndClr>();
		List<SaleEndClr> piAttrPrdSaleEndClr = new ArrayList<SaleEndClr>();
		ScmMd scmMd = new ScmMd();
		DSData returnDsData = new DSData();
		
		//현재 시간
		Date date = SysUtil.getCurrTime();
		String sysdate = DateUtils.format(date, "HHmmss");
		
		sysdate = sysdate.substring(0, 2) + "시" + sysdate.substring(2, 4) + "분" + sysdate.substring(4, 6) + "초";
		
		
		for (SaleEndClr itm : saleEndClrList) {
			// 상품에 대하여 판매종료해제 하는경우  G = 대표상품, 아니면  속성상품 ( SG, SP, SS )

			logger.debug("itm.getPrdTypCd()>>>>"+itm.getPrdTypCd());

			if ("G".equals(itm.getPrdTypCd())) {
				piPrdSaleEndClr.add(itm);
			}else {
				piAttrPrdSaleEndClr.add(itm);
			}
		}
		//협력사의 패널티 등급을 조회한다.
		for (int  i= 0; i < piPrdSaleEndClr.size();i++){
			/*협력사코드 없으면 확인 하지 않음 20110614 김주영*/
			if (piPrdSaleEndClr.get(i).getSupCd() != null && piPrdSaleEndClr.get(i).getSupCd().toString().length() > 0){
				scmMd.setSupCd(piPrdSaleEndClr.get(i).getSupCd());
				scmMd.setMdId(piPrdSaleEndClr.get(i).getMdId());
				scmMd.setPrdCd(piPrdSaleEndClr.get(i).getPrdCd());//[PD-2015-007] 정동국 2015-05-12 : 협력사패널티조회 수정
				scmMd.setGradeSt("S");
				String sSaleEndPrsnId   = StringUtils.NVL(piPrdSaleEndClr.get(i).getSaleEndPrsnId());
				String sOrgSaleEndRsnCd = StringUtils.NVL(piPrdSaleEndClr.get(i).getOrgSaleEndRsnCd());
				//판매종료해지일경우에만
				if("29991231235959".equals(piPrdSaleEndClr.get(i).getSaleEndDtm())){
					//이전 판매종료사유가 30( EC/DM 결품등록으로인한 판매중지)33(출고예정일 미입력)34(출고예정일 미준수)
					//이고 판매종료자가 BATCH,ecsys일경우는 협력사의패널티 등급을 조회해서 판매종료해지를 할수 없도록 처리
					if ((sSaleEndPrsnId.equals("BATCH") ||sSaleEndPrsnId.equals("ecsys") ) &&
					   ( sOrgSaleEndRsnCd.equals("30") || sOrgSaleEndRsnCd.equals("33") || sOrgSaleEndRsnCd.equals("34"))){
						//DSData outstkOrdRegCnt = ecScmMngEntity.getGsSuppGradeCnt(scmMd);
						DSData outstkOrdRegCnt = supPenltQryEntity.getSupPenltGradeCnt(scmMd);//[PD-2015-007] 정동국 2015-05-12 : 협력사패널티조회 수정
		                logger.debug("outstkOrdRegCnt1======>"+outstkOrdRegCnt);
						if (outstkOrdRegCnt.getInt("cnt")  > 0){
							returnDsData.put("retCd", "E");
							returnDsData.put("prdCd", piPrdSaleEndClr.get(i).getPrdCd() );
							returnDsData.put("retMsg", Message.getMessage("prd.msg.380"));
							returnMap.put("outmodifySaleEnd", EDSFactory.create(EsbCmm.class, returnDsData));
							return returnMap;
						}
					}
					
					//[SR02140603070][2014.07.30][김지혜] : [보증보험 미처리 대상 협력사 - 판매종료 해제 불가능]
					if ("45".equals( sOrgSaleEndRsnCd) ) {
						DSData getSupGuarntInsuNoProcYn = prdEntity.getSupGuarntInsuNoProcYn(piPrdSaleEndClr.get(i)).getValues();
						if( getSupGuarntInsuNoProcYn != null && "Y".equals(getSupGuarntInsuNoProcYn.getString("noProcSupYn"))) {
							returnDsData.put("retCd", "E");
							returnDsData.put("prdCd", piPrdSaleEndClr.get(i).getPrdCd() );
							returnDsData.put("retMsg", "협력사가 보증보험 미처리 협력사일 경우 \n판매종료해제할 수 없습니다.");
							
							//협력사가 보증보험 미처리 협력사일 경우 MD팀장 합격 처리할 수 없습니다.
							returnMap.put("outmodifySaleEnd", EDSFactory.create(EsbCmm.class, returnDsData));
							return returnMap;
						}
					}
				}
				//경고장과 라이센스로 인한 판매종료해제는 권한자만 가능하다.[SR02140425037] 2014.05.22
				if ("43".equals( sOrgSaleEndRsnCd) ||  "44".equals( sOrgSaleEndRsnCd)) {
					if (!"Y".equals(StringUtils.NVL(piPrdSaleEndClr.get(i).getLicensWarnAuthYn()) ) ) {
						
						returnDsData.put("retCd", "E");
						returnDsData.put("prdCd", piPrdSaleEndClr.get(i).getPrdCd() );
						returnDsData.put("retMsg", "라이센스 및 경고장으로 인한 판매종료해제는 \n"+
						                                 "라이센스/경고장 판매종료 관리화면에서 권한자만 가능합니다.");
						returnMap.put("outmodifySaleEnd", EDSFactory.create(EsbCmm.class, returnDsData));
						return returnMap;					
					}
				}	
			}
		
			if("29991231235959".equals(piPrdSaleEndClr.get(i).getSaleEndDtm())){
				//SR02150327087 재고나판매여부 상품의 경우는 반출중인 상품이 존재하면 판매중으로 상태를 수정할 수 없다.
				AttrPrdQryCond pAttrPrdQryCond = new AttrPrdQryCond();
				pAttrPrdQryCond.setPrdCd(piPrdSaleEndClr.get(i).getPrdCd());
				
				//반출진행상태를 확인한다 (값이 조회되면 반출이 진행중)
				EntityDataSet<DSData> eds1 = prdEntity.getPrdTofList(pAttrPrdQryCond);
				
				PrdQryCond pPrdQryCond = new PrdQryCond();
				pPrdQryCond.setPrdCd( piPrdSaleEndClr.get(i).getPrdCd());
				
				String szStockInSaleYn = "N";
				//재고내판매여부를 확인한다
				EntityDataSet<DSData> eds2 = prdEntity.getStockInSaleYn( pPrdQryCond);
				
				if(eds2.size() > 0){
					szStockInSaleYn = eds2.getValues().getString("stockInSaleYn");
				}
				//반출이 진행중이고 재고내판매상품일 경우는 오류 전송\
				if(szStockInSaleYn.equals("Y")){							
					if(eds1.size() > 0){
						returnDsData.put("retCd", "E");
						returnDsData.put("prdCd", piPrdSaleEndClr.get(i).getPrdCd() );
						returnDsData.put("retMsg", "이 상품은 재고범위내 판매 상품으로, 현재 반출이 진행 중입니다. 반출이 완료 된 후 종료해제 가능합니다");
						returnMap.put("outmodifySaleEnd", EDSFactory.create(EsbCmm.class, returnDsData));
						return returnMap;
					}
				}
			}
		}

		//협력사의 패널티 등급을 조회한다.(속성 했을경우)
		for (int  i= 0; i < piAttrPrdSaleEndClr.size();i++){
			/*협력사코드 없으면 확인 하지 않음 20110614 김주영*/
			if (piAttrPrdSaleEndClr.get(i).getSupCd() != null && piAttrPrdSaleEndClr.get(i).getSupCd().toString().length() > 0){
				scmMd.setSupCd(piAttrPrdSaleEndClr.get(i).getSupCd());
				scmMd.setMdId(piAttrPrdSaleEndClr.get(i).getMdId());
				scmMd.setPrdCd(piAttrPrdSaleEndClr.get(i).getPrdCd());//[PD-2015-007] 정동국 2015-05-12 : 협력사패널티조회 수정   
				scmMd.setGradeSt("S");
				String sSaleEndPrsnId   = StringUtils.NVL(piAttrPrdSaleEndClr.get(i).getSaleEndPrsnId());
				String sOrgSaleEndRsnCd = StringUtils.NVL(piAttrPrdSaleEndClr.get(i).getOrgSaleEndRsnCd());
				//판매종료해지일경우에만
				if("29991231235959".equals(piAttrPrdSaleEndClr.get(i).getSaleEndDtm())){
					if ((sSaleEndPrsnId.equals("BATCH") ||sSaleEndPrsnId.equals("ecsys") ) &&
					   ( sOrgSaleEndRsnCd.equals("30") || sOrgSaleEndRsnCd.equals("33") || sOrgSaleEndRsnCd.equals("34"))){
						//DSData outstkOrdRegCnt = ecScmMngEntity.getGsSuppGradeCnt(scmMd);
						DSData outstkOrdRegCnt = supPenltQryEntity.getSupPenltGradeCnt(scmMd);//[PD-2015-007] 정동국 2015-05-12 : 협력사패널티조회 수정
		                logger.debug("outstkOrdRegCnt2======>"+outstkOrdRegCnt);
						if (outstkOrdRegCnt.getInt("cnt")  > 0){
							returnDsData.put("retCd", "E");
							returnDsData.put("prdCd", piAttrPrdSaleEndClr.get(i).getPrdCd() );
							returnDsData.put("retMsg", Message.getMessage("prd.msg.380"));
							returnMap.put("outmodifySaleEnd", EDSFactory.create(EsbCmm.class, returnDsData));
							return returnMap;
						}
					}
					
					//경고장과 라이센스로 인한 판매종료해제는 권한자만 가능하다.[SR02140425037] 2014.05.22
					if ("43".equals( sOrgSaleEndRsnCd) ||  "44".equals( sOrgSaleEndRsnCd)) {
						if (!"Y".equals(StringUtils.NVL(piPrdSaleEndClr.get(i).getLicensWarnAuthYn()) ) ) {
							
							returnDsData.put("retCd", "E");
							returnDsData.put("prdCd", piPrdSaleEndClr.get(i).getPrdCd() );
							returnDsData.put("retMsg", "라이센스 및 경고장으로 인한 판매종료해제는 "+
							                                 "라이센스/경고장 판매종료 관리화면에서 권한자만 가능합니다.");
							returnMap.put("outmodifySaleEnd", EDSFactory.create(EsbCmm.class, returnDsData));
							return returnMap;					
						}
					}	
					
					//[SR02140603070][2014.07.30][김지혜] : [보증보험 미처리 대상 협력사 - 판매종료 해제 불가능]
					if ("45".equals( sOrgSaleEndRsnCd) ) {
						DSData getSupGuarntInsuNoProcYn = prdEntity.getSupGuarntInsuNoProcYn(piAttrPrdSaleEndClr.get(i)).getValues();
						if( getSupGuarntInsuNoProcYn != null && "Y".equals(getSupGuarntInsuNoProcYn.getString("noProcSupYn"))) {
							returnDsData.put("retCd", "E");
							returnDsData.put("prdCd", piAttrPrdSaleEndClr.get(i).getPrdCd() );
							returnDsData.put("retMsg", "협력사가 보증보험 미처리 협력사일 경우 \n판매종료해제할 수 없습니다.");
							
							//협력사가 보증보험 미처리 협력사일 경우 MD팀장 합격 처리할 수 없습니다.
							returnMap.put("outmodifySaleEnd", EDSFactory.create(EsbCmm.class, returnDsData));
							return returnMap;
						}
					}
				}
			}
			if("29991231235959".equals(piAttrPrdSaleEndClr.get(i).getSaleEndDtm())){
				//SR02150327087 재고나판매여부 상품의 경우는 반출중인 상품이 존재하면 판매중으로 상태를 수정할 수 없다.
				AttrPrdQryCond pAttrPrdQryCond = new AttrPrdQryCond();
				pAttrPrdQryCond.setAttrPrdCd(piAttrPrdSaleEndClr.get(i).getAttrPrdCd());
				
				//반출진행상태를 확인한다 (값이 조회되면 반출이 진행중)
				EntityDataSet<DSData> eds1 = prdEntity.getPrdTofList(pAttrPrdQryCond);
				PrdQryCond pPrdQryCond = new PrdQryCond();
				pPrdQryCond.setPrdCd( piAttrPrdSaleEndClr.get(i).getPrdCd());
				
				String szStockInSaleYn = "N";
				//재고내판매여부를 확인한다
				EntityDataSet<DSData> eds2 = prdEntity.getStockInSaleYn( pPrdQryCond);
				if(eds2.size() > 0){
					szStockInSaleYn = eds2.getValues().getString("stockInSaleYn");
				}
				//반출이 진행중이고 재고내판매상품일 경우는 오류 전송\
				if(szStockInSaleYn.equals("Y")){							
					if(eds1.size() > 0){
						returnDsData.put("retCd", "E");
						returnDsData.put("prdCd", piAttrPrdSaleEndClr.get(i).getAttrPrdCd() );
						returnDsData.put("retMsg", "이 상품은 재고범위내 판매 상품으로, 현재 반출이 진행 중입니다. 반출이 완료 된 후 종료해제 가능합니다");
						returnMap.put("outmodifySaleEnd", EDSFactory.create(EsbCmm.class, returnDsData));
						return returnMap;
					}
				}
			}
		}

		returnDsData.put("prdCd", "");
		returnDsData.put("retCd", "");
		returnDsData.put("retMsg", " 완료하였습니다.");
		// 속성상품 판매/종료 호출
		if (piAttrPrdSaleEndClr.size() > 0) {
			logger.debug("modifySaleEndClrtoMip =>2-1" + piAttrPrdSaleEndClr);
			Map<String, EntityDataSet> resultMap = attrPrdMngCmmProcess.modifyAttrPrdSaleEndClr(piAttrPrdSaleEndClr);
			DSData reuslt = (DSData) resultMap.get("outGetModifyPrdSaleEndClr").getValues();
			String pRetCd = StringUtils.NVL(reuslt.getString("retCd"));

            if ( pRetCd.equals("0")){
            	returnDsData.put("retCd", pRetCd);
        		returnDsData.put("retMsg", reuslt.getString("retMsg"));
            }

            boolean masterWithProc = false;

            for (SaleEndClr piAttrPrdSaleEndClr2 : piAttrPrdSaleEndClr){
            	//2012/02/01 속성상품이 전부 판매종료 혹은 품절되었을 때 상품의 상태를 마지막 속성상품의 판매종료 혹은 품절 상태로 수정
                //판매종료, 일시품절 되지 않은 속성상품 수를 가져오는 쿼리
                //[SR02160614077][2016.06.13][김영현]:상품판매상태 변경 EAI 과다 호출 로직 개선 및 배송/수거 방어 제한 로직 추가
            	//상품 상태 변경 대상에 중복 Add 되지 않도록 체크 로직 추가
            	boolean bAddPrdCdProc = true;
				for(int i = 0; i<piPrdSaleEndClr.size(); i++){
					if(piPrdSaleEndClr.get(i).getPrdCd().compareTo(piAttrPrdSaleEndClr2.getPrdCd()) == 0){
						bAddPrdCdProc = false;
						break;
					}
				}
				if(bAddPrdCdProc){
	            	//상품 상태 변경 대상에 중복 Add 되지 않도록 제한 로직 추가
					if(attrPrdEntity.getSaleEndClrCnt(piAttrPrdSaleEndClr2) == 0){	
						piPrdSaleEndClr.add(piAttrPrdSaleEndClr2);						
						masterWithProc = true;
					}
				}
            }

			logger.debug("modifySaleEndClrtoMip =>2-2" + piPrdSaleEndClr);
			returnMap.put("outGetModifySaleEndClr1", resultMap.get("outGetModifyPrdSaleEndClr"));
			logger.debug("modifySaleEndClrtoMip =>2-3" + returnDsData);

			

        	PrdmMain prdExposStCond = new PrdmMain();
    		prdExposStCond.setPrdCd(piAttrPrdSaleEndClr.get(0).getPrdCd());

            DSData saleEndClrTempoutClrCnt = attrPrdEntity.getSaleEndClrTempoutClrCnt(prdExposStCond);
            logger.debug("종료,품절되지 않은 속성갯수>>>>>"+saleEndClrTempoutClrCnt.getInt("attrCnt"));

            if( !"29991231235959".equals(piAttrPrdSaleEndClr.get(0).getSaleEndDtm()) ) {
            	//판매종료할 경우
                if( saleEndClrTempoutClrCnt.getInt("attrCnt") == 0 ) {
                	//전부 판매종료 혹은 일시품절이 되었다는 의미 -> 상품전체도 동일하게 수정
                	if( !masterWithProc ) {
                		piPrdSaleEndClr.add(piAttrPrdSaleEndClr.get(0));
                	}
                }

            } else {
            	//판매종료해제할 경우
            	if( saleEndClrTempoutClrCnt.getInt("attrCnt") != 0 ) {
            		//판매종료와 일시품절이 동시에 되지 않는 속성상품이 있다는 이야기
            		if( !masterWithProc && !"29991231235959".equals(saleEndClrTempoutClrCnt.getString("saleEndDtm")) ) {
            			piPrdSaleEndClr.add(piAttrPrdSaleEndClr.get(0));
            		}

            		// 일시품절해제도 시켜주기
            		if( "Y".equals(saleEndClrTempoutClrCnt.getString("tempoutYn")) ) {
            			List<Tempout> pTempoutPrdList = new ArrayList<Tempout>();
            			Tempout pTempoutPrdInfo = new Tempout();
            			pTempoutPrdInfo.setPrdCd(piAttrPrdSaleEndClr.get(0).getPrdCd());
            			pTempoutPrdInfo.setTempoutYn("N");
            			pTempoutPrdInfo.setSessionObject(piAttrPrdSaleEndClr.get(0));
            			pTempoutPrdList.add(pTempoutPrdInfo);

            			prdEntity.modifyPrdTempoutClrList(pTempoutPrdList);

            			/* DM외 주문수량은 받지 않기로 함
            			OrdPsblQty pOrdPsblQty = new OrdPsblQty();
        				pOrdPsblQty.setPrdCd(piAttrPrdSaleEndClr.get(0).getPrdCd());
        				pOrdPsblQty.setChanlGrpCd("AZ"); // dm 외
        				pOrdPsblQty.setChanlCd("P");
        				pOrdPsblQty.setOrdPsblQty(new BigDecimal(0));  //default값 정하기
        				pOrdPsblQty.setSessionUserId(piAttrPrdSaleEndClr.get(0).getSessionUserId());

        				// SAP 전송 주문가능 수량 로그
        				ordPsblQtyEntity.savePrdOrdPsblQtyLog(pOrdPsblQty);

        				ordPsblQtyEntity.modifyOrdPsblQtySimpl(pOrdPsblQty);

        				//20110504  추가 - 대표상품코드 필요
        				// 주문가능수량임시정보 --> 주문가능수량임시로그
        				ordPsblQtyEntity.insertPrdOrdPsblTmpL(pOrdPsblQty);
        			    // 주문가능수량임시정보 삭제
        	        	ordPsblQtyEntity.deletePrdOrdPsblTmpD(pOrdPsblQty);
        				*/
            		}

            	}
            }
            //-2012/02/01 end
		}

		// 대표상품 판매종료/해제 호출
		if (piPrdSaleEndClr.size() > 0) {
			Map<String, EntityDataSet> resultMap = modifyPrdSaleEndClr(piPrdSaleEndClr);
			DSData reuslt = (DSData) resultMap.get("outGetModifyPrdSaleEndClr").getValues();
			String pRetCd = StringUtils.NVL(reuslt.getString("retCd"));

            if ( pRetCd.equals("0")){
            	returnDsData.put("retCd", pRetCd);
            	returnDsData.put("prdCd", reuslt.getString("prdCd"));
        		returnDsData.put("retMsg", reuslt.getString("retMsg"));
            }
			returnMap.put("outGetModifySaleEndClr1", resultMap.get("outGetModifyPrdSaleEndClr"));
		}

		returnMap.put("outmodifySaleEnd", EDSFactory.create(EsbCmm.class, returnDsData));
		
		
		// [SR02190919794] 판매량급증 대상 추가 및 판매가능 재개 시 문자 발송 : 2019.09.26 황지현
		if ("".equals(returnDsData.getString("retCd")) && saleEndClrList != null && saleEndClrList.size() > 0) {
			SaleEndClr saleEndClr = saleEndClrList.get(0);
			
			if ("29991231235959".equals(saleEndClr.getSaleEndDtm()) && "56".equals(saleEndClr.getOrgSaleEndRsnCd())) {	
				
				if (saleEndClr.getMdId() != null && !"".equals(saleEndClr.getMdId()) && saleEndClr.getSalePrc() != null) {
				
					DecimalFormat df = new DecimalFormat("#,###");				
					String salePrc = df.format(saleEndClr.getSalePrc());			
					
					StringBuffer msgCntnt = new StringBuffer();
					
					msgCntnt.append("[판매량급증 상품 판매재개]\n");
					msgCntnt.append(saleEndClr.getPrdNm() + "\n");
					msgCntnt.append("상품이 " + sysdate + " 담당자 '" + saleEndClr.getSessionUserNm() + "'에 의해\n");
					msgCntnt.append("판매재개 되었습니다.\n");
					msgCntnt.append("현재 가격 " + salePrc + "원\n");
					msgCntnt.append("http://m.gsshop.com/prd/prd.gs?prdid="+saleEndClr.getPrdCd());				
					
					prdEntity.addPrdSaleSudrisSms(msgCntnt.toString(), saleEndClr.getMdId());	
				}
			}
		}			

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 일시품절 또는 품절해제 한다.
	 *       IF (속성상품코드가 없으면 OR 상품유형코드 &lt;&gt; 'S' OR 전체수정여부 = 'Y') THEN
	 *        (일시품절변경일시 = SYSDATE)
	 *        1. 상품일시품절해제목록수정한다
	 *        IF (상품유형코드 &lt;&gt; 'S' OR 전체수정여부 = 'Y') THEN
	 *        (일시품절일시 = SYSDATE)
	 *        2.일시품절해제목록수정한다.
	 *        FOR (i=1, 요청건수)
	 *        3.속성상품주문가능수량조회를 수행한다.
	 *       IF (수정경로 = 'M' OR 주문가능수량 = 0) THEN
	 *        4. 주문가능수량수정을 수행한다.
	 *       END IF
	 *       END LOOP
	 *        END IF
	 *       ELSE
	 *        (일시품절일시 = SYSDATE)
	 *        1.일시품절해제목록수정한다.
	 *        FOR (i=1, 요청건수)
	 *        2.속성상품주문가능수량조회를 수행한다.
	 *       IF (수정경로 = 'M' OR 주문가능수량 = 0) THEN
	 *        3. 주문가능수량수정을 수행한다.
	 *       END IF
	 *       END LOOP
	 *       END IF
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2011-01-09 09:11:08
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> modifyTempoutClrList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		String key = "inModifytempoutClrtoMip";

		List<Tempout> pTempout = dataSet.getDataset4NormalObj(key, Tempout.class);
		List<Tempout> pTempoutPrd = new ArrayList<Tempout>();
		List<Tempout> pTempoutAttr = new ArrayList<Tempout>();
		DSData returnDsData = new DSData();

		returnDsData.put("prdCd", "");
		returnDsData.put("retCd", "");
		returnDsData.put("retMsg", " 완료하였습니다.");

		//2012/01/13 상품코드, 속성상품코드, 속성대표상품코드 모두 안들어오는 경우가 있을 경우 error return
		for(Tempout pTempoutInfo : pTempout){
			if( pTempoutInfo.getPrdCd() == null && pTempoutInfo.getAttrPrdCd() == null && "".equals(StringUtils.NVL(pTempoutInfo.getAttrPrdRepCd())) ){
				returnDsData.put("prdCd", "");
				returnDsData.put("retCd", "E");
				returnDsData.put("retMsg", Message.getMessage("prd.eai.msg.027"));
				//prd.eai.msg.027=필수 항목이 없습니다.
				returnMap.put("outModifytempoutClrtoMip", EDSFactory.create(EsbCmm.class, returnDsData));
				return returnMap;
			}
		}

		// if(속성상품코드가 없으면 OR 상품유형코드 <> 'S' OR 전체수정여부 = 'Y') {
		// 상품일시품절해제목록수정
		for (Tempout itm : pTempout) {
			// 상품에 대하여 판매종료해제 하는경우 S = 속성상품, 나머지 = 상품
			if (!itm.getPrdTypCd().equals("S")) {
				pTempoutPrd.add(itm);
			}else{
				pTempoutAttr.add(itm);
			}
		}

		boolean masterWithProc = false;
		boolean skipSkuPrdQty  = false; // [SKU] SKU상품의 경우 속성상품 품절해제 시 상품 전체의 주문가능수량 변경을 태우지 않는다.
		
		ExposAprvCond pExposAprvCond = new ExposAprvCond();
		//속성상품 일시품절,해제
		if (pTempoutAttr.size() > 0) {
			prdStockInfoEntity.modifyTempoutClrList(pTempoutAttr);
			for (Tempout tempout : pTempoutAttr) {
				OrdPsblQty pOrdPsblQty = new OrdPsblQty();
				pOrdPsblQty.setPrdCd(tempout.getPrdCd());
				pOrdPsblQty.setAttrPrdRepCd(tempout.getAttrPrdRepCd());
				pOrdPsblQty.setChanlGrpCd("AZ"); // dm 외
				pOrdPsblQty.setChanlCd("P");

				//SR02150327087
				String szStockInSaleYn = "N";

				/* SKU PROJECT - 주문가능수량 수정. 2021.04.02 by kan.yp */
				String skuYn = "N";
				
				PrdQryCond pPrdQryCond = new PrdQryCond();
				pPrdQryCond.setPrdCd( tempout.getPrdCd());
				
				EntityDataSet<DSData> eds = prdEntity.getStockInSaleYn( pPrdQryCond);
				if(eds.size() > 0){
					szStockInSaleYn = eds.getValues().getString("stockInSaleYn");
					skuYn = eds.getValues().getString("skuYn");
				}
				
				if(!"Y".equals(szStockInSaleYn)){
					if ("Y".equals(tempout.getTempoutYn())) {   //일시품절인경우 주문가능수량 0처리
						pOrdPsblQty.setOrdPsblQty(new BigDecimal(0));
					} else {
						pOrdPsblQty.setOrdPsblQty(tempout.getOrdPsblQty());
					}
					pOrdPsblQty.setSessionUserId(tempout.getSessionUserId());
					
					// SKU의 경우 임시품절 해제가 아닌경우 PRD_ORD_PSBL_TMP_D의 데이터를 추가해야 한다(PRD_ORD_PSBL_TMP_D 삭제 안함) 
					if (    "Y".equals(skuYn)
						 &&  pOrdPsblQty.getOrdPsblQty() != null
						 && (pOrdPsblQty.getOrdPsblQty().compareTo(BigDecimal.ZERO) > 0) ) {

						// 주문가능수량조회
						EntityDataSet<DSData> getOrdPsblQty = ordPsblQtyEntity.getSaleQty(pOrdPsblQty);
						
						pOrdPsblQty.setSaleQty(getOrdPsblQty.getValues().getBigDecimal("saleQty")); // 판매수량
					}
					
					// SAP 전송 주문가능 수량 로그
					ordPsblQtyEntity.savePrdOrdPsblQtyLog(pOrdPsblQty);
					
					ordPsblQtyEntity.modifyOrdPsblQtySimpl(pOrdPsblQty);

					//20110504  추가 -
					// 주문가능수량로그등록
					ordPsblQtyEntity.addOrdPsblQtyLog(pOrdPsblQty);

					if ( !"Y".equals(skuYn) ) { 
						
						// 주문가능수량임시정보 --> 주문가능수량임시로그
						ordPsblQtyEntity.insertPrdOrdPsblTmpL(pOrdPsblQty);
						// 주문가능수량임시정보 삭제
						ordPsblQtyEntity.deletePrdOrdPsblTmpD(pOrdPsblQty);
					}
				}

	        	//속성이 전체다 일시품절되거나 하나라도 품절해제되는경우 상품도 함께 처리
	        	int TempOutCnt = prdStockInfoEntity.getTempOutCnt(tempout);
				if(TempOutCnt == 0){
					pTempoutPrd.add(tempout);
					masterWithProc = true;
					
					if ( "Y".equals(skuYn) ) {
						skipSkuPrdQty = true; // [SKU] 
					}
				}
			}

			//2012/02/01 속성상품이 전부 판매종료 혹은 품절되었을 때 상품의 상태를 마지막 속성상품의 판매종료 혹은 품절 상태로 수정
            //판매종료, 일시품절 되지 않은 속성상품 수를 가져오는 쿼리
			PrdmMain prdExposStCond = new PrdmMain();
    		prdExposStCond.setPrdCd(pTempoutAttr.get(0).getPrdCd());

			DSData saleEndClrTempoutClrCnt = attrPrdEntity.getSaleEndClrTempoutClrCnt(prdExposStCond);
            logger.debug("종료,품절되지 않은 속성갯수>>>>>"+saleEndClrTempoutClrCnt.getInt("attrCnt"));

			if( "Y".equals(pTempoutAttr.get(0).getTempoutYn()) ) {
				//일시품절일 경우
				if( saleEndClrTempoutClrCnt.getInt("attrCnt") == 0 ) {
					if( !masterWithProc ) {
						//전부 판매종료 혹은 일시품절이 되었다는 의미 -> 상품전체도 동일하게 수정
						pTempoutPrd.add(pTempoutAttr.get(0));
					}
                }

			} else {
				//일시품절해제일 경우
				if( saleEndClrTempoutClrCnt.getInt("attrCnt") != 0 ) {
            		//판매종료와 일시품절이 동시에 되지 않는 속성상품이 있다는 이야기
					//-> 상품마스터 일시품절 해제
					if( !masterWithProc && "Y".equals(saleEndClrTempoutClrCnt.getString("tempoutYn")) ) {
						pTempoutPrd.add(pTempoutAttr.get(0));
					}

					//판매종료해제
					if( !"29991231235959".equals(saleEndClrTempoutClrCnt.getString("saleEndDtm")) ) {
						// 대표상품 판매종료해제 호출
						List<SaleEndClr> piPrdSaleEndClrList = new ArrayList<SaleEndClr>();
						SaleEndClr saleEndClr = new SaleEndClr();

						saleEndClr.setPrdCd(pTempoutAttr.get(0).getPrdCd());
						saleEndClr.setOrgSaleEndRsnCd(saleEndClrTempoutClrCnt.getString("saleEndRsnCd"));
						saleEndClr.setSaleEndDtm("29991231235959");
						saleEndClr.setPrdTypCd("G");
						saleEndClr.setAttrInclYn("N");		//속성상품 같이 해제 여부 : 'N'
						saleEndClr.setSessionObject(pTempoutAttr.get(0));

						piPrdSaleEndClrList.add(saleEndClr);

						Map<String, EntityDataSet> resultMap = modifyPrdSaleEndClr(piPrdSaleEndClrList);
						DSData reuslt = (DSData) resultMap.get("outGetModifyPrdSaleEndClr").getValues();
						String pRetCd = StringUtils.NVL(reuslt.getString("retCd"));

			            if ( pRetCd.equals("0")){
			            	returnDsData.put("retCd", pRetCd);
			        		returnDsData.put("retMsg", reuslt.getString("retMsg"));
			        		returnMap.put("outModifytempoutClrtoMip", resultMap.get("outGetModifyPrdSaleEndClr"));

			        		return returnMap;
			            }
					}
            	}

			}

			masterWithProc = false;
            //-2012/02/01 end

		}

//		int  errCnt = 0;	//정보고시 오류 건수
		//대표상품 일시품절,해제
		if (pTempoutPrd.size() > 0) {
			//prdEntity.modifyPrdTempoutClrList(pTempoutPrd);

			for (int i = 0; i < pTempoutPrd.size(); i++) {
				//일시품절 해제 인경우 정보고시항목 체크하여 없는겨우 해제 할수 없도록 처리 2013.03.08 phs
				//정보고시 건수 조회 건수가 없으면 해지 불가 처리
				if ("N".equals(pTempoutPrd.get(i).getTempoutYn())) {
					pExposAprvCond.setPrdCd(pTempoutPrd.get(i).getPrdCd());
					DSData getGovPublsCnt = prdEntity.getGovPublsCnt(pExposAprvCond);

					if (getGovPublsCnt.getInt("cnt") <= 0)  {
						returnDsData.put("retCd", "0");
						returnDsData.put("prdCd",pExposAprvCond.getPrdCd());
		        		returnDsData.put("retMsg", Message.getMessage("prd.msg.387",new String[] { "일시품절해제"} ));
		        		returnMap.put("outModifytempoutClrtoMip", EDSFactory.create(EsbCmm.class, returnDsData));
		        		logger.debug("returnDsData=>"+returnDsData);
		        		return returnMap;

					}
				}
				logger.debug("returnDsData2=>"+returnDsData);
				List<Tempout> pTempoutPrdList = new ArrayList<Tempout>();
				pTempoutPrdList.add(pTempoutPrd.get(i));
				prdEntity.modifyPrdTempoutClrList(pTempoutPrdList);
				// 속성상품도 같이 일시품절/해제 할 경우.
				if (StringUtils.NVL(pTempoutPrd.get(i).getWhlModYn()).equals("Y")) {
					List<Tempout> pTempoutAttr2 = new ArrayList<Tempout>();
					pTempoutAttr2.add(pTempoutPrd.get(i));
					prdStockInfoEntity.modifyTempoutClrList(pTempoutAttr2); //속성상품 일시품절,해제
				}

				OrdPsblQty pOrdPsblQty = new OrdPsblQty();
				pOrdPsblQty.setPrdCd(pTempoutPrd.get(i).getPrdCd());
				pOrdPsblQty.setWhlModYn("Y");
				pOrdPsblQty.setChanlGrpCd("AZ"); // dm 외
				pOrdPsblQty.setChanlCd("P");

				//SR02150327087
				String szStockInSaleYn = "N";

				/* SKU PROJECT - 주문가능수량 수정. 2021.04.02 by kan.yp */
				String skuYn = "N";
				
				PrdQryCond pPrdQryCond = new PrdQryCond();
				pPrdQryCond.setPrdCd( pTempoutPrd.get(i).getPrdCd());
				
				EntityDataSet<DSData> eds = prdEntity.getStockInSaleYn( pPrdQryCond);
				if(eds.size() > 0){
					szStockInSaleYn = eds.getValues().getString("stockInSaleYn");
					skuYn = eds.getValues().getString("skuYn");
				}
				
				// [SKU] SKU속성상품 해제시 상품전체의 주문가능수량에는 변화를 주지 않는다.
				if ( !"Y".equals(szStockInSaleYn) && !skipSkuPrdQty ) {
					
					if ("Y".equals(pTempoutPrd.get(i).getTempoutYn())) {    //일시품절인경우 주문가능수량 0처리
						pOrdPsblQty.setOrdPsblQty(new BigDecimal(0));
					} else {
						pOrdPsblQty.setOrdPsblQty(pTempoutPrd.get(i).getOrdPsblQty());
					}
					pOrdPsblQty.setSessionUserId(pTempoutPrd.get(i).getSessionUserId());
					
					// SKU의 경우 임시품절 해제가 아닌경우 PRD_ORD_PSBL_TMP_D의 데이터를 추가해야 한다(PRD_ORD_PSBL_TMP_D 삭제 안함) 
					if (    "Y".equals(skuYn) 
						 &&  pOrdPsblQty.getOrdPsblQty() != null
						 && (pOrdPsblQty.getOrdPsblQty().compareTo(BigDecimal.ZERO) > 0) 
						 && pOrdPsblQty.getAttrPrdRepCd() != null ) 
					{
						// 주문가능수량조회
						EntityDataSet<DSData> getOrdPsblQty = ordPsblQtyEntity.getSaleQty(pOrdPsblQty);
						
						pOrdPsblQty.setSaleQty(getOrdPsblQty.getValues().getBigDecimal("saleQty")); // 판매수량
					}
					
					
					// SAP 전송 주문가능 수량 로그
					ordPsblQtyEntity.savePrdOrdPsblQtyLog(pOrdPsblQty);
					
//					ordPsblQtyEntity.modifyOrdPsblQtySimpl(pOrdPsblQty);
					
					// SKU의 경우 PRD_CD로 수정하는 경우 기존주문 계산 로직을 이용한다.
					if (     "Y".equals(skuYn) 
						 && !"Y".equals(pOrdPsblQty.getTempoutYn()) 
						 &&  pOrdPsblQty.getOrdPsblQty() != null
						 && (pOrdPsblQty.getOrdPsblQty().compareTo(BigDecimal.ZERO) > 0) ) 
					{
						ordPsblQtyEntity.modifyOrdPsblQtySku(pOrdPsblQty);
					} else {
						ordPsblQtyEntity.modifyOrdPsblQtySimpl(pOrdPsblQty);
					}
					
					//[SR02141211053][2014.12.15][김지혜] : [주문가능수량 이력관리 기능 개선]
					ordPsblQtyEntity.addOrdPsblQtyLog(pOrdPsblQty);
					//-END
					
					if ( !"Y".equals(skuYn) ) { 
						
						//20110504  추가 - 대표상품코드 필요
						// 주문가능수량임시정보 --> 주문가능수량임시로그
						ordPsblQtyEntity.insertPrdOrdPsblTmpL(pOrdPsblQty);
						// 주문가능수량임시정보 삭제
						ordPsblQtyEntity.deletePrdOrdPsblTmpD(pOrdPsblQty);
					}
				}

			}
		}
		returnDsData.put("prdCd", "0");
		returnDsData.put("retCd", "S");
		returnDsData.put("retMsg", Message.getMessage("prd.esb.msg.001"));
		returnMap.put("outModifytempoutClrtoMip", EDSFactory.create(EsbCmm.class, returnDsData));
		logger.debug("returnDsData3=>"+returnDsData);
		return returnMap;
	}

	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Need_Set_Close_Reason
	 */
//	public String Need_Set_Close_Reason(String FieldName, String FieldValue, PrdmMain prdmMain, SupDtlInfo supDtlInfo)
//	// function Need_Set_Close_Reason(FieldName, FieldValue)
//	{
//		String sReturn = "NO";
//
//		if (Check_MD_ID(prdmMain.getOperMdId()).equals("Y")) // 펑션추가
//		{
//		} else if (prdmMain.getPrdAprvStCd().equals("30")) // 상품결재상태코드
//		{
//			String gSetCloseReasonType = prdmMain.getSaleEndRsnCd();// 판매종료사유
//			if (StringUtils.NVL(FieldName).equals("Orderable Qty DM")
//					&& (StringUtils.NVL(FieldValue).equals("") || StringUtils.NVL(FieldValue).equals("0"))) // 주문가능수량 DM인 경우
//			{
//				// FS_LOG ("Orderable Qty DM", FieldValue);
//				// if( this.GetFieldValue("Close Reason Type").equals(""))
//				if (gSetCloseReasonType.equals("")) {
//					//sReturn = "상품종료 사유를 입력해 주세요.";
//					sReturn = Message.getMessage("prd.msg.371");
//				} else {
//					/*
//					 * gESCMFlag = "3"; gNeedWriteCloseLog = "Y"; gSetCloseReasonType = "";
//					 */
//				}
//			}
//
//			if (FieldName.equals("End Date")) // 판매종료일시
//			{
////				String sEndDt = prdmMain.getSaleEndDtm().substring(0, 10);
//				// FS_LOG ("sEndDt", sEndDt);
//				// FS_LOG ("FieldValue", FieldValue);
//				// var TimeBuf
//				// Clib.strftime(TimeBuf,"%m/%d/%Y" ,Clib.localtime(Clib.time()) //sysdate
//				// FS_LOG ("TimeBuf", TimeBuf);
//				/*
//				 * if(sEndDt == TimeBuf) { // if (this.GetFieldValue("Close Reason Type").equals("")) if ( gSetCloseReasonType.equals("")) { sReturn =
//				 * "상품종료 사유를 입력해 주세요." } else { gESCMFlag = "4"; gNeedWriteCloseLog = "Y"; gSetCloseReasonType = ""; } }
//				 */
//			}
//
//			if (FieldName.equals("Close Reason Type")) // 판매종료사유
//			{
//				// FS_LOG ("Close Reason Type Desc", FieldValue);
//				if ((!prdmMain.getSaleEndRsnCd().equals(""))) {
//					gSetCloseReasonType = "Y";
//				}
//			}
//			gSetCloseReasonType = "";
//		} else {
//			if (FieldName.equals("Close Reason Type") && (!prdmMain.getSaleEndRsnCd().equals(""))) {
//				sReturn = Message.getMessage("prd.msg.372");//MD팀장합격된 상품에 대해서만 상품종료사유 입력이 가능합니다.";
//			}
//
//			if (FieldName.equals("Close Reason Type Detail") && (!prdmMain.getSaleEndRsnCd().equals(""))) {
//				sReturn = Message.getMessage("prd.msg.372");//"MD팀장합격된 상품에 대해서만 상품종료사유 입력이 가능합니다.";
//			}
//		}
//		/*
//		 * FS_LOG ("gESCMFlag", gESCMFlag); FS_LOG ("gNeedWriteCloseLog", gNeedWriteCloseLog); FS_LOG ("Need_Set_Close_Reason:Return", sReturn);
//		 */
//
//		return sReturn;
//	}
	// SOURCE_CLEANSING : END
	
	
	/**
	 * <pre>
	 *
	 * desc : 상품기본정보목록을 수정한다.
	 *        ESB와 공통으로 사용하므로 Overloading 으로 사용
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG(kbog2089)
	 * @date 2010-12-16 12:07:53
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> savePrdBaseInfoList(List<PrdBaseInfo> modifyPrdBaseInfoList)
			throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		DSData returnDsData = new DSData();
		if (modifyPrdBaseInfoList.size() > 0) {
			for(PrdBaseInfo pPrdBaseInfo : modifyPrdBaseInfoList) {
			   // 상품기본정보수정
			   //prdEntity.modifyPrdBaseInfo(modifyPrdBaseInfoList); -- 리스트로 되어 있는 부분을 단건으로 처리

				DSMultiData modifyYnList = prdEntity.getModifyYn(pPrdBaseInfo);   // 세트상품 구성여부 조회.

				if(modifyYnList.size() > 0){
					throw new DevPrcException(pPrdBaseInfo.getPrdCd()+"는 세트상품구성품입니다. 유료배송여부, 카드사용제한, 무형상품유형코드, 당사상품권여부 확인 바랍니다.");
				}

			/*	List<PrdLimitChg> prdLimitChgList = new ArrayList();
				String changeYn = "";
				// 상품기본정보
				//if (pPrdBaseInfo != null) {
					// 변경이력 및 인터페이스 필수항목 체크에 필요 변경전 상품정보 조회
					PrdColChgLogQryCond prdColChgLogQryCond = new PrdColChgLogQryCond();
					prdColChgLogQryCond.setPrdCd(pPrdBaseInfo.getPrdCd());
					PrdColChgLog prdColChgLog = prdColChgLogEntity.getPrdPrdMInfo(prdColChgLogQryCond);

					PrdValidCond afterData = new PrdValidCond();
					PrdValidCond beforeData = new PrdValidCond();
					DevBeanUtils.wrappedObjToObj(beforeData, prdColChgLog);
					//20110613 확장정보추가
					DevBeanUtils.wrappedObjToObj(afterData, pPrdBaseInfo);
					changeYn = PrdValidUtil.prdValidCheck("key", beforeData,  afterData);

					if (prdColChgLog != null) {
						int returnValue = 0;

						// 상품기본정보 변경로그를 Entity로 이관 (2011/05/05 OSM)
						returnValue = prdColChgLogEntity.addPrdPrdMChgLog(pPrdBaseInfo, prdColChgLog);
						if (returnValue < 0) {
							logger.error("상품기본정보 로깅 실패!! ==> PrdMngProcess.setPrdUpdate");
						}// 제한정보 변경로그 (PRD_LIMIT_CHG_L) 추가
						PrdLimitChg prdLimitChg = new PrdLimitChg();


						if (prdColChgLog.getCpnMaxDcAmt() == null)
							prdColChgLog.setCpnMaxDcAmt(new BigDecimal("0"));
						if (pPrdBaseInfo.getSelAccRt() == null)
							pPrdBaseInfo.setSelAccRt(new BigDecimal("0"));
						if (pPrdBaseInfo.getCpnMaxDcAmt() == null)
							pPrdBaseInfo.setCpnMaxDcAmt(new BigDecimal("0"));

						if (!StringUtils.NVL(prdColChgLog.getBaseAccmLimitYn(), "").equals(
						        StringUtils.NVL(pPrdBaseInfo.getBaseAccmLimitYn(), ""))
						        || !StringUtils.NVL(prdColChgLog.getCardUseLimitYn(), "").equals(
						                StringUtils.NVL(pPrdBaseInfo.getCardUseLimitYn(), ""))
						        || !StringUtils.NVL(prdColChgLog.getCpnApplyTypCd(), "").equals(
						                StringUtils.NVL(pPrdBaseInfo.getCpnApplyTypCd(), ""))
						        || !StringUtils.NVL(prdColChgLog.getSelAccmApplyYn(), "").equals(
						                StringUtils.NVL(pPrdBaseInfo.getSelAccmApplyYn(), ""))
						        || !StringUtils.NVL(prdColChgLog.getSelAccRt().toString(), "0").equals(
						                StringUtils.NVL(pPrdBaseInfo.getSelAccRt().toString(), "0"))
						        || !StringUtils.NVL(prdColChgLog.getCpnMaxDcAmt().toString(), "0").equals(
						                StringUtils.NVL(pPrdBaseInfo.getCpnMaxDcAmt().toString(), "0"))
						        || !StringUtils.NVL(prdColChgLog.getImmAccmDcLimitYn(), "").equals(
						                StringUtils.NVL(pPrdBaseInfo.getImmAccmDcLimitYn(), ""))) {

							prdLimitChg.setSessionUserId(pPrdBaseInfo.getSessionUserId());
							prdLimitChg.setSessionUserIp(pPrdBaseInfo.getSessionUserIp());
							prdLimitChg.setPrdCd(pPrdBaseInfo.getPrdCd());
							prdLimitChg.setChk("1");
							prdLimitChg.setPreBaseAccmLimitYn(StringUtils.NVL(prdColChgLog.getBaseAccmLimitYn(), ""));
							prdLimitChg.setAftBaseAccmLimitYn(StringUtils.NVL(pPrdBaseInfo.getBaseAccmLimitYn(), ""));
							prdLimitChg.setPreCardUseLimitYn(StringUtils.NVL(prdColChgLog.getCardUseLimitYn(), ""));
							prdLimitChg.setAftCardUseLimitYn(StringUtils.NVL(pPrdBaseInfo.getCardUseLimitYn(), ""));
							prdLimitChg.setPreCpnApplyTypCd(StringUtils.NVL(prdColChgLog.getCpnApplyTypCd(), ""));
							prdLimitChg.setAftCpnApplyTypCd(StringUtils.NVL(pPrdBaseInfo.getCpnApplyTypCd(), ""));
							prdLimitChg.setPreSelAccmApplyYn(StringUtils.NVL(prdColChgLog.getSelAccmApplyYn(), ""));
							prdLimitChg.setAftSelAccmApplyYn(StringUtils.NVL(pPrdBaseInfo.getSelAccmApplyYn(), ""));
							prdLimitChg.setPreSelAccRt(StringUtils.NVL(prdColChgLog.getSelAccRt().toString(), ""));
							prdLimitChg.setAftSelAccRt(StringUtils.NVL(pPrdBaseInfo.getSelAccRt().toString(), ""));
							prdLimitChg.setPreCpnMaxDcAmt(StringUtils.NVL(prdColChgLog.getCpnMaxDcAmt().toString(), ""));
							prdLimitChg.setAftCpnMaxDcAmt(StringUtils.NVL(pPrdBaseInfo.getCpnMaxDcAmt().toString(), ""));
							prdLimitChg.setPreImmAccmDcLimitYn(StringUtils.NVL(prdColChgLog.getImmAccmDcLimitYn(), ""));
							prdLimitChg.setAftImmAccmDcLimitYn(StringUtils.NVL(pPrdBaseInfo.getImmAccmDcLimitYn(), ""));
							prdLimitChgList.add(prdLimitChg);
						}
				//	}
					int logResult = prdLimitChgLogEntity.addLimitLog(prdLimitChgList);
					logger.debug("PRD_LIMIT_CHG_L log result -----------> " + logResult);
				}
				*/


				// 해당 분류의 금지어가 상품명에 포함되었는지 체크한다. 20111018 mckim
				ProhibitWordCond prohibitWordCond = new ProhibitWordCond();

				prohibitWordCond.setMode("3");

				prohibitWordCond.setPrdCd(pPrdBaseInfo.getPrdCd());

				prohibitWordCond.setPrdClsCd("");
				prohibitWordCond.setPrdNm(pPrdBaseInfo.getPrdNm());
				prohibitWordCond.setExposPrdNm("");
				prohibitWordCond.setExposPmoNm("");
				prohibitWordCond.setExposPrSntncNm("");

				Map countMap = prdClsBaseEntity.getProhibitWordPrd(prohibitWordCond);
				if (countMap != null) {
					int count = ((BigDecimal)countMap.get("count")).intValue();
					if (count > 0) {
						returnDsData.put("retCd", "E1");
						returnDsData.put("retMsg", Message.getMessage("prd.msg.384", new String[] { "상품코드: " + pPrdBaseInfo.getPrdCd() + " 금지어: " + countMap.get("prhbWrdCntnt")}));
						returnMap.put("outEsbCmmRslt", EDSFactory.create(EsbCmm.class, returnDsData));
						return returnMap;
					}
				}

				prdEntity.modifyPrdBaseInfo(pPrdBaseInfo);

				if("QA".equals(StringUtils.NVL(pPrdBaseInfo.getEcGbn()))) { // QA에서 온 인터페이스 일 경우만 타게 함.
					// EAI 모듈 호출
					// 상품 동기화 상품코드 설정
					PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
					pPrdEaiPrdSyncInfo.setPrdCd(pPrdBaseInfo.getPrdCd());

				/*	if("N".equals(changeYn) ) {
						pPrdEaiPrdSyncInfo.setSapSaleBlockYn("Y");
					}
					*/
					wsPrdEaiPrdSyncProcess.prdEaiPrdSyncProcess(pPrdEaiPrdSyncInfo);
				}
			}
		}
		returnDsData.put("retCd", "S");
		returnDsData.put("retMsg", "");
		returnMap.put("outEsbCmmRslt", EDSFactory.create(EsbCmm.class, returnDsData));
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품기본정보목록을 저장한다.
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-12-16 12:07:53
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> savePrdBaseInfoList(RSPDataSet dataSet) throws DevEntException {
//		String key = "inSavePrdBaseInfoList";
//		List<PrdBaseInfo> addPrdBaseInfo = dataSet.getDataset4InsertObj(key, PrdBaseInfo.class);
//		List<PrdBaseInfo> modifyPrdBaseInfo = dataSet.getDataset4UpdateObj(key, PrdBaseInfo.class);
//		List<PrdBaseInfo> removePrdBaseInfo = dataSet.getDataset4DeleteObj(key, PrdBaseInfo.class);

		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		return returnMap;
	}

	@Override
	@Transactional(value = "smtcTransactionManager")
	public ImpQrySeq savePrdCdImp(List<ImpQry> addImpQryList) throws DevEntException {
		// 상품코드임포트조회순번채번
		ImpQrySeq impQrySeq = prdImpQryInfoEntity.getPrdCdImpQrySeq();
		for (ImpQry addImpQry : addImpQryList) {
			addImpQry.setQrySeq(impQrySeq.getQrySeq());
			impQrySeq.setSessionUserId(addImpQry.getSessionUserId());

			if( addImpQry.getPrdCd() != null ){
				// 상품코드임포트목록저장
				prdImpQryInfoEntity.savePrdCdImpList(addImpQry);
			}
		}
		logger.debug("\n impQrySeq = " + impQrySeq);
		return impQrySeq;
	}
	/**
	 *
	 * <pre>
	 *
	 * desc : 해외상품 임시상품 리스트에서 상품코드 Import
	 *
	 * </pre>
	 * @author kbog2089
	 * @date 2011. 7. 5. 오후 4:54:45
	 * @param addImpQryList
	 * @return
	 * @throws DevEntException
	 */
	@Override
	public ImpQrySeq saveLgecImportRetrieve(List<ImpQry> addImpQryList) throws DevEntException {
		// 상품코드임포트조회순번채번
		ImpQrySeq impQrySeq = prdImpQryInfoEntity.getPrdCdImpEcQrySeq();
		for(ImpQry addImpQry : addImpQryList) {
			addImpQry.setQrySeq(impQrySeq.getQrySeq());
			impQrySeq.setSessionUserId(addImpQry.getSessionUserId());
			// 상품코드임포트목록저장
			prdImpQryInfoEntity.savePrdCdImpEcList(addImpQry);
		}
		return impQrySeq;
	}

	/**
	 * <pre>
	 *
	 * desc : 전달받은 목록을 임포트 파일에 저장한다.
	 * .상품임포트조회정보 테이블에 저장할 조회순번을 seq로 채번한다.
	 * 채번할 조회순번을 입력키로 하여 목록을 저장한다.
	 * [HANGBOT-31738 GSITMBO-17570] 2022.02.21 이태호 표준출고일관리화면 상품코드추가후 조회시 조회안되는 현상 개선요청
	 * </pre>
	 *
	 * @author lgcns-213eab4ac
	 * @date 2010-09-16 06:41:20
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@Transactional(value = "smtcTransactionManager")
	public Map<String, ImpQrySeq> savePrdCdImp(RSPDataSet dataSet) throws DevEntException {
		String key = "inSavePrdCdImp";

		List<ImpQry> addImpQryList = dataSet.getDataset4NormalObj(key, ImpQry.class);
//		List<ImpQrySeq> addImpQrySeq = dataSet.getDataset4NormalObj(key, ImpQrySeq.class);

		// 상품코드임포트조회순번채번
		ImpQrySeq impQrySeq = prdImpQryInfoEntity.getPrdCdImpQrySeq();
		
		int subQrySeq = 0;

		for (ImpQry itm : addImpQryList) {
			itm.setQrySeq(impQrySeq.getQrySeq());
			impQrySeq.setSessionUserId(itm.getSessionUserId());
			
			//상품코드 중복을 맞기 위해 
			if( itm.getSubQrySeq() <= 0 ){
				itm.setSubQrySeq(++subQrySeq);
			}

			// 상품코드임포트목록저장
			prdImpQryInfoEntity.savePrdCdImpList(itm);
		}

		Map<String, ImpQrySeq> returnMap = new HashMap<String, ImpQrySeq>();
		returnMap.put("impQrySeq", impQrySeq);

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품채널정보를 등록,수정한다
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2011-01-01 07:01:44
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public int savePrdChanlList(RSPDataSet dataSet) throws DevEntException {
		String key = "inSavePrdChanlList";
		List<PrdChanlInfo> addPrdChanlInfo = dataSet.getDataset4InsertObj(key, PrdChanlInfo.class);
		List<PrdChanlInfo> modifyPrdChanlInfo = dataSet.getDataset4UpdateObj(key, PrdChanlInfo.class);
		List<PrdChanlInfo> removePrdChanlInfo = dataSet.getDataset4DeleteObj(key, PrdChanlInfo.class);

		// 상품채널목록저장
		prdChanlEntity.savePrdChanlList(addPrdChanlInfo, modifyPrdChanlInfo, removePrdChanlInfo);

		return 1;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품확장정보목록을 저장한다.
	 *    IF IsNuLL(상품기본구성내용 + 상품추가구성내용 + 상품사은품구성내용 + 상품기타구성내용) OR
	 *    ((상품기본구성내용 + 상품추가구성내용 + 상품사은품구성내용 + 상품기타구성내용) = '') THEN
	 *    ELSE
	 * 		IF ( 상품기본구성내용&lt;&gt; "" ) THEN 상품정보구성내용 = "[기본]" + 상품기본구성내용
	 *    IF ( 상품추가구성내용&lt;&gt; "" ) THEN 상품정보구성내용 = 상품정보구성내용 + "[추가]" + 상품추가구성내용
	 *    IF ( 상품사은품구성내용&lt;&gt; "" ) THEN 상품정보구성내용 = 상품정보구성내용 + "[사은품]"+ 상품사은품구성내용
	 *    IF ( 상품기타구성내용 &lt;&gt; "" ) THEN 상품정보구성내용 = 상품정보구성내용 + "[기타]" + 상품기타구성내용
	 *    1.상품확장정보목록등록을 수행한다
	 * OverLoading
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-12-15 10:40:33
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    @Transactional(value = "smtcTransactionManager")
	public Map<String, EntityDataSet> savePrdEnlagInfoList(List<PrdEnlagInfo> addPrdEnlagInfoList,
			List<PrdEnlagInfo> modifyPrdEnlagInfoList) throws DevEntException {
		// 로직 체크
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		DSData returnDsData = new DSData();

		// 로직 체크
		if (addPrdEnlagInfoList.size() > 0) {// if(InsertData) {
			// 상품확장정보목록등록
			prdEntity.addPrdEnlagInfoList(addPrdEnlagInfoList);
		}
		if (modifyPrdEnlagInfoList.size() > 0) {// else if(UpdateData) {
			// 상품확장정보목록수정
			prdEntity.modifyPrdEnlagInfoList(modifyPrdEnlagInfoList);

	        //상품마스터 eai interface
			for (PrdEnlagInfo prdEnlagInfo : modifyPrdEnlagInfoList) {
				PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
				pPrdEaiPrdSyncInfo.setPrdCd(prdEnlagInfo.getPrdCd());

				PRD_EAI_Res pRD_EAI_Res = new PRD_EAI_Res();
				// 상품 동기화
				try {
					pRD_EAI_Res = wsPrdEaiPrdSyncProcess.prdEaiPrdSyncProcess(pPrdEaiPrdSyncInfo);
					logger.debug("pRD_EAI_Res val=>" + pRD_EAI_Res.getResponseResult());
				} catch (Exception er) {
					pRD_EAI_Res.setResponseResult("E");
					pRD_EAI_Res.setResponseMessage("IFEAIPRD0121 :::: Exception 발생::");
					//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
					SMTCLogger.errorPrd("IFEAIPRD0121 :::: Exception 발생::", er);
					returnDsData.put("retCd", "E");
					returnDsData.put("retMsg", "");
					returnMap.put("outEsbCmmRslt", EDSFactory.create(EsbCmm.class, returnDsData));
					return returnMap;
				}
			}
		}


		returnDsData.put("retCd", "S");
		returnDsData.put("retMsg", "");
		returnMap.put("outEsbCmmRslt", EDSFactory.create(EsbCmm.class, returnDsData));
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품확장정보목록을 저장한다.
	 *    IF IsNuLL(상품기본구성내용 + 상품추가구성내용 + 상품사은품구성내용 + 상품기타구성내용) OR
	 *    ((상품기본구성내용 + 상품추가구성내용 + 상품사은품구성내용 + 상품기타구성내용) = '') THEN
	 *    ELSE
	 * 		IF ( 상품기본구성내용&lt;&gt; "" ) THEN 상품정보구성내용 = "[기본]" + 상품기본구성내용
	 *    IF ( 상품추가구성내용&lt;&gt; "" ) THEN 상품정보구성내용 = 상품정보구성내용 + "[추가]" + 상품추가구성내용
	 *    IF ( 상품사은품구성내용&lt;&gt; "" ) THEN 상품정보구성내용 = 상품정보구성내용 + "[사은품]"+ 상품사은품구성내용
	 *    IF ( 상품기타구성내용 &lt;&gt; "" ) THEN 상품정보구성내용 = 상품정보구성내용 + "[기타]" + 상품기타구성내용
	 *    1.상품확장정보목록등록을 수행한다
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-12-15 10:40:33
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    @Transactional(value = "smtcTransactionManager")
	public Map<String, EntityDataSet> savePrdEnlagInfoList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
//		DSData returnData = new DSData();
//		String key = "inSavePrdEnlagInfoList";
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품공지정보를 저장한다.
	 *     (유효시작일시가 없는 경우 SYSDATE를 지정한다.)
	 * OverLoading
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG (kbog2089)
	 * @date 2011-01-11 11:29:39
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> savePrdNtcInfo(List<PrdNtcInfo> addPrdNtcInfoList,
			List<PrdNtcInfo> modifyPrdNtcInfoList, List<PrdNtcInfo> removePrdNtcInfoList) throws DevEntException {

		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		if (addPrdNtcInfoList.size() > 0) {
			for (PrdNtcInfo prdNtcInfo : addPrdNtcInfoList) {
				if (prdNtcInfo.getValidStrDtm() == null) {
					prdNtcInfo.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
				}
				// 상품공지정보목록조회
				EntityDataSet<DSMultiData> pGetPrdNtcInfoList = prdEntity.getPrdNtcInfoList(prdNtcInfo);
				// 상품공지 정보목록조회가 0 이상이면 이력 수정
				if (pGetPrdNtcInfoList.size() > 0) {
					// 상품공지정보이력수정
					prdEntity.modifyPrdNtcInfoHist(prdNtcInfo);
				}
				// 상품공지정보등록
				prdEntity.addPrdNtcInfo(prdNtcInfo);
			}
		}
		if (modifyPrdNtcInfoList.size() > 0) {
			for (PrdNtcInfo prdNtcInfo : modifyPrdNtcInfoList) {
				// 상품공지정보수정
				prdEntity.modifyPrdNtcInfo(prdNtcInfo);
			}
		}
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품공지정보를 저장한다.
	 *    (유효시작일시가 없는 경우 SYSDATE를 지정한다.)
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG (kbog2089)
	 * @date 2011-01-11 11:29:39
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> savePrdNtcInfo(RSPDataSet dataSet) throws DevEntException {
//		String key = "inSavePrdNtcInfo";
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		return returnMap;
	}

	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Set_DM_Dvly_Day
	 */
//	public void Set_DM_Dvly_Day() {
//
//	}
	// SOURCE_CLEANSING : END
	
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Set_DM_Dvly_Day
	 */
//	public PrdmMain Set_DM_Dvly_Day(PrdmMain prdmMain, SupDtlInfo supDtlInfo) {
//		BigDecimal gDMDeliveryATPDay = prdmMain.getStdRelsDdcnt(); // DM인 경우 표준출고일수
//		BigDecimal iDvryATPDay = new BigDecimal("0");
//		logger.debug("Set_DM_Dvly_Day=>" + gDMDeliveryATPDay);
//		// gDMDeliveryATPDay.
//
//		// if ( gDMDeliveryATPDay > 0
//		if (gDMDeliveryATPDay == null) {
////			String sMdId = prdmMain.getOperMdId(); // 운영MDID
////			String sMdMedia = "";
//
//			EntityDataSet<DSMultiData> prdChanlcdFrMd = prdEntity.getChanlcdFrMd(prdmMain);// MDID별 채널 코드를 조회한다.
//			/*
//			 * MDID별 채널 코드를 조회한다. SELECT CHANL_CD FROM CMM_MDID_CHANL_RLRRST_D WHERE MD_ID = :operMdId AND ROWNUM = 1
//			 */
//			if (prdChanlcdFrMd.size() < 1) {
//				prdmMain.setStdRelsDdcnt(iDvryATPDay);
//				prdmMain.setRetCd("0");
//			    //MDID별 채널 코드를 조회 안됨
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.365", new String[]{"MDID별 채널 코드"}));
//				return prdmMain;
//			}
//			prdmMain.setSregChanlGrpCd(prdChanlcdFrMd.getValues().getString("chanlCd"));
//
//			EntityDataSet<DSMultiData> prdChanlDevCnt = prdEntity.getChanlDevCnt(prdmMain);// 표준출고일수 세팅
////			sMdMedia = "";// 조회결과
//			if (prdChanlDevCnt.size() < 1) {
//				prdmMain.setStdRelsDdcnt(iDvryATPDay);
//				prdmMain.setRetCd("0");
//				//표준출고일수 조회안됩니다.
//				prdmMain.setRetMsg(Message.getMessage("prd.msg.365", new String[]{"표준출고일수"}));
//				return prdmMain;
//			}
//			String sCarrier = prdmMain.getDlvPickMthodCd(); // 배송수거방법코드
//			if (sCarrier.equals("3100")) { // 직송설치상품
//				// iDvryATPDay = GetFieldValue("DM Delivery ATP Day3"); //INSTL_STD_RELS_DDCNT 설치표준출고일수 (채널 = 'D')
//				iDvryATPDay = new BigDecimal(prdChanlDevCnt.getValues().getString("instlStdRelsDdcnt"));
//			} else if (sCarrier.equals("3000") || sCarrier.equals("3200") || sCarrier.equals("3300")
//					|| sCarrier.equals("3400")) { // 직송상품
//				iDvryATPDay = new BigDecimal(prdChanlDevCnt.getValues().getString("dirdlvStdRelsDdcnt"));
//			} else { // 택배/직택배상품
//				iDvryATPDay = new BigDecimal(prdChanlDevCnt.getValues().getString("dlvsStdRelsDdcnt"));
//			}
//
//			prdmMain.setStdRelsDdcnt(iDvryATPDay);
//		}
//		prdmMain.setRetCd("0");
//		prdmMain.setRetMsg("0");
//		return prdmMain;
//	}
	// SOURCE_CLEANSING : END
	
	
	
	public String Set_EC_Dvly_Day2(PrdmMain prdmMain) {
		String iDvryATPDay = "";

//		String sMdId = prdmMain.getOperMdId(); // 운영MDID
//		String sMdMedia = "";
		/*
		 * MDID별 채널 코드를 조회한다. SELECT CHANL_CD FROM CMM_MDID_CHANL_RLRRST_D WHERE MD_ID = :operMdId AND ROWNUM = 1
		 */
		EntityDataSet<DSMultiData> prdChanlcdFrMd = prdEntity.getChanlcdFrMd(prdmMain);// MDID별 채널 코드를 조회한다.
		if (prdChanlcdFrMd.size() < 1) {
			return iDvryATPDay = "";
		}
//		sMdMedia = "";// 조회결과

		// oBusCompMD = null;
		// oBusObjProd = null;

		// 상품분류 세분류류의 표준출고일 값 가져오기
		/*
		 * var sProdLnName = this.GetFieldValue("Primary Product Line Code"); var sInstallFlg = this.GetFieldValue("Installation Flg"); //설치배송상품여부 var
		 * sCarrier = this.GetFieldValue("Carrier"); //배송수거방법코드 var sVendorId = this.GetFieldValue("Vendor Id"); //협력사코드 var sProductLine =
		 * this.GetFieldValue("Primary Product Line Code"); //상품분류코드
		 */
//		String sProdLnName = prdmMain.getPrdClsCd();
//		String sInstallFlg = prdmMain.getInstlDlvPrdYn(); // 설치배송상품여부
		String sCarrier = prdmMain.getDlvPickMthodCd(); // 배송수거방법코드
		BigDecimal sVendorId = prdmMain.getSupCd(); // 협력사코드
//		String sProductLine = prdmMain.getPrdClsCd(); // 상품분류코드

//		String gNoQaModifiedFlag = "Z"; // QA검사여부

		PrdCdQryCond pPrdCdQryCond = new PrdCdQryCond();
		pPrdCdQryCond.setSupCd(sVendorId);
		pPrdCdQryCond.setPrdClsCd(prdmMain.getPrdClsCd());
		pPrdCdQryCond.setMdId(prdmMain.getOperMdId());   //[PD-2015-007] 협력사별MDID별 표준출고일 
		EntityDataSet<DSMultiData> prdChanlDevCnt = prdEntity.getClsDevCnt(pPrdCdQryCond);// 표준출고일수 세팅

		if (prdChanlDevCnt != null) {
			// if ( gNoQaModifiedFlagCheck == "Y" ) //QA검사여부 = 'N'
			// gNoQaModifiedFlag = GetFieldValue("No QA Flag EC") ;
			if (sCarrier.equals("3100")) {// 직송설치상품
				// iDvryATPDay = GetFieldValue("Carrier3"); //INSTL_STD_RELS_DDCNT 설치표준출고일수
				iDvryATPDay = prdChanlDevCnt.getValues().getString("instlStdRelsDdcnt"); // INSTL_STD_RELS_DDCNT 설치표준출고일수
			} else if (sCarrier.equals("3000") || sCarrier.equals("3200") || sCarrier.equals("3300")
					|| sCarrier.equals("3400")) { // 직송상품
				// iDvryATPDay = GetFieldValue("Carrier2"); //DIRDLV_STD_RELS_DDCNT 직송표준출고일수
				iDvryATPDay = prdChanlDevCnt.getValues().getString("dirdlvStdRelsDdcnt"); // DIRDLV_STD_RELS_DDCNT 직송표준출고일수
			} else if (sCarrier.equals("2000") || sCarrier.equals("2100") || sCarrier.equals("2200") || sCarrier.equals("2300") 
					|| sCarrier.equals("2400") || sCarrier.equals("2500") || sCarrier.equals("2600") || sCarrier.equals("2700") || sCarrier.equals("2800")) {
				// iDvryATPDay = GetFieldValue("Carrier2"); //GTP_DDHD_STD_RELS_DDCNT 집하직택배표준출고일수	
				iDvryATPDay = prdChanlDevCnt.getValues().getString("gtpDdhdStdRelsDdcnt"); // GTP_DDHD_STD_RELS_DDCNT 집하직택배표준출고일수	
			}else { // 택배
				// iDvryATPDay = GetFieldValue("Carrier1"); //DLVS_STD_RELS_DDCNT 택배표준출고일수
				iDvryATPDay = prdChanlDevCnt.getValues().getString("dlvsStdRelsDdcnt");
			}
		} else {
			prdmMain.setSregChanlGrpCd(prdChanlcdFrMd.getValues().getString("chanlCd"));

			prdChanlDevCnt = prdEntity.getChanlDevCnt(prdmMain);// 표준출고일수 세팅
//			sMdMedia = "";// 조회결과
			if (prdChanlDevCnt.size() < 1) {
				return "";
			}

			if (sCarrier.equals("3100")) { // 직송설치상품
				// iDvryATPDay = GetFieldValue("DM Delivery ATP Day3"); //INSTL_STD_RELS_DDCNT 설치표준출고일수 (채널 = 'D')
				iDvryATPDay = prdChanlDevCnt.getValues().getString("instlStdRelsDdcnt");
			} else if (sCarrier.equals("3000") || sCarrier.equals("3200") || sCarrier.equals("3300")
					|| sCarrier.equals("3400")) { // 직송상품
				iDvryATPDay = prdChanlDevCnt.getValues().getString("dirdlvStdRelsDdcnt");
			} else if (sCarrier.equals("2000") || sCarrier.equals("2100") || sCarrier.equals("2200") || sCarrier.equals("2300") 
					|| sCarrier.equals("2400") || sCarrier.equals("2500") || sCarrier.equals("2600") || sCarrier.equals("2700") || sCarrier.equals("2800")) {
				iDvryATPDay = prdChanlDevCnt.getValues().getString("gtpDdhdStdRelsDdcnt"); // GTP_DDHD_STD_RELS_DDCNT 집하직택배표준출고일수	
			} else { // 택배/직택배상품
				iDvryATPDay = prdChanlDevCnt.getValues().getString("dlvsStdRelsDdcnt");
			}
		}

		String sRepFlg = prdmMain.getRepPrdYn(); // 대표상품여부
		if (sRepFlg.equals("Y"))
			iDvryATPDay = "";

		String sSoftwareFlg = prdmMain.getFrmlesPrdTypCd(); // 무형상품유형코드
		if (!sSoftwareFlg.equals("") && !sSoftwareFlg.equals("N"))
			iDvryATPDay = "";

		String sFreeGiftType = prdmMain.getGftTypCd(); // 사은품유형코드
		if (StringUtils.NVL(sFreeGiftType).equals("00"))
			iDvryATPDay = "";

		// C20080220_20249 인터넷 도서몰 구축 2008.04.02 jewelLee
		String sBundlePackagingType = prdmMain.getOboxCd(); // 합포장코드
		if (sBundlePackagingType.equals("2"))
			iDvryATPDay = "";

		return iDvryATPDay;
	}

	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Set_Fixed_Dlvy_log
	 */
//	public void Set_Fixed_Dlvy_log(PrdmMain prdmMain)
//	// function Set_Fixed_Dlvy_log()
//	{
//		// try{
////		BigDecimal sProdId = prdmMain.getPrdCd(); // 상품코드
////		String sBfLogField_1 = "";
////		String sAfLogField_1 = "";
////
////		String sBfLogField_2 = "";
////		String sAfLogField_2 = "";
////
////		// 2009.09.25 SNY 추가 시작
////		String sBfLogField_3 = "";
////		String sAfLogField_3 = "";
////
////		String sBfLogField_4 = "";
////		String sAfLogField_4 = "";
//		// 2009.09.25 SNY 추가 끝
//
//		// 지정택배수거 로그
//
//		/*
//		 * if ( gBeforFixedDeliveryRetFlag == "INIT") sBfLogField_1 = this.GetFieldValue("Fixed Delivery Ret Flag"); //지정택배시행여부 else sBfLogField_1 =
//		 * gBeforFixedDeliveryRetFlag;
//		 *
//		 * if ( gBeforeRetCenter == "INIT") sBfLogField_2 = this.GetFieldValue("Ret Receipt Center Address Code");//상품반송지주소코드 else sBfLogField_2 =
//		 * gBeforeRetCenter;
//		 *
//		 * // 2009.09.25 SNY 추가 시작 if ( gBeforeCVSRetFlag == "INIT") sBfLogField_3 = this.GetFieldValue("CVS Ret Flag");//편의점택배반품여부 else sBfLogField_3
//		 * = gBeforeCVSRetFlag;
//		 *
//		 * if ( gBeforeShipCenter == "INIT") sBfLogField_4 = this.GetFieldValue("Ship Center Address Code"); //상품출고지주소코드 else sBfLogField_4 =
//		 * gBeforeShipCenter; // 2009.09.25 SNY 추가 끝
//		 *
//		 * sAfLogField_1 = this.GetFieldValue("Fixed Delivery Ret Flag");//지정택배시행여부 sAfLogField_2 =
//		 * this.GetFieldValue("Ret Receipt Center Address Code");//상품반송지주소코드 sAfLogField_3 = this.GetFieldValue("CVS Ret Flag"); // 2009.09.25 SNY 추가
//		 * //편의점택배반품여부 sAfLogField_4 = this.GetFieldValue("Ship Center Address Code"); // 2009.09.25 SNY 추가 //상품출고지주소코드
//		 *
//		 * if ( sBfLogField_1 == sAfLogField_1 && sBfLogField_2 == sAfLogField_2 && sBfLogField_3 == sAfLogField_3 && sBfLogField_4 == sAfLogField_4)
//		 * return;
//		 */
//		/* 상품변경로그에 등록 */
//		/*
//		 * var oBusObj = TheApplication().GetBusObject("Admin Product Definition"); var oBusComp =
//		 * oBusObj.GetBusComp("Internal Product History for Fixed Dlvy");
//		 *
//		 * with (oBusComp) { NewRecord(NewAfter); SetFieldValue("Fixed Dlvy Flag Before", sBfLogField_1 ); SetFieldValue("Fixed Dlvy Flag After" ,
//		 * sAfLogField_1 ); SetFieldValue("Prod Id", sProdId); SetFieldValue("Ret Receipt Center Address Before", sBfLogField_2 );
//		 * SetFieldValue("Ret Receipt Center Address After" , sAfLogField_2 );
//		 *
//		 * // 2009.09.25 SNY 추가 시작 SetFieldValue("CVS Ret Flag Before", sBfLogField_3 ); SetFieldValue("CVS Ret Flag After" , sAfLogField_3 );
//		 * SetFieldValue("Ship Center Address Before", sBfLogField_4 ); SetFieldValue("Ship Center Address After" , sAfLogField_4 ); // 2009.09.25 SNY
//		 * 추가 끝
//		 *
//		 * SetFieldValue("Type", "FIXED_DLVY"); WriteRecord();
//		 *
//		 * gBeforFixedDeliveryRetFlag = "INIT"; gBeforeRetCenter = "INIT"; gBeforeCVSRetFlag = "INIT"; // 2009.09.25 SNY 추가 gBeforeShipCenter =
//		 * "INIT"; // 2009.09.25 SNY 추가 }
//		 *
//		 * //요기까지
//		 *
//		 * } catch (e) { throw e; } finally { oBusComp = null; oBusObj= null; }
//		 */
//	}
	// SOURCE_CLEANSING : END
	
	
	/**
	 * <pre>
	 *
	 * desc : 속성상품을 저장한다.(연계용)     *
	 * </pre>
	 *
	 * @author lgcns-213eab4ac
	 * @date 2010-09-15 04:51:39
	 * @param List <SaleEndClr> saleEndClrList
	 * @return Map<String, EntityDataSet>
	 */
	// List<PrdAttrPrdMinsert> pattrPrdM // 속성상품정보 insert
	// List<PrdAttrPrdMinsert> pattrPrdMUp // 속성상품정보 업데이트용
	// PrdOrdPsblQtyDinsert prdOrdPsblQtyD // 상품주문가능수량정보
	// PrdStockDinsert prdAttrPrdm // 상품재고정보
	// PrdmMain prdMain // 상품속성정보
	// boolean isAttrEaiTransPass // 속성EAI전송제외여부 
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> saveAttrPrdList(
			List<PrdAttrPrdMinsert> pattrPrdM,
			List<PrdAttrPrdMinsert> pattrPrdMUp,
			List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD,
			List<PrdStockDinsert> prdAttrPrdm,
			List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyDUp,
			List<PrdStockDinsert> prdAttrPrdmUp,
			PrdmMain prdMain
	         ) throws DevEntException {
    	return this.saveAttrPrdList(pattrPrdM, pattrPrdMUp, prdOrdPsblQtyD, prdAttrPrdm, prdOrdPsblQtyDUp, prdAttrPrdmUp, prdMain, false);
    }
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> saveAttrPrdList(
													List<PrdAttrPrdMinsert> pattrPrdM,
													List<PrdAttrPrdMinsert> pattrPrdMUp,
													List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD,
													List<PrdStockDinsert> prdAttrPrdm,
													List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyDUp,
													List<PrdStockDinsert> prdAttrPrdmUp,
													PrdmMain prdMain,
													boolean isAttrEaiTransPass //속성eai전송 제외여부
											         ) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		DSData returnDsData = new DSData();

        //속성상품 저장 for sm4c
		logger.debug("pattrPrdM.size()=>" + pattrPrdM.size());
		logger.debug("pattrPrdMUp.size()=>" + pattrPrdMUp.size());
		logger.debug("prdOrdPsblQtyD.size()=>" + prdOrdPsblQtyD.size());
		logger.debug("prdOrdPsblQtyDUp.size()=>" + prdOrdPsblQtyDUp.size());
		logger.debug("prdAttrPrdm.size()=>" + prdAttrPrdm.size());
		logger.debug("prdAttrPrdmUp.size()=>" + prdAttrPrdmUp.size());

		//[SR02180727185][2018.09.17][이용문]: 지정일(설치포함) 프로세스 개선을 위한 상품등록 화면 수정
		if( prdMain != null && ("Y".equals(StringUtils.NVL(prdMain.getApntDtDlvYn())) || "1".equals(StringUtils.NVL(prdMain.getApntDtDlvYn()))) ){
			
			//지정일자 유형코드
			String apntDtDlvTyp = StringUtils.NVL(prdMain.getApntDtDlvYn());
			
			//지정일 배송유형이 날짜지정이라면 날짜타입 및 공휴일 체크
			if( "D".equals(apntDtDlvTyp) ){
				DSData tmpApntCheck = isValidateApntAttrDate(pattrPrdM, prdMain.getPrdCd().toString());
				if( tmpApntCheck != null && !"1".equals(tmpApntCheck.getString("retCd")) ){
					returnMap.put("outPrdDecdItmList", EDSFactory.create(PrdCmm.class, tmpApntCheck));
					return returnMap;
				}
				
				tmpApntCheck = isValidateApntAttrDate(pattrPrdMUp, prdMain.getPrdCd().toString());
				if( tmpApntCheck != null && !"1".equals(tmpApntCheck.getString("retCd")) ){
					returnMap.put("outPrdDecdItmList", EDSFactory.create(PrdCmm.class, tmpApntCheck));
					return returnMap;
				}
			}
		}

		// 방송상품주문가능수량 수정제한
		if(prdOrdPsblQtyDUp.size()>0){
			EntityDataSet<DSData> broadCnt  = prdEntity.getBroadCnt(prdMain);
			int cnt = broadCnt.getValues().getInt("cnt");
			if( cnt > 0 ) {
				EntityDataSet<DSData> adminRoleCnt  = odsBroadBefMngInfoEntity.getAdminAuthCnt(prdMain);
				int roleCnt =  adminRoleCnt.getValues().getInt("authCnt");
				for (int i = 0; i < prdOrdPsblQtyDUp.size(); i++) {

					if( roleCnt == 0 && "AZ".equalsIgnoreCase(prdOrdPsblQtyDUp.get(i).getChanlGrpCd()) ){
						returnDsData.put("retMsg", Message.getMessage("prd.msg.378"));
						returnDsData.put("prdCd", prdMain.getPrdCd());
						returnDsData.put("retCd", "E");
						returnMap.put("outPrdDecdItmList", EDSFactory.create(PrdCmm.class, returnDsData));

						return returnMap;
					}
				}
			}
			
			/** 딜 테이블 데이터 유입 안되어 로직 삭제 [S] (DEAL_BEF_MNG_M, DEAL_FORM_PRD_D 등록일 2015년 데이터가 마지막)  2022.02.11 이용문 */
//			//MCPC대량판매 02140701 추가
//			EntityDataSet<DSData> dealCntInfo  = 		prdEntity.getDealCnt(prdMain);
//			EntityDataSet<DSData> dealAdminRoleCnt  = 	odsBroadBefMngInfoEntity.getDealAdminAuthCnt(prdMain);
//			EntityDataSet<DSData> dealMdRoleCnt  = 		dealMngEntity.getDealMdAuthCnt(prdMain);
//			
//			int mdUpCnt = 		dealCntInfo.getValues().getInt("mdUpCnt");
//			int admUpCnt = 		dealCntInfo.getValues().getInt("admUpCnt");
//			int dealMdAuthCnt = dealMdRoleCnt.getValues().getInt("authCnt");
//			int dealAdminAuthCnt = dealAdminRoleCnt.getValues().getInt("authCnt");
//			
//			for (int i = 0; i < prdOrdPsblQtyDUp.size(); i++) {
//				if(admUpCnt == 0 && mdUpCnt ==0){
//					continue;
//				}else if(admUpCnt > 0 && dealAdminAuthCnt > 0){ 
//					continue;
//				}else if(mdUpCnt > 0 && dealMdAuthCnt > 0){
//					continue;
//				}else{
//					//[SR02140805150][2014.08.08][김지혜] : [딜 진행 시 MD/협력사 수량 변경(감소, 증가는 불가) 가능 요청] 
//					PrdQryCond pPrdQryCond = new PrdQryCond();
//					pPrdQryCond.setPrdCd(prdMain.getPrdCd());
//					
//					boolean upQtyYn = true;
//					BigDecimal orgOrdPsblQty = new BigDecimal(0);
//					BigDecimal newOrdPsblQty = new BigDecimal(0);
//					
//					DSMultiData getOrgPrdAttrPrdList =  attrPrdEntity.getPrdAttrPrdList(pPrdQryCond).getValues();
//					if( getOrgPrdAttrPrdList != null && getOrgPrdAttrPrdList.size() > 0) {
//						for( DSData getOrgPrdAttrPrdInfo : getOrgPrdAttrPrdList) {
//							
//							if( getOrgPrdAttrPrdInfo.getString("attrPrdCd").equals(prdOrdPsblQtyDUp.get(i).getAttrPrdRepCd()) ) {
//								orgOrdPsblQty = getOrgPrdAttrPrdInfo.getBigDecimal("allOrdPsblQty");		//원본 주문가능수량
//								newOrdPsblQty = prdOrdPsblQtyDUp.get(i).getOrdPsblQty();					//변경 주문가능수량
//								
//								//신규수량이 원본수량보다 클 경우 체크
//								if(newOrdPsblQty.compareTo(orgOrdPsblQty) > 0){
//									upQtyYn = false;
//									break;
//								}
//							}
//						}
//					}
//					
//					if(upQtyYn) {
//						continue;
//					}
//					//-end
//					
//					returnDsData.put("retMsg", Message.getMessage("prd.msg.390"));
//					returnDsData.put("prdCd", prdMain.getPrdCd());
//					returnDsData.put("retCd", "E");
//					returnMap.put("outPrdDecdItmList", EDSFactory.create(PrdCmm.class, returnDsData));
//
//					return returnMap;
//				}
//			}
			/** 딜 테이블 데이터 유입 안되어 로직 삭제 [E] */
			
		}

		if("01".equals(prdMain.getOrdPrdTypCd())) { // 주문형태 : 속성 대표 상품주문 인경우 처리
			for (int stockIndex = 0; stockIndex != prdAttrPrdm.size(); stockIndex++) {
				PrdStockDinsert stock = prdAttrPrdm.get(stockIndex);
				String tempAttrPrdRepCd = stock.getAttrPrdRepCd();

				if(tempAttrPrdRepCd == null || tempAttrPrdRepCd.length() < 5) {  // 속성대표코드가 P1인경우 주문형태코드 생성 및 속성상품/상품재고에 연결함
					String attrPrdRepCd = prdEntity.getAttrPrdRepCd(prdMain);
					stock.setPrdCd(prdMain.getPrdCd());
					stock.setAttrPrdRepCd(attrPrdRepCd);

					for (int attrPrdIndex = 0; attrPrdIndex != pattrPrdM.size(); attrPrdIndex++) {
						PrdAttrPrdMinsert attrPrd = pattrPrdM.get(attrPrdIndex);

						logger.debug("tempAttrPrdRepCd =>" + tempAttrPrdRepCd);
						logger.debug("attrPrd.getAttrPrdRepCd() =>" + attrPrd.getAttrPrdRepCd());
						if (tempAttrPrdRepCd.equals(attrPrd.getAttrPrdRepCd())) {
							attrPrd.setAttrPrdRepCd(attrPrdRepCd);
							attrPrd.setPrdCd(prdMain.getPrdCd());
						}
					}
					for (int possQtyIndex = 0; possQtyIndex != prdOrdPsblQtyD.size(); possQtyIndex++) {
						// 임시 속성코드와 동일하면 채번 속성코드로 변경
						PrdOrdPsblQtyDinsert possQty = prdOrdPsblQtyD.get(possQtyIndex);

						if(tempAttrPrdRepCd.equals(possQty.getAttrPrdRepCd())) {
							possQty.setAttrPrdRepCd(attrPrdRepCd);
						}
					}
				}
			}
		}

		int e = 1;
		// 속성상품 INSERT
		for (int i = 0; i < pattrPrdM.size(); i++) {
			PrdAttrPrdMinsert attrPrd = pattrPrdM.get(i);
			BigDecimal prdCd = pattrPrdM.get(i).getPrdCd();

			if (prdCd == null || prdCd.toString().length() < 6) { // 여기서 상품코드가없는 속성상품코드처리
				attrPrd.setPrdCd(prdMain.getPrdCd());
			}

			// 임시 채번 속성코드
			BigDecimal tempAttrPrdCd = attrPrd.getAttrPrdCd();

			// 속성코드 채번
			attrPrd.setAttrPrdCd(prdEntity.getAttrPrdCd(prdMain));
			// 속성값코드가 모두 'None'인 경우 속성값코드4에 신규채번함 => E001 ~  E999
			if(attrPrd.getAttrValCd1().equals("None") && attrPrd.getAttrValCd2().equals("None")
				&& attrPrd.getAttrValCd3().equals("None") && attrPrd.getAttrValCd4().equals("None") ) {
                if(e==1){ //최초한번만 채번함
				    e = prdEntity.getAttrValCd4(prdMain);
                }
				String attrValCd4 = org.apache.commons.lang.StringUtils.leftPad(String.valueOf(e), 3, '0');
				attrPrd.setAttrValCd4("E" + attrValCd4);
				e = e + 1;
			}

			// 속성대표/속성일반(ordPrdTypCd)
			// 01 : 속성 대표 상품별 주문
			// 02 : 일반주문/상품속성별주문
			if (!"01".equals(prdMain.getOrdPrdTypCd())) { // 주문형태 : 속성일반 주문인경우 처리
				// 속성 대표코드 동일 코드로 처리
				attrPrd.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());

				for (int stockIndex = 0; stockIndex != prdAttrPrdm.size(); stockIndex++) {
					// 임시 속성코드와 동일하면 채번 속성코드로 변경
					PrdStockDinsert stock = prdAttrPrdm.get(stockIndex);
					if(stock.getAttrPrdRepCd() != null){
						if (tempAttrPrdCd.toString().equals(stock.getAttrPrdRepCd())) {
							stock.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());
							stock.setPrdCd(attrPrd.getPrdCd());
						}
					}else{
						stock.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());
						stock.setPrdCd(attrPrd.getPrdCd());
					}
				}
				for (int possQtyIndex = 0; possQtyIndex != prdOrdPsblQtyD.size(); possQtyIndex++) {
					// 임시 속성코드와 동일하면 채번 속성코드로 변경
					PrdOrdPsblQtyDinsert possQty = prdOrdPsblQtyD.get(possQtyIndex);
					if(possQty.getAttrPrdRepCd() != null){
						if(tempAttrPrdCd.toString().equals(possQty.getAttrPrdRepCd())) {
							possQty.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());
						}
					}else{
						possQty.setAttrPrdRepCd(attrPrd.getAttrPrdCd().toString());
					}
				}

			}

			// 속성상품코드 insert
			prdEntity.setInsertPrdAttrPrdM(pattrPrdM.get(i));
		}
		/***************************************************************************
		 *   로직을 하단에서 속성상품 인서트 후 바로  로직실행   by 20110622 정동국차장님 요청
		 ***************************************************************************/
		String prdIfYn = "N";  //상품마스터 eai호출여부
		if (pattrPrdM.size() > 0) {
//			logger.debug("sapr 테스트 clsChkAftAprvCd==>" + prdMain.getClsChkAftAprvCd());
//			logger.debug("sapr 테스트 getClsChkStCd==>" + prdMain.getClsChkStCd());
//			logger.debug("sapr 테스트 getPrdAprvStCd==>" + prdMain.getPrdAprvStCd());
			if (prdMain.getClsChkAftAprvCd().equals("M") && prdMain.getClsChkStCd().equals("20") ) {
				//SMTCLogger.writePrd("ec채널 상품인경우 MD팀장합격처리");

				if("30".equals(prdMain.getPrdAprvStCd())){
					// 속성상품 MD팀장합격처리
					List<MdTeamldrAprvTgt> pMdTeamldrAprvTgtAttrList = new ArrayList<MdTeamldrAprvTgt>();
					for (int i = 0; i < pattrPrdM.size(); i++) {
						MdTeamldrAprvTgt pMdTeamldrAprvTgtAttr = null;

						pMdTeamldrAprvTgtAttr = new MdTeamldrAprvTgt();

						pMdTeamldrAprvTgtAttr.setChk("1");
						pMdTeamldrAprvTgtAttr.setPrdCd(prdMain.getPrdCd());
						pMdTeamldrAprvTgtAttr.setAttrPrdCd(pattrPrdM.get(i).getAttrPrdCd());
						pMdTeamldrAprvTgtAttr.setPrdAprvStCd("30");
						pMdTeamldrAprvTgtAttr.setPrdTypCd("S");
						pMdTeamldrAprvTgtAttr.setPrdAttrGbnCd("A");
						pMdTeamldrAprvTgtAttr.setClsChkAftAprvCd("M");
						pMdTeamldrAprvTgtAttr.setStyleDirEntYn(prdMain.getStyleDirEntYn()); // 스타일 입력여부 EAI 값 셋팅
						pMdTeamldrAprvTgtAttr.setSessionUserId(prdMain.getSessionUserId());

						pMdTeamldrAprvTgtAttrList.add(pMdTeamldrAprvTgtAttr);
					}
					//ProcRsltInfo procRsltInfo1 = prdAprvMngProcess.modifyMdTeamldrPass(pMdTeamldrAprvTgtAttrList);
					ProcRsltInfo procRsltInfo1 = prdAprvMngProcess.modifyMdTeamldrPassToSm4c(pMdTeamldrAprvTgtAttrList);
					if (!procRsltInfo1.getRetCd().equals("S")) {
						returnDsData.put("prdCd", "");
						returnDsData.put("retCd", "E");
						returnDsData.put("retMsg", procRsltInfo1.getRetMsg());
						returnMap.put("outPrdDecdItmList", EDSFactory.create(PrdCmm.class, returnDsData));
						return returnMap;
					}
				}else if("00".equals(prdMain.getPrdAprvStCd())){
					// MD팀장합격수정
					List<MdTeamldrAprvTgt> mdTeamldrAprvTgtList = new ArrayList<MdTeamldrAprvTgt>();
					MdTeamldrAprvTgt mdTeamldrAprvTgt = new MdTeamldrAprvTgt();
					mdTeamldrAprvTgt.setPrdCd(prdMain.getPrdCd());
					mdTeamldrAprvTgt.setStyleDirEntYn(prdMain.getStyleDirEntYn()); // 스타일 입력여부 EAI 값 셋팅
					mdTeamldrAprvTgt.setPrdAprvStCd("30");
					mdTeamldrAprvTgt.setChk("1");
					mdTeamldrAprvTgt.setClsChkAftAprvCd("M");
					mdTeamldrAprvTgt.setPrdAttrGbnCd("P");
					mdTeamldrAprvTgt.setSessionObject(prdMain);
					mdTeamldrAprvTgtList.add(mdTeamldrAprvTgt);
					prdAprvMngProcess.modifyMdTeamldrPassToSm4c(mdTeamldrAprvTgtList);
					prdIfYn = "Y";  //상품마스터 eai호출여부
				}
			}else if(prdMain.getClsChkAftAprvCd().equals("Q") && prdMain.getClsChkStCd().equals("20")) {
				// 속성상품QA팀장합격처리
				for (int i = 0; i < pattrPrdM.size(); i++) {
					QaAprvRec pQaAprvRec = new QaAprvRec();

					pQaAprvRec.setPrdCd(prdMain.getPrdCd());
					pQaAprvRec.setAttrPrdCd(pattrPrdM.get(i).getAttrPrdCd());
					pQaAprvRec.setPrdAprvStCd("25");
					pQaAprvRec.setPrdTypCd("S");
					pQaAprvRec.setSessionUserId(prdMain.getSessionUserId());
					pQaAprvRec.setStyleDirEntYn(""); // 스타일 입력여부 EAI 값 셋팅

					// QA속성상품승인내역수정
					attrPrdEntity.modifyQaAttrPrdAprvRec(pQaAprvRec);
				}

				PrdQaAprvIfCond prdQaAprvIfCond = new PrdQaAprvIfCond();

				// QA승인내역수정 EAI 전송데이터 셋팅
				for (int i = 0; i < pattrPrdM.size(); i++) {
					logger.debug("test2");
					// QA승인내역수정 EAI 전송데이터 셋팅
					prdQaAprvIfCond.setStyleDirEntYn(prdMain.getStyleDirEntYn()); // 스타일 입력여부 EAI 값 셋팅
					prdQaAprvIfCond.setPrdCd(prdMain.getPrdCd());    // 상품코드
					prdQaAprvIfCond.setAttrPrdCd(pattrPrdM.get(i).getAttrPrdCd());     // 속성상품코드
					prdQaAprvIfCond.setPrdAprvStCd("25"); // 상품결재상태 코드
					prdQaAprvIfCond.setAttrPrdAprvStCd("25"); // 상품결재상태 코드
					prdQaAprvIfCond.setQaGrdCd(prdMain.getQaGrdCd());     // QA등급코드
					prdQaAprvIfCond.setQaPrgStCd(prdMain.getQaPrgStCd()); // QA진행상태코드
					prdQaAprvIfCond.setPrdTypCd(prdMain.getPrdTypCd());   //


					logger.debug("pPrdBaseInfo.getPrdCd->" +prdMain.getPrdCd());
					logger.debug("pGetAttrPrdList.attrPrdCd->" +pattrPrdM.get(i).getAttrPrdCd());
					logger.debug("prdQaAprvIfCond.getStyleDirEntYn->" +prdQaAprvIfCond.getStyleDirEntYn());
					logger.debug("pPrdBaseInfo.getPrdTypCd->" +prdMain.getPrdTypCd());

					/*if (prdQaAprvIfCond.getPrdCd() != null) {
						wsPrdEaiQaAprvProcess.prdEaiQaAprvProcess(prdQaAprvIfCond);
					 }
					 */
				}
			}
			// 속성 추가시, qa검사여부가 '검사'이고, 결재 상태가 신규 건이 아닐 경우에는 qa 검사 의뢰를 한다. (sap 재구축 추가 : 2013/01/29 안승훈 )
			else if (!StringUtils.NVL(prdMain.getPrdAprvStCd()).equals("00")){
				returnDsData = prdQaBefReqProcess.requestPrdAttrQaBefReq(prdMain, pattrPrdM);
				if (returnDsData.getString("retCd").equals("-1")) {
					returnDsData.put("prdCd", prdMain.getPrdCd());
					returnMap.put("outPrdDecdItmList", EDSFactory.create(PrdCmm.class, returnDsData));
				}
			}
		}

		// 속성상품 update
		for (int i = 0; i < pattrPrdMUp.size(); i++) {
			// 속성상품코드 update
			prdEntity.modifyPrdAttrPrdM(pattrPrdMUp.get(i));
		}

		// 재고수량 저장
		for (int i = 0; i < prdAttrPrdm.size(); i++) {
			int upAttrval = attrPrdEntity.modifyStockD(prdAttrPrdm.get(i));
			if (upAttrval < 1) {
				prdEntity.setInsertPrdStockD(prdAttrPrdm.get(i));
			}
		}
		// 재고수량 저장
		for (int i = 0; i < prdAttrPrdmUp.size(); i++) {
			int upAttrval = attrPrdEntity.modifyStockD(prdAttrPrdmUp.get(i));
			if (upAttrval < 1) {
				prdEntity.setInsertPrdStockD(prdAttrPrdmUp.get(i));
			}
		}
		
		//[SR02140613108][2014.06.26][김지혜] : [GSSHOP모바일 상품권 주문 가능수량 제한 해제요청]
		//[SR02141226076][2015.02.16][유수경]:모바일 상품권/티켓 관련 상품분류 재정비의 건 , B310111추가 
		/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 _490 Start */
		PrdClsUda param = new PrdClsUda();
		param.setUdaNo(new BigDecimal( "111" ));
		param.setPrdClsCd(prdMain.getPrdClsCd());		
		
		/*if( prdMain.getPrdClsCd() != null && ( "B31010511".equals(prdMain.getPrdClsCd()) ||
			"B310111".equals(prdMain.getPrdClsCd().substring(0, 7))	
				)) {*/
		if( prdMain.getPrdClsCd() != null && PrdClsUdaUtils.isPrdClsUdaFlag(param)) {
		/* [PD_2017_012][2017.08.25][김영현]:상품분류체계개편2차-하드코딩제거 End */
			if( prdOrdPsblQtyD != null && prdOrdPsblQtyD.size() > 0 ) {
				prdOrdPsblQtyD.get(0).setOrdPsblQtyLimitClrYn("Y");
			}
			
			if( prdOrdPsblQtyDUp != null && prdOrdPsblQtyDUp.size() > 0 ) {
				prdOrdPsblQtyDUp.get(0).setOrdPsblQtyLimitClrYn("Y");
			}
		}
		
		//-end
		
		// 주문가능 수량 저장
		this.saveOrdPsblQtyList(prdOrdPsblQtyD, prdOrdPsblQtyDUp);

		//속성유형명 update
		prdEntity.modifyPrdAttrHead(prdMain);

        //상품마스터 eai interface
		if("Y".equals(prdIfYn)){  //상품마스터 eai호출여부
			PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
			pPrdEaiPrdSyncInfo.setPrdCd(prdMain.getPrdCd());

			PRD_EAI_Res pRD_EAI_Res = new PRD_EAI_Res();
			// 상품 동기화
			try {
				pRD_EAI_Res = wsPrdEaiPrdSyncProcess.prdEaiPrdSyncProcess(pPrdEaiPrdSyncInfo);
				logger.debug("pRD_EAI_Res val=>" + pRD_EAI_Res.getResponseResult());
			} catch (Exception er) {
				pRD_EAI_Res.setResponseResult("E");
				pRD_EAI_Res.setResponseMessage("IFEAIPRD0121 :::: Exception 발생::");
				//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
				SMTCLogger.errorPrd("IFEAIPRD0121 :::: Exception 발생::", er);
			}

		}

		//속성상품 eai interface
		if( !isAttrEaiTransPass ) { //속성EAI 패스하는 로직이 아니라면 기존로직 처리. 리턴메세지는 아래 로직에서 재처리하므로 returnMap 따로 가공하지 않음.
			returnMap = saveAttrPrdListToEai(pattrPrdM, pattrPrdMUp, prdOrdPsblQtyD, prdAttrPrdm, prdOrdPsblQtyDUp,	prdAttrPrdmUp, prdMain);
		}
		

		returnDsData.put("prdCd", prdMain.getPrdCd());
		returnDsData.put("retCd", "0");
		returnDsData.put("retMsg", Message.getMessage("prd.esb.msg.006", new String[]{"속성상품"}));
		returnMap.put("outPrdDecdItmList", EDSFactory.create(PrdCmm.class, returnDsData));
		return returnMap;
	}

	/**
	 * <pre>
	 * desc : 속성상품을 저장한다.(eai)     *
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> saveAttrPrdListToEai(
													List<PrdAttrPrdMinsert> pattrPrdM,
													List<PrdAttrPrdMinsert> pattrPrdMUp,
													List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD,
													List<PrdStockDinsert> prdAttrPrdm,
													List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyDUp,
													List<PrdStockDinsert> prdAttrPrdmUp,
													PrdmMain prdMain
											         ) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		DSData returnDsData = new DSData();

		//스타일직접입력이 'E' -> 'N'으로 변경시 속성상품 전체 재전송함
		if("E".equals(prdMain.getBefStyleDirEntYn()) && "N".equals(prdMain.getStyleDirEntYn())){
			pattrPrdM.clear();
			pattrPrdMUp.clear();

			// 속성상품 조회
			PrdQryCond pPrdQryCond = new PrdQryCond();
			pPrdQryCond.setPrdCd(prdMain.getPrdCd());
			EntityDataSet<DSMultiData> pattrPrdAll = attrPrdEntity.getPrdAttrPrdList(pPrdQryCond);

			// 속성상품 동기화 ( 수정 )
			for (int i = 0; i < pattrPrdAll.size(); i++) {
				PrdAttrPrdMinsert  pattrPrd = null;
				pattrPrd = new PrdAttrPrdMinsert();

				pattrPrd.setPrdCd(pattrPrdAll.getValues().getBigDecimal(i, "prdCd"));
				pattrPrd.setAttrPrdCd(pattrPrdAll.getValues().getBigDecimal(i, "attrPrdCd"));
				pattrPrdMUp.add(pattrPrd);
			}
		}

		// 속성상품 동기화
		for (int i = 0; i < pattrPrdM.size(); i++) {
			PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
			pPrdEaiPrdSyncInfo.setPrdCd(pattrPrdM.get(i).getAttrPrdCd());
			logger.debug("pattrPrdM.get(i).getAttrPrdCd()=>" + pattrPrdM.get(i).getAttrPrdCd());
			// 속성 동기화
			gshseai.GSH_S4C_PRD02.WS.PRD_EAI_Attribute_Product.ws.wsProvider.PRD_EAI_Attribute_Product_P.PRD_EAI_Res resval = wsPrdEaiAttrPrdSyncProcess
					.prdEaiAttrPrdSyncProcess(pPrdEaiPrdSyncInfo);
			logger.debug("resval=>" + resval.getResponseResult());

			if (resval.getResponseResult().equals("E")) {
				returnDsData.put("retCd", "E");
				returnDsData.put("retMsg", Message.getMessage("prd.msg.366"));
				returnMap.put("outPrdDecdItmList", EDSFactory.create(EsbCmm.class, returnDsData));
			}
		}

		// 속성상품 동기화 ( 수정 )
		for (int i = 0; i < pattrPrdMUp.size(); i++) {
			PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
			pPrdEaiPrdSyncInfo.setPrdCd(pattrPrdMUp.get(i).getAttrPrdCd());
			logger.debug("pattrPrdMUp.get(i).getAttrPrdCd()=>" + pattrPrdMUp.get(i).getAttrPrdCd());
			//속성상품 수정인경우 sap전송안함
			if("G".equals(prdMain.getPrdTypCd())
					//|| (prdMain.getPrdCd().toString().length() >= 9) [HANGBOT-28953_GSITMBO-15549][2022.01.10][김진석]:[상품]SAP로 상품코드가 내려오지 않는 현상
					|| "20".equals(prdMain.getBundlPrdGbnCd())){
				pPrdEaiPrdSyncInfo.setSapSaleBlockYn("Y");
			}
	
			// 속성 동기화
			gshseai.GSH_S4C_PRD02.WS.PRD_EAI_Attribute_Product.ws.wsProvider.PRD_EAI_Attribute_Product_P.PRD_EAI_Res resval = wsPrdEaiAttrPrdSyncProcess
					.prdEaiAttrPrdSyncProcess(pPrdEaiPrdSyncInfo);
			logger.debug("resval=>" + resval.getResponseResult());

			if (resval.getResponseResult().equals("E")) {
				returnDsData.put("retCd", "E");
				returnDsData.put("retMsg", Message.getMessage("prd.msg.366"));
				returnMap.put("outPrdDecdItmList", EDSFactory.create(EsbCmm.class, returnDsData));
			}
		}
		returnDsData.put("retCd", "S");
		returnDsData.put("retMsg", Message.getMessage("prd.msg.367"));
		returnMap.put("outPrdDecdItmList", EDSFactory.create(EsbCmm.class, returnDsData));
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : // 속성정보를 입력 저장한다.. .
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG
	 * @date 2010-11-21 10:49:08
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> saveAttrPrdList(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		List<PrdAttrPrdMinsert> pattrPrdM = dataSet.getDataset4NormalObj("inAttrPrdMinsert", PrdAttrPrdMinsert.class); // 속성상품정보 insert
		List<PrdAttrPrdMinsert> pattrPrdMUp = dataSet.getDataset4NormalObj("inAttrPrdMupdate", PrdAttrPrdMinsert.class); // 속성상품정보 update
		List<PrdStockDinsert> prdAttrPrdm = dataSet.getDataset4NormalObj("inPrdStockD", PrdStockDinsert.class); // 상품재고정보
		List<PrdStockDinsert> prdAttrPrdmUp = dataSet.getDataset4NormalObj("inprdStockDUpdate", PrdStockDinsert.class); // 상품재고업데이트정보
		List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD = dataSet.getDataset4NormalObj("inPrdOrdPsblQtyD", PrdOrdPsblQtyDinsert.class); // 상품주문가능수량정보
		List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyDUp = dataSet.getDataset4NormalObj("inPrdOrdPsblQtyDUpdate", PrdOrdPsblQtyDinsert.class); // 상품주문가능수량업데이트정보
		PrdmMain prdMain = dataSet.getDataset4NormalObjFirst("inPrdmain", PrdmMain.class); // 상품속성정보

		returnMap = saveAttrPrdList(pattrPrdM, pattrPrdMUp, prdOrdPsblQtyD, prdAttrPrdm, prdOrdPsblQtyDUp, prdAttrPrdmUp, prdMain);
		DSData dsata = (DSData) returnMap.get("outPrdDecdItmList").getValues();

		DSData returnDsData = new DSData();

		if (dsata.getString("retCd").equals("E")) {
			logger.debug("속성상품 등록 실패");
			returnDsData.put("retCd", "E");
			returnDsData.put("retMsg", Message.getMessage("prd.msg.368", new String[]{"상품속성 저장오류"}));
		} else {
			logger.debug("속성상품 등록성공");
			returnDsData.put("retCd", "S");
		    returnDsData.put("retMsg", Message.getMessage("prd.msg.369", new String[]{"속성상품"}));
		}

		returnMap.put("outPrdDecdItmList", EDSFactory.create(EsbCmm.class, returnDsData));
		return returnMap;
	}

	public void setAttrPrdEndInfoEntity(AttrPrdEndInfoEntity attrPrdEndInfoEntity) {
		this.attrPrdEndInfoEntity = attrPrdEndInfoEntity;
	}

	public void setAttrPrdEntity(AttrPrdEntity attrPrdEntity) {
		this.attrPrdEntity = attrPrdEntity;
	}

	public void setAttrPrdMngCmmProcess(AttrPrdMngCmmProcess attrPrdMngCmmProcess) {
		this.attrPrdMngCmmProcess = attrPrdMngCmmProcess;
	}

	public void setAttrPrdStockShtInfoEntity(AttrPrdStockShtInfoEntity attrPrdStockShtInfoEntity) {
		this.attrPrdStockShtInfoEntity = attrPrdStockShtInfoEntity;
	}

	public void setBrandEntity(BrandEntity brandEntity) {
		this.brandEntity = brandEntity;
	}

	public void setBroadFormPrdEntity(BroadFormPrdEntity broadFormPrdEntity) {
		this.broadFormPrdEntity = broadFormPrdEntity;
	}

	public void setCntrbProfitMngEntity(CntrbProfitMngEntity cntrbProfitMngEntity) {
		this.cntrbProfitMngEntity = cntrbProfitMngEntity;
	}

	public void setCtiMngProcess(CtiMngProcess ctiMngProcess) {
	}

	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : SetDLogging
	 */
//	public void SetDLogging(PrdmMain prdmMain) {
//		// function SetDLogging()
//
//		/*
//		 * try { // var oBusObj = this.BusObject(); var oBusComp = oBusObj.GetBusComp("Deliberation-Log");
//		 *
//		 * ActivateField("Deliberation Level"); ActivateField("deliberation Text"); ActivateField("Id");
//		 *
//		 *
//		 * var sDbrL = this.GetFieldValue("deliberation level"); //협회심의정보.협회심의단계코드 var sDbrD = this.GetFieldValue("deliberation Text");
//		 * //협회심의정보.협회심의내용 var sPartId = this.GetFieldValue("Id"); //상품코드 /*협회심의정보에 등록 --> 기등록건이 있는 경우, 기등록건의 종료일시를 현재일시 -1초로 지정 UPDATE
//		 * PRD_FORGN_TRPT_D SET VALID_END_DTM = :validStrDtm - 0.00001 (SYSDATE) WHERE PRD_CD = :prdCd AND VALID_END_DTM = :validEndDtm ('2999-12-31
//		 * 23:59:59)
//		 *
//		 * 상품코드 유효종료일시 = 2999-12-31 23:59:59 유효시작일시 = SYSDATE 등록일시 = SYSDATE 등록자ID = session.userId 수정일시 = SYSDATE 수정자ID = session.userId 협회심의단계코드
//		 * 협회심의내용
//		 */
//
//		/*
//		 * with ( oBusComp ) { NewRecord(NewAfter);
//		 *
//		 * SetFieldValue("Deliberation Level",sDbrL); SetFieldValue("Deliberation Desc",sDbrD); SetFieldValue("Part Id",sPartId); WriteRecord(); }
//		 * //요기까지 } catch (e) { throw e; } finally { oBusComp = null; oBusObj = null; }
//		 */
//
//	}
	// SOURCE_CLEANSING : END
	
	
	public void setEcShopEntity(EcShopEntity ecShopEntity) {
		this.ecShopEntity = ecShopEntity;
	}

	public void setForgnPrdEntity(ForgnPrdEntity forgnPrdEntity) {
		this.forgnPrdEntity = forgnPrdEntity;
	}

	public void setItemCdEntity(ItemCdEntity itemCdEntity) {
		this.itemCdEntity = itemCdEntity;
	}

	public void setMailSndEntity(MailSndEntity mailSndEntity) {
	}

	public void setMultiCdEntity(MultiCdEntity multiCdEntity) {
		this.multiCdEntity = multiCdEntity;
	}

	public void setOrdPsblQtyEntity(OrdPsblQtyEntity ordPsblQtyEntity) {
		this.ordPsblQtyEntity = ordPsblQtyEntity;
	}

	public void setPrdChanlEntity(PrdChanlEntity prdChanlEntity) {
		this.prdChanlEntity = prdChanlEntity;
	}

	public void setPrdClsBaseEntity(PrdClsBaseEntity prdClsBaseEntity) {
		this.prdClsBaseEntity = prdClsBaseEntity;
	}

	public void setPrdEndLogEntity(PrdEndLogEntity prdEndLogEntity) {
		this.prdEndLogEntity = prdEndLogEntity;
	}

	public void setPrdEntity(PrdEntity prdEntity) {
		this.prdEntity = prdEntity;
	}

	public void setPrdHtmlDescdEntity(PrdHtmlDescdEntity prdHtmlDescdEntity) {
		this.prdHtmlDescdEntity = prdHtmlDescdEntity;
	}

	public void setPrdImpQryInfoEntity(PrdImpQryInfoEntity prdImpQryInfoEntity) {
		this.prdImpQryInfoEntity = prdImpQryInfoEntity;
	}

	public void setPrdMetaInfoEntity(PrdMetaInfoEntity prdMetaInfoEntity) {
		this.prdMetaInfoEntity = prdMetaInfoEntity;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품등록 시  엽관입력 값을 세팅한다.
	 *
	 * </pre>
	 *
	 * @author Sojunggu
	 * @date 2011-02-28 10:49:08
	 * @param SupDtlInfo supDtlInfo, PrdmMain prdmMain, PrdprcHinsert prdprcHinsert
	 *      , PrdPrdDinsert prdPrdD, SafeCertPrd psafeCertPrd
	 *      , List<PrdSpecVal> prdSpecInfo, List<PrdUdaDtl> prdUdaDtl
	 * @return Map
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Map setPrdmRelativeValue(SupDtlInfo supDtlInfo, PrdmMain prdmMain, PrdprcHinsert prdprcHinsert,
			PrdPrdDinsert prdPrdD, SafeCertPrd psafeCertPrd, List<PrdSpecVal> prdSpecInfo, List<PrdUdaDtl> prdUdaDtl) {

		// 사은품유형이 "판매상품" 이 아닌경우 null 설정
		// 반품/교환비, 환불유형, 환불시점, 반품배송비코드, 반품비용코드, 교환비용코드
		//[GRIT-71897]:(내부개선과제) 사은픔/배송품과 경품 상품 환불유형 값 신규등록 시 유지 작업
		if (!"00".equals(StringUtils.NVL(prdmMain.getGftTypCd()))) {
			if("01,02,03,04,07,08".indexOf(StringUtils.NVL(prdmMain.getGftTypCd())) == -1){
				prdmMain.setRfnTypCd(null);
			}
			prdmMain.setArfnTimeCd(null);
			prdmMain.setRtpDlvcCd(null);
			prdmMain.setRtpOnewyRndtrpCd(null);
			prdmMain.setExchOnewyRndtrpCd(null);
		}
		logger.debug("setPrdmRelativeValue1");
		// 배송수거방법코드가 3100(직송설치)인 경우 설치상품여부 = 'Y' 아니면 'N' 20110520
		if ("3100".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()))) {
			prdmMain.setInstlDlvPrdYn("Y");
		} else {
			prdmMain.setInstlDlvPrdYn("N");
		}
		// 사은품,경품구분에서 매입형태는 완전매입 설정
		// - 01 사은품-당사제공, 03 경품-당사제공(고객부담), 04 경품-당사제공(당사부담) 는 매입유형을 "완전매입"으로 설정
		if ("01".equals(StringUtils.NVL(prdmMain.getGftTypCd()))
				|| "03".equals(StringUtils.NVL(prdmMain.getGftTypCd()))
				|| "04".equals(StringUtils.NVL(prdmMain.getGftTypCd()))) {
			prdmMain.setPrchTypCd("01");
		}
		logger.debug("setPrdmRelativeValue2");
		// 당사상품권이 아니면 상품권액면가=null
		// GshsGftcertYn => Y : 당사상품권, N : 당사상품권 아님 ==> 로직을 'N'체크로 변경 (2011/03/29 OSM)
		//if ("Y".equals(StringUtils.NVL(prdmMain.getGshsGftcertYn()))) {
		if ("N".equals(StringUtils.NVL(prdmMain.getGshsGftcertYn(), "N"))) {
			prdmMain.setGftcertFacePrc(null);
		}
		logger.debug("setPrdmRelativeValue3");
		// ditto 상품이 아니면 ditto 단독포장여부는 무조건 'N'설정
		if (!"Y".equals(StringUtils.NVL(prdmMain.getDittoYn()))) {
			prdmMain.setDittoBundlDlvLimitYn("N");
		}
		logger.debug("setPrdmRelativeValue4");
		// 상품반송지를 입력되지 않은 경우 협력사.상품반송지리스트를 조회(s)하여 설정
		// 입력된 상품반송지 주소가 없는 경우 오류
		/* HANGBOT-16987_GSITMBO-9804 지정택배수거 종료
		if ("Y".equals(StringUtils.NVL(prdmMain.getApntDlvsImplmYn()))
				&& "".equals(StringUtils.NVL(prdmMain.getPrdRetpAddrCd()))) {
			PrdQryCond pPrdQryCond1 = new PrdQryCond();
			pPrdQryCond1.setSupCd(prdmMain.getSupCd());
			EntityDataSet<DSMultiData> prddevcBaseLst = prdEntity.setdevcBaseLst(pPrdQryCond1);

			if (prddevcBaseLst.size() > 0) {
				prdmMain.setPrdRetpAddrCd(prddevcBaseLst.getValues().getString("cmmCd"));
			} else {
				prdmMain.setRetCd("-1");
				// 상품의 업체정보에 상품반송지 정보가 없습니다.
				prdmMain.setRetMsg(Message.getMessage("prd.msg.282"));
			}
		} */
		
		logger.debug("setPrdmRelativeValue5");
		// 상품신규등록이고 판매종료일자를 입력하지 않은경우 협력사의 거래종료일자 또는 '2999-12-31' 입력
		if ((prdmMain.getPrdCd() == null || "".equals(prdmMain.getPrdCd().toString()))
				&& "".equals(StringUtils.NVL(prdmMain.getSaleEndDtm()))) {
			if (!"".equals(StringUtils.NVL(supDtlInfo.getTxnEndDt()))) {
				prdmMain.setSaleEndDtm(supDtlInfo.getTxnEndDt());
			} else {
				prdmMain.setSaleEndDtm("29991231235959");
			}
		}
		logger.debug("setPrdmRelativeValue6");
		// 환불방법이 "상품확인후환불10"이면 환불시점은 상품검품후(20) 으로 설정
		// 환불방법이 "상품확인후환불10" 이 아니면 환불시점을 null 설정
		if ("10".equals(StringUtils.NVL(prdmMain.getRfnTypCd()))) {
			prdmMain.setArfnTimeCd("20");
		} else {
			prdmMain.setArfnTimeCd(null);
		}
		logger.debug("setPrdmRelativeValue7");
		// 무형상품 , 대표상품은 환불유형 50(불가)로 설정
		if (!"N".equals(StringUtils.NVL(prdmMain.getFrmlesPrdTypCd()))
				|| "Y".equals(StringUtils.NVL(prdmMain.getRepPrdYn()))) {
			prdmMain.setRfnTypCd("50");
		}

		// 유료배송이 아니면 배송비코드 null 설정
		if (!"Y".equals(StringUtils.NVL(prdmMain.getChrDlvYn()))) {
			prdmMain.setChrDlvcCd(null);
		}
		logger.debug("setPrdmRelativeValue8");
		//if ("GC".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {
			String cmposCntntInfo = "";

			// 기본구성값이 없으면 기본구성수량=0
			if ("".equals(StringUtils.NVL(prdPrdD.getPrdBaseCmposCntnt()).trim())) {
				prdPrdD.setOrgprdPkgCnt(new BigDecimal("0"));
			} else {
				cmposCntntInfo = "[기본]" + prdPrdD.getPrdBaseCmposCntnt();
			}

			// 추가구성값이 없으면 추가구성수량 = 0
			if ("".equals(StringUtils.NVL(prdPrdD.getPrdAddCmposCntnt()).trim())) {
				if( prdPrdD.getAddCmposPkgCnt() == null) {
					prdPrdD.setAddCmposPkgCnt(new BigDecimal("0"));
				}
			} else {
				cmposCntntInfo = cmposCntntInfo + "[추가]" + prdPrdD.getPrdAddCmposCntnt();
			}

			// 사은품구성값이 없으면 사은품구성수량=0
			if ("".equals(StringUtils.NVL(prdPrdD.getPrdGftCmposCntnt()).trim())) {
				if( prdPrdD.getGftPkgCnt() == null) {
				prdPrdD.setGftPkgCnt(new BigDecimal("0"));
				}
			} else {
				cmposCntntInfo = cmposCntntInfo + "[사은품]" + prdPrdD.getPrdGftCmposCntnt();
			}
			if (!"".equals(StringUtils.NVL(prdPrdD.getPrdEtcCmposCntnt()).trim())) {
				cmposCntntInfo = cmposCntntInfo + "[기타]" + prdPrdD.getPrdEtcCmposCntnt();
			}
			cmposCntntInfo = cmposCntntInfo.replaceAll("\r", " ");
			cmposCntntInfo = cmposCntntInfo.replaceAll("\n", " ");
			prdPrdD.setPrdInfoCmposCntnt(cmposCntntInfo);
		//}
		logger.debug("setPrdmRelativeValue9");
		if ("GE".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {
			// 대표상품은 노출예정일=null
			// 대표상품아니면 노출예정일이 null 이면 tomorrow 로 설정
			if ("Y".equals(StringUtils.NVL(prdmMain.getRepPrdYn()))) {
				prdmMain.setExposSchdDt(null);
			} /* 노출예정일 sysdate+1일 변경 로직 제거 20110718
			else {
				if ("".equals(StringUtils.NVL(prdmMain.getExposSchdDt()))) {
					Date tommorow = new Date();

					SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
					tommorow.setTime(tommorow.getTime() + (1000 * 60 * 60 * 24));

					prdmMain.setExposSchdDt(simple.format(tommorow));
				}
			}*/
			logger.debug("setPrdmRelativeValue10");
			// 제휴특판제한 null 이면 N으로 설정
			if ("".equals(StringUtils.NVL(prdmMain.getAliaSpclsalLimitYn()))) {
				prdmMain.setAliaSpclsalLimitYn("N");
			}

			// 최대쿠폰할인 null 이면 0으로 설정
			if (prdmMain.getCpnMaxDcAmt() == null) {
				prdmMain.setCpnMaxDcAmt(new BigDecimal("0"));
			}
			logger.debug("setPrdmRelativeValue12");
			// TV아이콘노출=Y 이고 기존 EC상품구분이 "방송상품60" 인 경우 "방송상품" 으로 설정
			if ("60".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))) {
				prdmMain.setBroadPrdYn("Y");
			}
		}
		logger.debug("setPrdmRelativeValue13");

		/* 2011/12/16 KJH - 스타일 입력여부 셋팅
		 * 2011/12/30 KJY - 스타일 입력여부 디폴트값 설정 임시 제거
		if( !"WITH".equals(prdmMain.getSessionUserId()) ) {
			if("S".equals(prdmMain.getPrdTypCd())
			&& "3".equals(prdmMain.getDlvPickMthodCd().substring(0, 1))
			&& !"3000".equals(prdmMain.getDlvPickMthodCd())
			&& "GE".equals(prdmMain.getRegChanlGrpCd()) ) {

				if(!"E".equals(prdmMain.getStyleDirEntYn()) && !"Y".equals(prdmMain.getStyleDirEntYn())) {
					prdmMain.setStyleDirEntYn("E");
				}
			} else {
				prdmMain.setStyleDirEntYn("N");
			}
			logger.debug("setPrdmRelativeValue14");
		}
		*/

		//2013-05-02 입고택배가 아닐 경우 입고유형 빈값으로 셋팅
		if( !"1".equals(prdmMain.getDlvPickMthodCd().substring(0, 1)) ){
			prdmMain.setIstTypCd("");
		}

		Map resultMap = new HashMap();
		resultMap.put("prdmMain", prdmMain);
		resultMap.put("prdPrdD", prdPrdD);
		return resultMap;
	}

	/**
     * <pre>
     *
     * desc : -사양까지 등록한 후
     *  아이템등록.상품코드 = 전달받은 상품코드
     *  아이템등록여부조회
     *  IF(아이템코드 < 0) THEN
     *   	아이템코드채번
     *  	아이템마스터등록
     *  	아이템구성정보등록
     *  	상품마스터아이템수정
     *  ELSE
     *  	상품마스터아이템수정
     *  END IF
     *
     * </pre>
     * @author KIM-JOO-YOUNG(kbog2089)
     * @date 2011-03-27 07:17:04
     * @param RSPDataSet dataSet
     * @return Map<String, EntityDataSet>
     */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> modifyPrdMstItem(ItemReg pItemReg) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		try {
			//1. 예외아이템(협력사코드, 운영MDID) - 아이템등록여부조회
			EntityDataSet<DSData> getSupItemRegYn = itemCdEntity.getSupItemRegYn(pItemReg);

			if( getSupItemRegYn.getValues() != null ) {
				ItemReg pSupItemReg = DevBeanUtils.wrappedMapToBean(getSupItemRegYn.getValues(), ItemReg.class);
				logger.debug("KJH아이템번호>>>>"+pSupItemReg.getItemCd().intValue());

				if( pSupItemReg.getItemCd().intValue() < 0 ) {
					//아이템코드순번채번
					EntityDataSet<DSData>  getItemCdSeq = itemCdEntity.getItemCdSeq();
					logger.debug("seq ==>"+ getItemCdSeq.getValues().getBigDecimal("itemCd"));

					pSupItemReg.setItemCd(getItemCdSeq.getValues().getBigDecimal("itemCd"));
					pSupItemReg.setPrdCd(pItemReg.getPrdCd());
					pSupItemReg.setSessionObject(pItemReg);

					//상품마스터아이템수정
					prdEntity.modifyPrdMstItem(pSupItemReg);

					//아이템코드등록
					itemCdEntity.addItemCd(pSupItemReg);

					//아이템구성정보등록
					itemCmposInfoEntity.addItemCmposInfo(pSupItemReg);

					//2013-09-02 방송알림 공유 추가
					if( "Y".equals(pItemReg.getPrdPhnalamYn()) ) {
						itemCdEntity.addPhnalamItem(pSupItemReg);
					}

					//아이템의 방송여부를 조회한다.
					EntityDataSet<DSData> getBroadYn = itemCdEntity.getBroadYn(pSupItemReg);
					pSupItemReg.setBroadYn(getBroadYn.getValues().getString("broadYn"));
					Date date = SysUtil.getCurrTime();
					String sysdtm = DateUtils.format(date,"yyyyMMddHHmmss");
					List<PrdItemSyncIfInfo> pPrdBisItemSyncInfoList = new ArrayList<PrdItemSyncIfInfo>();
					PrdItemSyncIfInfo pPrdBisItemSyncInfo = new PrdItemSyncIfInfo();
					logger.debug(pSupItemReg.getItemCd());
					logger.debug(pSupItemReg.getItemNm());
					logger.debug(pSupItemReg.getUseYn());
					logger.debug(pSupItemReg.getPrdGrpCd());
					logger.debug(pSupItemReg.getPrdClsCd());
					logger.debug(pSupItemReg.getBroadYn());
					pPrdBisItemSyncInfo.setJobTyp("I");
					pPrdBisItemSyncInfo.setItemCd(pSupItemReg.getItemCd());
					pPrdBisItemSyncInfo.setItemNm(pSupItemReg.getItemNm());
					pPrdBisItemSyncInfo.setRegDtm(sysdtm);
					pPrdBisItemSyncInfo.setRegrId(pSupItemReg.getSessionUserId());
					pPrdBisItemSyncInfo.setUseYn("Y");
					pPrdBisItemSyncInfo.setPrdGrpCd(pSupItemReg.getPrdGrpCd());
					pPrdBisItemSyncInfo.setPrdClsCd(pSupItemReg.getPrdClsCd());
					pPrdBisItemSyncInfo.setBroadYn(pSupItemReg.getBroadYn());
					pPrdBisItemSyncInfoList.add(pPrdBisItemSyncInfo);

					try {
						wsPrdBisPrdItemMappnSyncProcess.prdBisPrdItemMappnSyncProcess(pPrdBisItemSyncInfoList);
						// 상품 item sap 동기화 (sap 재구축 2013/02/04 안승훈)
						//wsPrdScoProdItemMapProcess.prdScoProdItemMapCm(pPrdBisItemSyncInfoList);
						// [GS리테일 SAP 재무시스템 구축에 따른 Legacy 대응 개발 프로젝트][2023.09.01][최태웅] SAP 통합에 따른 신규 EAI 호출
						wsPrdHcoProdItemMapProcess.prdHcoProdItemMapCm(pPrdBisItemSyncInfoList);
					} catch (Exception e) {
						// Exception 로깅으로 전환 (2011/04/11 OSM)
						//throw new DevPrcException(Message.getMessage("util.msg.007"), e);
						logger.debug("PrdMngCmmProcessImpl.modifyPrdMstItem Interface Exception 1st!!! [" + Message.getMessage("util.msg.007") + "]");
					}

				} else {
					pSupItemReg.setPrdCd(pItemReg.getPrdCd());
					pSupItemReg.setSessionObject(pItemReg);
					//상품마스터아이템수정
					prdEntity.modifyPrdMstItem(pSupItemReg);

					//2013-09-02 방송알림 공유 추가
					if( "Y".equals(pItemReg.getPrdPhnalamYn()) ) {
						itemCdEntity.addPhnalamItem(pSupItemReg);
					}
				}

			} else {
			//2. 아이템등록여부조회
				EntityDataSet<DSData>  getItemRegYn = itemCdEntity.getItemRegYn(pItemReg);
				ItemReg pItemReg1  = DevBeanUtils.wrappedMapToBean(getItemRegYn.getValues(), ItemReg.class);
				//logger.debug("seq ==>"+ getItemRegYn.getValues().getBigDecimal("itemCd"));
				if(getItemRegYn.getValues().getBigDecimal("itemCd").intValue() <   0 ) {
					//아이템코드순번채번
					EntityDataSet<DSData>  getItemCdSeq = itemCdEntity.getItemCdSeq();
					logger.debug("seq ==>"+ getItemCdSeq.getValues().getBigDecimal("itemCd"));

					pItemReg1.setItemCd(getItemCdSeq.getValues().getBigDecimal("itemCd"));
					pItemReg1.setPrdCd(pItemReg.getPrdCd());
					pItemReg1.setSessionObject(pItemReg);

					//상품마스터아이템수정
					prdEntity.modifyPrdMstItem(pItemReg1);

					//아이템코드등록
					itemCdEntity.addItemCd(pItemReg1);

					//아이템구성정보등록
					itemCmposInfoEntity.addItemCmposInfo(pItemReg1);

					//2013-09-02 방송알림 공유 추가
					if( "Y".equals(pItemReg.getPrdPhnalamYn()) ) {
						itemCdEntity.addPhnalamItem(pItemReg1);
					}

					//아이템의 방송여부를 조회한다.
					EntityDataSet<DSData> getBroadYn = itemCdEntity.getBroadYn(pItemReg1);
					pItemReg1.setBroadYn(getBroadYn.getValues().getString("broadYn"));

					Date date = SysUtil.getCurrTime();
					String sysdtm = DateUtils.format(date,"yyyyMMddHHmmss");
					List<PrdItemSyncIfInfo> pPrdBisItemSyncInfoList = new ArrayList<PrdItemSyncIfInfo>();
					PrdItemSyncIfInfo pPrdBisItemSyncInfo = new PrdItemSyncIfInfo();
					logger.debug(pItemReg1.getItemCd());
					logger.debug(pItemReg1.getItemNm());
					logger.debug(pItemReg1.getUseYn());
					logger.debug(pItemReg1.getPrdGrpCd());
					logger.debug(pItemReg1.getPrdClsCd());
					logger.debug(pItemReg1.getBroadYn());
					pPrdBisItemSyncInfo.setJobTyp("I");
					pPrdBisItemSyncInfo.setItemCd(pItemReg1.getItemCd());
					pPrdBisItemSyncInfo.setItemNm(pItemReg1.getItemNm());
					pPrdBisItemSyncInfo.setRegDtm(sysdtm);
					pPrdBisItemSyncInfo.setRegrId(pItemReg1.getSessionUserId());
					pPrdBisItemSyncInfo.setUseYn("Y");
					pPrdBisItemSyncInfo.setPrdGrpCd(pItemReg1.getPrdGrpCd());
					pPrdBisItemSyncInfo.setPrdClsCd(pItemReg1.getPrdClsCd());
					pPrdBisItemSyncInfo.setBroadYn(pItemReg1.getBroadYn());
					pPrdBisItemSyncInfoList.add(pPrdBisItemSyncInfo);

					try {
						wsPrdBisPrdItemMappnSyncProcess.prdBisPrdItemMappnSyncProcess(pPrdBisItemSyncInfoList);
						// 상품 item sap 동기화 (sap 재구축 2013/02/04 안승훈)
						//wsPrdScoProdItemMapProcess.prdScoProdItemMapCm(pPrdBisItemSyncInfoList);
						// [GS리테일 SAP 재무시스템 구축에 따른 Legacy 대응 개발 프로젝트][2023.09.01][최태웅] SAP 통합에 따른 신규 EAI 호출
						wsPrdHcoProdItemMapProcess.prdHcoProdItemMapCm(pPrdBisItemSyncInfoList);
					} catch (Exception e) {
						// Exception 로깅으로 전환 (2011/04/11 OSM)
						//throw new DevPrcException(Message.getMessage("util.msg.007"), e);
						logger.debug("PrdMngCmmProcessImpl.modifyPrdMstItem Interface Exception 1st!!! [" + Message.getMessage("util.msg.007") + "]");
					}
				} else {
					pItemReg1.setPrdCd(pItemReg.getPrdCd());
					pItemReg1.setSessionObject(pItemReg);
					//상품마스터아이템수정
					prdEntity.modifyPrdMstItem(pItemReg1);

					//2013-09-02 방송알림 공유 추가
					if( "Y".equals(pItemReg.getPrdPhnalamYn()) ) {
						itemCdEntity.addPhnalamItem(pItemReg1);
					}
				}
				//-end
			}

		} catch (Exception ex) {
			logger.debug("PrdMngCmmProcessImpl.modifyPrdMstItem ITEM Exception Start!!!");
			logger.debug(ex.toString());
			logger.debug("PrdMngCmmProcessImpl.modifyPrdMstItem ITEM Exception End!!!");
		}

		return returnMap;
	}
	

	/**
     * <pre>
     *
     * desc : 중소기업구분 - 사양까지 등록한 후
     *  아이템등록.상품코드 = 전달받은 상품코드
     *  아이템등록여부조회
     *  IF(아이템코드 < 0) THEN
     *   	아이템코드채번 (smtc)
     *  	아이템마스터등록 (smtc)
     *  	아이템구성정보등록 (smtc)
     *  	상품풀인마스터아이템수정 (webdb)
     *  ELSE
     *  	상품풀인마스터아이템수정 (webdb)
     *  END IF
     *
     * </pre>
     * @author you.wd
     * @date 
     * @param 
     * @return 
     */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> modifyPrdPulinMstItem(ItemReg pItemReg) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		try {
			//1. 예외아이템(협력사코드, 운영MDID) - 아이템등록여부조회
			EntityDataSet<DSData> getSupItemRegYn = itemCdEntity.getPulinSupItemRegYn(pItemReg);

			if( getSupItemRegYn.getValues() != null ) {
				ItemReg pSupItemReg = DevBeanUtils.wrappedMapToBean(getSupItemRegYn.getValues(), ItemReg.class);
				logger.debug("itemCd>>>>" + pSupItemReg.getItemCd().intValue());

				if( pSupItemReg.getItemCd().intValue() < 0 ) {
					//아이템코드순번채번
					EntityDataSet<DSData>  getItemCdSeq = itemCdEntity.getItemCdSeq();
					logger.debug("seq ==>"+ getItemCdSeq.getValues().getBigDecimal("itemCd"));

					pSupItemReg.setItemCd(getItemCdSeq.getValues().getBigDecimal("itemCd"));
					pSupItemReg.setNewPrdPulinCd(pItemReg.getNewPrdPulinCd());
					pSupItemReg.setSessionObject(pItemReg);

					//상품풀인마스터아이템수정
					ecPrdNewPoolInEntity.modifyPrdPulinMstItem(pSupItemReg);

					//신상품풀인 아이템코드등록
					itemCdEntity.addPulinItemCd(pSupItemReg);

					//아이템구성정보등록
					itemCmposInfoEntity.addItemCmposInfo(pSupItemReg);

					/* 상품신규등록 아이템 코드 생성시에 업데이트 가능하므로 여기에서 등록하지 않는다.
					//2013-09-02 방송알림 공유 추가
					if( "Y".equals(pItemReg.getPrdPhnalamYn()) ) {
						itemCdEntity.addPhnalamItem(pSupItemReg);
					}
					*/

					//아이템의 방송여부를 조회한다.
					EntityDataSet<DSData> getBroadYn = itemCdEntity.getBroadYn(pSupItemReg);
					pSupItemReg.setBroadYn(getBroadYn.getValues().getString("broadYn"));
					Date date = SysUtil.getCurrTime();
					String sysdtm = DateUtils.format(date,"yyyyMMddHHmmss");
					List<PrdItemSyncIfInfo> pPrdBisItemSyncInfoList = new ArrayList<PrdItemSyncIfInfo>();
					PrdItemSyncIfInfo pPrdBisItemSyncInfo = new PrdItemSyncIfInfo();
					logger.debug(pSupItemReg.getItemCd());
					logger.debug(pSupItemReg.getItemNm());
					logger.debug(pSupItemReg.getUseYn());
					logger.debug(pSupItemReg.getPrdGrpCd());
					logger.debug(pSupItemReg.getPrdClsCd());
					logger.debug(pSupItemReg.getBroadYn());
					pPrdBisItemSyncInfo.setJobTyp("I");
					pPrdBisItemSyncInfo.setItemCd(pSupItemReg.getItemCd());
					pPrdBisItemSyncInfo.setItemNm(pSupItemReg.getItemNm());
					pPrdBisItemSyncInfo.setRegDtm(sysdtm);
					pPrdBisItemSyncInfo.setRegrId(pSupItemReg.getSessionUserId());
					pPrdBisItemSyncInfo.setUseYn("Y");
					pPrdBisItemSyncInfo.setPrdGrpCd(pSupItemReg.getPrdGrpCd());
					pPrdBisItemSyncInfo.setPrdClsCd(pSupItemReg.getPrdClsCd());
					pPrdBisItemSyncInfo.setBroadYn(pSupItemReg.getBroadYn());
					pPrdBisItemSyncInfoList.add(pPrdBisItemSyncInfo);

					try {
						wsPrdBisPrdItemMappnSyncProcess.prdBisPrdItemMappnSyncProcess(pPrdBisItemSyncInfoList);
						// 상품 item sap 동기화 (sap 재구축 2013/02/04 안승훈)
						//wsPrdScoProdItemMapProcess.prdScoProdItemMapCm(pPrdBisItemSyncInfoList);
						// [GS리테일 SAP 재무시스템 구축에 따른 Legacy 대응 개발 프로젝트][2023.09.01][최태웅] SAP 통합에 따른 신규 EAI 호출
						wsPrdHcoProdItemMapProcess.prdHcoProdItemMapCm(pPrdBisItemSyncInfoList);
					} catch (Exception e) {
						// Exception 로깅으로 전환 (2011/04/11 OSM)
						//throw new DevPrcException(Message.getMessage("util.msg.007"), e);
						logger.debug("PrdMngCmmProcessImpl.modifyPrdPulinMstItem Interface Exception 1st!!! [" + Message.getMessage("util.msg.007") + "]");
					}

				} else {
					pSupItemReg.setNewPrdPulinCd(pItemReg.getNewPrdPulinCd());
					pSupItemReg.setSessionObject(pItemReg);
					//상품풀인마스터아이템수정
					ecPrdNewPoolInEntity.modifyPrdPulinMstItem(pSupItemReg);

					/* 상품신규등록 아이템 코드 생성시에 업데이트 가능하므로 여기에서 등록하지 않는다.
					//2013-09-02 방송알림 공유 추가
					if( "Y".equals(pItemReg.getPrdPhnalamYn()) ) {
						itemCdEntity.addPhnalamItem(pSupItemReg);
					}
					*/
				}

			} else {
			//2. 아이템등록여부조회
				EntityDataSet<DSData>  getItemRegYn = itemCdEntity.getPulinItemRegYn(pItemReg);
				ItemReg pItemReg1  = DevBeanUtils.wrappedMapToBean(getItemRegYn.getValues(), ItemReg.class);
				//logger.debug("seq ==>"+ getItemRegYn.getValues().getBigDecimal("itemCd"));
				if(getItemRegYn.getValues().getBigDecimal("itemCd").intValue() <   0 ) {
					//아이템코드순번채번
					EntityDataSet<DSData>  getItemCdSeq = itemCdEntity.getItemCdSeq();
					logger.debug("seq ==>"+ getItemCdSeq.getValues().getBigDecimal("itemCd"));

					pItemReg1.setItemCd(getItemCdSeq.getValues().getBigDecimal("itemCd"));
					pItemReg1.setNewPrdPulinCd(pItemReg.getNewPrdPulinCd());
					pItemReg1.setSessionObject(pItemReg);

					//상품풀인마스터아이템수정
					ecPrdNewPoolInEntity.modifyPrdPulinMstItem(pItemReg1);

					//신상품풀인 아이템코드등록
					itemCdEntity.addPulinItemCd(pItemReg1);

					//아이템구성정보등록
					itemCmposInfoEntity.addItemCmposInfo(pItemReg1);

					/* 상품신규등록 아이템 코드 생성시에 업데이트 가능하므로 여기에서 등록하지 않는다.
					//2013-09-02 방송알림 공유 추가
					if( "Y".equals(pItemReg.getPrdPhnalamYn()) ) {
						itemCdEntity.addPhnalamItem(pItemReg1);
					}
					*/

					//아이템의 방송여부를 조회한다.
					EntityDataSet<DSData> getBroadYn = itemCdEntity.getBroadYn(pItemReg1);
					pItemReg1.setBroadYn(getBroadYn.getValues().getString("broadYn"));

					Date date = SysUtil.getCurrTime();
					String sysdtm = DateUtils.format(date,"yyyyMMddHHmmss");
					List<PrdItemSyncIfInfo> pPrdBisItemSyncInfoList = new ArrayList<PrdItemSyncIfInfo>();
					PrdItemSyncIfInfo pPrdBisItemSyncInfo = new PrdItemSyncIfInfo();
					logger.debug(pItemReg1.getItemCd());
					logger.debug(pItemReg1.getItemNm());
					logger.debug(pItemReg1.getUseYn());
					logger.debug(pItemReg1.getPrdGrpCd());
					logger.debug(pItemReg1.getPrdClsCd());
					logger.debug(pItemReg1.getBroadYn());
					pPrdBisItemSyncInfo.setJobTyp("I");
					pPrdBisItemSyncInfo.setItemCd(pItemReg1.getItemCd());
					pPrdBisItemSyncInfo.setItemNm(pItemReg1.getItemNm());
					pPrdBisItemSyncInfo.setRegDtm(sysdtm);
					pPrdBisItemSyncInfo.setRegrId(pItemReg1.getSessionUserId());
					pPrdBisItemSyncInfo.setUseYn("Y");
					pPrdBisItemSyncInfo.setPrdGrpCd(pItemReg1.getPrdGrpCd());
					pPrdBisItemSyncInfo.setPrdClsCd(pItemReg1.getPrdClsCd());
					pPrdBisItemSyncInfo.setBroadYn(pItemReg1.getBroadYn());
					pPrdBisItemSyncInfoList.add(pPrdBisItemSyncInfo);

					try {
						wsPrdBisPrdItemMappnSyncProcess.prdBisPrdItemMappnSyncProcess(pPrdBisItemSyncInfoList);
						// 상품 item sap 동기화 (sap 재구축 2013/02/04 안승훈)
						//wsPrdScoProdItemMapProcess.prdScoProdItemMapCm(pPrdBisItemSyncInfoList);
						// [GS리테일 SAP 재무시스템 구축에 따른 Legacy 대응 개발 프로젝트][2023.09.01][최태웅] SAP 통합에 따른 신규 EAI 호출
						wsPrdHcoProdItemMapProcess.prdHcoProdItemMapCm(pPrdBisItemSyncInfoList);
					} catch (Exception e) {
						// Exception 로깅으로 전환 (2011/04/11 OSM)
						//throw new DevPrcException(Message.getMessage("util.msg.007"), e);
						logger.debug("PrdMngCmmProcessImpl.modifyPrdPulinMstItem Interface Exception 1st!!! [" + Message.getMessage("util.msg.007") + "]");
					}
				} else {
					pItemReg1.setNewPrdPulinCd(pItemReg.getNewPrdPulinCd());
					pItemReg1.setSessionObject(pItemReg);
					//상품풀인마스터아이템수정
					ecPrdNewPoolInEntity.modifyPrdPulinMstItem(pItemReg1);

					/* 상품신규등록 아이템 코드 생성시에 업데이트 가능하므로 여기에서 등록하지 않는다.
					//2013-09-02 방송알림 공유 추가
					if( "Y".equals(pItemReg.getPrdPhnalamYn()) ) {
						itemCdEntity.addPhnalamItem(pItemReg1);
					}
					*/
				}
				//-end
			}

		} catch (Exception ex) {
			logger.debug("PrdMngCmmProcessImpl.modifyPrdPulinMstItem ITEM Exception Start!!!");
			logger.debug(ex.toString());
			logger.debug("PrdMngCmmProcessImpl.modifyPrdPulinMstItem ITEM Exception End!!!");
		}

		return returnMap;
	}
	
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : Var_App_Set
	 */
//	public void Var_App_Set(PrdmMain prdmMain) {
//		// function Var_App_Set()
//		// try {
//		/*
//		 * var sProdId = this.GetFieldValue("Id"); //상품코드 var QAApp = "25"; //상품결재상태코드 : QA승인 var MDApp = "30"; //상품결재상태코드 : MD승인 var sVarId,
//		 * sVarCode;
//		 */
////		BigDecimal sProdId = prdmMain.getPrdCd(); // 상품코드
////		String QAApp = "25"; // 상품결재상태코드 : QA승인
////		String MDApp = "30"; // 상품결재상태코드 : MD승인
////		String sVarId, sVarCode;
//		/* 속상상품마스터 지정 */
//		// var oBusObj = TheApplication().GetBusObject("Admin Product Definition");
//		// var oBusComp = oBusObj.GetBusComp("LGHS Variants");
//		// 요기까지
//		/*
//		 * with(oBusComp) { ActivateField("Product Id"); ActivateField("Approval Status"); ClearToQuery(); SetSearchExpr('[Product Id] =
//		 * "' + sProdId + '"' + ' and [Approval Status] = "' + QAApp + '"'); //상품코드 = :prdCd, 속성상품결재상태코드 = '25' ExecuteQuery(ForwardBackward); var
//		 * isRecord = FirstRecord(); //조회목록에 대하여 while(isRecord) //Variants의 Approval Status Setting { sVarId = GetFieldValue("Id"); //속성상품코드 sVarCode
//		 * = GetFieldValue("Variants Code"); //속성상품코드 Var_App_Set_Check_VChannel(sVarId, sVarCode,sProdId); //펑션추가 --> 제거
//		 * SetFieldValue("Approval Status",MDApp); //속성상품결재상태코드 = '30' WriteRecord(); isRecord = NextRecord(); } } oBusComp = null; oBusObj = null; }
//		 *
//		 * catch (e) { throw e; }
//		 */
//	}
	// SOURCE_CLEANSING : END
	
	
	/**
	 * <pre>
	 *
	 * desc : 원천세와 판매가를 계산한다.
	 *
	 * </pre>
	 * @author OSM
	 * @date 2011. 4. 10. 오후 12:41:08
	 * @param List<PrdCalcProprdWthtax> PrdCalcProprdWthtax
	 * @return List<PrdCalcProprdWthtax>
	 * @throws DevEntException
	 */
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : calcProprdWthtaxList
	 */
//	public List<PrdCalcProprdWthtax> calcProprdWthtaxList(List<PrdCalcProprdWthtax> prdCalcProprdWthtaxList) {
//		int i = 0;
//		for (PrdCalcProprdWthtax prdCalcProprdWthtax : prdCalcProprdWthtaxList) {
//			calcProprdWthtax(prdCalcProprdWthtax);
//			prdCalcProprdWthtaxList.get(i).setSalePrc(prdCalcProprdWthtax.getSalePrc());
//			prdCalcProprdWthtaxList.get(i).setProprdWthtax(prdCalcProprdWthtax.getProprdWthtax());
//			i++;
//		}
//		return prdCalcProprdWthtaxList;
//	}
	// SOURCE_CLEANSING : END
	
	
	
	/**
	 * <pre>
	 *
	 * desc : 원천세와 판매가를 계산한다.
	 * [SR02161118015][2016.11.24][김영현]:경품공동부담 프로세스 개발 관련 로직 변경의 건 
	 *
	 * </pre>
	 * @author OSM
	 * @date 2011. 4. 10. 오후 12:41:08
	 * @param PrdCalcProprdWthtax prdCalcProprdWthtax
	 * @return PrdCalcProprdWthtax
	 * @throws DevEntException
	 */
	public PrdCalcProprdWthtax calcProprdWthtax(PrdCalcProprdWthtax prdCalcProprdWthtax) {
		BigDecimal prdTaxAmt = new BigDecimal(0);
		BigDecimal prdSo = new BigDecimal(0);
		BigDecimal prdJu = new BigDecimal(0);
		BigDecimal proprdWthtax = new BigDecimal(0);
		BigDecimal salePrc = new BigDecimal(0);
		BigDecimal chkPt = new BigDecimal(0);
		String taxTypCd = (prdCalcProprdWthtax.getTaxTypCd() == null)
			? "" : prdCalcProprdWthtax.getTaxTypCd();
		String gftTypCd = (prdCalcProprdWthtax.getGftTypCd() == null)
			? "" : prdCalcProprdWthtax.getGftTypCd();
		BigDecimal prchPrc = (prdCalcProprdWthtax.getPrchPrc() == null)
			? new BigDecimal(0) : prdCalcProprdWthtax.getPrchPrc();
		BigDecimal supProprdUprc = (prdCalcProprdWthtax.getSupProprdUprc() == null)
			? new BigDecimal(0) : prdCalcProprdWthtax.getSupProprdUprc();

		// 세금유형코드나 사은품유형코드가 입력되지 않으면 -1을 리턴
		if (taxTypCd == "" || gftTypCd == "") {
			prdCalcProprdWthtax.setProprdWthtax(new BigDecimal(-1));
			prdCalcProprdWthtax.setSalePrc(new BigDecimal(-1));
			return prdCalcProprdWthtax;
		}
		logger.debug("세금유형코드(taxTypCd)==>[" + taxTypCd + "], 매입유형(사은품유형)코드(gftTypCd)==>[" + gftTypCd + "]");
		logger.debug("매입금액(prchPrc)==>[" + prchPrc + "], 경품단가(supProprdUprc)==>[" + supProprdUprc + "]");
		/** [2014.12.02][유수경]제세공과금계산로직 변경
		 *  고객부담 * 0.22, 당사부담/0.78*0.22 
		 * gftTypCd
			04	경품-당사제공(당사부담)			08	경품-업체제공(업체부담)
			03	경품-당사제공(고객부담)			07	경품-업체제공(고객부담)
			
			taxTypCd
			04	경품 과세			05	경품 면세
			*/
		//1. 세금유형코드에 따라 경품원천세를 계산하여 Setting한다.
		 //[SR02161118015][2016.11.24][김영현]:경품공동부담 프로세스 개발 관련 로직 변경의 건 
		//[HANGBOT-28095_GSITMBO-17845]:경품 제세공과금 계산로직 및 신고데이터 반영기준 변경요청
		if ("04".equals(taxTypCd)) {	//IF 세금유형코드 == '04' THEN(경품과세인 경우)
			//prdTaxAmt = (int)Math.floor((매입가격) * 0.1 /10) * 10; --> (매입가격) * 0.1
			
			//IF 사은품유형코드 == '03' THEN (사은품유형코드가 '경품-당사제공(고객부담)'인 경우)
			//[SR02160729022][2016.08.31][김영현]:경품 공동부담 개발요청
			if ("03".equals(gftTypCd) || "04".equals(gftTypCd) ) {
				//완전매입인 경우 매입가격으로 계산
				/*
				prdTaxAmt = prchPrc.multiply(BigDecimal.valueOf(0.1)).
						divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
						setScale(0, BigDecimal.ROUND_FLOOR).
						multiply(new BigDecimal(10));		*/		
				prdTaxAmt = prchPrc.multiply(BigDecimal.valueOf(0.1));
			}else{
				//수수료매입인 경우 경품단가로 계산
				/*
				prdTaxAmt = supProprdUprc.multiply(BigDecimal.valueOf(0.1)).
						divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
						setScale(0, BigDecimal.ROUND_FLOOR).
						multiply(new BigDecimal(10));		*/
				prdTaxAmt = supProprdUprc.multiply(BigDecimal.valueOf(0.1));
			}
			logger.debug("1. prdTaxAmt==>[" + prdTaxAmt + "]");
			if ("03".equals(gftTypCd) ) {
				//prdSo = (int)Math.floor(((매입가격 + prdTaxAmt) * 0.2) / 10) * 10; --> (매입가격 + prdTaxAmt) * 0.2
				/*
				prdSo = prchPrc.add(prdTaxAmt).
							multiply(BigDecimal.valueOf(0.2)).
							divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
							setScale(0, BigDecimal.ROUND_FLOOR).
							multiply(new BigDecimal(10));	*/
				prdSo = prchPrc.add(prdTaxAmt).multiply(BigDecimal.valueOf(0.2));
			} else if ("04".equals(gftTypCd) ) {	//경품-당사제공(당사부담)
				//prdSo = (int) Math.floor((((매입가격 + prdTaxAmt) / 0.78) * 0.2) / 10) * 10;  --> (매입가격 + prdTaxAmt) / 0.78) * 0.2
				/*
				prdSo = prchPrc.add(prdTaxAmt).
							divide(BigDecimal.valueOf(0.78), 16, BigDecimal.ROUND_HALF_UP).
							multiply(BigDecimal.valueOf(0.2)).
							divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
							setScale(0, BigDecimal.ROUND_FLOOR).
							multiply(new BigDecimal(10));*/
				prdSo = prchPrc.add(prdTaxAmt).
						divide(BigDecimal.valueOf(0.78), 16, BigDecimal.ROUND_HALF_UP).
						multiply(BigDecimal.valueOf(0.2));
			} else if ("07".equals(gftTypCd) ) { //경품-업체제공(고객부담)
				//prdSo = (int)Math.floor(((매입가격 + prdTaxAmt) * 0.2) / 10) * 10; -->(매입가격 + prdTaxAmt) * 0.2 
				/*prdSo = supProprdUprc.add(prdTaxAmt).
							multiply(BigDecimal.valueOf(0.2)).
							divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
							setScale(0, BigDecimal.ROUND_FLOOR).
							multiply(new BigDecimal(10));*/
				prdSo = supProprdUprc.add(prdTaxAmt).multiply(BigDecimal.valueOf(0.2));
			} else if ("08".equals(gftTypCd) ) {	//경품-업체제공(당사부담)
				//prdSo = (int) Math.floor((((매입가격 + prdTaxAmt) / 0.78) * 0.2) / 10) * 10; -->(매입가격 + prdTaxAmt) / 0.78) * 0.2 
				/*prdSo = supProprdUprc.add(prdTaxAmt).
							divide(BigDecimal.valueOf(0.78), 16, BigDecimal.ROUND_HALF_UP).
							multiply(BigDecimal.valueOf(0.2)).
							divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
							setScale(0, BigDecimal.ROUND_FLOOR).
							multiply(new BigDecimal(10));	*/
				prdSo = supProprdUprc.add(prdTaxAmt).
						divide(BigDecimal.valueOf(0.78), 16, BigDecimal.ROUND_HALF_UP).
						multiply(BigDecimal.valueOf(0.2));
			}
			
			
			logger.debug("2. prdSo==>[" + prdSo + "]");

			//prdJu = Floor((prdSo * 0.1) / 10) * 10;  -->prdSo * 0.1
			/*
			prdJu = prdSo.multiply(BigDecimal.valueOf(0.1)).
						divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
						setScale(0, BigDecimal.ROUND_FLOOR).
						multiply(new BigDecimal(10));*/
			prdJu = prdSo.multiply(BigDecimal.valueOf(0.1));
			logger.debug("3. prdJu==>[" + prdJu + "]");
			//proprdWthtax = Round((prdSo + prdJu) / 10) * 10; --> floor((prdSo + prdJu) / 10) * 10;
			/*
			  proprdWthtax = prdSo.add(prdJu).
						divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
						setScale(0, BigDecimal.ROUND_HALF_UP).
						multiply(new BigDecimal(10));			* 
			 */
			proprdWthtax = prdSo.add(prdJu).
						divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
						setScale(0, BigDecimal.ROUND_FLOOR).
						multiply(new BigDecimal(10));
			logger.debug("4. proprdWthtax==>[" + proprdWthtax + "]");
			//chkPt = prdTaxAmt + ToNumber(prchPrc);	//chkPt = prdTaxAmt + 매입가격;
			if ("03".equals(gftTypCd) || "04".equals(gftTypCd) ) {
				chkPt = prdTaxAmt.add(prchPrc);
			}else if ("07".equals(gftTypCd) || "08".equals(gftTypCd) ) {
				chkPt = prdTaxAmt.add(supProprdUprc);
			}
			logger.debug("5. chkPt==>[" + chkPt + "]");

			if (chkPt.compareTo(new BigDecimal(100000)) < 1)
				proprdWthtax = new BigDecimal(0);

			logger.debug("6. proprdWthtax==>[" + proprdWthtax + "]");
		}
		//ELSE IF 세금유형코드 == '05' THEN (경품면세인 경우)
		else if ("05".equals(taxTypCd)) {
			//IF 사은품유형코드 == '03' THEN (경품-당사제공(고객부담)인 경우)
			if ("03".equals(gftTypCd)) {
				//prdSo = (int) Math.floor((매입가격 * 0.2) / 10) * 10; --> 매입가격 * 0.2
				/*prdSo = prchPrc.multiply(BigDecimal.valueOf(0.2)).
						divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
						setScale(0, BigDecimal.ROUND_FLOOR).
						multiply(new BigDecimal(10));*/
				prdSo = prchPrc.multiply(BigDecimal.valueOf(0.2));
			}
			//ELSE IF 사은품유형코드 == '04' THEN (경품-당사제공(당사부담)인 경우)
			else if ("04".equals(gftTypCd)) {
				//prdSo = (int) Math.floor(((매입가격 /0.78) * 0.2) / 10) * 10; --> (매입가격 /0.78) * 0.2
				/*prdSo = prchPrc.divide(BigDecimal.valueOf(0.78), 16, BigDecimal.ROUND_HALF_UP).
						multiply(BigDecimal.valueOf(0.2)).
						divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
						setScale(0, BigDecimal.ROUND_FLOOR).
						multiply(new BigDecimal(10));*/
				prdSo = prchPrc.divide(BigDecimal.valueOf(0.78), 16, BigDecimal.ROUND_HALF_UP).
						multiply(BigDecimal.valueOf(0.2));
			}
			//ELSE IF 사은품유형코드 == '07' AND 협력사경품단가 >= 100000 THEN
			else if ("07".equals(gftTypCd) && supProprdUprc.compareTo(new BigDecimal(100000)) > 0){
				//prdSo = (int) Math.floor((협력사경품단가 * 0.2) / 10) * 10; --> 협력사경품단가 * 0.2
				/*prdSo = supProprdUprc.multiply(BigDecimal.valueOf(0.2)).
						divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
						setScale(0, BigDecimal.ROUND_FLOOR).
						multiply(new BigDecimal(10));*/
				prdSo = supProprdUprc.multiply(BigDecimal.valueOf(0.2));
			}else if ("08".equals(gftTypCd) && supProprdUprc.compareTo(new BigDecimal(100000)) > 0){
				//prdSo = (int) Math.floor(((매입가격 /0.78) * 0.2) / 10) * 10;  --> (매입가격 /0.78) * 0.2
				/*prdSo = supProprdUprc.divide(BigDecimal.valueOf(0.78), 16, BigDecimal.ROUND_HALF_UP).
						multiply(BigDecimal.valueOf(0.2)).
						divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
						setScale(0, BigDecimal.ROUND_FLOOR).
						multiply(new BigDecimal(10));*/
				prdSo = supProprdUprc.divide(BigDecimal.valueOf(0.78), 16, BigDecimal.ROUND_HALF_UP).
						multiply(BigDecimal.valueOf(0.2));
			}	
			logger.debug("7. prdSo==>[" + prdSo + "]");

			//prdJu = Floor((prdSo * 0.1) / 10) * 10; -->prdSo * 0.1 
			/*prdJu = prdSo.multiply(BigDecimal.valueOf(0.1)).
					divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
					setScale(0, BigDecimal.ROUND_FLOOR).
					multiply(new BigDecimal(10));*/
			prdJu = prdSo.multiply(BigDecimal.valueOf(0.1));
			logger.debug("8. prdJu==>[" + prdJu + "]");
			//proprdWthtax = Round((prdSo + prdJu) / 10) * 10; --> floor((prdSo + prdJu) / 10) * 10 
			/*proprdWthtax = prdSo.add(prdJu).
					divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
					setScale(0, BigDecimal.ROUND_HALF_UP).
					multiply(new BigDecimal(10));*/
			proprdWthtax = prdSo.add(prdJu).
					divide(new BigDecimal(10), 16, BigDecimal.ROUND_HALF_UP).
					setScale(0, BigDecimal.ROUND_FLOOR).
					multiply(new BigDecimal(10));
			logger.debug("9. proprdWthtax==>[" + proprdWthtax + "]");
/*
 * 	04	경품-당사제공(당사부담)			08	경품-업체제공(업체부담)			03	경품-당사제공(고객부담)			07	경품-업체제공(고객부담)
 */
			//IF 사은품유형코드 == '03' THEN
			if ("03".equals(gftTypCd)) {
				//IF 매입가격 <= 100000 THEN
				if (prchPrc.compareTo(new BigDecimal(100000)) < 1 )
					proprdWthtax = new BigDecimal(0);
			}
			//ELSE IF 사은품유형코드 == '04' THEN
			else if ("04".equals(gftTypCd)){
			    //IF 매입가격 <= 100000 THEN
				if (prchPrc.compareTo(new BigDecimal(100000)) < 1)
					proprdWthtax = new BigDecimal(0);
			}
			//ELSE IF 사은품유형코드 == '08' THEN
			else if ("08".equals(gftTypCd)){
			    //IF 매입가격 <= 100000 THEN
				if (supProprdUprc.compareTo(new BigDecimal(100000)) < 1)
					proprdWthtax = new BigDecimal(0);
			}
			//ELSE IF 사은품유형코드 == '07' AND 협력사경품단가 <= 100000 THEN
			else if ("07".equals(gftTypCd) && supProprdUprc.compareTo(new BigDecimal(100000)) < 1)
				proprdWthtax = new BigDecimal(0);
           
			logger.debug("10. proprdWthtax==>[" + proprdWthtax + "]");
		}

		//2. 사은품유형코드에 따라 판매가격을 원천세 또는 0으로 Setting
		if ("03".equals(gftTypCd) || "07".equals(gftTypCd)) {
			salePrc = proprdWthtax;	//판매가격 = proprdWthtax;
			logger.debug("11. salePrc==>[" + salePrc + "]");
		} else if("01".equals(gftTypCd) || "02".equals(gftTypCd) ||
				"04".equals(gftTypCd) || "08".equals(gftTypCd)   ) {
			salePrc = new BigDecimal(0);	//판매가격 = 0;
			logger.debug("12. salePrc==>[" + salePrc + "]");
		}

		prdCalcProprdWthtax.setProprdWthtax(proprdWthtax);	// 원천세
		prdCalcProprdWthtax.setSalePrc(salePrc);			// 판매가

		return prdCalcProprdWthtax;
	}

	@Override
	public List<PrdDescdSyncInfo> addPrdDescdS4c( PrdmMain prdmMain, List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList,   List<PrdDescdHtmlDInfo>  prdDescdHtmlDInfoList  ) {
		List<PrdDescdSyncInfo> prdDescdSyncInfoList = new ArrayList<PrdDescdSyncInfo>();
		PrdDesceGenrlDInfo prdDesceGenrlDInfo1  = new PrdDesceGenrlDInfo(); // 심의요청 용
		PrdDescdHtmlDInfo  prdDescdHtmlDInfo  = new PrdDescdHtmlDInfo();
		EntityDataSet<DSData> entityDataSet = null;

		// 일반기술서 승인 등록
		logger.debug("qryGbn==="+prdmMain.getQryGbn());
		if("Y".equals(StringUtils.NVL(prdmMain.getQryGbn()))) {
			// 기술서를 조회
			SupPrdAprvQryCond pSupPrdAprvQryCond = new SupPrdAprvQryCond();
			pSupPrdAprvQryCond.setPrdCd(prdmMain.getPrdCd());
			pSupPrdAprvQryCond.setSupCd(prdmMain.getSupCd());
			pSupPrdAprvQryCond.setSupPrdCd(prdmMain.getSupPrdCd());

			pSupPrdAprvQryCond.setSuppGoodsCode(prdmMain.getSupPrdCd());
			//GE-HTML/GD일 경우는-일반기술서
			//채널에 상관없이 등록 처리
		//	if("GE".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()) )){
				// HTML기술서 승인 등록
					DSMultiData copyHtml = ecScmMngEntity.getSupPrdAprvDescdHtmlDList(pSupPrdAprvQryCond).getValues();
					if (copyHtml != null) {
						List<PrdHtmlDescd> prdHtmlList = new ArrayList<PrdHtmlDescd>();
						for (int i = 0; i < copyHtml.size(); i++) {
							PrdHtmlDescd prdHtml = new PrdHtmlDescd();
							prdHtml.setPrdCd(prdmMain.getPrdCd()); // 상품코드
							prdHtml.setChanlCd(copyHtml.get(i).getString("chanlCd")); // 채널코드
							prdHtml.setRegGbnCd(copyHtml.get(i).getString("regGbnCd")); // 등록구분코드
							prdHtml.setDescdExplnCntnt(copyHtml.get(i).getString("descdExplnCntnt")); // 기술서설명내용
							prdHtml.setRcmdSntncCntnt(copyHtml.get(i).getString("rcmdSntncCntnt")); // 추천문구내용
							prdHtml.setWritePrevntYn(copyHtml.get(i).getString("writePrevntYn")); // 쓰기방지여부
							prdHtml.setEcExposYn("Y"); // EC노출여부
							prdHtml.setSessionUserIp(prdmMain.getSessionUserIp());
							prdHtml.setSessionUserId(prdmMain.getSessionUserId());
							prdHtml.setSessionUserNm(prdmMain.getSessionUserNm());
							prdHtmlList.add(prdHtml);
						}
						prdHtmlDescdEntity.addHtmlDescdList(prdHtmlList); // 상품HTML기술서등록
					}
		//	} else
		//	if("GD".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {
			     DSMultiData copyDesc = ecScmMngEntity.getSupPrdAprvDescdGenrlDList(pSupPrdAprvQryCond).getValues();
				if (copyDesc != null) {
					for (int i = 0; i < copyDesc.size(); i++) {
						PrdDesceGenrlDInfo prdDesceGenrlDInfo = new PrdDesceGenrlDInfo();
						// 상품기술서 정보를 INSERT -> 시퀀스 채번 후 입력방식으로 변경 (2011/03/26 OSM)
						entityDataSet = prdEntity.getDescdItmSeq();
						prdDesceGenrlDInfo.setDescdItmSeq(entityDataSet.getValues().getBigDecimal("descdItmSeq"));//기술서항목순번
						prdDesceGenrlDInfo.setPrdCd(prdmMain.getPrdCd()); // 상품코드
						prdDesceGenrlDInfo.setChanlCd(copyDesc.get(i).getString("chanlCd")); // 채널코드
						prdDesceGenrlDInfo.setDescdItmNm(copyDesc.get(i).getString("descdItmNm")); // 기술서항목명
						prdDesceGenrlDInfo.setDescdExplnCntnt(copyDesc.get(i).getString("descdExplnCntnt")); // 기술서설명내용
						prdDesceGenrlDInfo.setDescdItmCd(copyDesc.get(i).getString("descdItmCd")); // 기술서항목코드
						prdDesceGenrlDInfo.setSortSeq(copyDesc.get(i).getBigDecimal("sortSeq")); // 정렬순서
						prdDesceGenrlDInfo.setItmHiddnYn(copyDesc.get(i).getString("itmHiddnYn")); // 항목숨김여부
						prdDesceGenrlDInfo.setWrapYn(copyDesc.get(i).getString("wrapYn")); // 줄바꿈여부
						prdDesceGenrlDInfo.setLineInsertYn(copyDesc.get(i).getString("lineInsertYn")); // 줄삽입여부
						prdDesceGenrlDInfo.setFlckrYn(copyDesc.get(i).getString("flckrYn")); // 점멸여부
						prdDesceGenrlDInfo.setLtrColorNm(copyDesc.get(i).getString("ltrColorNm")); // 글자색상명
						prdDesceGenrlDInfo.setIntrntExposYn(copyDesc.get(i).getString("intrntExposYn")); // 인터넷노출여부
						prdDesceGenrlDInfo.setEaiLinkYn(copyDesc.get(i).getString("eaiLinkYn")); // EAI연동여부
						prdDesceGenrlDInfo.setSessionUserIp(prdmMain.getSessionUserIp());
						prdDesceGenrlDInfo.setSessionUserId(prdmMain.getSessionUserId());
						prdDesceGenrlDInfo.setSessionUserNm(prdmMain.getSessionUserNm());

						prdEntity.setPrdDescdGenrlD(prdDesceGenrlDInfo);
						prdDescdSyncInfoList.add(new PrdDescdSyncInfo(" " // attrPrdRepCd 상품코드 PRD_STOCK_D 테이블
						        , " " // descdExplnCntnt 기술서설명내용
						        , prdDesceGenrlDInfo.getDescdItmSeq() // descdItmSeq 기술서항목순번
						        , " " // genrlDescdId 일반기술서ID
						        , "I" // jobType JOB TYPE I:등록 D: 삭제 U: 수정
						        , prdDesceGenrlDInfo.getPrdCd() // prdCd 상품코드
						        ));
						prdDescdHtmlDInfo.setPrdCd(prdmMain.getPrdCd()) ;
						//prdDescdHtmlDInfo.setChanlCd(prdDesceGenrlDInfo.getChanlCd());
						prdDescdHtmlDInfo.setChanlCd("P");
						prdDescdHtmlDInfo.setRegGbnCd("A");
						prdDescdHtmlDInfo.setSessionObject(prdDesceGenrlDInfo);
						// 일반 기술서 등록 후  HTML 기술서 변환

						prdDesceGenrlDInfo1.setPrdCd(prdDesceGenrlDInfo.getPrdCd());
						prdDesceGenrlDInfo1.setPrdCnsdrStCd(prdDesceGenrlDInfo.getPrdCnsdrStCd());
						prdDesceGenrlDInfo1.setSessionObject(prdDesceGenrlDInfo);
					}
					this.setPrdDescdHtmlD(prdDescdHtmlDInfo);
				}
		//	}
		} else if("N".equals(StringUtils.NVL(prdmMain.getQryGbn()))) { // 직접받은 데이터
			// 일반기술서가 비어 있지 않을때
			if(prdDesceGenrlDInfoList.size() > 0 ) {
				logger.debug("size>>>>"+prdDesceGenrlDInfoList.size());
				for(PrdDesceGenrlDInfo prdDesceGenrlDInfo : prdDesceGenrlDInfoList) {
					prdDesceGenrlDInfo.setPrdCd(prdmMain.getPrdCd());
					if ("Y".equals(prdDesceGenrlDInfo.getQaCnfTgtYn())) {
						// QA요청 항목 정보를 INSERT
						prdEntity.setPrdDescdReqD(prdDesceGenrlDInfo);
					} else {
						// 상품기술서 정보를 INSERT -> 시퀀스 채번 후 입력방식으로 변경 (2011/03/26 OSM)
						entityDataSet = prdEntity.getDescdItmSeq();
						prdDesceGenrlDInfo.setDescdItmSeq(entityDataSet.getValues().getBigDecimal("descdItmSeq"));

						// 일본상품일 경우 DESCD_EXPLN_CNTNT이 .이면 INTRNT_EXPOS_YN를 N으로 셋팅하여 비노출로 한다 isKang 20130111
						if("33".equals(prdmMain.getPrdGbnCd().toString())){
							if(".".equals(prdDesceGenrlDInfo.getDescdExplnCntnt())){
								prdDesceGenrlDInfo.setIntrntExposYn("-1"); // 1이 아닌값이면 N
							}else{
								prdDesceGenrlDInfo.setIntrntExposYn("1"); // 1이면 Y
							}
							prdDesceGenrlDInfo.setChanlCd("A");
						}

						prdEntity.setPrdDescdGenrlD(prdDesceGenrlDInfo);


						prdDescdSyncInfoList.add(new PrdDescdSyncInfo(" " // attrPrdRepCd 상품코드 PRD_STOCK_D 테이블
						        , " " // descdExplnCntnt 기술서설명내용
						        , prdDesceGenrlDInfo.getDescdItmSeq() // descdItmSeq 기술서항목순번
						        , " " // genrlDescdId 일반기술서ID
						        , "I" // jobType JOB TYPE I:등록 D: 삭제 U: 수정
						        , prdDesceGenrlDInfo.getPrdCd() // prdCd 상품코드
						        ));
						prdDescdHtmlDInfo.setPrdCd(prdmMain.getPrdCd()) ;
						prdDescdHtmlDInfo.setChanlCd("P");
						prdDescdHtmlDInfo.setRegGbnCd("A");
						prdDescdHtmlDInfo.setSessionObject(prdmMain);
						// 일반 기술서 등록 후  HTML 기술서 변환
					}
				}
				this.setPrdDescdHtmlD(prdDescdHtmlDInfo);
			}

			// HTML 기술서가 비어 있지 않을때
			if(prdDescdHtmlDInfoList.size() > 0 ) {
				for(PrdDescdHtmlDInfo prdDescdHtmlDInfo1 : prdDescdHtmlDInfoList) {
					prdDescdHtmlDInfo1.setPrdCd(prdmMain.getPrdCd());
					PrdHtmlDescd prdHtmlDescd = new PrdHtmlDescd();
					DevBeanUtils.wrappedObjToObj(prdHtmlDescd, prdDescdHtmlDInfo1);
					List<PrdHtmlDescd> inputList = new ArrayList<PrdHtmlDescd>();
					inputList.add(prdHtmlDescd);
					prdHtmlDescdEntity.addHtmlDescdList(inputList);
					// HTML 기술서 등록
					prdDesceGenrlDInfo1.setPrdCd(prdmMain.getPrdCd());
					prdDesceGenrlDInfo1.setPrdCnsdrStCd(prdDescdHtmlDInfo1.getPrdCnsdrStCd());
					prdDesceGenrlDInfo1.setSessionObject(prdDescdHtmlDInfo1);
				}
			}
		}
		return prdDescdSyncInfoList;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품기술서 일반(HTML정보)를  입력한다.
	 *
	 * </pre>
	 *
	 * @author BAEK
	 * @date 2010-03-04 10:49:08
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public void setPrdDescdHtmlD(PrdDescdHtmlDInfo prdDescdHtmlDInfo) throws DevEntException {
		// 상품기술서(일반, INTRNT_EXPOS_YN = Y, CHANL_CD = A) 조회
		EntityDataSet<DSMultiData> prdDescdGenrlDList = prdDescdHtmlDEntity.getPrdDescdGenrlDList(prdDescdHtmlDInfo);
		// 상품기술서(일반 HTML정보) 저장

		String pChanlCd = StringUtils.NVL(prdDescdHtmlDInfo.getChanlCd(), "P"); //2013.3.25 고정현
		String pEcExposYn = StringUtils.NVL(prdDescdHtmlDInfo.getEcExposYn(), "N"); //2013.3.25 고정현

		if (prdDescdGenrlDList.size() > 0) {
			prdDescdHtmlDInfo.setChanlCd("P"); //2013.3.25 고정현

			// HTML정보 존재여부
			EntityDataSet<DSData> prdDescdHtmlDCnt = prdDescdHtmlDEntity.getPrdDescdHtmlDCnt(prdDescdHtmlDInfo);
			int cnt = prdDescdHtmlDCnt.getValues().getBigDecimal("cnt").intValue();

			if(pChanlCd.equals("P")) prdDescdHtmlDInfo.setEcExposYn(pEcExposYn) ;  //2013.3.25 고정현
			else prdDescdHtmlDInfo.setEcExposYn("N") ;  //2013.3.25 고정현

			prdDescdHtmlDEntity.setPrdDescdHtmlD(prdDescdGenrlDList, prdDescdHtmlDInfo, cnt);


			//2012.11.212 고정현 시니어몰 채널 존재여부 체크
			QaInspYnQryCond qaInspYnQryCond = new QaInspYnQryCond();
			qaInspYnQryCond.setPrdCd(prdDescdHtmlDInfo.getPrdCd());
			qaInspYnQryCond.setChanlCd("S");
			DSData prdChanlD = prdChanlEntity.getQaInspYn(qaInspYnQryCond);

			logger.debug("setPrdDescdHtmlD - 일반기술서 저장시 시니어몰 채널존재여부 체크 prdChanlD.size() >>>" + prdChanlD.size() );
			if(prdChanlD.size() > 0){

				prdDescdHtmlDInfo.setChanlCd("S");
				if(pChanlCd.equals("S")) prdDescdHtmlDInfo.setEcExposYn(pEcExposYn) ;  //2013.3.25 고정현
				else prdDescdHtmlDInfo.setEcExposYn("N") ;  //2013.3.25 고정현

				// HTML정보 존재여부
				prdDescdHtmlDCnt = prdDescdHtmlDEntity.getPrdDescdHtmlDCnt(prdDescdHtmlDInfo);
				cnt = prdDescdHtmlDCnt.getValues().getBigDecimal("cnt").intValue();

				prdDescdHtmlDEntity.setSnrmPrdDescdHtmlD(prdDescdGenrlDList, prdDescdHtmlDInfo, cnt);
			}
		}

	}
	// 일반 기술서 EAI 호출
	public void addPrdDescdEai(List<PrdDescdSyncInfo> prdDescdSyncInfoList) {
		// BIS 호출
		if (prdDescdSyncInfoList.size() > 0) {
			wsPrdBisPrdDescdRegModProcess.prdBisPrdDescdRegModProcess(prdDescdSyncInfoList);
		}
	}

	/**
	 * <pre>
	 *
	 * desc : 핸드폰상품목록을 등록 수정한다.
	 * PRDIF0820 IFESBWTP0186
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG (kbog2089)
	 * @date 2011-02-19 05:15:40
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : saveCelphnPrdList
	 */
//	@Transactional(value = "smtcTransactionManager")
//	public int saveCelphnPrdList(CelphnPrd celphnPre) throws DevEntException {
//		int returnMap = 0;
//
//		PrdQryCond prdQryCond = new PrdQryCond();
//		prdQryCond.setPrdCd(celphnPre.getPrdCd());
//		// 핸드폰상품목록조회
//		EntityDataSet<DSMultiData> pGetCelphnPrdList = prdMetaInfoEntity.getCelphnPrdList(prdQryCond);
//		if (pGetCelphnPrdList.size() == 0) {
//			// 등록여부 확인시 사용여부 Y 인거만 조회하므로 수정을 먼저하고 없을 경우 신규 등록함
//			int updateYn = prdMetaInfoEntity.modifyCelphnPrd(celphnPre);
//			if (updateYn == 0) {
//				// 핸드폰상품등록
//				prdMetaInfoEntity.addCelphnPrd(celphnPre);
//			}
//		} else {
//			// 핸드폰상품수정
//			prdMetaInfoEntity.modifyCelphnPrd(celphnPre);
//		}
//		PrdEnlagInfo prdEnlagInfo = new PrdEnlagInfo();
//		List<PrdEnlagInfo> prdEnlagInfoList = new ArrayList<PrdEnlagInfo>();
//
//		prdEnlagInfo.setPrdCd(celphnPre.getPrdCd());
//		prdEnlagInfo.setSessionObject(celphnPre);
//		// 검색키워드2 = 핸드폰모델명
//		prdEnlagInfo.setSrchKeywdNm2(celphnPre.getCelphnModelNm());
//
//		prdEnlagInfoList.add(prdEnlagInfo);
//		// 상품확장정보목록수정
//		returnMap = prdEntity.modifyPrdEnlagInfoList(prdEnlagInfoList);
//
//		return returnMap;
//	}
	// SOURCE_CLEANSING : END
	
	
	/**
	 * <pre>
	 *
	 * desc : 상품UDA목록을 저장한다.
	 *  상품UDA상세.UDA구분코드 = '10'
	 *  상품UDA상세.유효시작일시 = SYSDATE
	 *  workTyp에 따라 수행한다.
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG (kbog2089)
	 * @date 2010-12-30 11:05:52
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	// SOURCE_CLEANSING : START
	/* <pre>
	 * 처리내용 : 미사용코드 주석처리
	 * </pre>
	 * @Author  : th503 
	 * @Date    : 2017. 8. 22.
	 * @Method Name : savePrdUdaValList
	 */
//	@Transactional(value = "smtcTransactionManager")
//	public int savePrdUdaValList(PrdUdaDtl prdUdaDtl) throws DevEntException {
//		int returnMap = 0;
//
//		String sysdate = DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss");
//
//		if ( prdUdaDtl.getUdaGbnCd() == null ) {
//			prdUdaDtl.setUdaGbnCd("10"); // 상품UDA상세.UDA구분코드 = '10'
//		}
//		prdUdaDtl.setValidStrDtm(sysdate); // 상품UDA상세.유효시작일시 = SYSDATE
//		if ("I".equals(prdUdaDtl.getWorkTyp())) {
//			// UDA값등록
//			returnMap = prdUdaEntity.addUdaVal(prdUdaDtl);
//		} else if ("U".equals(prdUdaDtl.getWorkTyp())) {
//			// UDA값수정
//			returnMap += prdUdaEntity.modifyUdaVal(prdUdaDtl);
//		} else if ("D".equals(prdUdaDtl.getWorkTyp())) {
//			// UDA값삭제수정
//			returnMap += prdUdaEntity.modifyUdaValDel(prdUdaDtl);
//		}
//
//		return returnMap;
//	}
	// SOURCE_CLEANSING : END
	
	
	
	/**
	 * <pre>
	 *
	 * desc : 주문가능 수량 저장
	 * </pre>
	 *
	 * @author lgcns-213eab4ac
	 * @date 2010-09-15 04:51:39
	 * @param List<SaleEndClr> saleEndClrList
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public void saveOrdPsblQtyList(List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD, List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyDUp) {
		OrdPsblQty pOrdPsblQty = new OrdPsblQty();
		
		// 주문가능 수량 저장
		for (int i = 0; i < prdOrdPsblQtyD.size(); i++) {
			logger.debug("for_>>"+i);
			// 주문가능수량로그등록
			pOrdPsblQty.setAttrPrdRepCd(prdOrdPsblQtyD.get(i).getAttrPrdRepCd());
			pOrdPsblQty.setChanlGrpCd(prdOrdPsblQtyD.get(i).getChanlGrpCd());
			pOrdPsblQty.setOrdPsblQty(prdOrdPsblQtyD.get(i).getOrdPsblQty());
			pOrdPsblQty.setSessionUserId(prdOrdPsblQtyD.get(i).getSessionUserId());
			pOrdPsblQty.setSessionUserIp(prdOrdPsblQtyD.get(i).getSessionUserIp());
			//[SR02140613108][2014.06.26][김지혜] : [GSSHOP모바일 상품권 주문 가능수량 제한 해제요청]
			pOrdPsblQty.setOrdPsblQtyLimitClrYn(prdOrdPsblQtyD.get(0).getOrdPsblQtyLimitClrYn());
			
			//SR02150327087 재고내판매여부 속성을 조회
			String szStockInSaleYn = "N";
			String szChgOrdPsblQty = "Y";

			/* SKU PROJECT - 주문가능수량 수정. 2021.04.02 by kan.yp */
			String skuYn = "N";
			
			PrdQryCond pPrdQryCond = new PrdQryCond();
			EntityDataSet<DSData> eds = null;
			
			BigDecimal bAttrPrdCd ;
			if( prdOrdPsblQtyD.size() >0 && !StringUtils.isEmpty(prdOrdPsblQtyD.get(i).getAttrPrdRepCd()))
			{
				bAttrPrdCd = BigDecimal.valueOf( Long.valueOf( prdOrdPsblQtyD.get(i).getAttrPrdRepCd()));
				pPrdQryCond.setAttrPrdCd( bAttrPrdCd);
				eds = prdEntity.getStockInSaleYn( pPrdQryCond);
			}
			
			if(eds.size() > 0){
				szStockInSaleYn = eds.getValues().getString("stockInSaleYn");
				szChgOrdPsblQty = "N";
				skuYn = eds.getValues().getString("skuYn");
			}
			
			BigDecimal bOrdPsblQty =  prdOrdPsblQtyD.get(i).getOrdPsblQty();
			BigDecimal bTemp =  new BigDecimal(-1);
			
			if( bOrdPsblQty.compareTo(bTemp) == 0){
				prdOrdPsblQtyD.get(i).setOrdPsblQty(new BigDecimal(0));
				szChgOrdPsblQty = "Y";
			}
			 
			if( !szStockInSaleYn.equals("Y") || szChgOrdPsblQty.equals("Y")){
				// SAP 전송 주문가능 수량 로그  수정
				if(szChgOrdPsblQty.equals("Y") && szStockInSaleYn.equals("Y")){
					pOrdPsblQty.setOrdPsblQty(prdOrdPsblQtyD.get(i).getOrdPsblQty());
				}
								
				if (    "Y".equals(skuYn)
					 && prdOrdPsblQtyD.get(i).getOrdPsblQty() != null
					 && prdOrdPsblQtyD.get(i).getOrdPsblQty().compareTo(BigDecimal.ZERO) > 0 ) 
				{
					// 주문가능수량조회
					EntityDataSet<DSData> getOrdPsblQty = ordPsblQtyEntity.getSaleQty(pOrdPsblQty);
					
					prdOrdPsblQtyD.get(i).setSaleQty(getOrdPsblQty.getValues().getBigDecimal("saleQty")); // 판매수량
				}
					
				if("AZ".equals(prdOrdPsblQtyD.get(i).getChanlGrpCd())){
					ordPsblQtyEntity.savePrdOrdPsblQtyLog(pOrdPsblQty);
				}
				
				int upAttrval = attrPrdEntity.modifyOrdPsblQtyD(prdOrdPsblQtyD.get(i));
				
				if (upAttrval < 1) {
					prdEntity.setInsertPrdOrdPsblQtyD(prdOrdPsblQtyD.get(i));
				}
				
				// 주문가능수량 로그
				ordPsblQtyEntity.addOrdPsblQtyLog(pOrdPsblQty);
				
				/*
			// 주문가능수량임시정보 --> 주문가능수량임시로그
			ordPsblQtyEntity.insertPrdOrdPsblTmpL(pOrdPsblQty);

		    // 주문가능수량임시정보 삭제
        	ordPsblQtyEntity.deletePrdOrdPsblTmpD(pOrdPsblQty);
				 */
			}
		}

		// 주문가능 수량 저장
		for (int i = 0; i < prdOrdPsblQtyDUp.size(); i++) {
			// 주문가능수량로그등록
			pOrdPsblQty.setAttrPrdRepCd(prdOrdPsblQtyDUp.get(i).getAttrPrdRepCd());
			pOrdPsblQty.setChanlGrpCd(prdOrdPsblQtyDUp.get(i).getChanlGrpCd());
			pOrdPsblQty.setOrdPsblQty(prdOrdPsblQtyDUp.get(i).getOrdPsblQty());
			pOrdPsblQty.setSessionUserId(prdOrdPsblQtyDUp.get(i).getSessionUserId());
			pOrdPsblQty.setSessionUserIp(prdOrdPsblQtyDUp.get(i).getSessionUserIp());
			//[SR02140613108][2014.06.26][김지혜] : [GSSHOP모바일 상품권 주문 가능수량 제한 해제요청]
			pOrdPsblQty.setOrdPsblQtyLimitClrYn(prdOrdPsblQtyDUp.get(0).getOrdPsblQtyLimitClrYn());
			
			//SR02150327087 재고내판매여부 속성을 조회
			String szStockInSaleYn = "N";
			String szChgOrdPsblQty = "Y";

			/* SKU PROJECT - 주문가능수량 수정. 2021.04.02 by kan.yp */
			String skuYn = "N";
			
			PrdQryCond pPrdQryCond = new PrdQryCond();
			EntityDataSet<DSData> eds = null;
			
			if( prdOrdPsblQtyDUp.size() > 0 && !StringUtils.isEmpty(prdOrdPsblQtyDUp.get(i).getAttrPrdRepCd()))
			{
				pPrdQryCond.setAttrPrdRepCd( prdOrdPsblQtyDUp.get(i).getAttrPrdRepCd());
				pPrdQryCond.setPrdCd( new BigDecimal(0));
				eds = prdEntity.getStockInSaleYn( pPrdQryCond);
			}
			
			if(eds.size() > 0){
				szStockInSaleYn = eds.getValues().getString("stockInSaleYn");
				szChgOrdPsblQty = "N";
				skuYn = eds.getValues().getString("skuYn");
			}
			
			BigDecimal bOrdPsblQty =  prdOrdPsblQtyDUp.get(i).getOrdPsblQty();
			BigDecimal bTemp =  new BigDecimal(-1);
			
			if( bOrdPsblQty!= null && bOrdPsblQty.compareTo(bTemp) == 0){
				prdOrdPsblQtyDUp.get(i).setOrdPsblQty(new BigDecimal(0));
				szChgOrdPsblQty = "Y";
			}
			 
			if( !szStockInSaleYn.equals("Y") || szChgOrdPsblQty.equals("Y")){
				// SAP 전송 주문가능 수량 로그  수정
				if(szChgOrdPsblQty.equals("Y") && szStockInSaleYn.equals("Y")){
					pOrdPsblQty.setOrdPsblQty(prdOrdPsblQtyDUp.get(i).getOrdPsblQty());
				}
				
				if (    "Y".equals(skuYn)
					 && prdOrdPsblQtyDUp.get(i).getOrdPsblQty() != null
					 && prdOrdPsblQtyDUp.get(i).getOrdPsblQty().compareTo(BigDecimal.ZERO) > 0 ) 
				{

					// 주문가능수량조회
					EntityDataSet<DSData> getOrdPsblQty = ordPsblQtyEntity.getSaleQty(pOrdPsblQty);
					
					prdOrdPsblQtyDUp.get(i).setSaleQty(getOrdPsblQty.getValues().getBigDecimal("saleQty")); // 판매수량
				}
				
				if("AZ".equals(prdOrdPsblQtyDUp.get(i).getChanlGrpCd())){
					ordPsblQtyEntity.savePrdOrdPsblQtyLog(pOrdPsblQty);
				}
				
				int upAttrval = attrPrdEntity.modifyOrdPsblQtyD(prdOrdPsblQtyDUp.get(i));
				if (upAttrval < 1) {
					prdEntity.setInsertPrdOrdPsblQtyD(prdOrdPsblQtyDUp.get(i));
				}
				
				ordPsblQtyEntity.addOrdPsblQtyLog(pOrdPsblQty);
				
				if ( !"Y".equals(skuYn) ) { 
					
					// 주문가능수량임시정보 --> 주문가능수량임시로그
					ordPsblQtyEntity.insertPrdOrdPsblTmpL(pOrdPsblQty);
					// 주문가능수량임시정보 삭제
					ordPsblQtyEntity.deletePrdOrdPsblTmpD(pOrdPsblQty);
				}
			}
		}
	}

	/**
	 * <pre>
	 *        * desc : 디앤샵상품 주문가능수량을 조회한다.(재고체크)
	 *
	 * </pre>
	 *
	 * @author KIM-JOO-YOUNG (KJH)
	 * @date 2011-01-13 02:28:03
	 * @param RSPDataSet
	 *            dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public List<OrdPsblQtyRsltCond> getDnshpOrdPsblQtyList(List<OrdPsblQty> ordPsblQtyList) throws DevEntException{
		List<OrdPsblQtyRsltCond> resultList = new ArrayList<OrdPsblQtyRsltCond>();

		for (int inx = 0; inx < ordPsblQtyList.size(); inx++) {
			OrdPsblQty ordPsblQty = ordPsblQtyList.get(inx);

			if( ordPsblQty.getPrdCd() == null ) {
				OrdPsblQtyRsltCond returnData = new OrdPsblQtyRsltCond();

				returnData.setPrdCd(ordPsblQty.getPrdCd());
				returnData.setAttrPrdCd(ordPsblQty.getAttrPrdCd());
				returnData.setRetCd("E");
				//필수값 미입력(상품코드)
				returnData.setRetMsg(Message.getMessage("prd.eai.msg.001"));

				resultList.add(returnData);
				continue;
			}

			// 조건 셋팅
			OrdPsblQtyQryCond pOrdPsblQtyQryCond = new OrdPsblQtyQryCond();
			pOrdPsblQtyQryCond.setAttrPrdCd(ordPsblQty.getAttrPrdCd()); // 속성상품코드
			pOrdPsblQtyQryCond.setPrdCd(ordPsblQty.getPrdCd()); // 상품코드
			pOrdPsblQtyQryCond.setSessionObject(ordPsblQty);
			if (ordPsblQty.getSessionChanlCd() != null) {
				if ("GSEC".equals(ordPsblQty.getSessionChanlCd())) {
					pOrdPsblQtyQryCond.setChanlCd("P");
					ordPsblQty.setChanlCd("P");
				}
			}

			logger.debug("조회조건>>>>"+pOrdPsblQtyQryCond);

			// 디앤샵상품 주문가능수량을 조회
			EntityDataSet<DSMultiData> pGetOrdPsblQtyList = ordPsblQtyEntity.getDnshpOrdPsblQtyList(pOrdPsblQtyQryCond);
			logger.debug("조회결과>>>>"+pGetOrdPsblQtyList);

			if (pGetOrdPsblQtyList.getValues() != null && pGetOrdPsblQtyList.getValues().size() > 0) {
				for( int i = 0; i < pGetOrdPsblQtyList.getValues().size(); i++) {
					OrdPsblQtyRsltCond ordPsblQtyInfo = DevBeanUtils.wrappedMapToBean(pGetOrdPsblQtyList.getValues().get(i), OrdPsblQtyRsltCond.class);

					/*returnData.setPrdCd(ordPsblQty.getPrdCd());
					returnData.setAttrPrdCd(ordPsblQtyInfo.getAttrPrdCd());
					returnData.setSalePsblYn(ordPsblQtyInfo.getSalePsblYn());
					returnData.setSalePsblCnt(ordPsblQtyInfo.getSalePsblCnt());
					returnData.setSalePrc(ordPsblQtyInfo.getSalePrc());
					*/

					ordPsblQtyInfo.setRetCd("S");
					ordPsblQtyInfo.setRetMsg("정상");

					resultList.add(ordPsblQtyInfo);
				}
			} else {
				OrdPsblQtyRsltCond returnData = new OrdPsblQtyRsltCond();

				returnData.setPrdCd(ordPsblQty.getPrdCd());
				returnData.setAttrPrdCd(ordPsblQty.getAttrPrdCd());
				returnData.setRetCd("E");
				returnData.setRetMsg(Message.getMessage("prd.esb.msg.012"));

				resultList.add(returnData);
			}
		}// for end

		logger.debug("전송결과>>>>>"+resultList);
		return resultList;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품가격정보를 입력한다.(도서몰)
	 *
	 * </pre>
	 *
	 * @author KJH
	 * @date 2011-12-29 07:01:44
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> savePrdPrcInfo(PrdmMain prdmMain, PrdPrcGenrl pPrdPrcGenrl){
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		DSData resultData = new DSData();

		
		Date date = SysUtil.getCurrTime();
   	 	String sysdtm      = DateUtils.format(date,"yyyyMMddHHmmss");
   	 	String validStrDtm = (pPrdPrcGenrl.getValidStrDtm().length() == 8)?(pPrdPrcGenrl.getValidStrDtm()+"000000"):pPrdPrcGenrl.getValidStrDtm();
   	 	BigDecimal cmpSysdtm      = new BigDecimal(sysdtm);
   	    BigDecimal cmpValidStrDtm = new BigDecimal(validStrDtm);

   	 	if( cmpSysdtm.compareTo(cmpValidStrDtm) == 1 ) {
   	 		pPrdPrcGenrl.setValidStrDtm(sysdtm);
   	 	}
   	 	
		//[SR02160426120][2016.04.26][김영현]:API 가격 이중 등록 제한 로직 추가
		//초단위 입력을 0으로 변경 EX)20160426235959 -> 20160426235950
		//VALID_STR_DTM = TO_DATE(:validStrDtm,'YYYYMMDDHH24MISS')		
		String prcValidStrDtm = pPrdPrcGenrl.getValidStrDtm();
		if(prcValidStrDtm!=null && prcValidStrDtm.length()==14){
			prcValidStrDtm = prcValidStrDtm.substring(0, 13)+"0";
			pPrdPrcGenrl.setValidStrDtm(prcValidStrDtm);
		}
		
		PrdQryCond pPrdQryCond = new PrdQryCond();
		pPrdQryCond.setPrdCd(prdmMain.getPrdCd());
		pPrdQryCond.setValidStrDtm(pPrdPrcGenrl.getValidStrDtm());

		// 상품동일한 유효일시가격정보 체크
		DSData getPrcValidStrDtmCheck = prdPrcEntity.getPrcValidStrDtmCheck(pPrdQryCond);

		if(getPrcValidStrDtmCheck.getBigDecimal("cnt").intValue() > 0 ) {
			//동일한 유효시작일시가 존재합니다. 확인후 저장해 주세요.
			resultData.put("retCd", "E");
			resultData.put("retMsg", Message.getMessage("prd.esb.msg.016"));
			returnMap.put("outSavePrdPrcInfo", EDSFactory.create(EsbCmm.class, resultData));
			return returnMap;
		}

		/* [패널 2015_014_제도개선 변경]김현(slrip4) 2015-10-15 방송합의서 재합의 처리 */
		List<BroadBefMngInfo> broadBefMngInfoList = new ArrayList<BroadBefMngInfo>();		

		List<PrdEaiPrcSyncInfo> pPrdEaiPrcSyncInfoList = new ArrayList<PrdEaiPrcSyncInfo>(); // 웹서비스용

		// 가격등록상품정보조회
		EntityDataSet<DSData> pGetPrcRegPrdInfo = prdPrcEntity.getPrcRegPrdInfo(pPrdQryCond);

		if (pGetPrcRegPrdInfo.getValues() == null) {
			//상품정보가 존재하지 않습니다.
			resultData.put("retCd", "E");
			resultData.put("retMsg", Message.getMessage("prd.error.msg.185"));
			returnMap.put("outSavePrdPrcInfo", EDSFactory.create(EsbCmm.class, resultData));
			return returnMap;
		} else {
			// 필드값을 Validation
			PrcChkInfo prcChkInfo = new PrcChkInfo();

			prcChkInfo.setCvsDlvsRtpYn(pGetPrcRegPrdInfo.getValues().getString("cvsDlvsRtpYn"));
			prcChkInfo.setDlvPickMthodCd(pGetPrcRegPrdInfo.getValues().getString("dlvPickMthodCd"));
			prcChkInfo.setFrmlesPrdTypCd(pGetPrcRegPrdInfo.getValues().getString("frmlesPrdTypCd"));
			prcChkInfo.setGftTypCd(pGetPrcRegPrdInfo.getValues().getString("gftTypCd"));
			prcChkInfo.setInstlDlvPrdYn(pGetPrcRegPrdInfo.getValues().getString("instlDlvPrdYn"));
			prcChkInfo.setMinusMargnYn(pGetPrcRegPrdInfo.getValues().getString("minusMargnYn"));
			prcChkInfo.setOperMdId(pGetPrcRegPrdInfo.getValues().getString("operMdId"));
			prcChkInfo.setPrchPrc(pGetPrcRegPrdInfo.getValues().getBigDecimal("prchPrc"));
			prcChkInfo.setPrchTypCd(pGetPrcRegPrdInfo.getValues().getString("prchTypCd"));
			prcChkInfo.setPrchTypCd(pGetPrcRegPrdInfo.getValues().getString("prchTypCd"));
			prcChkInfo.setPrdCd(pGetPrcRegPrdInfo.getValues().getBigDecimal("prdCd"));
			prcChkInfo.setRepPrdYn(pGetPrcRegPrdInfo.getValues().getString("repPrdYn"));
			prcChkInfo.setSalePrc(pGetPrcRegPrdInfo.getValues().getBigDecimal("salePrc"));
			prcChkInfo.setSupCd(pGetPrcRegPrdInfo.getValues().getString("supCd"));
			prcChkInfo.setSupPrdCd(pGetPrcRegPrdInfo.getValues().getString("supPrdCd"));
			prcChkInfo.setTaxTypCd(pGetPrcRegPrdInfo.getValues().getString("taxTypCd"));
			prcChkInfo.setZrwonSaleYn(pGetPrcRegPrdInfo.getValues().getString("zrwonSaleYn"));

			// 가격정보체크
			PrcChkRslt prcChkRslt = prdPrcMngCmmProcess.checkPrcInfo(prcChkInfo, pPrdPrcGenrl);
			logger.debug("가격정보체크 ===>" + prcChkRslt.getRslt());
			if ("E".equals(prcChkRslt.getRslt())) {
				resultData.put("retCd", "E");
				resultData.put("retMsg", prcChkRslt.getErrMsg());
				returnMap.put("outSavePrdPrcInfo", EDSFactory.create(EsbCmm.class, resultData));
				return returnMap;
			}
			
			//변경가 - 기존가 동일여부 체크
			//1. 입력된 유효시작일시 이후로 등록된 기존 가격들을 조회함.
			PrdPmoDtlGenrl prdPmoDtlGenrl = new PrdPmoDtlGenrl();
			prdPmoDtlGenrl.setPrdCd(prdmMain.getPrdCd());
			prdPmoDtlGenrl.setValidStrDtm(pPrdPrcGenrl.getValidStrDtm());
			prdPmoDtlGenrl.setValidEndDtm(pPrdPrcGenrl.getValidEndDtm());
			
			EntityDataSet<DSMultiData> getPrdPmoPrcGenrl = prdPrcEntity.getPrdPmoPrcGenrl(prdPmoDtlGenrl);
			
			//2, 조회된 결과가 1건일 경우만 변경가-기존가 동일여부를 체크함(1건 이상일 경우에는 미래가격 삭제를 위해 체크하지 않음)
			//체크항목 : 판매가, 유효종료일시, 협력사지급율/액코드, 협력사지급율/액 
			String prdPrcSameYn = "N";
			if(getPrdPmoPrcGenrl.getValues() != null){
				if(getPrdPmoPrcGenrl.getValues().size() == 1){
					if(pPrdPrcGenrl.getSalePrc().equals(getPrdPmoPrcGenrl.getValues().getBigDecimal(0, "salePrc"))
						&& pPrdPrcGenrl.getSupGivRtamt().equals(getPrdPmoPrcGenrl.getValues().getBigDecimal(0, "supGivRtamt"))
						&& pPrdPrcGenrl.getSupGivRtamtCd().equals(getPrdPmoPrcGenrl.getValues().getString(0, "supGivRtamtCd"))){
						prdPrcSameYn = "Y";
						
						//[SR02191115633] 2019.11.19 이용문 : 백화점 연동시 OP코드 일치 여부 포함 및 데이터 정합성 요청 
						//백화점은 가격이 같아도 OP코드 변경이 있기 때문에 현재 마진 순번과 한번 더 체크
						if( !StringUtils.isEmpty(pPrdPrcGenrl.getDpatMargnSeq())
								&& !StringUtils.isEmpty(pPrdPrcGenrl.getDpatOpVal())
								&& !StringUtils.isEmpty(pPrdPrcGenrl.getDpatSaleTypVal()) ){
							PrdPmoDtlGenrl pPrdPmoDtlGenrl = new PrdPmoDtlGenrl();
							pPrdPmoDtlGenrl.setPrdCd(prdmMain.getPrdCd());
							pPrdPmoDtlGenrl.setValidStrDtm(sysdtm);
							EntityDataSet<DSData> prdPrcInfo = prdPrcEntity.getValidPrdPrcDpat(pPrdPmoDtlGenrl);
							if( !pPrdPrcGenrl.getDpatMargnSeq().equals(prdPrcInfo.getValues().getString("dpatMargnSeq")) ){
								prdPrcSameYn = "N";
							}
						}
					}else{
						prdPrcSameYn = "N";
					}
				}
			}
			//3.1. 동일한 가격이 입력된 경우에는 수정일시 업데이트만 진행함. 가격정보 수정 성공으로 리턴
			if(prdPrcSameYn.equals("Y")){
				resultData.put("retCd", "S");
				resultData.put("retMsg", Message.getMessage("prd.esb.msg.001"));		//성공하였습니다.
				returnMap.put("outSavePrdPrcInfo", EDSFactory.create(EsbCmm.class, resultData));
				return returnMap;
			}  
			//3.2. 동일하지 않은 경우 후속 로직을 진행함.
			
			
			// 상품가격기존정보조회
			DSMultiData getPrdPrcExistInfo = prdPrcEntity.getPrdPrcExistInfo(pPrdPrcGenrl);

			ExistPrdPrcModInfo pExistPrdPrcModInfo = new ExistPrdPrcModInfo(); // 기존상품가격수정정보

			boolean futurFlag = false;
			if (getPrdPrcExistInfo.size() > 0) {
				for (int i = 0; i < getPrdPrcExistInfo.size(); i++) {
					if ("CURR".equals(getPrdPrcExistInfo.getString(i, "infoGbnCd"))) {
						pExistPrdPrcModInfo.setPrdCd(getPrdPrcExistInfo.getBigDecimal(i, "prdCd"));
						pExistPrdPrcModInfo.setPrdAttrGbnCd(getPrdPrcExistInfo.getString(i, "prdAttrGbnCd"));
						pExistPrdPrcModInfo.setExistValidStrDtm(getPrdPrcExistInfo.getString(i, "existValidStrDtm"));
						pExistPrdPrcModInfo.setExistValidEndDtm(getPrdPrcExistInfo.getString(i, "existValidEndDtm"));
						pExistPrdPrcModInfo.setModValidEndDtm(getPrdPrcExistInfo.getString(i, "modValidEndDtm"));

					} else if ("FUTUR".equals(getPrdPrcExistInfo.get(i, "infoGbnCd"))) {
						//pPrdPrcGenrl.setValidEndDtm(getPrdPrcExistInfo.getString(i, "modValidEndDtm"));
						//2013-01-14 제휴몰의 경우 기존가격이 있을 경우 삭제처리
						futurFlag = true;
						//-end
					}
					pExistPrdPrcModInfo.setSessionObject(pPrdPrcGenrl);
					pExistPrdPrcModInfo.setModrId(prdmMain.getSessionUserId());

				}

				if (StringUtils.NVL(pPrdPrcGenrl.getValidEndDtm()).equals("")) {
					pPrdPrcGenrl.setValidEndDtm("29991231235959");
				}

				pPrdPrcGenrl.setModrId(prdmMain.getSessionUserId());

				// 상품가격종료일시수정
				prdPrcEntity.modifyPrdPrcEndDtm(pExistPrdPrcModInfo);

				// UPDATE EAI
				// EAI Interface 처리 시작
				EntityDataSet<DSData> prdPrcHInfoU = prdPrcEntity.getPrdPrcHInfoU(pExistPrdPrcModInfo);
				PrdEaiPrcSyncInfo pPrdEaiPrcSyncInfoU = null;

				if (prdPrcHInfoU.getValues() != null) {
					pPrdEaiPrcSyncInfoU = new PrdEaiPrcSyncInfo();
					pPrdEaiPrcSyncInfoU.setJobTyp("U");
					pPrdEaiPrcSyncInfoU.setPrdCd(prdPrcHInfoU.getValues().getBigDecimal("prdCd"));
					pPrdEaiPrcSyncInfoU.setPrdAttrGbnCd(prdPrcHInfoU.getValues().getString("prdAttrGbnCd"));
					pPrdEaiPrcSyncInfoU.setValidEndDtm(prdPrcHInfoU.getValues().getString("validEndDtm"));
					pPrdEaiPrcSyncInfoU.setValidStrDtm(prdPrcHInfoU.getValues().getString("validStrDtm"));
					pPrdEaiPrcSyncInfoU.setPreValidEndDtm(pExistPrdPrcModInfo.getExistValidEndDtm());
					pPrdEaiPrcSyncInfoU.setPreValidStrDtm(pExistPrdPrcModInfo.getExistValidStrDtm());
					pPrdEaiPrcSyncInfoU.setEnterDate(prdPrcHInfoU.getValues().getString("regDtm"));
					pPrdEaiPrcSyncInfoU.setEnterId(prdPrcHInfoU.getValues().getString("regrId"));
					pPrdEaiPrcSyncInfoU.setModifyDate(prdPrcHInfoU.getValues().getString("modDtm"));
					pPrdEaiPrcSyncInfoU.setModifyId(prdPrcHInfoU.getValues().getString("modrId"));
					pPrdEaiPrcSyncInfoU.setSalePrc(prdPrcHInfoU.getValues().getBigDecimal("salePrc"));
					pPrdEaiPrcSyncInfoU.setPrchPrc(prdPrcHInfoU.getValues().getBigDecimal("prchPrc"));
					pPrdEaiPrcSyncInfoU.setSupGivRtamtCd(prdPrcHInfoU.getValues().getString("supGivRtamtCd"));
					if (prdPrcHInfoU.getValues().getString("supGivRtamtCd").equals("02")) {
						pPrdEaiPrcSyncInfoU.setSupGivRtamt(prdPrcHInfoU.getValues().getBigDecimal("supGivRtamt"));
					} else {
						pPrdEaiPrcSyncInfoU.setSupGivRtamt(prdPrcHInfoU.getValues().getBigDecimal("supGivRtamt").setScale(0, BigDecimal.ROUND_DOWN));
					}
					pPrdEaiPrcSyncInfoU.setSupProprdUprc(prdPrcHInfoU.getValues().getBigDecimal("supProprdUprc"));
					pPrdEaiPrcSyncInfoU.setInstlCost(prdPrcHInfoU.getValues().getBigDecimal("instlCost"));
					pPrdEaiPrcSyncInfoU.setProprdWthtax(prdPrcHInfoU.getValues().getBigDecimal("proprdWthtax"));
					pPrdEaiPrcSyncInfoU.setVipDlvYn(prdPrcHInfoU.getValues().getString("vipDlvYn"));
					pPrdEaiPrcSyncInfoU.setVipDlvStdPrc(prdPrcHInfoU.getValues().getBigDecimal("vipDlvStdPrc"));
					pPrdEaiPrcSyncInfoU.setOnsitePrdPrc(prdPrcHInfoU.getValues().getBigDecimal("onsitePrdPrc"));
					pPrdEaiPrcSyncInfoU.setOnsiteDcPrc(prdPrcHInfoU.getValues().getBigDecimal("onsiteDcPrc"));
					pPrdEaiPrcSyncInfoU.setDetrmWeihtVal(prdPrcHInfoU.getValues().getString("detrmWeihtVal"));
					pPrdEaiPrcSyncInfoU.setOnsiteChrCost(prdPrcHInfoU.getValues().getBigDecimal("onsiteChrCost"));
					pPrdEaiPrcSyncInfoU.setWhsCd(prdPrcHInfoU.getValues().getString("whsCd"));
					pPrdEaiPrcSyncInfoU.setOtherSysTnsYn(prdPrcHInfoU.getValues().getString("otherSysTnsYn"));
					pPrdEaiPrcSyncInfoU.setNoteCntnt(prdPrcHInfoU.getValues().getString("noteCntnt"));
					pPrdEaiPrcSyncInfoU.setSapExcptYn("Y");

					List<PrdEaiPrcSyncInfo> pPrdEaiPrcSyncInfoListUp = new ArrayList<PrdEaiPrcSyncInfo>(); // 웹서비스용
					pPrdEaiPrcSyncInfoListUp.add(pPrdEaiPrcSyncInfoU);

					if (pPrdEaiPrcSyncInfoListUp.size() > 0) {
//						gshseai.GSH_S4C_PRD01.WS.PRD_EAI_Products_Price_Reg_Mod.ws.wsProvider.PRD_EAI_Products_Price_Reg_Mod_P.PRD_EAI_Res res = wsPrdEaiPrdPriceRegModProcess.prdEaiPrdPricdRegModProcess(pPrdEaiPrcSyncInfoListUp);
						wsPrdEaiPrdPriceRegModProcess.prdEaiPrdPricdRegModProcess(pPrdEaiPrcSyncInfoListUp);

					}
				} // EAI Interface 처리 끝
			}

			//2013-01-14 제휴몰의 경우 미래가격이 있을 경우 삭제처리
			if( futurFlag ) {
				PrcPmoQryCond prcPmoQryCond = new PrcPmoQryCond();
				prcPmoQryCond.setPrdCd(prdmMain.getPrdCd());
				prcPmoQryCond.setPmoCurFutGbn("CURR");

				DSMultiData sPrdPrcFuturExistList = prdPrcEntity.getPrdPrcListGenrl(prcPmoQryCond).getValues();
				for( int i = 0; i < sPrdPrcFuturExistList.size(); i++ ) {
					DSData sPrdPrcFuturExistInfo = sPrdPrcFuturExistList.get(i);
					if( "예정".equals(sPrdPrcFuturExistInfo.getString("prcAplySt")) ) {
						//예정일 경우 삭제
						PrdPrcGenrl dPrdPrcGenrl = DevBeanUtils.wrappedMapToBean(sPrdPrcFuturExistInfo, PrdPrcGenrl.class);
						prdPrcEntity.removePrdPrcExistInfo(dPrdPrcGenrl);

						EntityDataSet<DSData> prdPrcHInfoID = prdPrcEntity.getPrdPrcHInfoID(dPrdPrcGenrl);
						PrdEaiPrcSyncInfo pPrdEaiPrcSyncInfoID = null;

						if (prdPrcHInfoID.getValues() != null) {
							pPrdEaiPrcSyncInfoID = new PrdEaiPrcSyncInfo();
							pPrdEaiPrcSyncInfoID.setJobTyp("D");
							pPrdEaiPrcSyncInfoID.setPrdCd(prdPrcHInfoID.getValues().getBigDecimal("prdCd"));
							pPrdEaiPrcSyncInfoID.setPrdAttrGbnCd(prdPrcHInfoID.getValues().getString("prdAttrGbnCd"));
							pPrdEaiPrcSyncInfoID.setPreValidEndDtm(prdPrcHInfoID.getValues().getString("validEndDtm"));
							pPrdEaiPrcSyncInfoID.setPreValidStrDtm(prdPrcHInfoID.getValues().getString("validStrDtm"));
							pPrdEaiPrcSyncInfoID.setEnterDate(prdPrcHInfoID.getValues().getString("regDtm"));
							pPrdEaiPrcSyncInfoID.setEnterId(prdPrcHInfoID.getValues().getString("regrId"));
							pPrdEaiPrcSyncInfoID.setModifyDate(prdPrcHInfoID.getValues().getString("modDtm"));
							pPrdEaiPrcSyncInfoID.setModifyId(prdPrcHInfoID.getValues().getString("modrId"));
							pPrdEaiPrcSyncInfoID.setSalePrc(prdPrcHInfoID.getValues().getBigDecimal("salePrc"));
							pPrdEaiPrcSyncInfoID.setPrchPrc(prdPrcHInfoID.getValues().getBigDecimal("prchPrc"));
							pPrdEaiPrcSyncInfoID.setSupGivRtamtCd(prdPrcHInfoID.getValues().getString("supGivRtamtCd"));
							if (prdPrcHInfoID.getValues().getString("supGivRtamtCd").equals("02")) {
								pPrdEaiPrcSyncInfoID.setSupGivRtamt(prdPrcHInfoID.getValues().getBigDecimal("supGivRtamt"));
							} else {
								pPrdEaiPrcSyncInfoID.setSupGivRtamt(prdPrcHInfoID.getValues().getBigDecimal("supGivRtamt").setScale(0, BigDecimal.ROUND_DOWN));
							}
							pPrdEaiPrcSyncInfoID.setSupProprdUprc(prdPrcHInfoID.getValues().getBigDecimal("supProprdUprc"));
							pPrdEaiPrcSyncInfoID.setInstlCost(prdPrcHInfoID.getValues().getBigDecimal("instlCost"));
							pPrdEaiPrcSyncInfoID.setProprdWthtax(prdPrcHInfoID.getValues().getBigDecimal("proprdWthtax"));
							pPrdEaiPrcSyncInfoID.setVipDlvYn(prdPrcHInfoID.getValues().getString("vipDlvYn"));
							pPrdEaiPrcSyncInfoID.setVipDlvStdPrc(prdPrcHInfoID.getValues().getBigDecimal("vipDlvStdPrc"));
							pPrdEaiPrcSyncInfoID.setOnsitePrdPrc(prdPrcHInfoID.getValues().getBigDecimal("onsitePrdPrc"));
							pPrdEaiPrcSyncInfoID.setOnsiteDcPrc(prdPrcHInfoID.getValues().getBigDecimal("onsiteDcPrc"));
							pPrdEaiPrcSyncInfoID.setDetrmWeihtVal(prdPrcHInfoID.getValues().getString("detrmWeihtVal"));
							pPrdEaiPrcSyncInfoID.setOnsiteChrCost(prdPrcHInfoID.getValues().getBigDecimal("onsiteChrCost"));
							pPrdEaiPrcSyncInfoID.setWhsCd(prdPrcHInfoID.getValues().getString("whsCd"));
							pPrdEaiPrcSyncInfoID.setOtherSysTnsYn(prdPrcHInfoID.getValues().getString("otherSysTnsYn"));
							pPrdEaiPrcSyncInfoID.setNoteCntnt(prdPrcHInfoID.getValues().getString("noteCntnt"));
							pPrdEaiPrcSyncInfoID.setSapExcptYn("Y");

							List<PrdEaiPrcSyncInfo> delPrdEaiPrcSyncInfoList = new ArrayList<PrdEaiPrcSyncInfo>(); // 웹서비스용
							delPrdEaiPrcSyncInfoList.add(pPrdEaiPrcSyncInfoID);

							if (delPrdEaiPrcSyncInfoList.size() > 0) {
								gshseai.GSH_S4C_PRD01.WS.PRD_EAI_Products_Price_Reg_Mod.ws.wsProvider.PRD_EAI_Products_Price_Reg_Mod_P.PRD_EAI_Res res = wsPrdEaiPrdPriceRegModProcess.prdEaiPrdPricdRegModProcess(delPrdEaiPrcSyncInfoList);

								if (StringUtils.NVL(res.getResponseResult()).equals("E")) {
									throw new DevEntException(res.getResponseMessage());
								}
							}
						}
					}
				}

			}
			//-end

			// 상품속성구분코드는 무조건 P
			pPrdPrcGenrl.setPrdAttrGbnCd("P");
			// 상품가격등록을 실행한다.
			prdPrcEntity.addPrdPrc(pPrdPrcGenrl);

			resultData.put("retCd", "S");
			resultData.put("retMsg", Message.getMessage("prd.esb.msg.001"));		//성공하였습니다.

			// EAI
			PrdEaiPrcSyncInfo pPrdEaiPrcSyncInfoID = null;

			EntityDataSet<DSData> prdPrcHInfoID = prdPrcEntity.getPrdPrcHInfoID(pPrdPrcGenrl);

			if (prdPrcHInfoID.getValues() != null) {
				pPrdEaiPrcSyncInfoID = new PrdEaiPrcSyncInfo();
				pPrdEaiPrcSyncInfoID.setJobTyp("I");
				pPrdEaiPrcSyncInfoID.setPrdCd(prdPrcHInfoID.getValues().getBigDecimal("prdCd"));
				pPrdEaiPrcSyncInfoID.setPrdAttrGbnCd(prdPrcHInfoID.getValues().getString("prdAttrGbnCd"));
				pPrdEaiPrcSyncInfoID.setValidEndDtm(prdPrcHInfoID.getValues().getString("validEndDtm"));
				pPrdEaiPrcSyncInfoID.setValidStrDtm(prdPrcHInfoID.getValues().getString("validStrDtm"));
				pPrdEaiPrcSyncInfoID.setEnterDate(prdPrcHInfoID.getValues().getString("regDtm"));
				pPrdEaiPrcSyncInfoID.setEnterId(prdPrcHInfoID.getValues().getString("regrId"));
				pPrdEaiPrcSyncInfoID.setModifyDate(prdPrcHInfoID.getValues().getString("modDtm"));
				pPrdEaiPrcSyncInfoID.setModifyId(prdPrcHInfoID.getValues().getString("modrId"));
				pPrdEaiPrcSyncInfoID.setSalePrc(prdPrcHInfoID.getValues().getBigDecimal("salePrc"));
				pPrdEaiPrcSyncInfoID.setPrchPrc(prdPrcHInfoID.getValues().getBigDecimal("prchPrc"));
				pPrdEaiPrcSyncInfoID.setSupGivRtamtCd(prdPrcHInfoID.getValues().getString("supGivRtamtCd"));
				if (prdPrcHInfoID.getValues().getString("supGivRtamtCd").equals("02")) {
					pPrdEaiPrcSyncInfoID.setSupGivRtamt(prdPrcHInfoID.getValues().getBigDecimal("supGivRtamt"));
				} else {
					pPrdEaiPrcSyncInfoID.setSupGivRtamt(prdPrcHInfoID.getValues().getBigDecimal("supGivRtamt")
							.setScale(0, BigDecimal.ROUND_DOWN));
				}
				pPrdEaiPrcSyncInfoID.setSupProprdUprc(prdPrcHInfoID.getValues().getBigDecimal("supProprdUprc"));
				pPrdEaiPrcSyncInfoID.setInstlCost(prdPrcHInfoID.getValues().getBigDecimal("instlCost"));
				pPrdEaiPrcSyncInfoID.setProprdWthtax(prdPrcHInfoID.getValues().getBigDecimal("proprdWthtax"));
				pPrdEaiPrcSyncInfoID.setVipDlvYn(prdPrcHInfoID.getValues().getString("vipDlvYn"));
				pPrdEaiPrcSyncInfoID.setVipDlvStdPrc(prdPrcHInfoID.getValues().getBigDecimal("vipDlvStdPrc"));
				pPrdEaiPrcSyncInfoID.setOnsitePrdPrc(prdPrcHInfoID.getValues().getBigDecimal("onsitePrdPrc"));
				pPrdEaiPrcSyncInfoID.setOnsiteDcPrc(prdPrcHInfoID.getValues().getBigDecimal("onsiteDcPrc"));
				pPrdEaiPrcSyncInfoID.setDetrmWeihtVal(prdPrcHInfoID.getValues().getString("detrmWeihtVal"));
				pPrdEaiPrcSyncInfoID.setOnsiteChrCost(prdPrcHInfoID.getValues().getBigDecimal("onsiteChrCost"));
				pPrdEaiPrcSyncInfoID.setWhsCd(prdPrcHInfoID.getValues().getString("whsCd"));
				pPrdEaiPrcSyncInfoID.setOtherSysTnsYn(prdPrcHInfoID.getValues().getString("otherSysTnsYn"));
				pPrdEaiPrcSyncInfoID.setNoteCntnt(prdPrcHInfoID.getValues().getString("noteCntnt"));

				pPrdEaiPrcSyncInfoID.setSapExcptYn("Y"); // 위드넷 가격정보 수정시에는 SAP에는 전송하지 않는다.
				pPrdEaiPrcSyncInfoList.add(pPrdEaiPrcSyncInfoID);

				logger.debug("pPrdEaiPrcSyncInfoList -----------> \n" + pPrdEaiPrcSyncInfoList);
				if (pPrdEaiPrcSyncInfoList.size() > 0) {
					gshseai.GSH_S4C_PRD01.WS.PRD_EAI_Products_Price_Reg_Mod.ws.wsProvider.PRD_EAI_Products_Price_Reg_Mod_P.PRD_EAI_Res res = wsPrdEaiPrdPriceRegModProcess.prdEaiPrdPricdRegModProcess(pPrdEaiPrcSyncInfoList);

					if (StringUtils.NVL(res.getResponseResult()).equals("E")) {
						throw new DevPrcException(res.getResponseMessage());
					}
				}

			}

		}//-else end
		
		
		//이중가격 등록 체크
		//API 상품가격 수정 진행 후 날짜가 29991231235959로 등록되어 있는 상품의 건수를 체크한다.
		//변경가 - 기존가 동일여부 체크
		//1. 입력된 유효시작일시 이후로 등록된 기존 가격들을 조회함.
		PrdPmoDtlGenrl prdPmoDtlGenrl = new PrdPmoDtlGenrl();
		prdPmoDtlGenrl.setPrdCd(prdmMain.getPrdCd());
		prdPmoDtlGenrl.setValidEndDtm("29991231235959");
		
		EntityDataSet<DSMultiData> getPrdApiPrcGenrl = prdPrcEntity.getPrdApiPrcGenrl(prdPmoDtlGenrl);
		
		//2, 조회된 결과가 1건일 경우만 변경가-기존가 동일여부를 체크함(1건 이상일 경우에는 미래가격 삭제를 위해 체크하지 않음)
		//체크항목 : 판매가, 유효종료일시, 협력사지급율/액코드, 협력사지급율/액 
		if(getPrdApiPrcGenrl.getValues() != null &&  getPrdApiPrcGenrl.getValues().size() > 1){
			for(int i = 1; i< getPrdApiPrcGenrl.getValues().size(); i++){ //1번값은 스킵
				if(getPrdApiPrcGenrl.getValues().getBigDecimal(0, "salePrc").equals(getPrdApiPrcGenrl.getValues().getBigDecimal(i, "salePrc"))
					&& getPrdApiPrcGenrl.getValues().getBigDecimal(0, "supGivRtamt").equals(getPrdApiPrcGenrl.getValues().getBigDecimal(i, "supGivRtamt"))
					&& getPrdApiPrcGenrl.getValues().getString(0, "supGivRtamtCd").equals(getPrdApiPrcGenrl.getValues().getString(i, "supGivRtamtCd"))){
					//같은 값으로 판단하며 해당 값을 업데이트 처리함.
					
					// 상품가격종료일시수정
					ExistPrdPrcModInfo pExistPrdPrcModInfo = new ExistPrdPrcModInfo(); // 기존상품가격수정정보

					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						Date dValidStrDtm = sdf.parse(getPrdApiPrcGenrl.getValues().getString(i-1, "validStrDtm"));
						Calendar c = Calendar.getInstance();
						c.setTime(dValidStrDtm);
						c.add(Calendar.SECOND, -1);
						dValidStrDtm = c.getTime();
						String sValidStrDtm = sdf.format(dValidStrDtm);
						
						pExistPrdPrcModInfo.setPrdCd(prdmMain.getPrdCd());
						pExistPrdPrcModInfo.setExistValidStrDtm(getPrdApiPrcGenrl.getValues().getString(i, "validStrDtm"));
						pExistPrdPrcModInfo.setExistValidEndDtm(getPrdApiPrcGenrl.getValues().getString(i, "validEndDtm"));
						pExistPrdPrcModInfo.setModValidEndDtm(sValidStrDtm);
						
						pExistPrdPrcModInfo.setSessionObject(pPrdPrcGenrl);
						pExistPrdPrcModInfo.setModrId(prdmMain.getSessionUserId());
						prdPrcEntity.modifyPrdPrcEndDtm(pExistPrdPrcModInfo);
						
					} catch (Exception e) {
						//do nothing
					}
					
					//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
					SMTCLogger.infoPrd("상품API 가격 이중등록 확인 : " + getPrdApiPrcGenrl.getValues().get(i).toString());
				}
			}
		}
		

		/* [S] [패널 2015_014_제도개선 변경]김현(slrip4) 2015-10-15 방송합의서 재합의 처리 */
		BroadBefMngInfo pBroadBefMngInfo = new BroadBefMngInfo();
		pBroadBefMngInfo.setPrdCd(prdmMain.getPrdCd());
		pBroadBefMngInfo.setSessionUserId(prdmMain.getSessionUserId());
		broadBefMngInfoList.add(pBroadBefMngInfo);
		returnMap.put("outBroadBefMngInfoList", EDSFactory.createObj(BroadBefMngInfo.class, broadBefMngInfoList));
		/* [E] [패널 2015_014_제도개선 변경]김현(slrip4) 2015-10-15 */

		returnMap.put("outSavePrdPrcInfo", EDSFactory.create(EsbCmm.class, resultData));
		return returnMap;

	}

	/**
	 * <pre>
	 *
	 * desc : 협력사테이블에 상품정보를 입력한다.(도서몰)
	 *
	 * </pre>
	 *
	 * @author KJH
	 * @date 2011-12-29 07:01:44
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> saveSupPrdBaseEsbInfo(PrdmMain prdmMain, List<PrdAttrPrdMinsert> pAttrPrdM, PrdprcHinsert prdPrcHinsert
														  , List<PrdDescdHtmlDInfo> prdDescdHtmlDInfoList, List<PrdNmChgHinsert> prdNmChgList
														  , PrdPrdDinsert prdPrdD, List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList, List<Cntnt> pCntntList
														  , List<PrdOrdPsblQtyDinsert> prdOrdPsblQtyD, List<PrdSpecVal> prdSpecInfo, PrdBookInfo pPrdBookInfo
														  , List<EcdSectPrdlstInfo> pSupPrdCtgrShopList, List<PrdPmoStore> addPrdPmoStoreList, List<PrdPmoGft> addPrdPmoGftList
														  , List<PrdGovPubls> addPrdGovPublsList){

		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
//		DSData resultData = new DSData();

		//상품마스터
		logger.debug("1. sup-prdmMain save start>>>>");
		prdmMain.setAprvInfoCd("01");		// 접수
//		int modifyCnt = prdEntity.modifySupPrdInfo(prdmMain);
//		if( modifyCnt < 1 ) {
//			prdEntity.addSupPrdInfo(prdmMain);
//		}
		int modifyCnt = 0;
		//속성상품마스터
		logger.debug("2. sup-attrPrdInfo save start>>>>");
		int cnt = 0;

		for(PrdAttrPrdMinsert pAttrPrdInfo : pAttrPrdM ) {
			cnt++;

			pAttrPrdInfo.setSupCd(prdmMain.getSupCd());
			pAttrPrdInfo.setSupPrdCd(prdmMain.getSupPrdCd());

			if( "".equals(StringUtils.NVL(pAttrPrdInfo.getSupAttrPrdCd())) ){
				pAttrPrdInfo.setSupAttrPrdCd(prdmMain.getSupPrdCd()+"00"+cnt);
			}

			pAttrPrdInfo.setAprvInfoCd("01");		// 접수

			//속성상품코드만 키로 되어있어서 lOCK 발생 주석처리.
//			modifyCnt = prdEntity.modifySupAttrPrdInfo(pAttrPrdInfo);
//			if( modifyCnt < 1 ) {
//				prdEntity.addSupAttrPrdInfo(pAttrPrdInfo);
//			}
		}

		//상품가격정보
		logger.debug("3. sup-prdPrcH save start>>>>");
		prdPrcHinsert.setSupCd(prdmMain.getSupCd());
		prdPrcHinsert.setSupPrdCd(prdmMain.getSupPrdCd());
		prdPrcHinsert.setAprvInfoCd("01");		// 접수
		prdEntity.saveSupPrdPrcHInfo(prdPrcHinsert);

		//html기술서
		logger.debug("4. sup-prdDescdHtmlD save start>>>>");
		for(PrdDescdHtmlDInfo prdDescdHtmlDInfo : prdDescdHtmlDInfoList ) {
			prdDescdHtmlDInfo.setSupCd(prdmMain.getSupCd());
			prdDescdHtmlDInfo.setSupPrdCd(prdmMain.getSupPrdCd());
			prdDescdHtmlDInfo.setAprvInfoCd("01");		// 접수

			modifyCnt = prdEntity.modifySupPrdDescdHtmlInfo(prdDescdHtmlDInfo);
			if( modifyCnt < 1 ) {
				prdEntity.addSupPrdDescdHtmlInfo(prdDescdHtmlDInfo);
			}
		}

		//상품이름변경정보
		logger.debug("5. sup-prdNmChg save start>>>>");
		for(PrdNmChgHinsert prdNmChgInfo : prdNmChgList ) {
			prdNmChgInfo.setSupCd(prdmMain.getSupCd());
			prdNmChgInfo.setSupPrdCd(prdmMain.getSupPrdCd());
			prdNmChgInfo.setAprvInfoCd("01");		// 접수

			prdEntity.saveSupPrdNmChgInfo(prdNmChgInfo);
		}

		//상품확장정보
		logger.debug("6. sup-prdPrdD save start>>>>");
		prdPrdD.setSupCd(prdmMain.getSupCd());
		prdPrdD.setSupPrdCd(prdmMain.getSupPrdCd());
		prdEntity.saveSupPrdD(prdPrdD);

		//일반기술서
		logger.debug("7. sup-prdDescdGenrlD save start>>>>");
		if(!"33".equals(prdmMain.getPrdGbnCd().toString())){ // 일본상품 일반기술서와 BPR 일반기술서 정책이 틀림 isKang 20130320
			for( PrdDesceGenrlDInfo prdDesceGenrlDInfo : prdDesceGenrlDInfoList ) {
				prdDesceGenrlDInfo.setSupCd(prdmMain.getSupCd());
				prdDesceGenrlDInfo.setSupPrdCd(prdmMain.getSupPrdCd());

				prdEntity.saveSupPrdDescdGenrlInfo(prdDesceGenrlDInfo);
			}
		}

		//이미지컨텐츠
		logger.debug("8. sup-prdCntnt save start>>>>");
		for( Cntnt pCntntInfo : pCntntList ) {
			pCntntInfo.setSupCd(prdmMain.getSupCd());
			pCntntInfo.setSupPrdCd(prdmMain.getSupPrdCd());

			prdEntity.saveSupPrdCntntInfo(pCntntInfo);
		}

		//주문가능수량
		logger.debug("9. sup-prdOrdPsblQtyD save start>>>>");
		cnt = 0;
		for( PrdOrdPsblQtyDinsert prdOrdPsblQtyInfo : prdOrdPsblQtyD ) {
			cnt++;

			prdOrdPsblQtyInfo.setSupCd(prdmMain.getSupCd());
			if( "".equals(StringUtils.NVL(prdOrdPsblQtyInfo.getSupAttrPrdCd())) ){
				prdOrdPsblQtyInfo.setSupAttrPrdCd(prdmMain.getSupPrdCd()+"00"+cnt);
			}

			//prdEntity.saveSupPrdOrdPsblQtyInfo(prdOrdPsblQtyInfo);
		}

		//상품사양
		logger.debug("10. sup-prdSpecValD save start>>>>");
		for( PrdSpecVal prdSpecValInfo : prdSpecInfo ) {
			prdSpecValInfo.setSupCd(prdmMain.getSupCd());
			prdSpecValInfo.setSupPrdCd(prdmMain.getSupPrdCd());

			prdEntity.saveSupPrdSpecInfo(prdSpecValInfo);
		}

		//도서상품정보 - 도서제휴상품일 경우에만 도서정보 입력 - 차후 다른 도서제휴가 들어올 경우엔 sup_uda_d로 협력사 관리하는 코드로 따로 처리한다.
		//[2015-03-03][유수경]:도서몰 인터파크(1028208 ) 추가
		if(  "1018999".equals(prdmMain.getSupCd().toString()) ||
			 "1028208".equals(prdmMain.getSupCd().toString())	) {
			logger.debug("11. sup-prdBookD save start>>>>");
			pPrdBookInfo.setSupCd(prdmMain.getSupCd());
			pPrdBookInfo.setSupPrdCd(prdmMain.getSupPrdCd());
			prdEntity.saveSupPrdBookInfo(pPrdBookInfo);
		}

		//매장
		logger.debug("12. sup-prdSectShop save start>>>>");
		for( EcdSectPrdlstInfo pSupPrdCtgrShopInfo : pSupPrdCtgrShopList ){
			if( !"".equals(StringUtils.NVL(pSupPrdCtgrShopInfo.getSectid())) ) {
				pSupPrdCtgrShopInfo.setSupCd(prdmMain.getSupCd());
				pSupPrdCtgrShopInfo.setSupPrdCd(prdmMain.getSupPrdCd());

				prdEntity.saveSupPrdSectInfo(pSupPrdCtgrShopInfo);
			}
		}

		//프로모션
		logger.debug("13. sup-prdPmoStoreInfo save start>>>>");
		for( PrdPmoStore prdPmoStoreInfo : addPrdPmoStoreList ) {
			prdPmoStoreInfo.setSupCd(prdmMain.getSupCd());
			prdPmoStoreInfo.setSupPrdCd(prdmMain.getSupPrdCd());

			//협력사상품프로모션순번채번
			DSData supPrdPmoSeq = prdEntity.getSupPrdPmoSeq(prdPmoStoreInfo);
			if( supPrdPmoSeq != null ) {
				prdPmoStoreInfo.setPrdPmoSeq(supPrdPmoSeq.getBigDecimal("supPrdPmoSeq"));

				prdEntity.addSupPrdPmoInfo(prdPmoStoreInfo);

				//오퍼유형코드가 사은품일때 사은품프로모션 등록
				if("13".equals(prdPmoStoreInfo.getOfferTypCd()) && !"".equals(StringUtils.NVL(prdmMain.getPmoGftListCnt())) && !"0".equals(StringUtils.NVL(prdmMain.getPmoGftListCnt())) ){
					//사은품프로모션
					for( PrdPmoGft prdPmoGftInfo : addPrdPmoGftList ) {
						prdPmoGftInfo.setSupCd(prdmMain.getSupCd());
						prdPmoGftInfo.setSupPrdCd(prdmMain.getSupPrdCd());
						prdPmoGftInfo.setPrdPmoSeq(prdPmoStoreInfo.getPrdPmoSeq());

						prdEntity.addSupPrdPmoGftInfo(prdPmoGftInfo);
					}
				}
			}
		}

		/*
		//정부고시
		logger.debug("14. sup-prdGovPublsInfo save start>>>>");
		for( PrdGovPubls addPrdGovPublsInfo : addPrdGovPublsList ) {
			addPrdGovPublsInfo.setSupCd(prdmMain.getSupCd());
			addPrdGovPublsInfo.setSupPrdCd(prdmMain.getSupPrdCd());

			prdEntity.saveSupPrdGovPublsInfo(addPrdGovPublsInfo);
		}
		*/
		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 협력사테이블에 상품 로그정보를 입력한다.(도서몰)
	 *
	 * </pre>
	 *
	 * @author KJH
	 * @date 2011-12-29 07:01:44
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> addSupPrdBaseEsbLogInfo(PrdmMain prdmMain, List<PrdAttrPrdMinsert> pAttrPrdM, PrdprcHinsert prdPrcHinsert
														  , List<PrdDescdHtmlDInfo> prdDescdHtmlDInfoList, List<PrdNmChgHinsert> prdNmChgList){

		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
//		DSData resultData = new DSData();
		//상품마스터
		logger.debug("1. Log - sup-prdmMain save start>>>>");
		prdEntity.addSupPrdLogInfo(prdmMain);

		//속성상품마스터
		logger.debug("2. Log - sup-attrPrdInfo save start>>>>");
		int cnt = 0;
		for(PrdAttrPrdMinsert pAttrPrdInfo : pAttrPrdM ) {
			cnt++;
			pAttrPrdInfo.setSupCd(prdmMain.getSupCd());
			pAttrPrdInfo.setSupPrdCd(prdmMain.getSupPrdCd());

			if( "".equals(StringUtils.NVL(pAttrPrdInfo.getSupAttrPrdCd())) ){
				pAttrPrdInfo.setSupAttrPrdCd(prdmMain.getSupPrdCd()+"00"+cnt);
			}

			prdEntity.addSupAttrPrdLogInfo(pAttrPrdInfo);
		}

		//상품가격정보
		logger.debug("3. Log - sup-prdPrcH save start>>>>");
		prdPrcHinsert.setSupCd(prdmMain.getSupCd());
		prdPrcHinsert.setSupPrdCd(prdmMain.getSupPrdCd());
		prdEntity.addSupPrdPrcLogInfo(prdPrcHinsert);

		//html기술서
		logger.debug("4. Log - sup-prdDescdHtmlD save start>>>>");
		for(PrdDescdHtmlDInfo prdDescdHtmlDInfo : prdDescdHtmlDInfoList ) {
			prdDescdHtmlDInfo.setSupCd(prdmMain.getSupCd());
			prdDescdHtmlDInfo.setSupPrdCd(prdmMain.getSupPrdCd());

			prdEntity.addSupPrdDescdHtmlLogInfo(prdDescdHtmlDInfo);
		}

		//상품이름변경정보
		logger.debug("5. Log - sup-prdNmChg save start>>>>");
		for(PrdNmChgHinsert prdNmChgInfo : prdNmChgList ) {
			prdNmChgInfo.setSupCd(prdmMain.getSupCd());
			prdNmChgInfo.setSupPrdCd(prdmMain.getSupPrdCd());

			prdEntity.addSupPrdNmChgLogInfo(prdNmChgInfo);
		}

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 협력사테이블에 상품 등록상태를 수정한다.(도서몰)
	 *
	 * </pre>
	 *
	 * @author KJH
	 * @date 2011-12-29 07:01:44
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> modifySupPrdBaseEsbSt(PrdmMain prdmMain, List<PrdAttrPrdMinsert> pAttrPrdM, PrdprcHinsert prdPrcHinsert
														  , List<PrdDescdHtmlDInfo> prdDescdHtmlDInfoList, List<PrdNmChgHinsert> prdNmChgList){

		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
//		DSData resultData = new DSData();
		//상품마스터
		logger.debug("1. St - sup-prdmMain save start>>>>");
		prdmMain.setAprvInfoCd("03");		//성공
		prdEntity.modifySupPrdSt(prdmMain);

		//속성상품마스터
		logger.debug("2. St - sup-attrPrdInfo save start>>>>");
		for(PrdAttrPrdMinsert pAttrPrdInfo : pAttrPrdM ) {
			pAttrPrdInfo.setPrdCd(prdmMain.getPrdCd());
			pAttrPrdInfo.setSupCd(prdmMain.getSupCd());
			pAttrPrdInfo.setSupPrdCd(prdmMain.getSupPrdCd());
			pAttrPrdInfo.setAprvInfoCd("03");	//성공

			prdEntity.modifySupAttrPrdSt(pAttrPrdInfo);
		}

		//상품가격정보
		logger.debug("3. St - sup-prdPrcH save start>>>>");
		prdPrcHinsert.setSupCd(prdmMain.getSupCd());
		prdPrcHinsert.setSupPrdCd(prdmMain.getSupPrdCd());
		prdPrcHinsert.setAprvInfoCd("03");	//성공

		prdEntity.saveSupPrdPrcHInfo(prdPrcHinsert);

		//html기술서
		logger.debug("4. St - sup-prdDescdHtmlD save start>>>>");
		for(PrdDescdHtmlDInfo prdDescdHtmlDInfo : prdDescdHtmlDInfoList ) {
			prdDescdHtmlDInfo.setSupCd(prdmMain.getSupCd());
			prdDescdHtmlDInfo.setSupPrdCd(prdmMain.getSupPrdCd());
			prdDescdHtmlDInfo.setAprvInfoCd("03");	//성공

			prdEntity.modifySupPrdDescdHtmlInfo(prdDescdHtmlDInfo);
		}

		//상품이름변경정보
		logger.debug("5. St - sup-prdNmChg save start>>>>");
		for(PrdNmChgHinsert prdNmChgInfo : prdNmChgList ) {
			prdNmChgInfo.setSupCd(prdmMain.getSupCd());
			prdNmChgInfo.setSupPrdCd(prdmMain.getSupPrdCd());
			prdNmChgInfo.setAprvInfoCd("03");	//성공

			prdEntity.saveSupPrdNmChgInfo(prdNmChgInfo);
		}

		return returnMap;
	}

	@Override
	public List<PrdDescdSyncInfo> addPrdDescdASS4c( PrdmMain prdmMain, List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList  ) {
		List<PrdDescdSyncInfo> prdDescdSyncInfoList = new ArrayList<PrdDescdSyncInfo>();
		PrdDescdHtmlDInfo  prdDescdHtmlDInfo  = new PrdDescdHtmlDInfo();
		EntityDataSet<DSData> entityDataSet = null;

		if(prdDesceGenrlDInfoList.size() > 0 ) {
			logger.debug("size>>>>"+prdDesceGenrlDInfoList.size());
			for(PrdDesceGenrlDInfo prdDesceGenrlDInfo : prdDesceGenrlDInfoList) {
				prdDesceGenrlDInfo.setPrdCd(prdmMain.getPrdCd());
				// 상품기술서 정보를 INSERT -> 시퀀스 채번 후 입력방식으로 변경 (2011/03/26 OSM)
				entityDataSet = prdEntity.getDescdItmSeq();
				prdDesceGenrlDInfo.setDescdItmSeq(entityDataSet.getValues().getBigDecimal("descdItmSeq"));
				prdEntity.setPrdDescdGenrlD(prdDesceGenrlDInfo);

				prdDescdSyncInfoList.add(new PrdDescdSyncInfo(" " // attrPrdRepCd 상품코드 PRD_STOCK_D 테이블
				        , " " // descdExplnCntnt 기술서설명내용
				        , prdDesceGenrlDInfo.getDescdItmSeq() // descdItmSeq 기술서항목순번
				        , " " // genrlDescdId 일반기술서ID
				        , "I" // jobType JOB TYPE I:등록 D: 삭제 U: 수정
				        , prdDesceGenrlDInfo.getPrdCd() // prdCd 상품코드
				        ));
				prdDescdHtmlDInfo.setPrdCd(prdmMain.getPrdCd()) ;
				prdDescdHtmlDInfo.setChanlCd("P");
				prdDescdHtmlDInfo.setRegGbnCd("A");
				prdDescdHtmlDInfo.setSessionObject(prdmMain);
				// 일반 기술서 등록 후  HTML 기술서 변환
			}
			this.setPrdDescdHtmlD(prdDescdHtmlDInfo);
		}
		return prdDescdSyncInfoList;
	}

	/**
	 * <pre>
	 *
	 * desc : 협력사테이블에 상품예정정보를 입력한다.(제휴몰)
	 *
	 * </pre>
	 *
	 * @author KJH
	 * @date 2011-12-29 07:01:44
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public int saveSupPrdRsrvOrdPrdEsbInfo(RsrvOrdPrd pRsrvOrdPrdInfo){
		int cnt = prdEntity.saveSupPrdRsrvOrdPrdEsbInfo(pRsrvOrdPrdInfo);
		return cnt;
	}

	public void setPrdPmoEntity(PrdPmoEntity prdPmoEntity) {
		this.prdPmoEntity = prdPmoEntity;
	}

	public void setPrdSpecInfoEntity(PrdSpecInfoEntity prdSpecInfoEntity) {
		this.prdSpecInfoEntity = prdSpecInfoEntity;
	}

	public void setPrdStockInfoEntity(PrdStockInfoEntity prdStockInfoEntity) {
		this.prdStockInfoEntity = prdStockInfoEntity;
	}

	public void setPrdSuplyPlanEntity(PrdSuplyPlanEntity prdSuplyPlanEntity) {
		this.prdSuplyPlanEntity = prdSuplyPlanEntity;
	}

	public void setPrdUdaEntity(PrdUdaEntity prdUdaEntity) {
		this.prdUdaEntity = prdUdaEntity;
	}

	public void setShtprdOrdEntity(ShtprdOrdEntity shtprdOrdEntity) {
		this.shtprdOrdEntity = shtprdOrdEntity;
	}

	public void setSmsTnsBaseEntity(SmsTnsBaseEntity smsTnsBaseEntity) {
	}

	public void setSubSupEntity(SubSupEntity subSupEntity) {
		this.subSupEntity = subSupEntity;
	}

	public void setSupEntity(SupEntity supEntity) {
		this.supEntity = supEntity;
	}
	public void setPrdColChgLogEntity(PrdColChgLogEntity  prdColChgLogEntity) {
		this.prdColChgLogEntity = prdColChgLogEntity;
	}

	public void setPrdLimitChgLogEntity(PrdLimitChgLogEntity prdLimitChgLogEntity ) {
	}
	public void setWsPrdEaiAttrPrdSyncProcess(WsPrdEaiAttrPrdSyncProcess wsPrdEaiAttrPrdSyncProcess) {
		this.wsPrdEaiAttrPrdSyncProcess = wsPrdEaiAttrPrdSyncProcess;
	}

	public void setWsPrdEaiPrdSyncProcess(WsPrdEaiPrdSyncProcess wsPrdEaiPrdSyncProcess) {
		this.wsPrdEaiPrdSyncProcess = wsPrdEaiPrdSyncProcess;
	}

	public void setWsPrdBisPrdItemMappnSyncProcess(WsPrdBisPrdItemMappnSyncProcess wsPrdBisPrdItemMappnSyncProcess) {
		this.wsPrdBisPrdItemMappnSyncProcess = wsPrdBisPrdItemMappnSyncProcess;
	}
	public void setWsPrdBisPrdDescdRegModProcess(WsPrdBisPrdDescdRegModProcess wsPrdBisPrdDescdRegModProcess) {
		this.wsPrdBisPrdDescdRegModProcess = wsPrdBisPrdDescdRegModProcess;
	}
	public void setOdsBroadBefMngInfoEntity(OdsBroadBefMngInfoEntity odsBroadBefMngInfoEntity) {
		this.odsBroadBefMngInfoEntity = odsBroadBefMngInfoEntity;
	}

	public void setPrdClsSpecInfoEntity(PrdClsSpecInfoEntity prdClsSpecInfoEntity) {
		this.prdClsSpecInfoEntity = prdClsSpecInfoEntity;
	}

	public void setPrdPrcMngCmmProcess(PrdPrcMngCmmProcess prdPrcMngCmmProcess) {
		this.prdPrcMngCmmProcess = prdPrcMngCmmProcess;
	}
	/**
	 * <pre>
	 *
	 * desc : BPR프로젝트
	 * 속성코드별 사은품 등록
	 * </pre>
	 *
	 * @author
	 * @date 2012-12-24 10:24:37
	 * @param HttpServletRequest
	 *            request
	 * @return
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> getAttrListP(RSPDataSet dataSet) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		String key = "inGetPrdList";
		PrdQryCond pPrdQryCond = dataSet.getDataset4NormalObjFirst(key, PrdQryCond.class);

		// 속성 코드별 사은품 등록
		returnMap.put("outGetAttrList", prdEntity.getAttrListP(pPrdQryCond));

		return returnMap;
	}

	/**
	 * <pre>
	 *
	 * desc : 분류검증자동합격
	 *
	 * </pre>
	 *
	 * @author KJH
	 * @date 2011-12-29 07:01:44
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map<String, EntityDataSet> modifyPrdClsChkAutoPass(ClsChkChg pClsChkChg) throws Exception {
		// 분류검증변경상품수정
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();

		PrdQryCond pPrdQryCond = new PrdQryCond();
		pPrdQryCond.setPrdCd(pClsChkChg.getPrdCd());
		
		EntityDataSet<DSData> pPrdBaseEDS = prdEntity.getPrdBase(pPrdQryCond);
		
		if(pPrdBaseEDS == null) {
			DSData returnDsData = new DSData();
			returnDsData.put("prdCd", pClsChkChg.getPrdCd());
			returnDsData.put("retCd", "E");
			returnDsData.put("retMsg", "상품정보가 정확하지 않아 분류검증 변경되지 않았습니다.");
			returnMap.put("outSavePrdBase", EDSFactory.create(EsbCmm.class, returnDsData));
			return returnMap;
		}
		
		/* [SKU][2021.01.05]:SKU 프로젝트 */
		if ("Y".equals(pPrdBaseEDS.getValues().getString("skuYn"))
		    && !"Y".equals(pPrdBaseEDS.getValues().getString("skuCnfYn"))) {
			throw new DevPrcException(pClsChkChg.getPrdCd()+"는 SKU운영상품입니다. 매핑확정 정보를 확인 하시기 바랍니다.");
		}
		
		DSData pPrdBaseInfoDS =  prdEntity.getPrdBase(pPrdQryCond).getValues();
		
		PrdBaseInfo pPrdBaseInfo = DevBeanUtils.wrappedMapToBean(pPrdBaseInfoDS, PrdBaseInfo.class);
		pPrdBaseInfo.setSessionObject(pClsChkChg);

		pClsChkChg.setPrdClsCd(pPrdBaseInfo.getPrdClsCd());
		pClsChkChg.setPrdChkGbnCd(pPrdBaseInfo.getChanlCd());
		pClsChkChg.setCopyCnt(pPrdBaseInfo.getCopyPrdYn().equals("Y")? "1":"0");
		pClsChkChg.setChkCnt("1");
		pClsChkChg.setChgCnt("0");
		pClsChkChg.setOperMdId(pPrdBaseInfo.getOperMdId());
		pClsChkChg.setRegDt(pPrdBaseInfo.getRegDtm());
		pClsChkChg.setSessionUserId("AUTO");
		pPrdQryCond.setPrdClsCd(pPrdBaseInfo.getPrdClsCd());

		prdEntity.modifyClsChkChgPrd(pClsChkChg);

		String clsChkAftAprvCd = pPrdBaseInfo.getClsChkAftAprvCd();
		String prdAprvStCd = pPrdBaseInfo.getPrdAprvStCd();

		/* 분류검증 완료 후 qa검사 상품은 qa의뢰로 처리한다.2013.02.05 [bpr] */
	    if(!"Q".equals(StringUtils.NVL(clsChkAftAprvCd.trim())) &&
	    		!"M".equals(StringUtils.NVL(clsChkAftAprvCd.trim())) &&
	    		StringUtils.NVL(prdAprvStCd).equals("00")) {
	    	logger.debug("clsChkAftAprvCd ================== " + clsChkAftAprvCd);
	    	//prdEntity.insertQaReq(pPrdBaseInfo);
	    	//QA 의뢰 신규메소드로 변경 (sap 재구축 2013/04/11 안승훈)
	    	PrdmMain prdmMain = new PrdmMain();
	    	prdmMain.setSessionObject(pPrdBaseInfo);
	    	prdmMain.setPrdCd(pPrdBaseInfo.getPrdCd());
	    	prdmMain.setQaGrdCd(pPrdBaseInfo.getQaGrdCd());

	    	EntityDataSet<DSMultiData> attrPrdCdList = prdEntity.getAttrPrdCdList(pPrdBaseInfo);
			List<PrdAttrPrdMinsert> attrPrdMList = DevBeanUtils.wrappedListMapToBeans(attrPrdCdList.getValues(), PrdAttrPrdMinsert.class);
			for (PrdAttrPrdMinsert attrPrd : attrPrdMList) {
				attrPrd.setSessionObject(pPrdBaseInfo);
			}
	    	prdQaBefReqProcess.requestMdAprvQaBefReq(prdmMain, attrPrdMList);			//prdmMain에는 prdCd, qaGrdCd 가 필요함.

	    	logger.debug("QA 의뢰 완료 ================= " );
	    }
	    /* 심의대상 키워드에 포함된경우 심의요청처리 한다. SR02130801086 2013.08.13*/
	    PrdDesceGenrlDInfo prdDesceGenrlDInfo = new PrdDesceGenrlDInfo();
	    int kekwdCnt =  prdClsBaseEntity.getExposKeywdCnt(pPrdQryCond).getInt("cnt");		//prdClsCd, prdCd가 필요함
	    if (kekwdCnt > 0 ) {
	    	pPrdBaseInfo.setCnsdrTgtYn("Y");
	    	prdDesceGenrlDInfo.setCnsdrReqStdVal("KEYWD");
	    	prdDesceGenrlDInfo.setCnsdrCntnt(prdClsBaseEntity.getExposKeywdCnt(pPrdQryCond).getString("keywdCntnt"));
	    }
		/*심의대상분류이고 심의 신규 상태이면 심의 요청으로 변경 2013.03.11 BPR 박현신*/
	    /* 사은품, 경품의 경우 EC심의 면제 2013-04-23 KJH */
	    if (StringUtils.NVL(pPrdBaseInfo.getCnsdrTgtYn()).equals("Y") && "00".equals(pPrdBaseInfo.getGftTypCd()) ) {
	    	prdDesceGenrlDInfo.setSessionObject(pPrdBaseInfo);
	    	prdDesceGenrlDInfo.setPrdCd(pPrdBaseInfo.getPrdCd());
	    	prdDesceGenrlDInfo.setPrdCnsdrStCd("1");
	    	logger.debug("심의요청");
	    	prdEntity.setPrdCnsdrStCd(prdDesceGenrlDInfo) ;
	    }
		if (clsChkAftAprvCd.equals("M") && !prdAprvStCd.equals("30")) {
			// MD팀장합격수정
			List<MdTeamldrAprvTgt> mdTeamldrAprvTgtList = new ArrayList<MdTeamldrAprvTgt>();
			MdTeamldrAprvTgt mdTeamldrAprvTgt = new MdTeamldrAprvTgt();
			mdTeamldrAprvTgt.setPrdCd(pPrdBaseInfo.getPrdCd());
			mdTeamldrAprvTgt.setStyleDirEntYn(pPrdBaseInfo.getStyleDirEntYn()); // 스타일 입력여부 EAI 값 셋팅
			mdTeamldrAprvTgt.setPrdAprvStCd("30");
			mdTeamldrAprvTgt.setChk("1");
			mdTeamldrAprvTgt.setClsChkAftAprvCd("M");
			mdTeamldrAprvTgt.setPrdAttrGbnCd("P");
			mdTeamldrAprvTgt.setSessionObject(pPrdBaseInfo);
			mdTeamldrAprvTgtList.add(mdTeamldrAprvTgt);
			prdAprvMngProcess.modifyMdTeamldrPass(mdTeamldrAprvTgtList);

		}else if(clsChkAftAprvCd.equals("Q")) {

			MdTeamldrAprvTgt mdTeamldrAprvTgt = new MdTeamldrAprvTgt();
			mdTeamldrAprvTgt.setPrdCd(pPrdBaseInfo.getPrdCd());
			mdTeamldrAprvTgt.setPrdAprvStCd("25");
			mdTeamldrAprvTgt.setChk("1");
			mdTeamldrAprvTgt.setClsChkAftAprvCd("Q");
			mdTeamldrAprvTgt.setPrdAttrGbnCd("P");
			mdTeamldrAprvTgt.setSessionUserId(pClsChkChg.getSessionUserId());
			logger.debug(mdTeamldrAprvTgt);
			//QA팀장합격처리 to sm4c
			prdEntity.modifyPrdMdTeamldrPass(mdTeamldrAprvTgt);
			attrPrdEntity.modifyAttrPrdAprv(mdTeamldrAprvTgt);

			// QA팀장합격  EAI 전송
			gshseai.GSH_S4C_PRD03.WS.PRD_EAI_QA_Approval_Record.ws.wsProvider.PRD_EAI_QA_Approval_Record_P.PRD_EAI_Res res = new gshseai.GSH_S4C_PRD03.WS.PRD_EAI_QA_Approval_Record.ws.wsProvider.PRD_EAI_QA_Approval_Record_P.PRD_EAI_Res();
			PrdQaAprvIfCond prdQaAprvIfCond = new PrdQaAprvIfCond();

			logger.debug("test1");
//						String strStyleDirEntYn = pPrdBaseInfo.getStyleDirEntYn(); // STYLE_DIR_ENT_YN
			// QA승인내역수정 EAI 전송데이터 셋팅
			AttrPrdQryCond pAttrPrdQryCond = new AttrPrdQryCond();
			pAttrPrdQryCond.setPrdCd(pClsChkChg.getPrdCd()); // 속성상품코드 셋팅
			EntityDataSet<DSMultiData> pGetAttrPrdList = attrPrdEntity.getAttrPrdList(pAttrPrdQryCond);

			for (int i = 0; i < pGetAttrPrdList.size(); i++) {
				logger.debug("test2");
				// QA승인내역수정 EAI 전송데이터 셋팅
				prdQaAprvIfCond.setStyleDirEntYn(pPrdBaseInfo.getStyleDirEntYn()); // 스타일 입력여부 EAI 값 셋팅
				prdQaAprvIfCond.setPrdCd(pClsChkChg.getPrdCd());    // 상품코드
				prdQaAprvIfCond.setAttrPrdCd(pGetAttrPrdList.getValues().getBigDecimal(i, "attrPrdCd"));     // 속성상품코드
				prdQaAprvIfCond.setPrdAprvStCd("25"); // 상품결재상태 코드
				prdQaAprvIfCond.setAttrPrdAprvStCd("25"); // 상품결재상태 코드
				prdQaAprvIfCond.setQaGrdCd(pPrdBaseInfo.getQaGrdCd());     // QA등급코드
				prdQaAprvIfCond.setQaPrgStCd(pPrdBaseInfo.getQaPrgStCd()); // QA진행상태코드
				prdQaAprvIfCond.setPrdTypCd(pPrdBaseInfo.getPrdTypCd());   //

				if (prdQaAprvIfCond.getPrdCd() != null) {
					res = wsPrdEaiQaAprvProcess.prdEaiQaAprvProcess(prdQaAprvIfCond);
					if ("E".equals(res.getResponseResult())) {
						// 롤백
						throw new Exception(res.getResponseMessage());

					}
				 }
			}
		}

		// 분류검증변경이력등록
		prdEntity.addClsChkChgHist(pClsChkChg);

		PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
		pPrdEaiPrdSyncInfo.setPrdCd(pClsChkChg.getPrdCd());

		gshseai.GSH_S4C_PRD02.WS.PRD_EAI_Internal_Product.ws.wsProvider.PRD_EAI_Internal_Product_P.PRD_EAI_Res pRD_EAI_Res = new gshseai.GSH_S4C_PRD02.WS.PRD_EAI_Internal_Product.ws.wsProvider.PRD_EAI_Internal_Product_P.PRD_EAI_Res();
		// 상품 동기화
		try {

			if(//( pClsChkChg.getPrdCd().toString().length() >= 9) || [HANGBOT-28953_GSITMBO-15549][2022.01.10][김진석]:[상품]SAP로 상품코드가 내려오지 않는 현상
				 "20".equals(StringUtils.NVL(pPrdBaseInfo.getBundlPrdGbnCd()))){
				pPrdEaiPrdSyncInfo.setSapSaleBlockYn("Y");
			}
	
			pRD_EAI_Res = wsPrdEaiPrdSyncProcess.prdEaiPrdSyncProcess(pPrdEaiPrdSyncInfo);
			logger.debug("pRD_EAI_Res val=>" + pRD_EAI_Res.getResponseResult());
		} catch (Exception e) {
			pRD_EAI_Res.setResponseResult("E");
			pRD_EAI_Res.setResponseMessage("IFEAIPRD0121 :::: Exception 발생");
			logger.debug("IFEAIPRD0121 :::: Exception 발생::" + e);
		}

		if (pRD_EAI_Res.getResponseResult().equals("E")) {
			// throw new Exception();
		}

		DSData returnDsData = new DSData();
		returnDsData.put("prdCd", pPrdBaseInfo.getPrdCd());
		returnDsData.put("retCd", "S");
		returnDsData.put("retMsg", "저장성공");
		returnMap.put("outSavePrdBase", EDSFactory.create(EsbCmm.class, returnDsData));
		return returnMap;
	}

	public void setWsPrdEaiQaAprvProcess(WsPrdEaiQaAprvProcess wsPrdEaiQaAprvProcess) {
		this.wsPrdEaiQaAprvProcess = wsPrdEaiQaAprvProcess;
	}

	/**
	 * <pre>
	 *
	 * desc : 상품기술서 일반(HTML정보)를  입력한다.
	 * 일반기술서 저장(AS정보 포함) / 2013-10-22 / [일반기술서 정보고시 통합] / kimky73
	 *
	 * </pre>
	 * @author kimky73
	 * @date 2013-10-29
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public void setPrdDescdHtmlDNew(PrdDescdHtmlDInfo prdDescdHtmlDInfo) throws DevEntException {
		// 상품기술서(일반, INTRNT_EXPOS_YN = Y, CHANL_CD = A) 조회
		/*
		 * [일반기술서 정보고시 통합] : 기술서항목코드(DESCD_ITM_CD)는 시너어몰 코드 매핑을 위해
		 * 시니어몰상품설명항목코드(SNRM_PRD_EXPLN_ITM_CD)를 가지고 온다.
		*/
		EntityDataSet<DSMultiData> prdDescdGenrlDList = prdExplnEntity.getPrdDescdHtmlDListNew(prdDescdHtmlDInfo);
		// 상품기술서(일반 HTML정보) 저장

		String pChanlCd = StringUtils.NVL(prdDescdHtmlDInfo.getChanlCd(), "P"); //2013.3.25 고정현
		String pEcExposYn = StringUtils.NVL(prdDescdHtmlDInfo.getEcExposYn(), "N"); //2013.3.25 고정현

		if (prdDescdGenrlDList.size() > 0) {
			prdDescdHtmlDInfo.setChanlCd("P"); //2013.3.25 고정현

			//2012.11.212 고정현 시니어몰 채널 존재여부 체크
			QaInspYnQryCond qaInspYnQryCond = new QaInspYnQryCond();
			qaInspYnQryCond.setPrdCd(prdDescdHtmlDInfo.getPrdCd());
			qaInspYnQryCond.setChanlCd("S");
			DSData prdChanlD = prdChanlEntity.getQaInspYn(qaInspYnQryCond);

			logger.debug("setPrdDescdHtmlD - 일반기술서 저장시 시니어몰 채널존재여부 체크 prdChanlD.size() >>>" + prdChanlD.size() );
			if(prdChanlD.size() > 0){

				prdDescdHtmlDInfo.setChanlCd("S");
				if(pChanlCd.equals("S")) prdDescdHtmlDInfo.setEcExposYn(pEcExposYn) ;  //2013.3.25 고정현
				else prdDescdHtmlDInfo.setEcExposYn("N") ;  //2013.3.25 고정현

				// HTML정보 존재여부
				EntityDataSet<DSData> prdDescdHtmlDCnt = prdDescdHtmlDEntity.getPrdDescdHtmlDCnt(prdDescdHtmlDInfo);
				int cnt = prdDescdHtmlDCnt.getValues().getBigDecimal("cnt").intValue();

				prdDescdHtmlDEntity.setSnrmPrdDescdHtmlD(prdDescdGenrlDList, prdDescdHtmlDInfo, cnt);
			}
		}
	}

	/**
	 * <pre>
	 *
	 * desc : 상품기술서 일반(협력사)를  입력한다.
	 * 상품 승인 저장 / 2013-11-05 / [일반기술서 정보고시 통합] / kimky73
	 *
	 * </pre>
	 * @author kimky73
	 * @date 2013-11-05
	 * @param RSPDataSet dataSet
	 * @return Map<String, EntityDataSet>
	 */
	@Override
	public List<PrdDescdSyncInfo> addPrdDescdS4cNew( PrdmMain prdmMain, List<PrdDesceGenrlDInfo> prdDesceGenrlDInfoList,   List<PrdDescdHtmlDInfo>  prdDescdHtmlDInfoList ) {
		List<PrdDescdSyncInfo> prdDescdSyncInfoList = new ArrayList<PrdDescdSyncInfo>();
		PrdDesceGenrlDInfo prdDesceGenrlDInfo1  = new PrdDesceGenrlDInfo(); // 심의요청 용
		PrdDescdHtmlDInfo  prdDescdHtmlDInfo  = new PrdDescdHtmlDInfo();
//		EntityDataSet<DSData> entityDataSet = null;

		// 일반기술서 승인 등록
		logger.debug("qryGbn==="+prdmMain.getQryGbn());
		if("Y".equals(StringUtils.NVL(prdmMain.getQryGbn()))) {
			// 기술서를 조회
			SupPrdAprvQryCond pSupPrdAprvQryCond = new SupPrdAprvQryCond();
			pSupPrdAprvQryCond.setPrdCd(prdmMain.getPrdCd());
			pSupPrdAprvQryCond.setSupCd(prdmMain.getSupCd());
			pSupPrdAprvQryCond.setSupPrdCd(prdmMain.getSupPrdCd());

			pSupPrdAprvQryCond.setSuppGoodsCode(prdmMain.getSupPrdCd());
			//GE-HTML/GD일 경우는-일반기술서
			//채널에 상관없이 등록 처리
		//	if("GE".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()) )){
				// HTML기술서 승인 등록
					DSMultiData copyHtml = ecScmMngEntity.getSupPrdAprvDescdHtmlDList(pSupPrdAprvQryCond).getValues();
					if (copyHtml != null) {
						List<PrdHtmlDescd> prdHtmlList = new ArrayList<PrdHtmlDescd>();
						for (int i = 0; i < copyHtml.size(); i++) {
							PrdHtmlDescd prdHtml = new PrdHtmlDescd();
							prdHtml.setPrdCd(prdmMain.getPrdCd()); // 상품코드
							prdHtml.setChanlCd(copyHtml.get(i).getString("chanlCd")); // 채널코드
							prdHtml.setRegGbnCd(copyHtml.get(i).getString("regGbnCd")); // 등록구분코드
							prdHtml.setDescdExplnCntnt(copyHtml.get(i).getString("descdExplnCntnt")); // 기술서설명내용
							prdHtml.setRcmdSntncCntnt(copyHtml.get(i).getString("rcmdSntncCntnt")); // 추천문구내용
							prdHtml.setWritePrevntYn(copyHtml.get(i).getString("writePrevntYn")); // 쓰기방지여부
							prdHtml.setEcExposYn("Y"); // EC노출여부
							prdHtml.setSessionUserIp(prdmMain.getSessionUserIp());
							prdHtml.setSessionUserId(prdmMain.getSessionUserId());
							prdHtml.setSessionUserNm(prdmMain.getSessionUserNm());
							prdHtmlList.add(prdHtml);
						}
						prdHtmlDescdEntity.addHtmlDescdList(prdHtmlList); // 상품HTML기술서등록
					}
		//	} else
		//	if("GD".equals(StringUtils.NVL(prdmMain.getRegChanlGrpCd()))) {
			     DSMultiData copyDesc = ecScmMngEntity.getSupPrdAprvDescdGenrlDListNew(pSupPrdAprvQryCond).getValues();
				if (copyDesc != null) {
					for (int i = 0; i < copyDesc.size(); i++) {
						PrdDesceGenrlDInfo prdDesceGenrlDInfo = DevBeanUtils.wrappedMapToBean(copyDesc.get(i), PrdDesceGenrlDInfo.class);

						prdDesceGenrlDInfo.setPrdCd(prdmMain.getPrdCd()); // 상품코드
						prdDesceGenrlDInfo.setSessionObject(prdmMain);

						prdExplnEntity.setPrdDescdGenrlDNew(prdDesceGenrlDInfo);

						prdDescdSyncInfoList.add(new PrdDescdSyncInfo(" " // attrPrdRepCd 상품코드 PRD_STOCK_D 테이블
						        , " " // descdExplnCntnt 기술서설명내용
						        , prdDesceGenrlDInfo.getDescdItmSeq() // descdItmSeq 기술서항목순번
						        , " " // genrlDescdId 일반기술서ID
						        , "I" // jobType JOB TYPE I:등록 D: 삭제 U: 수정
						        , prdDesceGenrlDInfo.getPrdCd() // prdCd 상품코드
						        ));
						prdDescdSyncInfoList.get(prdDescdSyncInfoList.size()-1).setGovPublsPrdGrpCd(prdDesceGenrlDInfo.getGovPublsPrdGrpCd());
						prdDescdSyncInfoList.get(prdDescdSyncInfoList.size()-1).setPrdExplnItmCd(prdDesceGenrlDInfo.getPrdExplnItmCd());

						if( StringUtils.isEmpty( prdDescdHtmlDInfo.getGovPublsPrdGrpCd())
								&& !StringUtils.isEmpty(prdDesceGenrlDInfo.getGovPublsPrdGrpCd())
						){
							prdDescdHtmlDInfo.setGovPublsPrdGrpCd(prdDesceGenrlDInfo.getGovPublsPrdGrpCd());
						}

						prdDescdHtmlDInfo.setPrdCd(prdmMain.getPrdCd()) ;
						//prdDescdHtmlDInfo.setChanlCd(prdDesceGenrlDInfo.getChanlCd());
						prdDescdHtmlDInfo.setChanlCd("P");
						prdDescdHtmlDInfo.setRegGbnCd("A");
						prdDescdHtmlDInfo.setSessionObject(prdDesceGenrlDInfo);
						// 일반 기술서 등록 후  HTML 기술서 변환

						prdDesceGenrlDInfo1.setPrdCd(prdDesceGenrlDInfo.getPrdCd());
						prdDesceGenrlDInfo1.setPrdCnsdrStCd(prdDesceGenrlDInfo.getPrdCnsdrStCd());
						prdDesceGenrlDInfo1.setSessionObject(prdDesceGenrlDInfo);
					}

					this.setPrdDescdHtmlDNew(prdDescdHtmlDInfo);
				}
		//	}
		} else if("N".equals(StringUtils.NVL(prdmMain.getQryGbn()))) { // 직접받은 데이터
			// 일반기술서가 비어 있지 않을때
			if(prdDesceGenrlDInfoList.size() > 0 ) {
				logger.debug("size>>>>"+prdDesceGenrlDInfoList.size());

				String pPrdExplnItmCd = null; //상품설명항목코드

				for(PrdDesceGenrlDInfo prdDesceGenrlDInfo : prdDesceGenrlDInfoList) {
					prdDesceGenrlDInfo.setPrdCd(prdmMain.getPrdCd());

					//상품설명항목코드 생성
					if( StringUtils.isEmpty(prdDesceGenrlDInfo.getPrdExplnItmCd()) ){
						pPrdExplnItmCd = prdExplnEntity.getPrdExplnItmCd(prdDesceGenrlDInfo);

						if( logger.isDebugEnabled() ) logger.debug("pPrdExplnItmCd::" + pPrdExplnItmCd);

						prdDesceGenrlDInfo.setPrdExplnItmCd(pPrdExplnItmCd);
					}
					//결재상태가 신규인경우에는 요청에 넣지 않는다.
					if ("Y".equals(prdDesceGenrlDInfo.getQaCnfTgtYn()) && ! ("00".equals(prdmMain.getPrdAprvStCd())) ) {
						// QA요청 항목 정보를 INSERT
						prdExplnEntity.setPrdDescdReqDNew(prdDesceGenrlDInfo);
					} else {
						// 일본상품일 경우 DESCD_EXPLN_CNTNT이 .이면 INTRNT_EXPOS_YN를 N으로 셋팅하여 비노출로 한다 isKang 20130111
						if( "33".equals(StringUtils.NVL(prdmMain.getPrdGbnCd()))) {
							if(".".equals(prdDesceGenrlDInfo.getDescdExplnCntnt())){
								prdDesceGenrlDInfo.setIntrntExposYn("0"); // 1이 아닌값이면 N
							}else{
								prdDesceGenrlDInfo.setIntrntExposYn("1"); // 1이면 Y
							}
							prdDesceGenrlDInfo.setChanlCd("A");
						}

						prdExplnEntity.setPrdDescdGenrlDNew(prdDesceGenrlDInfo);

						prdDescdSyncInfoList.add(new PrdDescdSyncInfo(" " // attrPrdRepCd 상품코드 PRD_STOCK_D 테이블
						        , " " // descdExplnCntnt 기술서설명내용
						        , prdDesceGenrlDInfo.getDescdItmSeq() // descdItmSeq 기술서항목순번
						        , " " // genrlDescdId 일반기술서ID
						        , "I" // jobType JOB TYPE I:등록 D: 삭제 U: 수정
						        , prdDesceGenrlDInfo.getPrdCd() // prdCd 상품코드
						        ));
						prdDescdSyncInfoList.get(prdDescdSyncInfoList.size()-1).setGovPublsPrdGrpCd(prdDesceGenrlDInfo.getGovPublsPrdGrpCd());
						prdDescdSyncInfoList.get(prdDescdSyncInfoList.size()-1).setPrdExplnItmCd(prdDesceGenrlDInfo.getPrdExplnItmCd());

						if( StringUtils.isEmpty( prdDescdHtmlDInfo.getGovPublsPrdGrpCd())
								&& !StringUtils.isEmpty(prdDesceGenrlDInfo.getGovPublsPrdGrpCd())
						){
							prdDescdHtmlDInfo.setGovPublsPrdGrpCd(prdDesceGenrlDInfo.getGovPublsPrdGrpCd());
						}

						prdDescdHtmlDInfo.setPrdCd(prdmMain.getPrdCd()) ;
						prdDescdHtmlDInfo.setChanlCd("P");
						prdDescdHtmlDInfo.setRegGbnCd("A");
						prdDescdHtmlDInfo.setSessionObject(prdmMain);
						// 일반 기술서 등록 후  HTML 기술서 변환
					}
				}
				this.setPrdDescdHtmlDNew(prdDescdHtmlDInfo);
			}

			// HTML 기술서가 비어 있지 않을때
			if(prdDescdHtmlDInfoList.size() > 0 ) {
				for(PrdDescdHtmlDInfo prdDescdHtmlDInfo1 : prdDescdHtmlDInfoList) {
					prdDescdHtmlDInfo1.setPrdCd(prdmMain.getPrdCd());
					PrdHtmlDescd prdHtmlDescd = new PrdHtmlDescd();
					DevBeanUtils.wrappedObjToObj(prdHtmlDescd, prdDescdHtmlDInfo1);
					List<PrdHtmlDescd> inputList = new ArrayList<PrdHtmlDescd>();
					inputList.add(prdHtmlDescd);
					prdHtmlDescdEntity.addHtmlDescdList(inputList);
					// HTML 기술서 등록
					prdDesceGenrlDInfo1.setPrdCd(prdmMain.getPrdCd());
					prdDesceGenrlDInfo1.setPrdCnsdrStCd(prdDescdHtmlDInfo1.getPrdCnsdrStCd());
					prdDesceGenrlDInfo1.setSessionObject(prdDescdHtmlDInfo1);
					
					
					//-end
				}
			}
		}
		//[SR02140514059][2014.06.20][김지혜] : MC 단품 기술서 캡쳐 솔루션 적용
		//[GRIT-65674]:모바일 전용 이미지 저장 기능 삭제 요청
		//imgMngProcess.connectMobileDescdHtmlDInfo(prdDescdHtmlDInfo);
		
		return prdDescdSyncInfoList;
	}
	
	/**
	 * <pre>
	 *
	 * desc : [SR02140822039][2014.11.20][김지혜] : [분류가 변경되었을 경우]
	 *
	 * </pre>
	 *
	 * @author 김지혜
	 * @date 2014.11.20
	 * @param SupDtlInfo supDtlInfo, PrdmMain prdmMain, PrdprcHinsert prdprcHinsert
	 *      , PrdPrdDinsert prdPrdD, SafeCertPrd psafeCertPrd
	 *      , List<PrdSpecVal> prdSpecInfo, List<PrdUdaDtl> prdUdaDtl
	 * @return Map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Map setPrdClsChgSet(PrdmMain prdmMain, List<PrdChanlInfo> prdChanlList) {
		
		PrdQryCond pPrdQryCond = new PrdQryCond();
		pPrdQryCond.setPrdCd(prdmMain.getPrdCd());
		DSMultiData orgPrdChanlList = prdChanlEntity.getPrdChanlList(pPrdQryCond).getValues();
		prdChanlList = DevBeanUtils.wrappedListMapToBeans(orgPrdChanlList, PrdChanlInfo.class);
			
		//QA검사 및 표준출고일 조회 및 셋팅
		PrdClsByBaseValQryCond pPrdClsByBaseValQryCond = new PrdClsByBaseValQryCond();
		pPrdClsByBaseValQryCond.setSupCd(prdmMain.getSupCd().toString());
		pPrdClsByBaseValQryCond.setDtlClsCd(prdmMain.getPrdClsCd());
		pPrdClsByBaseValQryCond.setBrandCd(prdmMain.getBrandCd().toString());
		pPrdClsByBaseValQryCond.setUseYn("Y");
		pPrdClsByBaseValQryCond.setClsLvlNo("4");
		pPrdClsByBaseValQryCond.setGbnNo("%");
		pPrdClsByBaseValQryCond.setMdId(prdmMain.getOperMdId().toString());//[PD-2015-007] 협력사MDID별 표준출고일조회
		
		EntityDataSet<DSMultiData> pPrdClsBaseValListEDS = prdClsBaseEntity.getPrdClsBaseValList(pPrdClsByBaseValQryCond);

		if( pPrdClsBaseValListEDS.getValues() != null && pPrdClsBaseValListEDS.getValues().size() > 0 ) {
			//QA검사여부 셋팅
			String caQaInspYn = pPrdClsBaseValListEDS.getValues().get(0).getString("caQaInspYn");
			String dmQaInspYn = pPrdClsBaseValListEDS.getValues().get(0).getString("dmQaInspYn");
			String ecQaInspYn = pPrdClsBaseValListEDS.getValues().get(0).getString("ecQaInspYn");
			caQaInspYn = "1".equals(caQaInspYn) ? "Y" : "Y".equals(caQaInspYn) ? "Y" : "N";
			dmQaInspYn = "1".equals(dmQaInspYn) ? "Y" : "Y".equals(dmQaInspYn) ? "Y" : "N";
			ecQaInspYn = "1".equals(ecQaInspYn) ? "Y" : "Y".equals(ecQaInspYn) ? "Y" : "N";
			
			
			//표준출고일 셋팅
			BigDecimal ecDirdlvStdRelsDdcnt = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("ecDirdlvStdRelsDdcnt");
			BigDecimal ecInstlStdRelsDdcnt  = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("ecInstlStdRelsDdcnt");
			BigDecimal ecDlvsStdRelsDdcnt   = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("ecDlvsStdRelsDdcnt");
			BigDecimal ecGtpDdhdStdRelsDdcnt   = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("ecGtpDdhdStdRelsDdcnt");
			BigDecimal dmDirdlvStdRelsDdcnt = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("dmDirdlvStdRelsDdcnt");
			BigDecimal dmInstlStdRelsDdcnt  = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("dmInstlStdRelsDdcnt");
			BigDecimal dmDlvsStdRelsDdcnt   = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("dmDlvsStdRelsDdcnt");
			BigDecimal dmGtpDdhdStdRelsDdcnt   = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("dmGtpDdhdStdRelsDdcnt");
			BigDecimal caDirdlvStdRelsDdcnt = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("caDirdlvStdRelsDdcnt");
			BigDecimal caInstlStdRelsDdcnt  = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("caInstlStdRelsDdcnt");
			BigDecimal caDlvsStdRelsDdcnt   = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("caDlvsStdRelsDdcnt");
			BigDecimal caGtpDdhdStdRelsDdcnt   = pPrdClsBaseValListEDS.getValues().get(0).getBigDecimal("caGtpDdhdStdRelsDdcnt");
			
			for( int i = 0; i < prdChanlList.size() ; i++) {
				prdChanlList.get(i).setSessionObject(prdmMain);
				
				// QA검사여부 셋팅
				if ("P,B,S".indexOf(prdChanlList.get(i).getChanlCd()) > -1) {
					prdChanlList.get(i).setQaInspYn(ecQaInspYn);
				} else if ("D".equals(prdChanlList.get(i).getChanlCd())) {
					prdChanlList.get(i).setQaInspYn(dmQaInspYn);				
				} else {
					prdChanlList.get(i).setQaInspYn(caQaInspYn);
				}
				
				//표준출고일 셋팅
				if( "Y".equals(prdmMain.getRepPrdYn()) || "00,01,02".indexOf(prdmMain.getGftTypCd()) == -1 || !"N".equals(prdmMain.getFrmlesPrdTypCd())){
					//prdChanlList.get(i).setStdRelsDdcnt(new BigDecimal(0)); 
					prdChanlList.get(i).setStdRelsDdcnt(null); //[PD-2015-007] 협력사MDID별 표준출고일조회
				}else if( "Y".equals(prdmMain.getOrdMnfcYn()) ){
					if( prdmMain.getOrdMnfcTermDdcnt() != null) {
						prdChanlList.get(i).setStdRelsDdcnt(prdmMain.getOrdMnfcTermDdcnt());
					}
				}else{
					if("3100".equals(prdmMain.getDlvPickMthodCd())){
						if ("P,B,S".indexOf(prdChanlList.get(i).getChanlCd()) != -1) {
							prdChanlList.get(i).setStdRelsDdcnt(ecInstlStdRelsDdcnt);
						} else if ("D".equals(prdChanlList.get(i).getChanlCd())) {
							prdChanlList.get(i).setStdRelsDdcnt(dmInstlStdRelsDdcnt);				
						} else {
							prdChanlList.get(i).setStdRelsDdcnt(caInstlStdRelsDdcnt);
						}
						
					}else if("3000,3200,3300,3400".indexOf(prdmMain.getDlvPickMthodCd()) != -1){
						if ("P,B,S".indexOf(prdChanlList.get(i).getChanlCd()) > -1) {
							prdChanlList.get(i).setStdRelsDdcnt(ecDirdlvStdRelsDdcnt);
						} else if ("D".equals(prdChanlList.get(i).getChanlCd())) {
							prdChanlList.get(i).setStdRelsDdcnt(dmDirdlvStdRelsDdcnt);				
						} else {
							prdChanlList.get(i).setStdRelsDdcnt(caDirdlvStdRelsDdcnt);
						}
						
					}else if("2000,2100,2200,2300,2400,2500,2600,2700,2800".indexOf(prdmMain.getDlvPickMthodCd()) != -1){
						if ("P,B,S".indexOf(prdChanlList.get(i).getChanlCd()) > -1) {
							prdChanlList.get(i).setStdRelsDdcnt(ecGtpDdhdStdRelsDdcnt);
						} else if ("D".equals(prdChanlList.get(i).getChanlCd())) {
							prdChanlList.get(i).setStdRelsDdcnt(dmGtpDdhdStdRelsDdcnt);				
						} else {
							prdChanlList.get(i).setStdRelsDdcnt(caGtpDdhdStdRelsDdcnt);
						}
					}else{
						if ("P,B,S".indexOf(prdChanlList.get(i).getChanlCd()) != -1) {
							prdChanlList.get(i).setStdRelsDdcnt(ecDlvsStdRelsDdcnt);
						} else if ("D".equals(prdChanlList.get(i).getChanlCd())) {
							prdChanlList.get(i).setStdRelsDdcnt(dmDlvsStdRelsDdcnt);				
						} else {
							prdChanlList.get(i).setStdRelsDdcnt(caDlvsStdRelsDdcnt);
						}
					}
					
				}
			}
			
			//상품기본정보 환불유형값셋팅
			if( "20,30".indexOf(pPrdClsBaseValListEDS.getValues().get(0).getString("rfnTypCd")) != -1 && "3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))) {
				prdmMain.setRfnTypCd("10");
			} else {
				prdmMain.setRfnTypCd(pPrdClsBaseValListEDS.getValues().get(0).getString("rfnTypCd"));
			}
			//심의검사여부 set 
			if("1".equals(pPrdClsBaseValListEDS.getValues().get(0).getString("cnsdrTgtYn")) || "Y".equals(pPrdClsBaseValListEDS.getValues().get(0).getString("cnsdrTgtYn"))) {
				prdmMain.setPrdCnsdrStCd("A");
			} else {
				prdmMain.setPrdCnsdrStCd("N");
			}
			
			//분류별 구매수량제한 설정
			if("1".equals(pPrdClsBaseValListEDS.getValues().get(0).getString("prchLimitYn")) && "GE".equals(prdmMain.getRegChanlGrpCd()) ) {
				PrdUdaDtl prdUdaDtlL = new PrdUdaDtl();
				prdUdaDtlL.setPrdCd(prdmMain.getPrdCd());
				prdUdaDtlL.setUdaGbnCd("10");
				prdUdaDtlL.setUdaNo(new BigDecimal("15"));
				//기간별 구매제한 - 기본 제한 일수 누락 시 입력 추가
				prdUdaDtlL.setUdaVal1("30");
				prdUdaDtlL.setUdaVal(pPrdClsBaseValListEDS.getValues().get(0).getString("prchLimitQty"));
				prdUdaDtlL.setUseYn("1");
				prdUdaDtlL.setChk("Y");
				prdUdaDtlL.setValidEndDtm("29991231235959");
				prdUdaDtlL.setValidStrDtm(DateUtils.format(SysUtil.getCurrTime(), "yyyyMMddHHmmss"));
				prdUdaDtlL.setSessionUserId(prdmMain.getSessionUserId());
				
				DSData duplCnt = prdUdaEntity.getUdaCdDuplCnt(prdUdaDtlL);
				if( duplCnt != null && duplCnt.getInt("cnt") == 0 ){
					prdUdaEntity.setInsertPrdUda(prdUdaDtlL);
				}
			}
		}
		
		Map resultMap = new HashMap();
		resultMap.put("prdmMain", prdmMain);
		resultMap.put("prdChanlInfo", prdChanlList);
		
		return resultMap;
	}
	
	//[SR02160115126][2016.01.19][백동현]API 주문가능수량과 상품 판매상태 불일치에 대한 규칙 변경의 건
	@Override
	public void modifyPrdStdRelsDdcnt(PrdmMain prdmMain) throws DevEntException {
		prdChanlEntity.updatePrdStdRelsDdcnt(prdmMain);
		
		List<PrdChanlInfo> modifyPrdChanlInfoPrd = prdChanlEntity.getPrdChanlD(prdmMain);
		Date date = SysUtil.getCurrTime();
		String sysdtm = DateUtils.format(date,"yyyyMMddHHmmss");

		if (modifyPrdChanlInfoPrd.size() > 0) {
			for (int i = 0; i < modifyPrdChanlInfoPrd.size(); i++) {
				List<PrdChanlSyncInfo> prdChanlSyncInfoList = new ArrayList<PrdChanlSyncInfo>();
				// 상품채널정보 eai 인터페이스
				PrdChanlSyncInfo pPrdChanlSyncInfoID = null;

		    	pPrdChanlSyncInfoID = new PrdChanlSyncInfo();
		    	pPrdChanlSyncInfoID.setJobType("U");
		    	pPrdChanlSyncInfoID.setPrdCd(modifyPrdChanlInfoPrd.get(i).getPrdCd());
		    	pPrdChanlSyncInfoID.setChanlCd(modifyPrdChanlInfoPrd.get(i).getChanlCd());
		    	pPrdChanlSyncInfoID.setChanlMdId(modifyPrdChanlInfoPrd.get(i).getChanlMdId());
		    	pPrdChanlSyncInfoID.setSalePsblYn(modifyPrdChanlInfoPrd.get(i).getSalePsblYn());
		    	pPrdChanlSyncInfoID.setQaInspYn(modifyPrdChanlInfoPrd.get(i).getQaInspYn());
		    	pPrdChanlSyncInfoID.setStdRelsDdcnt(modifyPrdChanlInfoPrd.get(i).getStdRelsDdcnt());
		    	pPrdChanlSyncInfoID.setStdRelsDdcnt(modifyPrdChanlInfoPrd.get(i).getStdRelsDdcnt());
		    	pPrdChanlSyncInfoID.setRepMdUserId(modifyPrdChanlInfoPrd.get(i).getRepMdUserId());
		    	pPrdChanlSyncInfoID.setRegDtm(sysdtm);
		    	pPrdChanlSyncInfoID.setRegrId(prdmMain.getSessionUserId());
		    	pPrdChanlSyncInfoID.setModDtm(sysdtm);
		    	pPrdChanlSyncInfoID.setModrId(prdmMain.getSessionUserId());

				prdChanlSyncInfoList.add(pPrdChanlSyncInfoID);

				if (prdChanlSyncInfoList.size() > 0) {
						wsPrdEaiPrdChanlSyncProcess.prdChanlSyncProcess(prdChanlSyncInfoList);
				}
			}
		}
	}
	
	//[SR02160405072][2016.04.05][백동현]:API - 상품정보변경 : 노출매장 수정 기능 추가
	@Override
	@SuppressWarnings("rawtypes")
	@Transactional(value = "smtcTransactionManager")
	public Map<String, EntityDataSet> modifyPrdSectListInfo(PrdmMain pPrdmMain, List<EcdSectPrdlstInfo> pSupPrdCtgrShopList) throws DevEntException{
				
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		DSData resultData = new DSData();
		PrdmMain prdmMain = pPrdmMain;
		BigDecimal prdCd = prdmMain.getPrdCd();
		List<EcdSectPrdlstInfo> supPrdCtgrShopList = pSupPrdCtgrShopList;

		PrdQryCond pPrdQryCond = new PrdQryCond();
		pPrdQryCond.setPrdCd(prdCd);

		EntityDataSet<DSMultiData> prdCtgrShopList = ecShopEntity.getPrdCtgrShopList(pPrdQryCond);// 상품카테고리매장목록조회/**/
		List<EcdSectPrdlstInfo> targetSectPrdlstInfo =  new ArrayList<EcdSectPrdlstInfo>();
		List<EcdSectPrdlstInfo> insecdSectPrdlstInfo = new ArrayList<EcdSectPrdlstInfo>();	// 입력정보
		List<EcdSectPrdlstInfo> delecdSectPrdlstInfo = new ArrayList<EcdSectPrdlstInfo>();	// 삭제정보
		
		//중복제거
		for(int i = 0; i < supPrdCtgrShopList.size() ; i++){
			String targetYn = "Y";
			for(EcdSectPrdlstInfo target : targetSectPrdlstInfo){
				if(target.getSectid().equals(supPrdCtgrShopList.get(i).getSectid())){
					targetYn = "N";
					break;
				}
			}
			if(targetYn.equals("Y")){
				targetSectPrdlstInfo.add(supPrdCtgrShopList.get(i));
			}
		}
		
		//삭제 대상 확인
		if( prdCtgrShopList.getValues() != null && prdCtgrShopList.getValues().size() > 0 ) {
			
			for(int i = 0; i < prdCtgrShopList.getValues().size(); i++){
				
				// GRIT-104293 [상품API] [UX팀]TV 카테고리 등록, 수정 불가 처리 요청 2024.01.05
				// SHOP_ATTR_CD  매장속성코드(S:일반매장, V:TV매장, P:파트너스매장, D:백화점매장, B:BP전용매장, T:테마매장)
				// 등록된 매장이 TV매장이라면 삭제하지 않는다.
				if( "V".equals(prdCtgrShopList.getValues().get(i).getString("shopAttrCd")) ) {
					continue;
				}
				String sectid = prdCtgrShopList.getValues().get(i).getString("shopCd");
				String deleteYn = "Y";
				
				for(int j = 0; j < targetSectPrdlstInfo.size(); j++){
					if(targetSectPrdlstInfo.get(j).getSectid().equals(sectid)){
						deleteYn = "N";
						break;
					}
				}
				
				if(deleteYn.equals("Y")){
					EcdSectPrdlstInfo ecdSectPrdlstInfo = new EcdSectPrdlstInfo();
					ecdSectPrdlstInfo.setSectid(sectid);
					ecdSectPrdlstInfo.setPrdid(prdCd);
					ecdSectPrdlstInfo.setSessionObject(prdmMain);
					delecdSectPrdlstInfo.add(ecdSectPrdlstInfo);
				}
			}
		}
		
		//신규 대상 확인
		if(targetSectPrdlstInfo != null && targetSectPrdlstInfo.size() > 0){
			
			for(int i = 0; i < targetSectPrdlstInfo.size(); i++){
				
				String sectid = targetSectPrdlstInfo.get(i).getSectid();
				//매장순번(기본(대표)매장) [SR02200323382] 2020.04.13 이용문 : 카테고리 개편] 기간계/API 개발 요청
				String sectSeq = "";
				try{
					if( BigDecimalUtil.NVL(targetSectPrdlstInfo.get(i).getSectSeq()).compareTo(new BigDecimal("0")) > 0 ){
						sectSeq = BigDecimalUtil.NVL(targetSectPrdlstInfo.get(i).getSectSeq()).toString();
					}
				}catch(Exception e){}
				
				String insertYn = "Y";
				String updateSeq = "";
				
				for(int j = 0; j <  prdCtgrShopList.getValues().size(); j++){
					if( prdCtgrShopList.getValues().get(j).getString("shopCd").equals(sectid)){
						
						//대표매장 정보 변경시 수정으로 처리 [SR02200323382]
						if( !sectSeq.equals(prdCtgrShopList.getValues().get(j).getString("shopSeq")) ){
							updateSeq = prdCtgrShopList.getValues().get(j).getString("seq");	
						}
						insertYn = "N";
						break;
					}
				}
				
				if(insertYn.equals("Y") || !"".equals(updateSeq) ){
					EcdSectPrdlstInfo ecdSectPrdlstInfo = new EcdSectPrdlstInfo();
					ecdSectPrdlstInfo.setSectid(sectid);
					ecdSectPrdlstInfo.setPrdid(prdCd);
					ecdSectPrdlstInfo.setDbsts("A");
					ecdSectPrdlstInfo.setSectSeq((!"".equals(sectSeq) ? new BigDecimal(sectSeq) : null)); //매장순번(기본(대표)매장)
					if( !"".equals(updateSeq) ){
						ecdSectPrdlstInfo.setGubun("U");
						ecdSectPrdlstInfo.setseq(updateSeq); // 등록된 키값
					}
					ecdSectPrdlstInfo.setSessionObject(prdmMain);
					insecdSectPrdlstInfo.add(ecdSectPrdlstInfo);
				}
			} //end for i
		}
		
		for (int i = 0; i < insecdSectPrdlstInfo.size(); i++) {
			if ("U".equals(insecdSectPrdlstInfo.get(i).getGubun())) {
				ecShopEntity.updateSectPrdLst(insecdSectPrdlstInfo.get(i));
			}else{
				ecShopEntity.insertsectPrdlst(insecdSectPrdlstInfo.get(i));
			}
		}
		for (int i = 0; i < delecdSectPrdlstInfo.size(); i++) {
			ecShopEntity.deletesectPrdlst(delecdSectPrdlstInfo.get(i));
		}
		
		if(insecdSectPrdlstInfo != null && insecdSectPrdlstInfo.size() > 0){
			EcdSectPrdlstInfo ecdSectPrdlistInfo = new EcdSectPrdlstInfo();
			ecdSectPrdlistInfo.setPrdid(prdCd);
			ecdSectPrdlistInfo.setSessionObject(prdmMain);
			prdEntity.modifyExposStCd(ecdSectPrdlistInfo);
		}
		
		resultData.put("retCd", "S");
		resultData.put("retMsg","");
		logger.debug("resultData => " + resultData);
		returnMap.put("outResult", EDSFactory.create(EsbCmm.class, resultData));

		return returnMap;
	
	}
	
	/**
	 * <pre>
	 *
	 * desc : 전시가능매장 셋팅.
	 * [SR02200323382] 2020.04.14 이용문 : [카테고리 개편] 기간계/API 개발 요청
	 * 
	 * </pre>
	 * @author ymlee
	 * @date 2020. 4. 14.
	 * @param pSupPrdCtgrShopSendList
	 * @param supCd
	 * @return Map<String, Object> "outResultData" 안에 List<EcdSectPrdlstInfo> 데이터를 넘김.
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> setPossibleSectInfo(List<EcdSectPrdlstInfo> pSupPrdCtgrShopSendList, String supCd) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		try{
			EntityDataSet<DSMultiData> tShopList = ecShopEntity.getSectInfoListByAPI(pSupPrdCtgrShopSendList, supCd);
			if( tShopList.getValues() == null || tShopList.getValues().size() == 0 ) {
				retMap.put("retCd", "E");
				retMap.put("retMsg","전시매장 정보를 확인해 주세요. (전시가능 매장 없음)");
				return retMap;
			}
			
			Map<String, Object> sectMap = new HashMap<String, Object>();
			int errCnt[] = {0, 0, 0};
			for(EcdSectPrdlstInfo ecdSectPrdlstInfo : pSupPrdCtgrShopSendList){
				
				for(int j = 0; j < tShopList.getValues().size(); j++){
					
					if( ecdSectPrdlstInfo.getSectid().equals(tShopList.getValues().get(j).getString("sectid")) ){
						
						// 특정협력사만 가능한 매장인지 체크
						if( "Y".equals(StringUtils.NVL(tShopList.getValues().get(j).getString("privateSectYn")))
								&& "N".equals(StringUtils.NVL(tShopList.getValues().get(j).getString("privateSectPossYn"))) ){ //내가 등록할수 없는 매장이라면.
							retMap.put("retCd", "E");
							retMap.put("retMsg","전시매장 정보를 확인해 주세요. (등록 할수 없는 매장입니다["+supCd+","+tShopList.getValues().get(j).getString("sectid")+"])");
							return retMap;
						}
						
						String shopAttrCd = StringUtils.NVL(tShopList.getValues().get(j).getString("shopAttrCd"), "S");
						if( "Y".equals(tShopList.getValues().get(j).getString("prdDispYn")) //상품진열가능매장이면서
								&& "N".equals(tShopList.getValues().get(j).getString("prdAutoClctYn")) //자동수집매장이 아니면서
								&& "Y".equals(tShopList.getValues().get(j).getString("upperLiveYn")) //상위매장이 살아있는 경우
								){
							
							//매장정보를 보내줬는데 일치하지 않는다면 패스
							if( !StringUtils.isEmpty(ecdSectPrdlstInfo.getSectGbn()) && !shopAttrCd.equals(ecdSectPrdlstInfo.getSectGbn()) ){
//								errCnt[1]++;
								retMap.put("retCd", "E");
								retMap.put("retMsg","전시매장 정보를 확인해 주세요. (매장속성코드가 일치하지 않습니다.)["+ecdSectPrdlstInfo.getSectid()+","+ecdSectPrdlstInfo.getSectGbn()+","+shopAttrCd+"])");
								return retMap;
							}
							
							//매장속성별로 한개만 등록.
							if( sectMap.containsKey(shopAttrCd) 
									&& "N".equals(StringUtils.NVL(ecdSectPrdlstInfo.getSectStdYn(), "N")) ){   //두번째에 기본매장이 있을수 있기 때문에..
								errCnt[2]++;
								break;
							}

							ecdSectPrdlstInfo.setSectGbn(shopAttrCd);
							ecdSectPrdlstInfo.setSectSeq(("Y".equals(StringUtils.NVL(ecdSectPrdlstInfo.getSectStdYn(), "N"))? new BigDecimal("1") : null ) );
							sectMap.put(shopAttrCd, ecdSectPrdlstInfo);
						}else{
							errCnt[0]++;
						}
						
						break;
					}
				}//end for j
			} // end for pSupPrdCtgrShopSendList

			int sectListCnt = sectMap.keySet().size();
			if( sectListCnt == 0 ){
				retMap.put("retCd", "E");
				retMap.put("retMsg","전시매장 정보를 확인해 주세요. (전시가능 진열매장 정보 없음["+pSupPrdCtgrShopSendList.size()+","+errCnt[0]+","+errCnt[1]+","+errCnt[2]+"])");
				return retMap;
			}
			
			Iterator iter = sectMap.keySet().iterator();
			int stdCntChk = 0;
			List<EcdSectPrdlstInfo> newSectList = new ArrayList<EcdSectPrdlstInfo>();
			while (iter.hasNext()) {
				String key = (String)iter.next();
				EcdSectPrdlstInfo ecdSectPrdlstInfo = (EcdSectPrdlstInfo)sectMap.get(key);
				if( sectListCnt == 1 ){ //전시매장 정보가 1개라면 대표로 찍어줌.
					ecdSectPrdlstInfo.setSectSeq(new BigDecimal("1"));
					stdCntChk++;
				}else{
					//기본(대표)매장 개수 체크
					if( "Y".equals(StringUtils.NVL(ecdSectPrdlstInfo.getSectStdYn(), "N")) ){
						stdCntChk++;
					}
				}
				newSectList.add(ecdSectPrdlstInfo);
			}
			//대표매장이 여러개 라면.
			if( stdCntChk > 1 ){
				retMap.put("retCd", "E");
				retMap.put("retMsg","전시매장 정보를 확인해 주세요. (기본전시여부[prdSectListSectStdYn]를 확인해주세요:"+stdCntChk+")");
				return retMap;
			}
			if( stdCntChk == 0 ){
				//2개월 유예기간동안 예외처리 주석처리
				logger.debug("기본매장여부 미입력(검증 후)");
//				retMap.put("retCd", "E");
//				retMap.put("retMsg","전시매장 정보를 확인해 주세요. (기본매장여부 미입력(검증 후))");
//				return retMap;
			}
			
			retMap.put("retCd", "S");
			retMap.put("retMsg","");
			retMap.put("outResultData", newSectList);
			
		}catch(Exception e){
			retMap.put("retCd", "E");
			retMap.put("retMsg", "매장정보 가공시 에러가 발생하였습니다.");
			//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
			SMTCLogger.errorPrd("매장정보 가공시 에러가 발생하였습니다.", e);
		}
		return retMap;
	}
	
	
	/**
	 * <pre>
	 *
	 * desc : [PD_2016_009] 도서산간 배송비추가 ljb 2016.07.13 
	 *       - 상품 배송불가지역 등록 (배송불가 입력 구분값 01:수동, 02:자동)
	 * </pre>
	 *
	 * @author ljb
	 * @date 2011-02-28 10:49:08
	 * @param PrdPrdDinsert prdPrdD, List<PrdDlvNoadmtRegon> pPrdDlvNoadmtRegonList
	 * @return Map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Map setPrdDlvNoadmtRegonList(PrdPrdDinsert prdPrdD) {
		
		Map resultMap = new HashMap();

		//상품 배송불가지역 자동 입력 삭제 , 저장 
		prdEntity.setPrdDlvInfo(prdPrdD);
		
		return resultMap;
	}
	
	 /**
		* <pre>
		*
		* desc : 신규상품 이미지 복사등록 URL 다운로드 후 FTP 업로드로 변경
		* 대표이미지 -> imagecreate_mq 폴더 FTP 업로드 후 리사이징 mq 호출
		* 배너이미지 -> image 폴더 FTP 업로드
		*
		* </pre>
		* @author DHBAEK
		* @date 2024. 1. 10. 오전 10:02:00
		* @param oldPrdCd
		* @param newPrdCd
		* @param images
		* @param mqResize
		* @return
		*/
	private Map<String, String> imgFileCopy(BigDecimal oldPrdCd, BigDecimal newPrdCd, DSMultiData images, boolean mqResize ) {
		List<FtpTransferFile> files = new ArrayList<FtpTransferFile>();
		//logger.debug("setPrdBase==>oldUrl" + oldUrl);
		//logger.debug("setPrdBase==>newUrl" + newUrl);
		
		Map<String, String> result = new HashMap<String, String>();
		HashMap<String, Object> fileChkRslt = null;
		List<?> success =  null;
		List<?> fail =  null;
		
		String retCd = "S";
		String retMsg = "";
		BufferedImage img = null;
		
		try {
			String oldUrl = "";
			String newUrl ="";
			
			if(mqResize) {
				oldUrl = Constant.getString("prd.asset.api.img.url");
				//[GRIT-112159]:[세일즈원 상품] 상품대표이미지 aws 전환을 위한 개발
				//newUrl =  Constant.getString("mq.upload.image.server.path"); 
				newUrl =  Constant.getString("mq.aws.upload.image.server.path"); 
				
				for (int i = 0; i < images.size(); i++) {
					HttpURLConnection urlConnection = null;
					int TIMEOUT_VALUE = 3000;

					String oldfileNm = images.get(i).getString("oldFileNm");
					String seq = oldfileNm.substring(oldfileNm.indexOf("_L")+2, oldfileNm.indexOf("."));
					String urlStr =  oldUrl + "550/" + seq + "/" + oldPrdCd + ".jpg";
					String newPrdNm = newPrdCd.toString() + "_L"+ seq + ".jpg";
					
					URL url = new URL(urlStr);
					urlConnection = (HttpURLConnection)url.openConnection();
					urlConnection.setConnectTimeout(TIMEOUT_VALUE);
					urlConnection.setReadTimeout(TIMEOUT_VALUE);
					img = ImageIO.read(url);
					File file = new File(Constant.getString("upload.image.temp") + oldfileNm);
					ImageIO.write(img, "jpg", file);
					img.flush();
					
					FtpTransferFile ftpTransferFile = new FtpTransferFile();
					ftpTransferFile.setFileUplodPathNm(Constant.getString("upload.image.temp"));
					ftpTransferFile.setFileNm(oldfileNm);
					ftpTransferFile.setFileExtnsNm(oldfileNm.substring(oldfileNm.length() - 3, oldfileNm.length()));
					ftpTransferFile.setRemotePath(newUrl);
					ftpTransferFile.setRemoteFileNm(newPrdNm );
					files.add(ftpTransferFile);
				}

				imgFileUploadProcess.transferImgFilesToAwsServer(files);
				fileChkRslt = (HashMap<String, Object>) imgFileUploadProcess.fileCheckAws(files);
				
			}else {
				String olrPrdCdStr = oldPrdCd.toString();
				String newPrdCdStr = newPrdCd.toString();
				oldUrl = Constant.getString("prd.img.banner.server.url") +"image/" + olrPrdCdStr.substring(0, 2) + "/" + olrPrdCdStr.substring(2, 4) + "/";
				newUrl = "/multimedia/image/" + newPrdCdStr.substring(0, 2) + "/" + newPrdCdStr.substring(2, 4) + "/";
				if(olrPrdCdStr.length() >= 8) {
					oldUrl = oldUrl + olrPrdCdStr.substring(4, 6) + "/";
				}
				if (newPrdCdStr.length() >= 8) {
					newUrl = newUrl + newPrdCdStr.substring(4, 6) + "/";
				}
				
				for (int i = 0; i < images.size(); i++) {
					HttpURLConnection urlConnection = null;
					int TIMEOUT_VALUE = 3000;

					String oldfileNm = images.get(i).getString("oldFileNm");
					String urlStr =  oldUrl + oldfileNm;
					String newPrdNm =  images.get(i).getString("newFileNm");
					
					URL url = new URL(urlStr);
					urlConnection = (HttpURLConnection)url.openConnection();
					urlConnection.setConnectTimeout(TIMEOUT_VALUE);
					urlConnection.setReadTimeout(TIMEOUT_VALUE);
					img = ImageIO.read(url);
					File file = new File(Constant.getString("upload.image.temp") + oldfileNm);
					ImageIO.write(img, "jpg", file);
					img.flush();
					
					FtpTransferFile ftpTransferFile = new FtpTransferFile();
					ftpTransferFile.setFileUplodPathNm(Constant.getString("upload.image.temp"));
					ftpTransferFile.setFileNm(oldfileNm);
					ftpTransferFile.setFileExtnsNm(oldfileNm.substring(oldfileNm.length() - 3, oldfileNm.length()));
					ftpTransferFile.setRemotePath(newUrl);
					ftpTransferFile.setRemoteFileNm(newPrdNm );
					files.add(ftpTransferFile);
				}
				
				imgFileUploadProcess.transferImgFilesToServer(files);
				fileChkRslt = (HashMap<String, Object>) imgFileUploadProcess.fileCheck(files);
			}
			
			success = (List<?>) fileChkRslt.get("success");
			fail = (List<?>) fileChkRslt.get("fail");
			String filePath="";
			String fileName="";
			//이미지 실시간 리사이징 모듈 호출
			if(mqResize){
				if(success!=null && success.size() > 0){
//					boolean isSuccess = true;
					IrisClientImage client = new RpcClientImage("imageresize-mq");
					Map<String, Object> message = new HashMap<String, Object>();
					Map<String, Object> mqResult = null;
					for (Object file : success) {
						if(file instanceof ReUploadImageFileInfo){
							ReUploadImageFileInfo info = (ReUploadImageFileInfo)file;
							filePath = info.getUplaodRemoteFilePath() + info.getUploadRemoteFileName();
							fileName = info.getUploadRemoteFileName();
						}else if(file instanceof FtpTransferFile){
							FtpTransferFile info = (FtpTransferFile)file;
							filePath = info.getRemotePath() + info.getRemoteFileNm();
							fileName = info.getRemoteFileNm();
						}
						message.put("filePath",filePath);
						mqResult = client.send(message);
						//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
						SMTCLogger.infoPrd(" mqRequest : " + message.toString());
						SMTCLogger.infoPrd(" mqResult : " + mqResult);
						if(mqResult==null || mqResult.get("msg")== null || !"OK".equals(mqResult.get("msg").toString())){
							retMsg += "\n(mq)"+ fileName;
							retCd = "E";
						}
					}
				}
			}
			
			// 처리안된 파일이 있을경우 메시지 처리 
			for (Object file : fail) {
				if(file instanceof ReUploadImageFileInfo){
					ReUploadImageFileInfo info = (ReUploadImageFileInfo)file;
					fileName = info.getUploadRemoteFileName();
				}else if(file instanceof FtpTransferFile){
					FtpTransferFile info = (FtpTransferFile)file;
					fileName = info.getFileNm();
				}
				retMsg += "\n"+ fileName;
				retCd = "E";
			}
			//한개라도 처리 안된 파일이 있을경우 메시지 처리 
			if("E".equals(retCd)){
				retMsg =  Message.getMessage("util.msg.006")+retMsg + "\n이미지를 다시 등록해 주세요.";
			}
		} catch (Exception e) {
			if(img != null) {img.flush();};
			
			retCd = "E";
			retMsg =  Message.getMessage("util.msg.006") + "\n이미지를 다시 등록해 주세요.";
			//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
			SMTCLogger.errorPrd(Message.getMessage("util.msg.006"),e);
		}catch (ExceptionInInitializerError e) {
			retCd = "E";
			//mqClinetImage 생성중 오류가 발생하였습니다.
			retMsg =  Message.getMessage("prd.msg.548");
			//[HANGBOT-3880_GSITMBO-2890]:WAS Log 정리
			SMTCLogger.infoPrd(Message.getMessage("prd.msg.548") + e);
		} 
		
		result.put("retCd", retCd);
		result.put("retMsg", retMsg);
		return result;
	}

	/**
	 * 
	 * <pre>
	 * 지정일배송 날짜 지정 체크.
	 * [SR02180727185][2018.09.17][이용문]: 지정일(설치포함) 프로세스 개선을 위한 상품등록 화면 수정
	 * </pre>
	 *
	 * @author ymlee
	 * @date 2018. 9. 18.
	 * @param pattrPrdM
	 * @param prdCd
	 * @return
	 */
	private DSData isValidateApntAttrDate(List<PrdAttrPrdMinsert> pattrPrdM, String prdCd){
		DSData returnDsData = new DSData();
		
		for (int i = 0; i < pattrPrdM.size(); i++) {
			PrdAttrPrdMinsert attrPrd = pattrPrdM.get(i);
			String tmpAttrVal1 = StringUtils.NVL(attrPrd.getAttrVal1());
			if( !DateUtils.isValidDate(tmpAttrVal1, "yyyy-MM-dd") ){
				returnDsData.put("prdCd", prdCd);
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", "지정일배송이 날짜지정인데 올바르지 않는 날짜 속성입니다. ["+tmpAttrVal1+"]");
			}
			// 공휴일 체크.
			if( !isValidateApntAttrByHoldyDate(attrPrd.getAttrVal1()) ){
				returnDsData.put("prdCd", prdCd);
				returnDsData.put("retCd", "-1");
				returnDsData.put("retMsg", "지정일배송이 날짜지정이라면 공휴일 또는 일,월요일을 선택 할 수 없습니다.["+tmpAttrVal1+"]");
			}
		}
		
		returnDsData.put("prdCd", prdCd);
		returnDsData.put("retCd", "1");
		returnDsData.put("retMsg", "성공");
		
		return returnDsData;
	}
	
	/**
	 * 
	 * <pre>
	 * 지정일배송 유형이 날짜지정일때 공휴일 체크
	 * [SR02180727185][2018.09.17][이용문]: 지정일(설치포함) 프로세스 개선을 위한 상품등록 화면 수정
	 * </pre>
	 *
	 * @author ymlee
	 * @date 2018. 9. 18.
	 * @param attrCheckVal
	 * @return true (공휴일아님), false (공휴일 or 유효하지 않은값)
	 */
	private boolean isValidateApntAttrByHoldyDate(String attrCheckVal){
		if( StringUtils.isEmpty(attrCheckVal) || !DateUtils.isValidDate(attrCheckVal) ){
			return false;
		}
		
		CalndQryCond calndDtQryCond = new CalndQryCond();
		calndDtQryCond.setStdDt(attrCheckVal.replaceAll("-", "").replaceAll("[.]", ""));
		EntityDataSet<DSData> tHoldyData = calndCdEntity.getDstrbEchoHoldyYn(calndDtQryCond);
		if (tHoldyData != null && tHoldyData.getValues() != null) {
			if( "Y".equals(tHoldyData.getValues().getString("holidayFlag")) ){
				return false;
			}else if( "Mon".equals(tHoldyData.getValues().getString("wekdyNm")) ){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * <pre>
	 * 멀티 상품코드 상품임포트조회정보 테이블 저장 후 조회순번정보 리턴
	 * [SR02190619568][2019.07.11][박민수]:상품코드 임포트 임시저장 후, 멀티 상품코드 조건으로 대상조회
	 * </pre>
	 *
	 * @author aegis
	 * @date 2019. 07. 11.
	 * @param addImpQryList
	 * @return ImpQry
	 */
	@Override
	@Transactional(value = "smtcTransactionManager")
	public ImpQry savePrdCdImpQryD(List<ImpQry> addImpQryList) throws DevEntException {

		ImpQry impQrySeq = ecdPrdImpQryDEntity.getPrdCdImpQrySeq();
		
		for (ImpQry addImpQry : addImpQryList) {
			addImpQry.setQrySeq(impQrySeq.getQrySeq());
			impQrySeq.setSessionUserId(addImpQry.getSessionUserId());

			if( addImpQry.getPrdCd() != null ){
				ecdPrdImpQryDEntity.savePrdCdImpQryD(addImpQry);
			}
		}
		//logger.debug("\n impQrySeq = " + impQrySeq);
		return impQrySeq;
	}
	
	/**
	 * [새벽배송2차] 새벽배송정보 세팅
	 *
	 * @author aegis
	 * @date 2019. 12. 18.
	 * @param prdmMain
	 * @param prdPrdD
	 * @return PrdPrdDinsert
	 */
	public PrdPrdDinsert setDawnDlvInfo(PrdmMain prdmMain, PrdPrdDinsert prdPrdD){
		
		// 새벽배송비 유료여부
		if ( "Y".equals(StringUtils.NVL(prdPrdD.getDawnChrDlvYn())) ){
			
			// 새벽배송비 코드 없는경우
			if ( StringUtils.isEmpty(prdPrdD.getDawnChrDlvcCd()) ){
			
				// 배송비코드 조회
				SupDlvcPrd supDlvcPrd = new SupDlvcPrd();
				supDlvcPrd.setDlvcAmt(new BigDecimal(prdPrdD.getDawnChrDlvcAmt()));
				supDlvcPrd.setSupCd(new BigDecimal("989081"));
				supDlvcPrd.setDlvcLimitAmt(prdPrdD.getDawnDlvcLimitAmt());
				
				// 직송이 아닐경우 당사배송비를 조회 하기 위해 배송비구분코드에 'GS', 협력사코드에 '989081' 를 세팅한다.
				if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){
					supDlvcPrd.setDlvcGbnCd("GS") ; 				// 배송비구분코드 [ 당사 배송비 : 'GS'(협력사는 당사협력사코드 : 989081) ]
					supDlvcPrd.setSupCd(new BigDecimal("989081")) ; // 당사협력사코드
				}
				
				DSData dlvcCd = supDlvcEntity.getCntSupDlvcCd(supDlvcPrd);
				
				//배송비코드 없으면 생성
				if (dlvcCd.size()  <= 0 ) {
					List<SupDlvcPrd> supDlvcPrdRegList = new ArrayList<SupDlvcPrd>();
					List<SupDlvcPrd> supDlvcPrdModList = new ArrayList<SupDlvcPrd>();
	
					supDlvcPrd.setSessionObject(prdmMain);
					supDlvcPrd.setMdId("60027");
					supDlvcPrd.setStdAmtYn(prdPrdD.getDawnDlvcLimitYn());
					supDlvcPrdRegList.add(supDlvcPrd);
					PrdEaiPrdReTnsRslt result = supDlvcMngProcess.saveSupDlvcPrdList2(supDlvcPrdRegList, supDlvcPrdModList);
					if ("S".equals(result.getResponseResult())) {
						prdPrdD.setDawnChrDlvcCd(result.getResponseMessage());
					}
	
				} else{
					prdPrdD.setDawnChrDlvcCd(dlvcCd.getBigDecimal("dlvcCd").toString());
				}
			}
		}else if ( "N".equals(StringUtils.NVL(prdPrdD.getDawnChrDlvYn())) ){
			
			prdPrdD.setDawnChrDlvcCd("");
			//prdPrdD.setDawnChrDlvcAmt("");
		}
		
		// 반품/교환비 유료여부
		if ( "Y".equals(StringUtils.NVL(prdPrdD.getDawnExchRtpChrYn())) ){
			
			// 반품/교환배송비코드 없는경우
			if ( StringUtils.isEmpty(prdPrdD.getDawnRtpDlvcCd()) ){
				
				// 배송비코드 조회
				SupDlvcPrd supDlvcPrd = new SupDlvcPrd();
				supDlvcPrd.setDlvcAmt(new BigDecimal("100"));	// 100원
				supDlvcPrd.setSupCd(new BigDecimal("989081"));	// 당사
				supDlvcPrd.setDlvcLimitAmt(null);
				
				// 직송이 아닐경우 당사배송비를 조회 하기 위해 배송비구분코드에 'GS', 협력사코드에 '989081' 를 세팅한다.
				if (!"3".equals(StringUtils.NVL(prdmMain.getDlvPickMthodCd()).substring(0, 1))){
					supDlvcPrd.setDlvcGbnCd("GS") ; 				// 배송비구분코드 [ 당사 배송비 : 'GS'(협력사는 당사협력사코드 : 989081) ]
					supDlvcPrd.setSupCd(new BigDecimal("989081")) ; // 당사협력사코드
				}
				
				DSData dlvcCd = supDlvcEntity.getCntSupDlvcCd(supDlvcPrd);
				
				prdPrdD.setDawnRtpDlvcCd(dlvcCd.getBigDecimal("dlvcCd").toString());
			}
			
		}else {
			prdPrdD.setDawnRtpDlvcCd("");
			prdPrdD.setDawnRtpOnewyRndtrpCd("");
			prdPrdD.setDawnExchOnewyRndtrpCd("");
		}
		
		return prdPrdD;
	}	
	
	// [JANUS1] 프레쉬몰 채널/매장을 추가
	// [GRIT-89267] 2023.10.25 이태호  [프레시몰내] 와인25+& GS SHOP 위수탁상품 연동종료 요청의 건 (세일즈원-상품)
	private void checkFreshMallUda(PrdmMain prdmMain, List<PrdChanlInfo> prdChanlDinsert, List<EcdSectPrdlstInfo> ecdSectPrdlstInfo) {

		// 딜상품의 경우 프레쉬 채널/전시매장 처리를 할 필요가 없음
		if ( "88".equals(prdmMain.getPrdGbnCd()) ) return; 

		PrdChanlInfo freshChanl = null;
		
		boolean bHasCatv = false;
		
		// 프레쉬몰 채널 F 등록을 위해 B 또는 P 채널을 복제
		for ( PrdChanlInfo prdChanl : prdChanlDinsert ) {
			
			if (    "P".equals(prdChanl.getChanlCd()) 
				 || "B".equals(prdChanl.getChanlCd()) ) 
			{
				if ( freshChanl == null ) {
					
					freshChanl = prdChanl.clone();

					freshChanl.setChanlCd("F"); // 프레쉬몰 채널정보 복제
				}
			
			} else if (    "C".equals(prdChanl.getChanlCd()) 
					    || "H".equals(prdChanl.getChanlCd()) ) { // 방송상품인 경우
				
				bHasCatv = true;
			}
		}
		
		if ( new BigDecimal("1036018").compareTo(prdmMain.getSupCd()) == 0 && !bHasCatv) { // 프레쉬몰 협력사

			//1036018 협력사의 경우 지우고 
			prdChanlDinsert.clear();
			//프레쉬 직매입 상뭎은 프레쉬몰 채널을 추가
			prdChanlDinsert.add(freshChanl);
			
			// [프레쉬몰 전용] 제휴여부가 "N"인 경우 
			if ( "N".equals(prdmMain.getAfflYn()) ) { 
				// 제휴여부가 N인상품은 하드코딩 된 비노출된 특정매장으로만 GS매장을 입력한다.(1611694)
				if ( ecdSectPrdlstInfo.size() > 0 ) {
					ecdSectPrdlstInfo.get(0).setSectid("1611694");
				}
			}
		} 
	}
	
	/**
	 * <pre>
	 *
	 * desc : 상품 EAI 호출 
	 * [GRIT-53312]:(내부개선과제) 상품 SMTC->SAP 연동 시 정보 누락 개선 개발
	 * </pre>
	 * @author dhbaek
	 * @date 2023. 2. 28. 오후 3:06:56
	 * @param prdmMain
	 * @return
	 * @throws DevEntException
	 */
	@Override
	@SuppressWarnings("rawtypes")
    public Map<String, EntityDataSet> savePrdEaiInfo(PrdmMain prdmMain) throws DevEntException {
		Map<String, EntityDataSet> returnMap = new HashMap<String, EntityDataSet>();
		DSData resultData = new DSData();

		try{
			if ( prdmMain != null  && "I".equals(prdmMain.getRegGbn())) {
				BigDecimal prdCd = prdmMain.getPrdCd();
				String sessionUserId = prdmMain.getSessionUserId();
				Date date = SysUtil.getCurrTime();
				String sysdtm = DateUtils.format(date,"yyyyMMddHHmmss");
				
				//1. 상품마스터
				PrdEaiPrdSyncInfo pPrdEaiPrdSyncInfo = new PrdEaiPrdSyncInfo();
				pPrdEaiPrdSyncInfo.setPrdCd(prdCd);
				if("20".equals(prdmMain.getBundlPrdGbnCd())){
					pPrdEaiPrdSyncInfo.setSapSaleBlockYn("Y");
				}
				gshseai.GSH_S4C_PRD02.WS.PRD_EAI_Internal_Product.ws.wsProvider.PRD_EAI_Internal_Product_P.PRD_EAI_Res resPrdVal 
				= wsPrdEaiPrdSyncProcess.prdEaiPrdSyncProcess(pPrdEaiPrdSyncInfo);
				//SMTCLogger.infoPrd("상품동기화 EAI 결과:" + resPrdVal.getResponseResult());
				
				if(resPrdVal != null && !resPrdVal.getResponseResult().isEmpty()){
					PrdMetaDInfo prdMetaDEaiInfo = new PrdMetaDInfo();
					prdMetaDEaiInfo.setPrdCd(prdCd);
					prdMetaDEaiInfo.setSessionUserId(sessionUserId);
					prdMetaDEaiInfo.setPrdMetaTypCd("94");
					prdMetaDEaiInfo.setAttrCharVal1(resPrdVal.getResponseResult());
					prdMetaDEaiInfo.setAttrCharVal2(StringUtil.nvl(pPrdEaiPrdSyncInfo.getSapSaleBlockYn()));
					prdMetaInfoEntity.addPrdMetaD(prdMetaDEaiInfo);
				};
				
				//2. 상품속성
				PrdQryCond pPrdQryCond = new PrdQryCond();
				pPrdQryCond.setPrdCd(pPrdEaiPrdSyncInfo.getPrdCd());
				EntityDataSet<DSMultiData> pattrPrdM= attrPrdEntity.getAttrListN(pPrdQryCond);
				
				if(pattrPrdM.getValues() != null  && pattrPrdM.getValues().size()> 0){
					// 속성상품 동기화
					for (int i = 0; i < pattrPrdM.size(); i++) {
						pPrdEaiPrdSyncInfo.setPrdCd(pattrPrdM.getValues().getBigDecimal(i,"attrPrdCd"));
						// 속성 동기화
//						gshseai.GSH_S4C_PRD02.WS.PRD_EAI_Attribute_Product.ws.wsProvider.PRD_EAI_Attribute_Product_P.PRD_EAI_Res resAttrVal = 
						wsPrdEaiAttrPrdSyncProcess.prdEaiAttrPrdSyncProcess(pPrdEaiPrdSyncInfo);
						//SMTCLogger.infoPrd("속성동기화 EAI 결과:" + resAttrVal.getResponseResult());
					}
				}
				
				//3. 상품가격
				PrcPmoQryCond pPrcPmoQryCond = new PrcPmoQryCond();
				pPrcPmoQryCond.setPmoCurFutGbn("SAP");
				pPrcPmoQryCond.setPrdCd(prdCd);
				EntityDataSet<DSMultiData> pGetPrdPrcListGenrl = prdPrcEntity.getPrdPrcListGenrl(pPrcPmoQryCond);
				
				if(pGetPrdPrcListGenrl.getValues() != null  && pGetPrdPrcListGenrl.getValues().size()> 0){
					List<PrdEaiPrcSyncInfo> pPrdEaiPrcSyncInfoList = new ArrayList<PrdEaiPrcSyncInfo>();
					
					for(int i = 0 ; i< pGetPrdPrcListGenrl.size(); i++) {
						PrdEaiPrcSyncInfo pPrdEaiPrcSyncInfo = new PrdEaiPrcSyncInfo();
						pPrdEaiPrcSyncInfo.setJobTyp("I");				
						pPrdEaiPrcSyncInfo.setPrdCd(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"prdCd"));
						pPrdEaiPrcSyncInfo.setPrdAttrGbnCd(pGetPrdPrcListGenrl.getValues().getString(i,"prdAttrGbnCd"));
						pPrdEaiPrcSyncInfo.setValidEndDtm(pGetPrdPrcListGenrl.getValues().getString(i,"validEndDtm"));				
						pPrdEaiPrcSyncInfo.setValidStrDtm(pGetPrdPrcListGenrl.getValues().getString(i,"validStrDtm"));	
						pPrdEaiPrcSyncInfo.setPreValidEndDtm(pGetPrdPrcListGenrl.getValues().getString(i,"validEndDtm"));				
						pPrdEaiPrcSyncInfo.setPreValidStrDtm(pGetPrdPrcListGenrl.getValues().getString(i,"validStrDtm"));
						pPrdEaiPrcSyncInfo.setEnterDate(pGetPrdPrcListGenrl.getValues().getString(i,"regDtm"));
						pPrdEaiPrcSyncInfo.setEnterId(pGetPrdPrcListGenrl.getValues().getString(i,"regrId"));
						pPrdEaiPrcSyncInfo.setModifyDate(pGetPrdPrcListGenrl.getValues().getString(i,"modDtm"));
						pPrdEaiPrcSyncInfo.setModifyId(pGetPrdPrcListGenrl.getValues().getString(i,"modrId"));
						pPrdEaiPrcSyncInfo.setSalePrc(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"salePrc"));
						pPrdEaiPrcSyncInfo.setPrchPrc(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"prchPrc"));
						pPrdEaiPrcSyncInfo.setSupGivRtamtCd(pGetPrdPrcListGenrl.getValues().getString(i,"supGivRtamtCd"));					
						if (StringUtils.NVL(pGetPrdPrcListGenrl.getValues().getString(i,"supGivRtamtCd")).equals("02")) {
							pPrdEaiPrcSyncInfo.setSupGivRtamt(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"supGivRtamt"));
						} else {
							if(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"supGivRtamt") != null ) {
								pPrdEaiPrcSyncInfo.setSupGivRtamt(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"supGivRtamt").setScale(0, BigDecimal.ROUND_DOWN));
								
							} else {
								pPrdEaiPrcSyncInfo.setSupGivRtamt(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"supGivRtamt"));
							}
						}
						pPrdEaiPrcSyncInfo.setSupProprdUprc(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"supProprdUprc"));
						pPrdEaiPrcSyncInfo.setInstlCost(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"instlCost"));
						pPrdEaiPrcSyncInfo.setProprdWthtax(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"proprdWthtax"));
						pPrdEaiPrcSyncInfo.setVipDlvYn(pGetPrdPrcListGenrl.getValues().getString(i,"vipDlvYn"));
						pPrdEaiPrcSyncInfo.setVipDlvStdPrc(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"vipDlvStdPrc"));
						pPrdEaiPrcSyncInfo.setOnsitePrdPrc(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"onsitePrdPrc"));
						pPrdEaiPrcSyncInfo.setOnsiteDcPrc(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"onsiteDcPrc"));
						pPrdEaiPrcSyncInfo.setDetrmWeihtVal(pGetPrdPrcListGenrl.getValues().getString(i,"detrmWeihtVal"));
						pPrdEaiPrcSyncInfo.setOnsiteChrCost(pGetPrdPrcListGenrl.getValues().getBigDecimal(i,"onsiteChrCost"));
						pPrdEaiPrcSyncInfo.setWhsCd(pGetPrdPrcListGenrl.getValues().getString("whsCd"));
						pPrdEaiPrcSyncInfo.setOtherSysTnsYn("N");
						pPrdEaiPrcSyncInfo.setNoteCntnt(pGetPrdPrcListGenrl.getValues().getString(i,"noteCntnt"));
						pPrdEaiPrcSyncInfo.setSapExcptYn("N");
						
						pPrdEaiPrcSyncInfoList.add(pPrdEaiPrcSyncInfo);
					}
					
					if (pPrdEaiPrcSyncInfoList.size() > 0) {
//						gshseai.GSH_S4C_PRD01.WS.PRD_EAI_Products_Price_Reg_Mod.ws.wsProvider.PRD_EAI_Products_Price_Reg_Mod_P.PRD_EAI_Res resPrcVal = 
						wsPrdEaiPrdPriceRegModProcess.prdEaiPrdPricdRegModProcess(pPrdEaiPrcSyncInfoList);
						//SMTCLogger.infoPrd("가격동기화 EAI 결과:" + resPrcVal.getResponseResult());
					}
					
				}
	
				//4. 상품채널
				List<PrdChanlInfo> prdChanlDinsert = prdChanlEntity.getPrdChanlD(prdmMain);
	
				if (prdChanlDinsert.size() > 0) {
					List<PrdChanlSyncInfo> prdChanlSyncInfoList = new ArrayList<PrdChanlSyncInfo>();
	
					for (int i = 0; i < prdChanlDinsert.size(); i++) {
						// 상품채널정보 eai 인터페이스
						PrdChanlSyncInfo pPrdChanlSyncInfoID =  new PrdChanlSyncInfo();;
				    	pPrdChanlSyncInfoID.setJobType("I");
				    	pPrdChanlSyncInfoID.setPrdCd(prdChanlDinsert.get(i).getPrdCd());
				    	pPrdChanlSyncInfoID.setChanlCd(prdChanlDinsert.get(i).getChanlCd());
				    	pPrdChanlSyncInfoID.setChanlMdId(prdChanlDinsert.get(i).getChanlMdId());
				    	pPrdChanlSyncInfoID.setSalePsblYn(prdChanlDinsert.get(i).getSalePsblYn());
				    	pPrdChanlSyncInfoID.setQaInspYn(prdChanlDinsert.get(i).getQaInspYn());
				    	pPrdChanlSyncInfoID.setStdRelsDdcnt(prdChanlDinsert.get(i).getStdRelsDdcnt());
				    	pPrdChanlSyncInfoID.setStdRelsDdcnt(prdChanlDinsert.get(i).getStdRelsDdcnt());
				    	pPrdChanlSyncInfoID.setRepMdUserId(prdChanlDinsert.get(i).getRepMdUserId());
				    	pPrdChanlSyncInfoID.setRegDtm(sysdtm);
				    	pPrdChanlSyncInfoID.setRegrId(sessionUserId);
				    	pPrdChanlSyncInfoID.setModDtm(sysdtm);
				    	pPrdChanlSyncInfoID.setModrId(sessionUserId);
						prdChanlSyncInfoList.add(pPrdChanlSyncInfoID);
					}
	
					if (prdChanlSyncInfoList.size() > 0) {
//						gshseai.GSH_S4C_PRD01.WS.PRD_EAI_Prd_Ch_Reg_Modify_Sync.ws.wsProvider.PRD_EAI_Prd_Ch_Reg_Modify_Sync_P.PRD_EAI_Res resChanlVal = 
						wsPrdEaiPrdChanlSyncProcess.prdChanlSyncProcess(prdChanlSyncInfoList);
						//SMTCLogger.infoPrd("채널동기화 EAI 결과:" + resChanlVal.getResponseResult());
					}
				}
				
				//5. 상품기술서
				PrdDesceGenrlDInfo prdDesceGenrlDInfo = new PrdDesceGenrlDInfo();
				prdDesceGenrlDInfo.setPrdCd(prdCd);
				EntityDataSet<DSMultiData> prdDescdGenrlDList = prdExplnEntity.getPrdDescdGenrlDListNew(prdDesceGenrlDInfo);
	
				if(prdDescdGenrlDList.getValues() != null  && prdDescdGenrlDList.getValues().size()> 0){
					List<PrdDescdSyncInfo> prdDescdSyncInfoList = new ArrayList<PrdDescdSyncInfo>();
					for(DSData dsData : prdDescdGenrlDList.getValues()){
						PrdDescdSyncInfo prdDescdSyncInfo= new PrdDescdSyncInfo();
						prdDescdSyncInfo.setPrdCd(dsData.getBigDecimal("prdCd"));
						prdDescdSyncInfo.setGovPublsPrdGrpCd(dsData.getString("govPublsPrdGrpCd"));
						prdDescdSyncInfo.setPrdExplnItmCd(dsData.getString("prdExplnItmCd"));
						prdDescdSyncInfo.setJobType("I");
						prdDescdSyncInfoList.add(prdDescdSyncInfo);
					}
					if(prdDescdSyncInfoList.size() > 0) {
//					gshseai.GSH_S4C_PRD01.WS.PRD_BIS_Prod_Desc_Sync.ws.wsProvider.PRD_BIS_Prod_Desc_Sync_P.PRD_EAI_Res resDescdVal = 
					wsPrdBisPrdDescdRegModProcess.prdBisPrdDescdRegModProcess(prdDescdSyncInfoList);
					//SMTCLogger.infoPrd("기술서동기화 EAI 결과:" + resDescdVal.getResponseResult());
					}
				}
			}	
		}catch(Exception e){
			resultData.put("retCd", "E");
			resultData.put("retMsg", Message.getMessage("prd.eai.msg.025"));//prd.eai.msg.025=전송실패하였습니다.
			returnMap.put("eaiResult", EDSFactory.create(EsbCmm.class, resultData));
			
			return returnMap;
		}
		
		resultData.put("retCd", "S");
		resultData.put("retMsg", Message.getMessage("prd.eai.msg.024"));//prd.eai.msg.024=전송성공하였습니다.
		returnMap.put("eaiResult", EDSFactory.create(EsbCmm.class, resultData));
		
		return returnMap;	
	}	
}