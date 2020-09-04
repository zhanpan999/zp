package cn.llg.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.llg.dao.ProjectteamsMapper;
import cn.llg.dao.WorkorderMapper;
import cn.llg.pojo.Projectteams;
import cn.llg.pojo.Workorder;

@Controller
@RequestMapping("/workorder")
public class WorkorderController {
	@Resource
	WorkorderMapper workorderDao;
	
	@Resource
	ProjectteamsMapper projectteamsMapper;
	
	public String toUtf(String str) throws UnsupportedEncodingException{
		return new String(str.getBytes("ISO-8859-1"),"utf-8");
	}
	//访问项目的方法 ，获得企业工单数据和项目组数据
	@RequestMapping(value="/login.html")
	public String login(Model model,HttpSession session){
		List<Workorder> workorderList=new ArrayList<Workorder>();
		workorderList=workorderDao.getWorkorder();
		model.addAttribute("workorderList",workorderList);
		
		List<Projectteams> ProjectNameList=new ArrayList<Projectteams>();
		ProjectNameList=projectteamsMapper.getProjectName();
		model.addAttribute("ProjectNameList",ProjectNameList);
		
		return "Workorderlist";
	}
	@RequestMapping(value="/addPro.html")
	public String addPro(Model model,HttpSession session){
		List<Projectteams> ProjectNameList=new ArrayList<Projectteams>();
		ProjectNameList=projectteamsMapper.getProjectName();
		model.addAttribute("ProjectNameList",ProjectNameList);
		return "ShowProjectName";
	}
	@RequestMapping(value="/addWorkorder.html")
	public String addworkorder(@ModelAttribute("workorder") Workorder workorder,@RequestParam("projectId") String projectId,Model model){
		if (!projectId.equals("0")) {
			System.out.println("projectId======"+projectId);
			model.addAttribute("projectId",projectId);
			return "addworkorder";
		}
		return "ShowProjectName";
	}
	
	@RequestMapping(value="/addWorkordersave.html")
	public String addworkordersave(@ModelAttribute("workorder") Workorder workorder,HttpSession session,Model model){
		//workorder.setProjectId(Integer.parseInt(projectId));
		workorder.setCreateDate(new Date());
		try {
			workorder.setExecutor(toUtf(workorder.getExecutor()));
			workorder.setDescription(toUtf(workorder.getDescription()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Integer result=workorderDao.addWorkorder(workorder);
		
		List<Workorder> workorderList=new ArrayList<Workorder>();
		workorderList=workorderDao.getWorkorder();
		model.addAttribute("workorderList",workorderList);
		
		if (result>0) {
			//model.addAttribute("result","添加成功！");
			session.setAttribute("result","添加成功！2222");
			return "redirect:/workorder/login.html";
			//return "Workorderlist";
		}else {
			session.setAttribute("result","添加失败！");
			return "redirect:/workorder/login.html";
			//return "Workorderlist";
		}
	}
}
