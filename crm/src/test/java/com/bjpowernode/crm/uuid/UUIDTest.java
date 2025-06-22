package com.bjpowernode.crm.uuid;

import java.util.UUID;

/**
 * 使用 JDK 生成一个 32 字符串作为 id
 *
 * @date: 2025/6/16 7:04
 */
public class UUIDTest {

	public static void main(String[] args) {

		String uuid = UUID.randomUUID().toString().replaceAll("-","");
		// .toString()                     15319211-6875-4735-9fc2-85da0b40b653
		// .toString().replaceAll("-","")  aad69ceeb2b74823ba0d508a074e9f57
		System.out.println(uuid);
	}

}
