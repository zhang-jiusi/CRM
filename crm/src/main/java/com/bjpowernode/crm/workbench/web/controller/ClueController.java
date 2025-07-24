package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.setting.domain.DicValue;
import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.setting.service.DicValueService;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @date: 2025/7/23 1:53
 */
@Controller
public class ClueController {

	@Autowired
	private UserService userService;

	@Autowired
	private DicValueService dicValueService;

	@Autowired
	private ClueService clueService;

	/**
	 * 导航页跳转到线索页
	 * @date:   2025/7/23 23:47
	 **/
	@RequestMapping("/workbench/clue/index.do")
	public String index(HttpServletRequest request){

		// 调用 srvice 方法，查询用户数据
		List<User> userList = userService.queryAllUsers();
		List<DicValue> appellationList = dicValueService.queryDictValueByTypeCode("appellation");
		List<DicValue> clueStateList = dicValueService.queryDictValueByTypeCode("clueState");
		List<DicValue> sourceList = dicValueService.queryDictValueByTypeCode("source");

		// 把数据保存在作用域中，request
		request.setAttribute("userList",userList);
		request.setAttribute("appellationList",appellationList);
		request.setAttribute("clueStateList",clueStateList);
		request.setAttribute("sourceList",sourceList);

		// 请求转发
		return "/workbench/clue/index";
	}

	/**
	 * 保存创建的线索
	 * @date:   2025/7/23 23:48
	 **/
	@RequestMapping("/workbench/clue/saveCreateClue.do")
	@ResponseBody
	public Object saveCreateClue(HttpSession session, Clue clue){
		// 封装参数，创建 id,CreateTime，CreateBy
		User user = (User)session.getAttribute(Contants.SESSION_USER);
		clue.setId(UUIDUtils.getUUID());
		clue.setCreateTime(DateUtils.formateDatTIme(new Date()));
		clue.setCreateBy(user.getName());

		System.out.println("============ clue  ============");
		System.out.println(clue.toString());
		ReturnObject returnObject = new ReturnObject();
		try{
			// 调用 service 方法，保存 clue
			int ret = clueService.saveCreateClue(clue);
			System.out.println("======clueService.saveCreateClue(clue)====");
			System.out.println(ret);
			if (ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			}else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("数据库失败：保存创建的线索");
			}

		}catch (Exception e){
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("数据库失败：保存创建的线索");
		}


		return returnObject;
	}


}