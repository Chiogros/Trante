package chiogros.trante.domain.adapters

import chiogros.trante.data.room.Connection
import chiogros.trante.protocols.common.CommonConnectionEditFormState

interface FormStateToRoomAdapter {
    fun convert(formState: CommonConnectionEditFormState): Connection
    fun convert(con: Connection): CommonConnectionEditFormState
}