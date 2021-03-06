package kr.or.ddit.groupware.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.groupware.model.CommCdVo;
import kr.or.ddit.groupware.model.PmsEmpVo;
import kr.or.ddit.groupware.model.PmsVo;
import kr.or.ddit.groupware.model.WorkFileVo;
import kr.or.ddit.groupware.repository.PmsDao;

@Service("pmsService")
public class PmsService implements PmsServicei{
	private static final Logger logger = LoggerFactory.getLogger(PmsService.class);


	@Resource(name = "pmsDao")
	private PmsDao pmsDao;

	/* 해당 사원 pms 조회*/
	@Override
	public PmsVo selectPmsemp(int emp_no) {
		
		return pmsDao.selectPmsemp(emp_no);
	}
	

	/* 전체 조회*/
	@Override
	public List<PmsVo> selectAll(PageVo pageVo) {
		// TODO Auto-generated method stub
		return pmsDao.selectAll(pageVo);
	}

	@Override
	public int registPsm(PmsVo pmsVo) {
		// TODO Auto-generated method stub
		return pmsDao.registPsm(pmsVo);
	}

	@Override
	public int selectPmsMaxNo() {
		// TODO Auto-generated method stub
		return pmsDao.selectPmsMaxNo();
	}

	@Override
	public int registPsmEmp(PmsEmpVo pmsEmpVo) {
		// TODO Auto-generated method stub
		return pmsDao.registPsmEmp(pmsEmpVo);
	}

	@Override
	public List<Integer> selectPsmEmpList(int pms_no) {
		// TODO Auto-generated method stub
		return pmsDao.selectPsmEmpList(pms_no);
	}

	@Override
	public int selectPmsNo(String app_char) {
		// TODO Auto-generated method stub
		return pmsDao.selectPmsNo(app_char);
	}

	@Override
	public int approvePms(String app_char) {
		// TODO Auto-generated method stub
		return pmsDao.approvePms(app_char);
	}

	@Override
	public List<PmsVo> pmsEmpList(int pms_no) {
		// TODO Auto-generated method stub
		return pmsDao.pmsEmpList(pms_no);
	}

	/*일감 리스트 페이징 처리*/
	@Override
	public Map<String, Object> workAllList(Map<String, Object> map) {
		
		logger.debug("workAllList() ================== \n");
		logger.debug("map : {}", map);
		
		Map<String, Object> paramap =  new HashMap<String, Object>();
		
		
		List<PmsVo> pmsList = pmsDao.workAllList(map);
		logger.debug("pmsList : {}", pmsList);
		
		int pmsCnt = pmsDao.workAllCnt((int)map.get("pms_no"));
		
		PageVo pageVo = new PageVo((int)map.get("page"), (int)map.get("pageSize"));
		
		paramap.put("pmsList", pmsList);
		paramap.put("pmsCnt", pmsCnt);
		paramap.put("pageVo", pageVo);
		logger.debug("paramap : {}", paramap);
		return paramap;
	}

	/*일감 상세정보*/
	@Override
	public PmsVo workDetail(PmsVo pmsVo) {
		// TODO Auto-generated method stub
		return pmsDao.workDetail(pmsVo);
	}
	/*일감 등록*/
	@Override
	public int registWork(PmsVo pmsVo) {
		// TODO Auto-generated method stub
		return pmsDao.registWork(pmsVo);
	}

	@Override
	public List<CommCdVo> selectComm_cd() {
		// TODO Auto-generated method stub
		return pmsDao.selectComm_cd();
	}
	
	@Override
	public List<CommCdVo> selectComm_cdPri() {
		// TODO Auto-generated method stub
		return pmsDao.selectComm_cdPri();
	}

	@Override
	public List<Integer> selectPms(int attend_emp_no) {
		// TODO Auto-generated method stub
		return pmsDao.selectPms(attend_emp_no);
	}

	@Override
	public int modifyWork(PmsVo pmsVo) {
		// TODO Auto-generated method stub
		return pmsDao.modifyWork(pmsVo);
	}

	@Override
	public int deleteWork(int work_no) {
		// TODO Auto-generated method stub
		return pmsDao.deleteWork(work_no);
	}

	@Override
	public int selectWork() {
		// TODO Auto-generated method stub
		return pmsDao.selectWork();
	}

	@Override
	public int registLowerWork(PmsVo pmsVo) {
		// TODO Auto-generated method stub
		return pmsDao.registLowerWork(pmsVo);
	}

	@Override
	public List<PmsVo> selectListChart(int pms_no) {
		// TODO Auto-generated method stub
		return pmsDao.selectListChart(pms_no);
	}

	@Override
	public List<PmsVo> selectAppList() {
		// TODO Auto-generated method stub
		return pmsDao.selectAppList();
	}

	@Override
	public List<PmsVo> selectAppPmsempList(int emp_no) {
		// TODO Auto-generated method stub
		return pmsDao.selectAppPmsempList(emp_no);
	}


	@Override
	public int PmsAllCnt() {
		// TODO Auto-generated method stub
		return pmsDao.PmsAllCnt();
	}


	@Override
	public List<PmsVo> SelectLowerWork(int higher_work_no) {
		// TODO Auto-generated method stub
		return pmsDao.SelectLowerWork(higher_work_no);
	}

	//일감 첨부파일 
	@Override
	public int registWorkFile(WorkFileVo workFileVo) {
		// TODO Auto-generated method stub
		return pmsDao.registWorkFile(workFileVo);
	}


	@Override
	public int modifyWorkProg(int work_no) {
		// TODO Auto-generated method stub
		return pmsDao.modifyWorkProg(work_no);
	}


	@Override
	public List<Integer> workFileList(int file_no) {
		// TODO Auto-generated method stub
		return pmsDao.workFileList(file_no);
	}

	
}
