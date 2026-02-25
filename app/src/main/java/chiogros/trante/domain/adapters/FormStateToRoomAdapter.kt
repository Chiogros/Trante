package chiogros.trante.domain.adapters

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.common.CommonConnectionEditFormState

/** Convert UI data model to Room data model. It avoids handling data types out of their context.
 * @see CommonConnectionEditFormState
 * @see Connection
 */
interface FormStateToRoomAdapter {
    fun convert(formState: CommonConnectionEditFormState): Connection
    fun convert(con: Connection): CommonConnectionEditFormState
}