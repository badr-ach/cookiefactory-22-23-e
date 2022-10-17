# Cookiefactory-22-23-Team-#template
_Template for classroom SI4-COO_

## doc
Contient le rapport final

## .github
   1. Contient sous workflows/maven.yml, une version d'un fichier d'actions qui est déclenché dès que vous poussez du code. 
Sur cette version initiale seule un test Junit5 est déclenché pour vérifier que tout fonctionne.
       - Github Actions (See in .github/workflows) to simply make a maven+test compilation
  2. Contient sous ISSUE_TEMPLATE, les modèles pour les issues user_story et bug. Vous pouvez le compléter à votre guise.

## src
 - pom.xml : 
       - Cucumber 7 et JUnit 5
       - Maven compatible
       - JDK 17



## User stories 
La liste des fonctionnalités livrées par user story.<br>
### **As a** Customer **I want to** order 1 basic recipe **in order** to buy cookies<br>
**Acceptance criterias:**<br>
- **Background:**<br>
  **Given** a customer named Bob <br>
- **Scenario:** Bob retrieves the basic cookie list<br>
  **When** Bob retrieves the cookie list<br>
  **Then** 1 basic recipe is returned<br>
- **Scenario:** Bob adds a cookie to the cart<br>
  **When** Bob orders 1 basic recipes named "recipe1"<br>
  **Then** the cookies are added to the cart<br>
- **Scenario:** Bob retrieves the cart price<br>
  **When** Bob retrieves the total from the cart<br>
  **Then** the correct price is returned<br>
- **Scenario:** Bob pays the order<br>
  **When** Bob pays the order<br>
  **Then** the order is confirmed<br>

### **As a** Customer **I want to** select my cookies **in order** choose my cookies and buy multiple cookies<br>
**Acceptance criterias:**<br>
- **Background:**<br>
  **Given** a customer named Bob<br>
- **Scenario:** Bob retrieves the full cookies list<br>
  **When** Bob retrieves the cookie list<br>
  **Then** different cookie types are returned<br>
- **Scenario:** Bob updates his cart<br>
  **When** Bob updated his cart<br>
  **Then** the cart is updated<br>


### **As a** Cook **I want to** be able to know the orders that were assigned to me  **in order** to prepare them<br>
**Acceptance criterias:**<br>
- **Background:**<br>
  **Given:** an order has been submited to be cooked<br>
- **Scenario:** The order is assigned to a cook<br>
  **Given:** the list of cooks that are working in the store<br>
  **When:** the order suits the schedule of some of these cooks<br>
  **Then:** the order is assigned to one cook and one only of the free cooks<br>
- **Scenario:** A cook retrieves the orders that he was assigned<br>
  **When:** A cook wants to retrieve the orders that he has to prepare<br>
  **Then:** the orders that were assigned to this cook are returned<br>


### **As a** Store Employee **I want to** mark order fulfilled **in order** declutter the order list<br>
**Acceptance criterias:**<br>
- **Background:**<br>
  **Given** a store employee named John<br>
- **Scenario:** John fulfills the order<br>
  **When** John marks the order as fulfilled<br>
  **Then** the order is not returned in the pending order list<br>

### Milestone X

Chaque user story doit être décrite par 
   - son identifiant en tant que issue github (#), 
   - sa forme classique (As a… I want to… In order to…) (pour faciliter la lecture)
   - Le nom du fichier feature Cucumber et le nom des scénarios qui servent de tests d’acceptation pour la story.
   Les contenus détaillés sont dans l'issue elle-même.
   
   
