package org.kevoree.tools.aether.framework

import org.kevoree.DeployUnit
import org.kevoree.kcl.KevoreeJarClassLoader
import org.kevoree.api.Bootstraper

/**
 * Created by IntelliJ IDEA.
 * User: duke
 * Date: 30/01/12
 * Time: 16:05
 */

trait KCLBootstrap : Bootstraper {

    fun buildKEY(du: DeployUnit): String {
        return du.name + "/" + buildQuery(du, null)
    }

    fun buildQuery(du: DeployUnit, repoUrl: String?): String {
        val query = StringBuilder()
        query.append("mvn:")
        if(repoUrl != null){
            query.append(repoUrl); query.append("!")
        }
        query.append(du.groupName)
        query.append("/")
        query.append(du.unitName)
        if(!du.version.equals("default") && !du.version.equals("")){
            query.append("/"); query.append(du.version)
        }
        return query.toString()
    }

    fun installDeployUnit(du: DeployUnit): KevoreeJarClassLoader? {
        try {
            val previousKCL = getKevoreeClassLoaderHandler().getKevoreeClassLoader(du)
            if (previousKCL != null) {
                return previousKCL
            } else {
                val arteFile = AetherUtil.resolveDeployUnit(du)
                if (arteFile != null) {
                    val kcl = getKevoreeClassLoaderHandler().installDeployUnit(du, arteFile)
                    return kcl
                } else {
                    return null
                }
            }
        } catch(e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}