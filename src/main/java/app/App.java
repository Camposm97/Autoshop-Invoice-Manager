package app;

import controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.ui.FX;

import java.io.IOException;

/**
 * TODO
 * Update offsets for each column in tables:
 *  customer
 *  vehicle
 *  work order
 *  parts
 *  labor
 * Considering the root of the ui is a tab pane, in future updates we can potentially implement
 * a multi-task feature where the user is able to have multiple work orders open in tabs. For example,
 * if I had work order #1 and #2 open, the program can distinguish the tabs by setting the title to that
 * work order's id. In addition, since we've de-coupled majority of the components used in the work order
 * workspace, it shouldn't be a big issue when implementing the feature. I think the only issue would be
 * avoiding 'errors'. When I say 'errors' I mean that at the moment of editing a work order, the main
 * menu bar is disabled which is good as of now, but to achieve this feature we need enable the main menu
 * bar and allow users to create work orders which should be an easy since we can just disable every other
 * menu item except for creating a new order. In fact, thinking about it now, leaving the main menu
 * enabled should be fine now since we decoupled the work order workspace controller.
 *
 * The main issue is what if I want to delete a work order and I'm in the middle editing it? To solve this
 * issue we can have a list of currently opened work orders and if the user tries to delete that work order
 * then the program can check if the work order is being edited in a currently opened list of work orders.
 * If the work order is being edited then show an pop-window error saying the work order is currently open
 * and the user has to close it in order to save it. Otherwise, delete the work order.
 *
 * What flags a work order to be added to the list of currently opened work orders?
 * For sake of better documentation, refer the list as {currOWOs}.
 * From the work order table controller, if the user double-clicks of clicks the edit button when a work order
 * is currently selected then before switching to work order workspace the program will add that selected work order
 * to {currOWOs}.
 *
 * And what flags a work order to be removed from the list of currently opened work orders?
 * If the user clicks either two buttons to 'save and close' or 'cancel and close' then the work order is removed
 * from {currOWOs}. Those are the only two ways to exit the workspace. But, since we have tabs now to display a
 * work order workspace we have to think about whether the user will close the tab itself. So, if the user does close
 * the work order workspace tab then the program will consider that as 'cancel and close' and remove the work order
 * from {currOWOs}.
 *
 * The list {currOWOs} could be saved on secondary storage, all the list would is only the id of the work order similar to
 * recent work orders. So, the list would be a list of integers. If the user closes (exit) the program, then {currOWOs} will
 * be saved. And on start of the program, {currOWOs} will be loaded into memory and automatically open the work orders
 * in the process of being edited.
 *
 * How many work orders should {currOWOs} keep track of?
 * As of now, I think it would be best to leave it as many as the user's local machine can handle.
 * @implNote Now that I'm thinking about it, it reminds of the problem on browsers where what if I have hundreds of tabs open.
 * The computer will eventually slow down. If that's the case, then a neat optimization feature would load only information of a single
 * work order since the user can only view one work order throughout the program. However, I don't think we have to go that far.
 * Just something interesting I thought of.
 * @see controller.WorkOrderTableController
 * @see controller.WorkOrderWorkspaceController
 */
public class App extends Application {
    private static AppController controller;

    public static AppController get() {
        return controller;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxml = FX.load("App.fxml");
        stage = fxml.load();
        controller = fxml.getController();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
