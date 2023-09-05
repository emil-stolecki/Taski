package tasksPackage;



import Interface.MainWindow;

import tasksPackage.StoragesFolder.Day;



import java.time.LocalDate;

import DatabaseTools.TableControls;


/////CREDDDITTT FOR Jdatepicker
/*
 * THIS SOFTWARE IS PROVIDED BY JUAN HEYNS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JUAN HEYNS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */




public class Main {
	public static void main(String[] args){
		
		
		TableControls.create();
	  		
		LocalDate today= LocalDate.now();//will be displayed first 
		
		Day [] cal= new Day[GlobalVariables.daysInMemory];
		// GlobalVariables.daysInMemory -how many days will be taken from DB and temporarily stored at one time - must be odd number
		for(int i=0;i<GlobalVariables.daysInMemory;i++) {
			long dayid =i+(GlobalVariables.daysInMemory-1)/-2;
			LocalDate day =today.plusDays(dayid);
			cal[i]= new Day(day);
		}
		
				
		
	
	    MainWindow mainWindow = new MainWindow(cal);
	    mainWindow.setVisible(true);
	    
	    
	}
}
