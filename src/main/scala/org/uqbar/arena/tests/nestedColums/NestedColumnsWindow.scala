package org.uqbar.arena.tests.nestedColums


import java.awt.Color
import java.util.HashMap
import java.util.Map
import org.uqbar.arena.actions.MessageSend
import org.uqbar.arena.bindings.PropertyAdapter
import org.uqbar.arena.bindings.transformers.AbstractReadOnlyTransformer
import org.uqbar.arena.layout.VerticalLayout
import org.uqbar.arena.widgets.Button
import org.uqbar.arena.widgets.Label
import org.uqbar.arena.widgets.List
import org.uqbar.arena.widgets.Panel
import org.uqbar.arena.widgets.RadioSelector
import org.uqbar.arena.widgets.Selector
import org.uqbar.arena.widgets.TextBox
import org.uqbar.arena.widgets.tables.Column
import org.uqbar.arena.widgets.tables.Table
import org.uqbar.arena.windows.MainWindow
import org.uqbar.lacar.ui.model.Action
import com.uqbar.commons.collections.Transformer
import org.uqbar.arena.layout.ColumnLayout
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.uqbar.arena.test.scala.collections.binding.TableWithScalaListWindow


/**
 * Se testea las dos formas de bindear el background y tambien el binding anidado en las columnas
 *
 */

object NestedColumnsWindow extends MainWindow[University](University.university) with App{
	startApplication()
	
	override def createContents(mainPanel:Panel) {
		
		mainPanel.setLayout(new VerticalLayout())
		
		val table = new Table[Estudent](mainPanel, classOf[Estudent])
		table.setWidth(200)
		table.setHeight(200)
		table.bindItemsToProperty("students")
		table.bindValueToProperty("currentEstudent")
		
		val statusTransomer = new Transformer[EstudenStatus.Status, Color](){
		          def transform(status:EstudenStatus.Status) =  status match{
		            case EstudenStatus.EXPELLED => Color.RED
		            case EstudenStatus.FREE => Color.ORANGE
		            case EstudenStatus.REGULAR => Color.BLUE
		          }
	     }
		
		val nameColumn = new Column[Estudent](table)
		nameColumn setTitle("Name")	bindContentsToProperty("name") setTransformer(new Transformer[String,String]() {
		  override def transform(name:String) = name.toUpperCase()
		})
		
		nameColumn bindBackground("status", statusTransomer)
		
		val departmentColumn = new Column[Estudent](table)
		departmentColumn.setTitle("Department").bindContentsToProperty("department.name")
		departmentColumn.bindBackground("status", statusTransomer)
		
		val formPanel = new Panel(mainPanel)
		formPanel.setLayout(new ColumnLayout(2))
		
		new Label(formPanel).setText("Nombre")
		val nameTextbox = new TextBox(formPanel)
		nameTextbox.bindValueToProperty("currentEstudent.name")
		
		nameTextbox.bindBackground("currentEstudent.status")
			.when(EstudenStatus.EXPELLED, Color.RED)
			.when(EstudenStatus.FREE, Color.ORANGE)
			
		new Label(formPanel).setText("Status")
		val provinces = new Selector[EstudenStatus.Status](formPanel)
		provinces.setContents(EstudenStatus.values.toList, "name")
		provinces.bindValueToProperty("currentEstudent.status");
	}

}

