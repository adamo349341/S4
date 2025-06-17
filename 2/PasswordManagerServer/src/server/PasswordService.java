package server;

/**
 * Interface pour définir les opérations disponibles sur le service de mots de passe.
 */
public interface PasswordService {

    /**
     * Récupère tous les mots de passe au format JSON.
     *
     * @return une chaîne JSON représentant la liste des mots de passe
     */
    String getAllPasswordsAsJson();

    /**
     * Ajoute une nouvelle entrée de mot de passe.
     *
     * @param site     le site ou service concerné
     * @param username le nom d'utilisateur associé
     * @param password le mot de passe
     */
    void addPassword(String site, String username, String password);

    /**
     * Supprime une entrée de mot de passe en fonction du site et du nom d'utilisateur.
     *
     * @param site     le site ou service concerné
     * @param username le nom d'utilisateur associé
     */
    void deletePassword(String site, String username);
}
